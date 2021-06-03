package Model;

import Client.IClientStrategy;
import algrorithms.mazeGenerators.Maze;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Observable;

public class ClientStrategyGenerateMaze extends Observable implements IClientStrategy
 {
    private Maze maze;

    @Override
    public void clientStrategy(InputStream inputStream, OutputStream outputStream)
    {


//        setMaze(generatedMaze);
    }

    public Maze getMaze() { return maze; }
    public void setMaze(Maze maze) { this.maze = maze; }
}
