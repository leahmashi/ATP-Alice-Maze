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
    private int rows;
    private int cols;

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

    public void raiseErrorWindow(String text)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(text);
        alert.show();
    }

    public void generateMaze(ActionEvent actionEvent)
    {
        String colsInput = EnterColsText.getText();
        String rowsInput = EnterRowsText.getText();
        try
        {
            rows = Integer.parseInt(rowsInput);
            cols = Integer.parseInt(colsInput);
            String mazeChoice = (String) primChoice.getValue();
            if (mazeChoice == null)
                raiseErrorWindow("Please choose a maze type");
            else
                ChooseMaze(mazeChoice);
        }
        catch (Exception e)
        {
            raiseErrorWindow("One or more inputs isn't a positive number, please try again");
            return; //stay on choose scene
        }

        Stage MazeWindowStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/FXMLs/MazeView.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        root.setId("mazeScene");
        MazeViewController controller = fxmlLoader.getController();
        viewModel.addObserver(controller);
        Scene MazeWindowScene = new Scene(root);
        controller.initData(rows, cols, false);
        MazeWindowStage.setOnCloseRequest(e -> {
            System.exit(0);
        });

        MazeWindowStage.setScene(MazeWindowScene);
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
