package View.controllers;


import View.AView;
import View.MazeDisplayer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.Observable;
import java.util.ResourceBundle;

public class MazeViewController extends AView
{
    public int _rows;
    public int _cols;
    public boolean _isLoaded;

    @FXML
    private MazeDisplayer mazeDisplayerFXML;
    @FXML
    private ScrollPane pane;

    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    //getters & setters
    public void setUpdatePlayerRow(int updatePlayerRow) { this.updatePlayerRow.set(updatePlayerRow + ""); }
    public void setUpdatePlayerCol(int updatePlayerCol) { this.updatePlayerCol.set(updatePlayerCol + ""); }
    public String getUpdatePlayerRow() { return updatePlayerRow.get(); }
    public StringProperty updatePlayerRowProperty() { return updatePlayerRow; }
    public String getUpdatePlayerCol() { return updatePlayerCol.get(); }
    public StringProperty updatePlayerColProperty() { return updatePlayerCol; }

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
        if (row == viewModel.getMaze().getGoalPosition().getRowIndex() && col == viewModel.getMaze().getGoalPosition().getColumnIndex())
        {
            Stage clipStage = new Stage();
            BorderPane borderPane = new BorderPane();
            borderPane.setMinHeight(500);
            borderPane.setMinWidth(500);
            Media media = new Media(new File("resources/clips/finalStageClip.mp4").toURI().toString());
            addClip(clipStage, borderPane, media, "View/FXMLs/finalSceneView.fxml", "finalStage", "finalStage");
            addContinueButton(borderPane, clipStage, "View/FXMLs/finalSceneView.fxml", "finalStage", "finalStage");
            Scene clipScene = new Scene(borderPane, 900, 650);
            clipScene.getStylesheets().add("View/CSSs/finalStageStyle.css");
            clipStage.setScene(clipScene);
            clipStage.setTitle("veryMerryUnbirthday");
            clipStage.showAndWait();

            Window currWindow = mazeDisplayerFXML.getScene().getWindow();
            currWindow.setOnHidden(e -> mediaPlayer.stop());
            currWindow.hide();
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
        Stage MainWindowStage = changeScene("View/FXMLs/MyView.fxml", "mainScene", "mainWindow");
        MainWindowStage.getScene().getWindow().setOnHidden(e -> mediaPlayer.stop());

        Window window = ((Node)(actionEvent.getSource())).getScene().getWindow();
        window.setOnHidden(e -> mediaPlayer.stop());
        window.hide();
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
    public void solveMaze() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
        viewModel.solveMaze();
    }

    public void zoom(ScrollEvent scrollEvent) { mazeDisplayerFXML.zoom(scrollEvent); }

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
}
