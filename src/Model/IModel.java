package Model;


import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;


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
}
