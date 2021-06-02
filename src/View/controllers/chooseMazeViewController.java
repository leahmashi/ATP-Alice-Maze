package View.controllers;

import View.IView;
import View.MazeDisplayer;
import algorithms.mazeGenerators.*;
import algorithms.mazeGenerators.Maze;
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
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class chooseMazeViewController
{
    private static MediaPlayer mediaPlayer;
    private int rows;
    private int cols;
    private AMazeGenerator mg;
    @FXML
    public TextField EnterRowsText;
    @FXML
    public TextField EnterColsText;
    @FXML
    public ChoiceBox primChoice;


    public void initialize()
    {
        Media musicFile = new Media(new File("resources/AliceChooseMazeMusic.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(musicFile);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });
    }

    public void raiseErrorWindow(String text)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(text);
        alert.show();
    }

    public void generateMaze() throws IOException
    {
        String colsInput = EnterColsText.getText();
        String rowsInput = EnterRowsText.getText();
        String mazeChoice;
        try {
            rows = Integer.parseInt(rowsInput);
            cols = Integer.parseInt(colsInput);
            mazeChoice = (String) primChoice.getValue();
            if (mazeChoice == null) {
                raiseErrorWindow("Please choose a maze type");
            }
            else
            {
                mg = ChooseMaze(mazeChoice);
                Maze maze = mg.generate(rows, cols);
            }
        }
        catch (Exception e)
        {
            raiseErrorWindow("One or more inputs isn't a positive number, please try again");
        }



//        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/FXMLs/MazeView.fxml"));
//        root.setId("maze");
//        Stage MazeWindowStage = new Stage();
//        Scene MazeWindowScene = new Scene(root, 900, 650);
//        MazeWindowStage.setTitle("mazeScene");
//        MazeWindowStage.setScene(MazeWindowScene);
//        MazeWindowStage.show();
//        System.out.println(String.format("rows = %d, columns = %d, Choice: %s", rows, cols, mazeChoice));
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/FXMLs/MyView.fxml"));
        root.setId("mainWindow");
        Stage MainWindowStage = new Stage();
        Scene MainWindowScene = new Scene(root, 900, 650);
        MainWindowStage.setTitle("mainScene");
        MainWindowStage.setScene(MainWindowScene);
        MainWindowStage.show();

        ((Node)(actionEvent.getSource())).getScene().getWindow().setOnHidden(e -> {
            mediaPlayer.stop();
        });

        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        Media musicFile = new Media(getClass().getClassLoader().getResource("AliceMainWindowMusic.mp3").toString());
        mediaPlayer = new MediaPlayer(musicFile);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });
    }

    private AMazeGenerator ChooseMaze(String mazeChoice)
    {
        switch(mazeChoice)
        {
            case "Simple":
                mg = new SimpleMazeGenerator();
                break;
            case "Randomized Prim":
                mg = new MyMazeGenerator();
                break;
        }
        return mg;
    }
}
