package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public abstract class AView implements IView, Observer, Initializable
{
    protected static MyViewModel viewModel;
    protected MenuBarOptions menuBarOptions = new MenuBarOptions();
    protected MediaPlayer mediaPlayer;
    protected static boolean isOff;
    static double volume = 100;
    protected Configurations configurations;


    public void setViewModel(MyViewModel viewModel)
    {
        AView.viewModel = viewModel;
        AView.viewModel.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) { }

    public void createNewFile(ActionEvent actionEvent)
    {
        boolean success = menuBarOptions.createNewFile(actionEvent, mediaPlayer);
        if (!success)
        {
            raisePopupWindow("Couldn't create new maze", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void loadFile(ActionEvent actionEvent)
    {
        boolean success = menuBarOptions.loadFile(viewModel);
        if (!success)
        {
            raisePopupWindow("Couldn't load file\nChoose a legal file (type *.maze)", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.ERROR);
            return;
        }
        Window window = ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow();
        window.setOnHidden(e -> mediaPlayer.stop());
        window.hide();
    }

    @FXML
    public void showProperties(ActionEvent actionEvent)
    {
        Window window = ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow();
        window.setOnHidden(e -> mediaPlayer.stop());
        viewModel.showProperties(window, mediaPlayer, this);
        window.hide();
    }

    @FXML
    public void showSettings(ActionEvent actionEvent)
    {
        isOff = menuBarOptions.showSettings(mediaPlayer);
        volume = mediaPlayer.getVolume();
    }

    @FXML
    public void showHelp(ActionEvent actionEvent)
    {
        Window window = ((Node)(actionEvent.getSource())).getScene().getWindow();
        window.setOnHidden(e -> mediaPlayer.stop());
        menuBarOptions.showHelp(mediaPlayer, this, window);
    }

    @FXML
    public void showAbout(ActionEvent actionEvent)
    {
        Window window = ((Node)(actionEvent.getSource())).getScene().getWindow();
        menuBarOptions.showAbout(window);
    }

    @FXML
    public void exitProgram(ActionEvent actionEvent) { menuBarOptions.exitProgram(); }

    public void setMediaPlayer(MediaPlayer mediaPlayer)
    {
        this.mediaPlayer = mediaPlayer;
        this.mediaPlayer.setVolume(volume);
    }

    public void setMusic(Media musicFile)
    {
        mediaPlayer = new MediaPlayer(musicFile);
        mediaPlayer.setVolume(volume);
        setMediaPlayer(mediaPlayer);
        if (!isOff)
        {
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setVolume(100);
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            });
        }
    }

    public Stage changeScene(String fxmlFile, String stageTitle, String rootID)
    {
        Stage stage = new Stage();
        Parent root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFile));
        try
        {
            root = fxmlLoader.load();
            root.setId(rootID);
            AView controller = fxmlLoader.getController();
            Scene scene = new Scene(root, 900, 650);
            viewModel.addObserver(controller);
            stage.setTitle(stageTitle);
            stage.setOnCloseRequest(e -> System.exit(0));
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.show();
        return stage;
    }

    public void raisePopupWindow(String text, String path, Alert.AlertType type)
    {
        Media oldMedia = mediaPlayer.getMedia();
        MediaPlayer oldMediaPlayer = mediaPlayer;
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        MediaView mediaView = new MediaView(mediaPlayer);
        setMediaPlayer(mediaPlayer);
        Alert alert = new Alert(type);
        alert.getDialogPane().setMinHeight(250);
        alert.getDialogPane().setMinWidth(200);
        AnchorPane content = new AnchorPane();
        content.getChildren().addAll(mediaView);
        DoubleProperty width = mediaView.fitWidthProperty();
        DoubleProperty height = mediaView.fitHeightProperty();
        width.bind(Bindings.selectDouble(alert.getDialogPane().sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(alert.getDialogPane().sceneProperty(), "height"));
        mediaView.setPreserveRatio(true);
        alert.getDialogPane().setContent(content);
        if (type == Alert.AlertType.ERROR) {
            alert.setTitle("Off With There Heads!!!");
        }
        else {
            alert.setTitle("Well Done!");
        }
        alert.setHeaderText(text);
        alert.getDialogPane().getStylesheets().add("CSSs/Alerts.css");
        alert.setOnShowing(e -> {
            oldMediaPlayer.stop();
            mediaPlayer.play();
        });
        alert.setOnCloseRequest(event -> {
            mediaPlayer.stop();
            setMusic(oldMedia);
        });
        alert.showAndWait();
    }

    protected void addClip(Stage clipStage, BorderPane borderPane, Media media, String nextSceneFxml, String nextStageTitle, String nextRootID)
    {
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnReady(() -> mediaPlayer.play());
        mediaView.setPreserveRatio(true);
        borderPane.setCenter(mediaView);

        mediaView.fitWidthProperty().bind(clipStage.widthProperty());
        mediaView.fitHeightProperty().bind(clipStage.heightProperty());

        clipStage.setOnHidden(e -> mediaPlayer.stop());
        clipStage.setOnCloseRequest(e -> System.exit(0));
        if (isOff)
            mediaPlayer.setMute(true);

        mediaPlayer.setOnEndOfMedia(() -> {
            changeScene(nextSceneFxml, nextStageTitle, nextRootID);
            clipStage.hide();
            clipStage.close();
        });
    }

    protected void addControlButtons(BorderPane borderPane, Stage clipStage, String nextSceneFxml, String nextStageTitle, String nextRootID)
    {
        Button continueButton = createContinueButton();
        Button muteButton = createMuteButton();
        muteButton.setMinHeight(60);
        muteButton.setMinWidth(130);
        HBox hBox = new HBox(10, muteButton, continueButton);
        hBox.setAlignment(Pos.TOP_CENTER);
        borderPane.setPadding(new Insets(15, 25, 25, 0));
        borderPane.setBottom(hBox);

        continueButton.setOnAction(actionEvent -> {
            mediaPlayer.stop();
            changeScene(nextSceneFxml, nextStageTitle, nextRootID);
            clipStage.hide();
            clipStage.close();
        });
    }

    private Button createMuteButton()
    {
        Button muteButton = new Button();
        if (isOff)
            muteButton.setText("UnMute");
        else
            muteButton.setText("Mute");
        muteButton.setId("muteButton");
        muteButton.setMinHeight(60);
        muteButton.setMinWidth(130);
        muteButton.setOnAction(e -> {
            if (muteButton.getText().equals("Mute"))
            {
                muteButton.setText("UnMute");
                mediaPlayer.setVolume(0);
            }
            else if (muteButton.getText().equals("UnMute"))
            {
                muteButton.setText("Mute");
                mediaPlayer.setMute(false);
                mediaPlayer.setVolume(100);
            }
        });
        return muteButton;
    }

    private Button createContinueButton()
    {
        Button continueButton = new Button("Continue");
        continueButton.setId("continueButton");
        continueButton.setMinHeight(60);
        continueButton.setMinWidth(130);
        return continueButton;
    }

    protected void hideOldWindow(ActionEvent actionEvent)
    {
        Window window = ((Node)(actionEvent.getSource())).getScene().getWindow();
        window.setOnHidden(e -> mediaPlayer.stop());
        window.hide();
    }

    @FXML
    protected void loadFileButton(ActionEvent actionEvent)
    {
        boolean success = menuBarOptions.loadFile(viewModel);
        if (!success)
        {
            raisePopupWindow("Couldn't load file\nChoose a legal file (type *.maze)", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.ERROR);
            return;
        }
        hideOldWindow(actionEvent);
    }

}
