package View;

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

public class MazeDisplayer extends Canvas
{
    private int[][] mazeArr;
    private Solution solution;
    private double zoomFactor = 1.05;
    // player position:
    private int playerRow = 0;
    private int playerCol = 0;
    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();

    public void drawMaze(int[][] mazeArr)
    {
        this.mazeArr = mazeArr;
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
    public void setImageFileNameWall(String imageFileNameWall) { this.imageFileNameWall.set(imageFileNameWall); }
    public String getImageFileNamePlayer() { return imageFileNamePlayer.get(); }
    public void setImageFileNamePlayer(String imageFileNamePlayer) { this.imageFileNamePlayer.set(imageFileNamePlayer); }


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
        }
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth)
    {
        //TODO: fill solution with roses
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols)
    {
        Image wallImage = null;
        try { wallImage = new Image(new FileInputStream(getImageFileNameWall())); }
        catch (FileNotFoundException e) { System.out.println("There is no wall image file"); }

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                if(mazeArr[i][j] == 1) //if it is a wall
                {
                    double x = j * cellWidth;
                    double y = i * cellHeight;
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
//        displaySolution();
    }

    public void zoom(ScrollEvent scrollEvent)
    {
        if (scrollEvent.getDeltaY() < 0)
        {
            setHeight(getHeight() / zoomFactor);
            setWidth(getWidth() / zoomFactor);
        }
        else
        {
            setWidth(getWidth() * zoomFactor);
            setHeight(getHeight() * zoomFactor);
        }
        draw();
    }
}
