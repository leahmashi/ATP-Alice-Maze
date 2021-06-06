package Model;


import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import Client.IClientStrategy;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;


public class MyModel extends Observable implements IModel
{
    //class members
    private Maze maze;
    private Solution solution;
    private int playerRow;
    private int playerCol;
    Server mazeGeneratingServer;
    Server solveSearchProblemSolver;

    //constructor
    public MyModel()
    {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemSolver = new Server(5401,1000, new ServerStrategySolveSearchProblem());

        solveSearchProblemSolver.start();
        mazeGeneratingServer.start();
    }


    @Override
    public void assignObserver(Observer observer) { this.addObserver(observer); }



    //getters
    @Override
    public Maze getMaze() { return maze; }

    @Override
    public Solution getSolution() { return solution; }

    @Override
    public int getPlayerRow() { return playerRow; }

    @Override
    public int getPlayerCol() { return playerCol; }



    @Override
    public void generateMaze(int rows, int cols)
    {
        try
        {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy()
            {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer)
                {
                    try
                    {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[(rows*cols)+12 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        playerCol = 0;
        playerRow = 0;
        setChanged();
        notifyObservers("maze generated");
    }



    @Override
    public void solveMaze()
    {
        try
        {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy()
            {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer)
                {
                    try
                    {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        solution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("maze solved");
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

}
