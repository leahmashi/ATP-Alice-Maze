package ViewModel;

import Model.IModel;
import Model.MovementDirection;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;


import java.io.File;
import java.util.*;

public class MyViewModel extends Observable implements Observer
{
    private IModel model;

    @Override
    public void update(java.util.Observable o, Object arg)
    {
        setChanged();
        notifyObservers(arg);
    }

    //constructor
    public MyViewModel(IModel model)
    {
        this.model = model;
        this.model.assignObserver(this);
    }

    //getters
    public Maze getMaze() { return model.getMaze(); }
    public Solution getSolution() { return model.getSolution(); }
    public int getPlayerCol() { return model.getPlayerCol(); }
    public int getPlayerRow() { return model.getPlayerRow(); }

   //other functions
    public void generateMaze(int rows, int cols) { model.generateMaze(rows, cols); }
    public void solveMaze() { model.solveMaze(); }

    public void movePlayer(KeyEvent keyEvent)
    {
        MovementDirection direction; //add diagonal movement and movement by numpad
        //TODO: two arrows together for diagonal?
//        KeyCombination keyCombinationUpLeft = new MultiKeyCombi(KeyCode.UP, KeyCode.LEFT);
//        KeyCombination keyCombinationUpRight = new MultiKeyCombi(KeyCode.UP, KeyCode.RIGHT);
//        KeyCombination keyCombinationDownLeft = new MultiKeyCombi(KeyCode.DOWN, KeyCode.LEFT);
//        KeyCombination keyCombinationDownRight = new MultiKeyCombi(KeyCode.DOWN, KeyCode.RIGHT);
        switch (keyEvent.getCode())
        {
            case UP, NUMPAD8 -> direction = MovementDirection.UP;
            case DOWN, NUMPAD2 -> direction = MovementDirection.DOWN;
            case RIGHT, NUMPAD6 -> direction = MovementDirection.RIGHT;
            case LEFT, NUMPAD4 -> direction = MovementDirection.LEFT;
            case NUMPAD7 -> direction = MovementDirection.UPLEFT;
            case NUMPAD9 -> direction = MovementDirection.UPRIGHT;
            case NUMPAD1 -> direction = MovementDirection.DOWNLEFT;
            case NUMPAD3 -> direction = MovementDirection.DOWNRIGHT;
            default -> {
                //pressed different key - no need to move
                 return;
            }
        }
        model.updatePlayerLocation(direction);
    }

    public void saveMaze(File file) { this.model.saveMaze(file); }

    public void loadMaze(String fileName, ActionEvent actionEvent) { this.model.loadMaze(fileName); }
}

//TODO: check if relevant multiKey class
//class MultiKeyCombi extends KeyCombination
//{
//    private static List<KeyCode> codes = new ArrayList<KeyCode>();
//    private List<KeyCode> neededCodes;
//
//    public MultiKeyCombi(KeyCode... codes)
//    {
//        neededCodes = Arrays.asList(codes);
//    }
//
//    @Override
//    public boolean match(KeyEvent keyEvent)
//    {
//        return codes.containsAll(neededCodes);
//    }
//}
