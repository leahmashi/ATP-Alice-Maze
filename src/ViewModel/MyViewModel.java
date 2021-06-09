package ViewModel;

import Model.IModel;
import Model.MovementDirection;
import View.controllers.ChangePropertiesController;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

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

    public boolean saveMaze(File file) { return this.model.saveMaze(file); }

    public boolean loadMaze(File file) { return this.model.loadMaze(file); }

    public void showProperties(Window parentWindow, MediaPlayer mediaPlayer)
    {
        Media parentStageMedia = mediaPlayer.getMedia();
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/FXMLs/changePropertiesView.fxml"));
        try
        {
            root = fxmlLoader.load();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        root.setId("changeProperties");
        Stage changePropertiesStage = new Stage();
        ChangePropertiesController controller = fxmlLoader.getController();
//        FXMLLoader parentFxmlLoader = (FXMLLoader) parentWindow.getScene().getUserData();
//        IView parentController = parentFxmlLoader.getController();
//        parentController.setMusic(parentStageMedia);
        Scene changePropertiesScene = new Scene(root, 900, 650);
        changePropertiesStage.setTitle("changeProperties");
        changePropertiesStage.setScene(changePropertiesScene);
        changePropertiesStage.show();

        changePropertiesStage.setOnHidden(e -> {
            mediaPlayer.stop();
            controller.setMusic(parentStageMedia);
            ((Stage) parentWindow.getScene().getWindow()).show();
        });
    }
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
