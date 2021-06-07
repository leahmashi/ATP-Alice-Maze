package View;

import IO.MyDecompressorInputStream;
import Model.IModel;
import Model.MyModel;
import View.controllers.MazeViewController;
import ViewModel.MyViewModel;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;


public class MenuBarOptions
{
    private static ToggleButton toggleButton = new ToggleButton("ON");

    @FXML
    public void createNewFile(ActionEvent actionEvent, MediaPlayer mediaPlayer)
    {
        Parent root = null;
        try
        {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("View/FXMLs/ChooseMazeView.fxml"));
        } catch (IOException e)
        {
            e.printStackTrace();
            //TODO: what happens when cant be created?
        }
        root.setId("chooseMaze");
        Stage ChooseMazeStage = new Stage();
        Scene ChooseMazeScene = new Scene(root, 900, 650);
        ChooseMazeStage.setTitle("chooseMazeScene");
        ChooseMazeStage.setScene(ChooseMazeScene);
        ChooseMazeStage.show();

        ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow().setOnHidden(e -> mediaPlayer.stop());
        ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow().hide();
    }

    @FXML
    public void loadFile(ActionEvent actionEvent, MediaPlayer mediaPlayer, MyViewModel viewModel)
    {
        Stage loadStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        File file = fileChooser.showOpenDialog(loadStage);
        if (file != null)
        {
            viewModel.loadMaze(file.toString(), actionEvent);
        }

        ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow().setOnHidden(e -> mediaPlayer.stop());
        ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow().hide();
    }

    @FXML
    public void showProperties(ActionEvent actionEvent, MyViewModel viewModel, MediaPlayer mediaPlayer)
    {
        Window window = ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow();
        viewModel.showProperties(window, mediaPlayer);
        ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow().setOnHidden(e -> mediaPlayer.stop());
        ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow().hide();
    }

    @FXML
    public boolean showSettings(ActionEvent actionEvent, MediaPlayer mediaPlayer)
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

    private boolean setMusic(MediaPlayer mediaPlayer, ActionEvent actionEvent)
    {
        System.out.println(toggleButton.isSelected());
        if (toggleButton.isSelected()) //TODO style the toggle button and add label
        {
            mediaPlayer.stop();
            actionEvent.consume();
            toggleButton.setText("OFF");
            return true;
        }
        else
        {
            mediaPlayer.play();
            actionEvent.consume();
            toggleButton.setText("ON");
            return false;
        }
    }

    @FXML
    public void showHelp(ActionEvent actionEvent)
    {
        String text = "play as Alice and help the cards paint the roses red.\n" +
                        "to move right press the right arrow or numpad 6\n" +
                        "to move left press the left arrow or numpad 4\n" +
                        "to move down press the down arrow or numpad 2\n" +
                        "to move up press the up arrow or numpad 8\n" +
                        "to move diagonally:\n" +
                            "diagonall up right press numpad 9\n" +
                            "diagonall up left press numpad 7\n" +
                            "diagonal down right press numpad 3\n" +
                            "diagonal down left press numpad 1";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text, ButtonType.OK);
        alert.setContentText(text);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
        alert.setTitle("Instruction");
        alert.showAndWait();
    }

    @FXML
    public void showAbout(ActionEvent actionEvent)
    {
        String text ="we are Leah and Shahar students information system degree in ben-gurion university\n" +
                     "Shahar love to code in his available time\n" +
                     "Leah \n" +
                     "we use these algorithm:\n" +
                     "1. Randomized prim - we use this algorithm to create random maze\n" +
                     "2. Simple maze - we use this algorithm to create simple maze\n" +
                     "3. Best fisrt search - we use this algorithm to solve the maze. this is the fastest algorithm that solve the maze\n" +
                     "4. Breadth first search - we use this algorithm to solve the maze\n" +
                     "5. Depth first search - we use this algorithm to solve the maze. this is the slowest algorithm to solve the maze\n";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text, ButtonType.OK);
        alert.setContentText(text);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
        alert.setTitle("About us");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    public void exitProgram(ActionEvent actionEvent)
    {
        System.exit(0);
    }


}
