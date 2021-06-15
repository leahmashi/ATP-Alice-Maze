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
    public boolean loadFile(MyViewModel viewModel)
    {
        Stage loadStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        File file;
        try
        {
             file = fileChooser.showOpenDialog(loadStage);
        } catch (Exception e) {
            return false;
        }
        if (file == null)
            return false;
        else
        {
            return viewModel.loadMaze(file);
        }
    }

    @FXML
    public boolean showSettings(MediaPlayer mediaPlayer)
    {
        Stage dialog = new Stage();
        dialog.setTitle("Music Setting");
        dialog.initModality(Modality.APPLICATION_MODAL);
        AnchorPane pane = new AnchorPane();
        //toggle switch
        String status;
        if(musicSwitch.isSelected())
            status = "Music OFF";
        else
            status = "Music ON";
        musicLabel = new Label(status);
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
            {
                mediaPlayer.setVolume(slider.getValue() / 100.0);
                if (slider.getValue() == 0)
                {
                    musicSwitch.setSelected(true);
                    musicSwitch.setText("OFF");
                    musicLabel.setText("Music OFF");
                    mediaPlayer.stop();
                }
                else
                {
                    musicSwitch.setSelected(false);
                    musicSwitch.setText("ON");
                    musicLabel.setText("Music ON");
                    mediaPlayer.play();
                }
            }
        });
        Scene scene = new Scene(pane);
        dialog.setScene(scene);
        dialog.showAndWait();
        return musicSwitch.isSelected();
    }

    private void setMusic(MediaPlayer mediaPlayer, ActionEvent actionEvent)
    {
        if (musicSwitch.isSelected())
        {
            mediaPlayer.stop();
            actionEvent.consume();
            musicSwitch.setText("OFF");
            musicLabel.setText("Music OFF");
            slider.setValue(0);
            mediaPlayer.setVolume(0);
        }
        else
        {
            mediaPlayer.play();
            actionEvent.consume();
            musicSwitch.setText("ON");
            musicLabel.setText("Music ON");
            slider.setValue(100);
            mediaPlayer.setVolume(100);
        }
    }

    @FXML
    public void showHelp()
    {
        String text = """
                The queen has summoned you!
                Get to her as fast as you can!
                You can move:
                    Right by pressing the right arrow or numpad 6
                    Left by pressing the left arrow or numpad 4
                    Down by pressing the down arrow or numpad 2
                    UP by pressing the up arrow or numpad 8
                    Or even quicker diagonally:
                        diagonal up right by pressing numpad 9
                        diagonal up left by pressing numpad 7
                        diagonal down right by pressing numpad 3
                        diagonal down left by pressing numpad 1""";
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
