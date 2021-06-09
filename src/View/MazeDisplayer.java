package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas
{
    private int[][] mazeArr;
    private Solution solution;
    // end position:
    private int endRow;
    private  int endCol;
    // player position:
    private int playerRow = 0;
    private int playerCol = 0;
    // images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameEndPoint = new SimpleStringProperty();
    StringProperty imageFileNameSol = new SimpleStringProperty();


    public void drawMaze(Maze maze)
    {
        this.mazeArr = maze.getMazeArray();
        this.endRow = maze.getGoalPosition().getRowIndex();
        this.endCol = maze.getGoalPosition().getColumnIndex();
        draw();
    }

    public int getPlayerRow() { return playerRow; }
    public int getPlayerCol() { return playerCol; }

    public void setPlayerPosition(int row, int col)
    {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public String getImageFileNameWall() { return imageFileNameWall.get(); }
    public String getImageFileNamePlayer() { return imageFileNamePlayer.get(); }
    public String getImageFileNameEndPoint() { return imageFileNameEndPoint.get(); }
    public String getImageFileNameSol() { return imageFileNameSol.get(); }
    public void setImageFileNameWall(String imageFileNameWall) { this.imageFileNameWall.set(imageFileNameWall); }
    public void setImageFileNamePlayer(String imageFileNamePlayer) { this.imageFileNamePlayer.set(imageFileNamePlayer); }
    public void setImageFileNameEndPoint(String imageFileNameEndPoint) { this.imageFileNameEndPoint.set(imageFileNameEndPoint); }
    public void setImageFileNameSol(String imageFileNameSol) { this.imageFileNameSol.set(imageFileNameSol); }

    private void draw()
    {
        if(mazeArr != null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = mazeArr.length;
            int cols = mazeArr[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            if (solution != null)
                drawSolution(graphicsContext, cellHeight, cellWidth);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawEndPoint(graphicsContext, cellHeight, cellWidth);
        }
    }

    private void drawEndPoint(GraphicsContext graphicsContext, double cellHeight, double cellWidth)
    {
        Image endPointImage = null;
        try { endPointImage = new Image(new FileInputStream(getImageFileNameEndPoint())); }
        catch (FileNotFoundException e) { System.out.println("There is no end image file"); }
        double x = endCol * cellWidth;
        double y = endRow * cellHeight;
        graphicsContext.drawImage(endPointImage, x, y, cellWidth, cellHeight);
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth)
    {
        int playerRow = getPlayerRow();
        int playerCol = getPlayerCol();

        Image solPathImage = null;
        try { solPathImage = new Image(new FileInputStream(getImageFileNameSol())); }
        catch (FileNotFoundException e) { System.out.println("There is no sol image file"); }
        ArrayList<AState> solutionPath = solution.getSolutionPath();
        for (int i = 0; i < solutionPath.size(); i++)
        {
            int row = ((MazeState) solutionPath.get(i)).getPosition().getRowIndex();
            int col = ((MazeState) solutionPath.get(i)).getPosition().getColumnIndex();
            if ((row == playerRow && col == playerCol) || (row == endRow && col == endCol))
                continue;
            double x = col * cellWidth;
            double y = row * cellHeight;
            if (solPathImage == null)
                graphicsContext.fillRect(x, y, cellWidth, cellHeight);
            else
                graphicsContext.drawImage(solPathImage, x, y, cellWidth, cellHeight);
        }
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols)
    {
        Image wallImage = null;
        try { wallImage = new Image(new FileInputStream(getImageFileNameWall())); }
        catch (FileNotFoundException e) { System.out.println("There is no wall image file"); }

        double x;
        double y;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                if(mazeArr[i][j] == 1) //if it is a wall
                {
                    x = j * cellWidth;
                    y = i * cellHeight;
                    if (wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth)
    {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.LAVENDER);

        Image playerImage = null;
        try
        {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e)
        {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    public void setSolution(Solution solution)
    {
        this.solution = solution;
        draw();
    }

    public void zoom(ScrollEvent scrollEvent) //TODO: check problem with VRam
    {
        if (scrollEvent.isControlDown())
        {
            double zoomFactor = 1.05;
            if (scrollEvent.getDeltaY() < 0)
            {
                setHeight(getHeight() / zoomFactor);
                setWidth(getWidth() / zoomFactor);
            }
            else if (scrollEvent.getDeltaY() > 0)
            {
                setWidth(getWidth() * zoomFactor);
                setHeight(getHeight() * zoomFactor);
            }
            else if (scrollEvent.getDeltaY() == 0)
            {
                return;
            }
            draw();
        }
        scrollEvent.consume();
    }

}



