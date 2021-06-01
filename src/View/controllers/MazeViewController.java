package View.controllers;


import algorithms.mazeGenerators.Maze;
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


    public void initialize() throws Exception
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
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
        {
            System.out.println("here");
        }
    }

    public void getColsInput(KeyEvent keyEvent)
    {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
        {
            System.out.println("here2");
        }
    }

//    int rows = Integer.valueOf(textField_mazeRows.getText());
//    int cols = Integer.valueOf(textField_mazeColumns.getText());
//    String mazeGeneratorType = textField_mazeGeneratorType.getText();
//
//    boolean setSuccess = setGenerator(mazeGeneratorType);
//        if (setSuccess)
//    {
//        Maze maze = mazeGenrator.generate(rows, cols);
//
//    }

}
