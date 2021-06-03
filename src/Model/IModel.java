package Model;


import algrorithms.mazeGenerators.Maze;
import algrorithms.search.Solution;

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
}
