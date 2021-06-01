package View.controllers;

import algorithms.mazeGenerators.*;
import algorithms.mazeGenerators.Maze;
import com.sun.jdi.IntegerValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
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
    public TextField EnterColsText;
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

//    public void getRowsInput()
//    {
//        String rowsInput = EnterRowsText.getText();
//        try
//        {
//            rows = Integer.parseInt(rowsInput);
//        }
//        catch (Exception e)
//        {
//            raiseErrorWindow();
//        }
//    }
//
//    public void getColsInput()
//    {
//        String colsInput = EnterColsText.getText();
//        try
//        {
//            cols = Integer.parseInt(colsInput);
//        }
//        catch (Exception e)
//        {
//            raiseErrorWindow();
//        }
//    }

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
        int rows = -1;
        int cols = -1;
        String mazeChoice = null;
        AMazeGenerator mg;
        try
        {
            rows = Integer.parseInt(rowsInput);
            cols = Integer.parseInt(colsInput);
            mazeChoice = (String)primChoice.getValue();
            if(mazeChoice == null)
            {
                raiseErrorWindow("You need to choose maze type");
            }
            else
            {
                mg = ChooseMaze(mazeChoice);
                Maze maze = mg.generate(rows, cols);
//                maze.print();
//                System.out.println("\n");
            }
        }
        catch (Exception e)
        {
            raiseErrorWindow("one of the input include value that is not a number, please try again");
        }
//        System.out.println(String.format("rows = %d, columns = %d, Choice: %s", rows, cols, mazeChoice));

    }

    private AMazeGenerator ChooseMaze(String mazeChoice)
    {
        AMazeGenerator mg = null;
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
