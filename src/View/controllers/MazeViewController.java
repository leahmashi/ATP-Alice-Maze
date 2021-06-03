package View.controllers;


import View.MazeDisplayer;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MazeViewController implements Initializable
{
    public AMazeGenerator generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
//    public MazeDisplayer mazeDisplayer;
//    public Label playerRow;
//    public Label playerCol;
    public int _rows;
    public int _cols;
    public Maze maze;

    @FXML
    BorderPane borderPane;
    @FXML
    private MazeDisplayer mazeDisplayerFXML;
    @FXML
    private Pane pane;

    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    public String getUpdatePlayerRow() { return updatePlayerRow.get(); }
    public void setUpdatePlayerRow(int updatePlayerRow) { this.updatePlayerRow.set(updatePlayerRow + ""); }
    public String getUpdatePlayerCol() { return updatePlayerCol.get(); }
    public void setUpdatePlayerCol(int updatePlayerCol) { this.updatePlayerCol.set(updatePlayerCol + ""); }


    MazeViewController(int rows, int cols, AMazeGenerator mg)
    {
        _rows = rows;
        _cols = cols;
        generator = mg;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
//        playerRow.textProperty().bind(updatePlayerRow);
//        playerCol.textProperty().bind(updatePlayerCol);
        maze = generator.generate(_rows, _cols);
        mazeDisplayerFXML.drawMaze(maze.getMazeArray());
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

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayerFXML.requestFocus();
    }

    public void resize()
    {
        System.out.println("hello from resize;");
    }

    void initData(int rows, int cols, AMazeGenerator mg)
    {
        _rows = rows;
        _cols = cols;
        generator = mg;
    }
}
