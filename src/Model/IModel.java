package Model;


import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.event.ActionEvent;


import java.io.File;
import java.util.Observer;

public interface IModel
{
    void generateMaze(int rows, int cols);
    Maze getMaze();
    void solveMaze();
    Solution getSolution();
    void updatePlayerLocation(MovementDirection direction);// responsible for finish line
    int getPlayerRow();
    int getPlayerCol();
    void assignObserver(Observer observer);
    void saveMaze(File file);
    void loadMaze(String fileName);
}
