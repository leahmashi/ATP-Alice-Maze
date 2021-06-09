package View.controllers;

import Server.Configurations;
import View.AView;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;


public class ChangePropertiesController extends AView
{
    @FXML
    TextField numberOfThreads;
    @FXML
    ChoiceBox<String> solvingAlgorithm;
    @FXML
    ChoiceBox<String> mazeGenerator;
    private int threadsAmount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        configurations = Configurations.getInstance();
        Media musicFile = new Media(new File("resources/music/caterpillarSong.mp3").toURI().toString());
        setMusic(musicFile);
    }

    public void saveNewProperties()
    {
        String threadsNum = numberOfThreads.getText();
        String solvingAlgoChoice = solvingAlgorithm.getValue();
        String mazeGeneratorChoice = mazeGenerator.getValue();

        boolean successSetSolver = false;
        boolean successSetGenerator = false;
        boolean successSetThreadNum = false;

        try
        {
            if (threadsNum != "")
            {
                threadsAmount = Integer.parseInt(threadsNum);
                if (threadsAmount <= 0)
                    throw new Exception();

                configurations.setThreadPoolSize(threadsNum);
                Properties properties = new Properties();
                properties.load(new FileInputStream("resources/config.properties"));
                if (properties.getProperty("threadPoolSize").equals(threadsNum))
                    successSetThreadNum = true;
            }
        }
        catch (Exception e)
        {
            raisePopupWindow("Please enter an integer value for number of threads", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.INFORMATION);
            return;
        }

        if ((solvingAlgoChoice == null || solvingAlgoChoice == "") && (mazeGeneratorChoice == null || mazeGeneratorChoice == "") && threadsNum == "")
            raisePopupWindow("Please enter a property value you want to change", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.INFORMATION);
        else
        {
            if (solvingAlgoChoice != null)
            {
                successSetSolver = setSolver(solvingAlgoChoice);
            }
            if (mazeGeneratorChoice != null)
            {
                successSetGenerator = setGenerator(mazeGeneratorChoice);
            }
        }
        if (successSetGenerator || successSetSolver || successSetThreadNum)
            raisePopupWindow("the properties were saved successfully", "resources/clips/SuccessClip.mp4", Alert.AlertType.INFORMATION);

    }


    private boolean setGenerator(String mazeGeneratorChoice)
    {
        switch (mazeGeneratorChoice)
        {
            case "Simple":
                configurations.setMazeGeneratingAlgorithm("SimpleMazeGenerator");
                if (configurations.getMazeGeneratingAlgorithm().getClass().getName() == new SimpleMazeGenerator().getClass().getName())
                    return true;
                break;
            case "Randomized Prim":
                configurations.setMazeGeneratingAlgorithm("MyMazeGenerator");
                if (configurations.getMazeGeneratingAlgorithm().getClass().getName() == new MyMazeGenerator().getClass().getName())
                    return true;
                break;
        }
        return false;
    }

    private boolean setSolver(String solvingAlgoChoice)
    {
        switch (solvingAlgoChoice)
        {
            case "Depth First Search":
                configurations.setMazeSearchingAlgorithm("DepthFirstSearch");
                if (configurations.getMazeSearchingAlgorithm().getName() == new DepthFirstSearch().getName())
                    return true;
                break;
            case "Breadth First Search":
                configurations.setMazeSearchingAlgorithm("BreadthFirstSearch");
                if (configurations.getMazeSearchingAlgorithm().getName() == new BreadthFirstSearch().getName())
                    return true;
                break;
            case "Best First Search":
                configurations.setMazeSearchingAlgorithm("BestFirstSearch");
                if (configurations.getMazeSearchingAlgorithm().getName() == new BestFirstSearch().getName())
                    return true;
                break;
        }
        return false;
    }
}
