package View.controllers;

import algorithms.mazeGenerators.*;
import algorithms.mazeGenerators.Maze;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    public void generateMaze()
    {
        String colsInput = EnterColsText.getText();
        String rowsInput = EnterRowsText.getText();
        String mazeChoice;
        try
        {
            rows = Integer.parseInt(rowsInput);
            cols = Integer.parseInt(colsInput);
            mazeChoice = (String)primChoice.getValue();
            if(mazeChoice == null)
            {
                raiseErrorWindow("Please choose a maze type");
            }
            else
            {
                mg = ChooseMaze(mazeChoice);
                Maze maze = mg.generate(rows, cols);

                int[][] mazeArr = maze.getMazeArray();
                Group root = new Group();
                for (int i = 0; i < mazeArr.length; i++)
                {
                    for (int j = 0; j < mazeArr[0].length; j++)
                    {
                        if (mazeArr[i][j] == 1)
                        {
                            MyNode node = new MyNode("1", i, j, 900, 650);
                            root.getChildren().add(node);
                        }
                        MyNode node = new MyNode("0", i, j, 900, 650);
                        root.getChildren().add(node);
                    }
                }

                Scene MazeWindowScene = new Scene(root, 900, 650);
                Stage MazeWindowStage = new Stage();
                MazeWindowStage.setTitle("mazeScene");
                MazeWindowStage.setScene(MazeWindowScene);
                MazeWindowStage.show();

//                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/FXMLs/MazeView.fxml"));
//                root.setId("maze");
//                Stage MazeWindowStage = new Stage();
//                Scene MazeWindowScene = new Scene(root, 900, 650);
//                MazeWindowStage.setTitle("mazeScene");
//                MazeWindowStage.setScene(MazeWindowScene);
//                MazeWindowStage.show();
            }
        }
        catch (Exception e)
        {
            raiseErrorWindow("One or more inputs isn't a positive number, please try again");
        }
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

class MyNode extends StackPane
{
    public MyNode(String name, double x, double y, double width, double height)
    {

        // create rectangle
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.LIGHTBLUE);

        // create label
        Label label = new Label(name);

        // set position
        setTranslateX(x);
        setTranslateY(y);

        getChildren().addAll(rectangle, label);

    }
}
