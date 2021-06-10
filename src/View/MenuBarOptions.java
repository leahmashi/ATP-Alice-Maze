package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class MenuBarOptions
{
    private static final ToggleButton musicSwitch = new ToggleButton("ON");
    private static Label musicLabel;
    private static Slider slider;

    @FXML
    public boolean createNewFile(ActionEvent actionEvent, MediaPlayer mediaPlayer)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/FXMLs/ChooseMazeView.fxml")));
        } catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            String text = "File is missing";
            alert.setContentText(text);
            alert.showAndWait();
            return false;

        }
        root.setId("chooseMaze");
        Stage ChooseMazeStage = new Stage();
        Scene ChooseMazeScene = new Scene(root, 900, 650);
        ChooseMazeStage.setTitle("chooseMazeScene");
        ChooseMazeStage.setScene(ChooseMazeScene);
        ChooseMazeStage.show();

        ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow().setOnHidden(e -> mediaPlayer.stop());
        ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow().hide();
        return true;
    }

    @FXML
    public boolean loadFile(ActionEvent actionEvent, MediaPlayer mediaPlayer, MyViewModel viewModel)
    {
        Stage loadStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        File file = fileChooser.showOpenDialog(loadStage);
        if (file != null)
        {
            boolean success = viewModel.loadMaze(file);
            if (!success)
                return false;
        }

        ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow().setOnHidden(e -> mediaPlayer.stop());
        ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow().hide();
        return true;
    }

    @FXML
    public void showProperties(ActionEvent actionEvent, MyViewModel viewModel, MediaPlayer mediaPlayer)
    {
        Window window = ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow();
        viewModel.showProperties(window, mediaPlayer);
        window.setOnHidden(e -> mediaPlayer.stop());
        window.hide();
    }

    @FXML
    public boolean showSettings(MediaPlayer mediaPlayer)
    {
        Stage dialog = new Stage();
        dialog.setTitle("Music Setting");
        dialog.initModality(Modality.APPLICATION_MODAL);
        AnchorPane pane = new AnchorPane();
        //toggle switch
        musicLabel = new Label("Music ON");
        HBox hbox = new HBox(2);
        hbox.setPadding(new Insets(0, 10, 10, 10));
        hbox.setSpacing(10);
        hbox.getChildren().addAll(musicSwitch, musicLabel);
        //slider
        Label label = new Label("Volume");
        slider = new Slider();
        slider.setMax(100);
        slider.setValue(mediaPlayer.getVolume() * 100);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(40, 60, 60, 60));
        vbox.setSpacing(10);
        vbox.getChildren().addAll(hbox, label, slider);
        pane.getChildren().addAll(vbox);
        musicSwitch.setOnAction(actionEvent -> setMusic(mediaPlayer, actionEvent));
        slider.valueProperty().addListener(observable -> {
            if (slider.isValueChanging())
                mediaPlayer.setVolume(slider.getValue() / 100.0);
        });
        Scene scene = new Scene(pane);
        dialog.setScene(scene);
        dialog.showAndWait();
        return musicSwitch.isSelected();
    }

    private void setMusic(MediaPlayer mediaPlayer, ActionEvent actionEvent)
    {
        if (musicSwitch.isSelected()) //TODO style the toggle button and add label
        {
            mediaPlayer.stop();
            actionEvent.consume();
            musicSwitch.setText("OFF");
            musicLabel.setText("Music OFF");
            slider.setValue(0);
        }
        else
        {
            mediaPlayer.play();
            actionEvent.consume();
            musicSwitch.setText("ON");
            musicLabel.setText("Music ON");
            slider.setValue(100);
        }
    }

    @FXML
    public void showHelp()
    {
        String text = """
                The queen has summoned you!
                get to her as fast as you can while painting the roses red
                to move right press the right arrow or numpad 6
                to move left press the left arrow or numpad 4
                to move down press the down arrow or numpad 2
                to move up press the up arrow or numpad 8
                to move diagonally:
                diagonal up right press numpad 9
                diagonal up left press numpad 7
                diagonal down right press numpad 3
                diagonal down left press numpad 1""";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text, ButtonType.OK);
        alert.setContentText(text);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
        alert.setTitle("Instructions");
        alert.showAndWait();
    }

    @FXML
    public void showAbout()
    {
        String text = """
                Leah and Shahar are students from information systems in Ben-Gurion university
                Shahar loves to code in his available time
                Leah is a disney lover, and likes to dance and sing in her spare time
                They used these algorithms to create and solve the maze:
                1. Randomized prim - allows the user to create a random maze based on this algorithm.
                2. Simple maze - allows the user to create a simple maze
                3. Best first search - allows the user to solve the maze with the cheapest path (moving diagonal is cheaper)
                4. Breadth first search - allows the user to solve the maze in the shortest time.
                5. Depth first search - allows the user to solve the maze with a random path.
                """;
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text, ButtonType.OK);
        alert.setContentText(text);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
        alert.setTitle("About us");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    public void exitProgram() { System.exit(0); }

}
