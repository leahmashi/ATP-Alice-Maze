//package Model;
//
//import Client.IClientStrategy;
//import Server.Server;
//import algorithms.mazeGenerators.Maze;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.util.Observable;
//
//public class ClientStrategyGenerateMaze extends Observable implements IClientStrategy
// {
//    private Maze maze;
//    private InetAddress serverIP;
//    private int serverPort;
//    private IClientStrategy strategy;
//
//
//    public ClientStrategyGenerateMaze(InetAddress serverIP, int serverPort, IClientStrategy strategy)
//    {
//       this.serverIP = serverIP;
//       this.serverPort = serverPort;
//       this.strategy = strategy;
//    }
//
//    public void start()
//    {
//       try (Socket serverSocket = new Socket(serverIP, serverPort))
//       {
//          strategy.clientStrategy(serverSocket.getInputStream(), serverSocket.getOutputStream());
//       } catch (IOException e)
//       {
//          e.printStackTrace();
//       }
//    }
//
//    public void communicateWithServer()
//    {
//       try
//       {
//          Socket clientSocket = new Socket(this.serverIP, this.serverPort);
//          strategy.clientStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
//          clientSocket.getInputStream().close();
//          clientSocket.getOutputStream().close();
//          clientSocket.close();
//       }
//       catch (IOException e)
//       {
//
//       }
//    }
//
//    @Override
//    public void clientStrategyGenerate(InputStream inputStream, OutputStream outputStream)
//    {
//
//
////        setMaze(generatedMaze);
//    }
//
//    public Maze getMaze() { return maze; }
//    public void setMaze(Maze maze) { this.maze = maze; }
//
//
//}
//
