package Model;

import Server.ServerStrategyGenerateMaze;
import algrorithms.mazeGenerators.AMazeGenerator;
import algrorithms.mazeGenerators.Maze;
import algrorithms.search.Solution;

import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel
{
    private Maze maze;
    private Solution solution;
    private int playerRow;
    private int playerCol;
    private AMazeGenerator mg;

    public MyModel(AMazeGenerator mg)
    {
        this.mg = mg;
    }

    @Override
    public void assignObserver(Observer observer)
    {
        this.addObserver(observer);
    }

    @Override
    public void generateMaze(int rows, int cols)
    {
        //call generating server
        maze = mg.generate(rows, cols);
        playerCol = 0;
        playerRow = 0;
        setChanged();
        notifyObservers("maze generated");
    }

    @Override
    public Maze getMaze()
    {
        return maze;
    }

    @Override
    public void solveMaze()
    {
        //call solving server
        //catch the solution
        setChanged();
        notifyObservers("maze solved");
    }

    @Override
    public Solution getSolution()
    {
        return solution;
    }

    @Override
    public void updatePlayerLocation(MovementDirection direction)
    {
        int[][] mazeArr = maze.getMazeArray();
        switch (direction)
        {
            case UP -> {
                if (playerRow > 0 && mazeArr[playerRow - 1][playerCol] != 1)
                    updateLocation(playerRow - 1, playerCol);
            }
            case DOWN -> {
                if (playerRow < maze.getTotalRows() - 1 && mazeArr[playerRow + 1][playerCol] != 1)
                    updateLocation(playerRow + 1, playerCol);
            }
            case RIGHT -> {
                if (playerCol < maze.getTotalCols() - 1 && mazeArr[playerRow][playerCol + 1] != 1)
                    updateLocation(playerRow, playerCol + 1);
            }
            case LEFT -> {
                if (playerCol > 0 && mazeArr[playerRow][playerCol - 1] != 1)
                    updateLocation(playerRow, playerCol - 1);
            }
            case UPRIGHT -> {
                if (playerRow > 0 && playerCol < maze.getTotalCols() - 1 && mazeArr[playerRow - 1][playerCol + 1] != 1)
                    updateLocation(playerRow - 1, playerCol + 1);
            }
            case UPLEFT -> {
                if (playerRow > 0 && playerCol > 0 && mazeArr[playerRow - 1][playerCol - 1] != 1)
                    updateLocation(playerRow - 1, playerCol - 1);
            }
            case DOWNRIGHT -> {
                if (playerRow < maze.getTotalRows() - 1 && playerCol < maze.getTotalCols() - 1 && mazeArr[playerRow + 1][playerCol + 1] != 1)
                    updateLocation(playerRow + 1, playerCol + 1);
            }
            case DOWNLEFT -> {
                if (playerRow < maze.getTotalRows() - 1 && playerCol > 0 && mazeArr[playerRow + 1][playerCol - 1] != 1)
                    updateLocation(playerRow + 1, playerCol - 1);
            }
        }
    }

    private void updateLocation(int newPlayerRow, int newPlayerCol)
    {
        playerRow = newPlayerRow;
        playerCol = newPlayerCol;
        setChanged();
        notifyObservers("player moved");
    }

    @Override
    public int getPlayerRow()
    {
        return playerRow;
    }

    @Override
    public int getPlayerCol()
    {
        return playerCol;
    }


}
