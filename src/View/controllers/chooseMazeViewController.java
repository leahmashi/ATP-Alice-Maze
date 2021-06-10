package View.controllers;


import Server.Configurations;
import View.AView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class chooseMazeViewController extends AView
{

    @FXML
    public TextField EnterRowsText;
    @FXML
    public TextField EnterColsText;
    @FXML
    public ChoiceBox primChoice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        configurations = Configurations.getInstance();
        Media musicFile = new Media(new File("resources/music/AliceChooseMazeMusic.mp3").toURI().toString());
        setMusic(musicFile);
    }

    public void generateMaze(ActionEvent actionEvent)
    {
        String colsInput = EnterColsText.getText();
        String rowsInput = EnterRowsText.getText();
        int rows;
        int cols;
        try
        {
            rows = Integer.parseInt(rowsInput);
            cols = Integer.parseInt(colsInput);
            if (rows <= 2 || cols <= 2)
            {
                raisePopupWindow("One or more inputs isn't a positive number over 2, please try again", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.INFORMATION);
                return;
            }
            String mazeChoice = (String) primChoice.getValue();
            if (mazeChoice == null)
            {
                raisePopupWindow("Please choose a maze type", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.INFORMATION);
                return;
            }
            else
                ChooseMaze(mazeChoice);
        }
        catch (Exception e)
        {
            raisePopupWindow("One or more inputs isn't a positive number over 2, please try again", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.INFORMATION);
            return; //stay on choose scene
        }

        Stage MazeWindowStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/FXMLs/MazeView.fxml"));
        Parent root;
        try {
            root = fxmlLoader.load();
            root.setId("mazeScene");
            MazeViewController controller = fxmlLoader.getController();
            viewModel.addObserver(controller);
            Scene MazeWindowScene = new Scene(root);
            controller.initData(rows, cols, false);
            MazeWindowStage.setScene(MazeWindowScene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MazeWindowStage.setOnCloseRequest(e -> System.exit(0));
        MazeWindowStage.show();

        ((Node)(actionEvent.getSource())).getScene().getWindow().setOnHidden(e -> mediaPlayer.stop());
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();


    }

    public void returnToMain(ActionEvent actionEvent)
    {
        changeScene("View/FXMLs/MyView.fxml", "mainScene", "mainWindow");
        ((Node)(actionEvent.getSource())).getScene().getWindow().setOnHidden(e -> mediaPlayer.stop());
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    private void ChooseMaze(String mazeChoice)
    {
        switch (mazeChoice)
        {
            case "Simple" -> configurations.setMazeGeneratingAlgorithm("SimpleMazeGenerator");
            case "Randomized Prim" -> configurations.setMazeGeneratingAlgorithm("MyMazeGenerator");
        }
    }
}
