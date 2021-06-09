package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;


public class MenuBarOptions
{
    private static ToggleButton toggleButton = new ToggleButton("ON");

    @FXML
    public boolean createNewFile(ActionEvent actionEvent, MediaPlayer mediaPlayer)
    {
        Parent root = null;
        try
        {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("View/FXMLs/ChooseMazeView.fxml"));
        } catch (IOException e)
        {
            e.printStackTrace(); //TODO: remove at end
            //TODO: raise popup window
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
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVBox = new VBox(20);
        dialogVBox.getChildren().add(toggleButton);
        toggleButton.setId("toggleButton");
        toggleButton.getStylesheets().addAll(this.getClass().getResource("CSSs/ToggleButton.css").toExternalForm());
        toggleButton.setOnAction((e) -> setMusic(mediaPlayer, e));
        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
        return !toggleButton.isSelected();

    }

    private void setMusic(MediaPlayer mediaPlayer, ActionEvent actionEvent)
    {
        if (toggleButton.isSelected()) //TODO style the toggle button and add label
        {
            mediaPlayer.stop();
            actionEvent.consume();
            toggleButton.setText("OFF");
        }
        else
        {
            mediaPlayer.play();
            actionEvent.consume();
            toggleButton.setText("ON");
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
        alert.setTitle("Instruction");
        alert.showAndWait();
    }

    @FXML
    public void showAbout()
    {
        String text = """
                we are Leah and Shahar students information system degree in Ben-Gurion university
                Shahar love to code in his available time
                Leah\s
                we use these algorithm:
                1. Randomized prim - we use this algorithm to create random maze
                2. Simple maze - we use this algorithm to create simple maze
                3. Best first search - we use this algorithm to solve the maze. this is the fastest algorithm that solve the maze
                4. Breadth first search - we use this algorithm to solve the maze
                5. Depth first search - we use this algorithm to solve the maze. this is the slowest algorithm to solve the maze
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
    public void exitProgram()
    {
        System.exit(0);
    }


}
