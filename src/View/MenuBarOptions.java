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
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    public void showProperties(ActionEvent actionEvent)
    {

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

    }

    @FXML
    public void showAbout(ActionEvent actionEvent)
    {

    }

    @FXML
    public void exitProgram(ActionEvent actionEvent)
    {
        System.exit(0);
    }


}
