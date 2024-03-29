package View.controllers;


import View.AView;
import View.MazeDisplayer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class MazeViewController extends AView
{
    public boolean isFinish;
    public int _rows;
    public int _cols;
    public boolean _isLoaded;
    private int scrollCount;

    @FXML
    private MazeDisplayer mazeDisplayerFXML;
    @FXML
    ScrollPane ScrollPane;
    @FXML
    BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Platform.runLater(() -> {
            if (!_isLoaded)
                viewModel.generateMaze(_rows, _cols);
            Media musicFile = new Media(new File("resources/music/PaintingTheRosesRed.mp3").toURI().toString());
            setMusic(musicFile);
            mazeDisplayerFXML.widthProperty().bind(ScrollPane.widthProperty());
            mazeDisplayerFXML.heightProperty().bind(ScrollPane.heightProperty());
            mazeDisplayerFXML.widthProperty().addListener(event -> mazeDisplayerFXML.drawMaze(viewModel.getMaze()));
            mazeDisplayerFXML.heightProperty().addListener(event -> mazeDisplayerFXML.drawMaze(viewModel.getMaze()));

        });
    }

    @FXML
    public void keyPressed(KeyEvent keyEvent)
    {
        viewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col)
    {
        mazeDisplayerFXML.setPlayerPosition(row, col);
        if (row == viewModel.getMaze().getGoalPosition().getRowIndex() && col == viewModel.getMaze().getGoalPosition().getColumnIndex())
        {
            if (!isFinish)
            {
                isFinish = true;
                Stage clipStage = new Stage();
                BorderPane borderPane = new BorderPane();
                borderPane.setMinHeight(500);
                borderPane.setMinWidth(500);
                Media media = new Media(new File("resources/clips/finalStageClip.mp4").toURI().toString());
                Window currWindow = mazeDisplayerFXML.getScene().getWindow();
                MediaPlayer mediaPlayerCurr = mediaPlayer;
                currWindow.setOnHidden(e -> mediaPlayerCurr.stop());
                addClip(clipStage, borderPane, media, "FXMLs/finalSceneView.fxml", "finalStage", "finalStage");
                addControlButtons(borderPane, clipStage, "FXMLs/finalSceneView.fxml", "finalStage", "finalStage");
                Scene clipScene = new Scene(borderPane, 900, 650);
                clipScene.getStylesheets().add("CSSs/finalStageStyle.css");
                clipStage.setScene(clipScene);
                clipStage.setTitle("veryMerryUnbirthday");
                currWindow.hide();
                clipStage.showAndWait();
            }
        }
    }

    @FXML
    public void mouseClicked() { mazeDisplayerFXML.requestFocus(); }

    public void initData(int rows, int cols, boolean isLoaded)
    {
        _rows = rows;
        _cols = cols;
        _isLoaded = isLoaded;
    }

    @FXML
    public void returnToMain(ActionEvent actionEvent)
    {
        Stage MainWindowStage = changeScene("FXMLs/MyView.fxml", "mainScene", "mainWindow");
        MainWindowStage.getScene().getWindow().setOnHidden(e -> mediaPlayer.stop());
        hideOldWindow(actionEvent);
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
        mazeDisplayerFXML.drawMaze(viewModel.getMaze());
        playerMoved();
    }

    private void playerMoved() { setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol()); }

    @FXML
    private void mazeSolved() { mazeDisplayerFXML.setSolution(viewModel.getSolution()); }

    @FXML
    public void solveMaze() { viewModel.solveMaze(); }


    @FXML
    public void saveFile()
    {
        boolean success;
        Stage saveStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fileChooser.setInitialFileName("*.maze");
        File file = fileChooser.showSaveDialog(saveStage);
        if (file != null)
        {
            success = viewModel.saveMaze(file);
            if (success)
                raisePopupWindow("The maze was saved successfully", "resources/clips/SuccessClip.mp4", Alert.AlertType.INFORMATION);
            else
                raisePopupWindow("The maze wasn't saved please choose a legal file (type *.maze)", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.INFORMATION);
        }
    }
    public void zoom(ScrollEvent scrollEvent) {
        double zoomFactor = 1.05;
        double deltaY = scrollEvent.getDeltaY();
        if (scrollEvent.isControlDown()) {
            if (deltaY < 0) {
                if (scrollCount < -(viewModel.getMaze().getMazeArray().length + viewModel.getMaze().getMazeArray()[0].length) / 9) {
                    scrollEvent.consume();
                    return;
                }

                scrollCount--;
                mazeDisplayerFXML.setScaleY(mazeDisplayerFXML.getScaleY() / zoomFactor);
                mazeDisplayerFXML.setScaleX(mazeDisplayerFXML.getScaleX() / zoomFactor);

            } else if (scrollEvent.getDeltaY() > 0) {
                if (scrollCount > (viewModel.getMaze().getMazeArray().length + viewModel.getMaze().getMazeArray()[0].length) / 9) {
                    scrollEvent.consume();
                    return;
                }
                scrollCount++;
                mazeDisplayerFXML.setScaleY(mazeDisplayerFXML.getScaleY() * zoomFactor);
                mazeDisplayerFXML.setScaleX(mazeDisplayerFXML.getScaleX() * zoomFactor);

            } else if (scrollEvent.getDeltaY() == 0)
                return;
            mazeDisplayerFXML.drawMaze(viewModel.getMaze());
        }
        scrollEvent.consume();

    }
}
