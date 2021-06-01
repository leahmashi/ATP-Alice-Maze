package View.controllers;


import algorithms.mazeGenerators.Maze;
import com.sun.jdi.IntegerValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class MazeViewController
{
    private static MediaPlayer mediaPlayer;
    private int rows;
    private int cols;
    @FXML
    public TextField EnterRowsText;


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

    public void getRowsInput(KeyEvent keyEvent)
    {
        String rowsInput = EnterRowsText.getText();
        try
        {
            rows = Integer.parseInt(rowsInput);
        }
        catch (Exception e)
        {
            raiseErrorWindow();
        }
    }

    public void getColsInput(KeyEvent keyEvent)
    {
        String rowsInput = EnterRowsText.getText();
        try
        {
            rows = Integer.parseInt(rowsInput);
        }
        catch (Exception e)
        {
            raiseErrorWindow();
        }
    }

    public void raiseErrorWindow()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("One of the values you entered isn't an integer, please try again");
        alert.show();
    }

    public void generateMaze(ActionEvent actionEvent)
    {

    }
}
