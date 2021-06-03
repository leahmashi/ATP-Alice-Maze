package View.controllers;


import View.MazeDisplayer;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MazeViewController implements Initializable
{
    public AMazeGenerator generator;
    public int _rows;
    public int _cols;
    public Maze maze;
    private static MediaPlayer mediaPlayer;

    @FXML
    private MazeDisplayer mazeDisplayerFXML;

    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    public String getUpdatePlayerRow() { return updatePlayerRow.get(); }
    public void setUpdatePlayerRow(int updatePlayerRow) { this.updatePlayerRow.set(updatePlayerRow + ""); }
    public String getUpdatePlayerCol() { return updatePlayerCol.get(); }
    public void setUpdatePlayerCol(int updatePlayerCol) { this.updatePlayerCol.set(updatePlayerCol + ""); }


//    MazeViewController(int rows, int cols, AMazeGenerator mg)
//    {
//        _rows = rows;
//        _cols = cols;
//        generator = mg;
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
//        playerRow.textProperty().bind(updatePlayerRow);
//        playerCol.textProperty().bind(updatePlayerCol);
        Platform.runLater(() -> {
            maze = generator.generate(_rows, _cols);
            mazeDisplayerFXML.drawMaze(maze.getMazeArray());
        });

    }


    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //...
    }

    public void keyPressed(KeyEvent keyEvent)
    {
        int row = mazeDisplayerFXML.getPlayerRow();
        int col = mazeDisplayerFXML.getPlayerCol();

        switch (keyEvent.getCode())
        {
            case UP -> row -= 1;
            case DOWN -> row += 1;
            case RIGHT -> col += 1;
            case LEFT -> col -= 1;
        }
        setPlayerPosition(row, col);

        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col)
    {
        mazeDisplayerFXML.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    public void mouseClicked(MouseEvent mouseEvent)
    {
        mazeDisplayerFXML.requestFocus();
    }

    void initData(int rows, int cols, AMazeGenerator mg)
    {
        _rows = rows;
        _cols = cols;
        generator = mg;
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

//        ((Node)(actionEvent.getSource())).getScene().getWindow().setOnHidden(e -> {
//            mediaPlayer.stop();
//        });

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

        MainWindowStage.getScene().getWindow().setOnHidden(e -> {
            mediaPlayer.stop();
        });
    }

}
