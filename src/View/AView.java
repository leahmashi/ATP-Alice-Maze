package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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

    @FXML
    public void createNewFile(ActionEvent actionEvent)
    {
        boolean success = menuBarOptions.createNewFile(actionEvent, mediaPlayer);
        if (!success)
        {
            raisePopupWindow("couldn't create new maze", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.INFORMATION);
            return;
        }
    }

    @FXML
    public void loadFile(ActionEvent actionEvent)
    {
        boolean success = menuBarOptions.loadFile(viewModel);
        if (!success)
        {
            raisePopupWindow("couldn't load file choose a legal file (type *.maze)", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.INFORMATION);
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
        menuBarOptions.showHelp(actionEvent, mediaPlayer, this, window);
    }

    @FXML
    public void showAbout(ActionEvent actionEvent) { menuBarOptions.showAbout(); }
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
        alert.setTitle("popupWindow");
        alert.setHeaderText(text);
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
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.play();
            }
        });
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


    protected void addContinueButton(BorderPane borderPane, Stage clipStage, String nextSceneFxml, String nextStageTitle, String nextRootID)
    {
        Button continueButton = new Button("continue");
        continueButton.setId("continueButton");
        continueButton.prefHeight(60);
        continueButton.prefWidth(130);
        VBox vBox = new VBox(4);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().add(continueButton);
        borderPane.setBottom(vBox);

        continueButton.setOnAction(actionEvent -> {
            mediaPlayer.stop();
            changeScene(nextSceneFxml, nextStageTitle, nextRootID);
            clipStage.hide();
            clipStage.close();
        });
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
            raisePopupWindow("couldn't load file choose a legal file (type *.maze)", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.INFORMATION);
            return;
        }
        hideOldWindow(actionEvent);
    }

    @Override
    public MediaPlayer getMediaPlayer() { return mediaPlayer; }
}
