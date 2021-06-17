package View.controllers;

import Server.Configurations;
import View.AView;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;

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
            if (!threadsNum.equals(""))
            {
                int threadsAmount = Integer.parseInt(threadsNum);
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

        if ((solvingAlgoChoice == null || solvingAlgoChoice.equals("")) && (mazeGeneratorChoice == null || mazeGeneratorChoice.equals("")) && threadsNum.equals(""))
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
        switch (mazeGeneratorChoice) {
            case "Simple" -> {
                configurations.setMazeGeneratingAlgorithm("SimpleMazeGenerator");
                if (configurations.getMazeGeneratingAlgorithm().getClass().getName().equals(SimpleMazeGenerator.class.getName()))
                    return true;
            }
            case "Randomized Prim" -> {
                configurations.setMazeGeneratingAlgorithm("MyMazeGenerator");
                if (configurations.getMazeGeneratingAlgorithm().getClass().getName().equals(MyMazeGenerator.class.getName()))
                    return true;
            }
        }
        return false;
    }

    private boolean setSolver(String solvingAlgoChoice)
    {
        switch (solvingAlgoChoice) {
            case "Depth First Search" -> {
                configurations.setMazeSearchingAlgorithm("DepthFirstSearch");
                if (configurations.getMazeSearchingAlgorithm().getName().equals(new DepthFirstSearch().getName()))
                    return true;
            }
            case "Breadth First Search" -> {
                configurations.setMazeSearchingAlgorithm("BreadthFirstSearch");
                if (configurations.getMazeSearchingAlgorithm().getName().equals(new BreadthFirstSearch().getName()))
                    return true;
            }
            case "Best First Search" -> {
                configurations.setMazeSearchingAlgorithm("BestFirstSearch");
                if (configurations.getMazeSearchingAlgorithm().getName().equals(new BestFirstSearch().getName()))
                    return true;
            }
        }
        return false;
    }
}
