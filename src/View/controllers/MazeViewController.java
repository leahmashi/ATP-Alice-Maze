package View.controllers;


import View.AView;
import View.MazeDisplayer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class MazeViewController extends AView
{
    public int _rows;
    public int _cols;
    public boolean _isLoaded;

    @FXML
    private MazeDisplayer mazeDisplayerFXML;

    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    //getters & setters
    public String getUpdatePlayerRow() { return updatePlayerRow.get(); }
    public void setUpdatePlayerRow(int updatePlayerRow) { this.updatePlayerRow.set(updatePlayerRow + ""); }
    public String getUpdatePlayerCol() { return updatePlayerCol.get(); }
    public void setUpdatePlayerCol(int updatePlayerCol) { this.updatePlayerCol.set(updatePlayerCol + ""); }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Platform.runLater(() -> {
            if (!_isLoaded)
                viewModel.generateMaze(_rows, _cols);
            Media musicFile = new Media(new File("resources/music/PaintingTheRosesRed.mp3").toURI().toString());
            setMusic(musicFile);
        });
    }

//    public void openFile(ActionEvent actionEvent) {
//        FileChooser fc = new FileChooser();
//        fc.setTitle("Open maze");
//        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
//        fc.setInitialDirectory(new File("./resources"));
//        File chosen = fc.showOpenDialog(null);
//        //...
//    }

    @FXML
    public void keyPressed(KeyEvent keyEvent)
    {
        viewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col)
    {
        mazeDisplayerFXML.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
        if(row == viewModel.getMaze().getGoalPosition().getRowIndex() && col == viewModel.getMaze().getGoalPosition().getColumnIndex())
        {
            Stage MazeWindowStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/FXMLs/FinishLineView.fxml"));
            Parent root = null;
            try
            {
                root = fxmlLoader.load();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            //MazeViewController controller = fxmlLoader.getController();
            Scene MazeWindowScene = new Scene(root);
            MazeWindowStage.setScene(MazeWindowScene);
            MazeWindowStage.show();
        }

    }


    @FXML
    public void mouseClicked(MouseEvent mouseEvent) { mazeDisplayerFXML.requestFocus(); }

    public void initData(int rows, int cols, boolean isLoaded)
    {
        _rows = rows;
        _cols = cols;
        _isLoaded = isLoaded;
    }

    @FXML
    public void returnToMain(ActionEvent actionEvent)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/FXMLs/MyView.fxml"));
        Parent root = null;
        try
        {
            root = fxmlLoader.load();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        root.setId("mainWindow");
        MyViewController controller = fxmlLoader.getController();
        viewModel.addObserver(controller);
        Stage MainWindowStage = new Stage();
        Scene MainWindowScene = new Scene(root, 900, 650);
        MainWindowStage.setTitle("mainScene");
        MainWindowStage.setScene(MainWindowScene);
        MainWindowStage.show();

        ((Node)(actionEvent.getSource())).getScene().getWindow().setOnHidden(e -> mediaPlayer.stop());
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();


        Media musicFile = new Media(getClass().getClassLoader().getResource("music/AliceMainWindowMusic.mp3").toString());
        mediaPlayer = new MediaPlayer(musicFile);
        setMediaPlayer(mediaPlayer);
        if (!isOff)
        {
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setOnEndOfMedia( new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.play();
                }
            });
        }

        MainWindowStage.getScene().getWindow().setOnHidden(e -> mediaPlayer.stop());
    }

    @Override
    public void update(Observable observable, Object arg)
    {
        String change = (String) arg;
        switch (change)
        {
            case "maze generated" -> mazeGenerated();
            case "maze solved" -> mazeSolved();
            case "player moved" -> playerMoved();
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    private void mazeGenerated()
    {
        mazeDisplayerFXML.drawMaze(viewModel.getMaze().getMazeArray());
        playerMoved();
    }

    private void playerMoved() { setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol()); }

    @FXML
    private void mazeSolved() { mazeDisplayerFXML.setSolution(viewModel.getSolution()); }

    @FXML
    public void solveMaze(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
        viewModel.solveMaze();
    }

    public void zoom(ScrollEvent scrollEvent) { mazeDisplayerFXML.zoom(scrollEvent); }

    @FXML
    public void saveFile(ActionEvent actionEvent)
    {
        Stage saveStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
//
//        //convert the maze to bytes
//        byte[] mazeByteArray = viewModel.getMaze().toByteArray();
//
//        //compress the bytes
//        byte[] compressedByteArray = compressEight(mazeByteArray);
//
        File file = fileChooser.showSaveDialog(saveStage);
        if (file != null)
        {
            viewModel.saveMaze(file);
        }


    }

    private void saveTextToFile(byte[] mazeByteArray, File file)
    {
        try (FileOutputStream out = new FileOutputStream(file))
        {
            out.write(mazeByteArray);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private byte[] compressEight(byte[] uncompressed)
    {
        int toCompressSize = uncompressed.length - 12;
        int size = (int) Math.ceil(toCompressSize / 8.0);
        byte[] compress = new byte[size + 12];
        System.arraycopy(uncompressed, 0, compress, 0, 12);
        int compressIndex = 12;
        int uncompressIndex = 12;
        while (compressIndex < compress.length)
        {
            int min = Math.min(toCompressSize, 8);
            String binaryString = "";
            for (int i = 0; i < min; i++)
            {
                if (uncompressed[uncompressIndex] == 1)
                    binaryString = binaryString.concat("1");
                else
                    binaryString = binaryString.concat("0");
                uncompressIndex++;
            }
            int decimal = Integer.parseInt(binaryString, 2);
            compress[compressIndex] = Integer.valueOf(decimal).byteValue();
            toCompressSize = toCompressSize - min;
            compressIndex++;
        }
        return compress;
    }
}
