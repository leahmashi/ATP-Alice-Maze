package Model;


import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;


import java.io.File;
import java.util.Observer;

public interface IModel
{
    void generateMaze(int rows, int cols);
    Maze getMaze();
    void solveMaze();
    Solution getSolution();
    void updatePlayerLocation(MovementDirection direction);
    int getPlayerRow();
    int getPlayerCol();
    void assignObserver(Observer observer);
    boolean saveMaze(File file);
    boolean loadMaze(File file);
}
