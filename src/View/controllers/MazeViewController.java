package View.controllers;


import View.MazeDisplayer;
import algorithms.mazeGenerators.AMazeGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MazeViewController implements Initializable
{
    public AMazeGenerator generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;

    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    public String getUpdatePlayerRow() { return updatePlayerRow.get(); }
    public void setUpdatePlayerRow(int updatePlayerRow) { this.updatePlayerRow.set(updatePlayerRow + ""); }
    public String getUpdatePlayerCol() { return updatePlayerCol.get(); }
    public void setUpdatePlayerCol(int updatePlayerCol) { this.updatePlayerCol.set(updatePlayerCol + ""); }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
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
        int row = mazeDisplayer.getPlayerRow();
        int col = mazeDisplayer.getPlayerCol();

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

    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
}
