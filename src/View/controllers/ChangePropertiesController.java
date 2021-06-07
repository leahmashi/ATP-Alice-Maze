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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URL;
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
    }

    public void saveNewProperties(ActionEvent actionEvent)
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
                {
                    raiseErrorWindow("Please enter an integer value for number of threads");
                    return;
                }
                configurations.setThreadPoolSize(threadsNum); //TODO: check size
                if (configurations.getThreadPoolSize() == threadsAmount)
                    successSetThreadNum = true;
            }
        }
        catch (Exception e)
        {
            raiseErrorWindow("Please enter an integer value for number of threads");
            return;
        }
        if ((solvingAlgoChoice == null || solvingAlgoChoice == "") && (mazeGeneratorChoice == null || mazeGeneratorChoice == "") && threadsNum == "")
            raiseErrorWindow("Please enter a property value you want to change");
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
            raiseSetSuccessWindow();
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

    private void raiseSetSuccessWindow()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("success!");
        alert.show();
    }

    public void raiseErrorWindow(String text)
    {
        Media media = new Media(new File("resources/clips/offWithTheirHeads.mp4").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Label label = new Label(text);
        AnchorPane content = new AnchorPane();
        content.getChildren().addAll(label, mediaView);
        DoubleProperty width = mediaView.fitWidthProperty();
        DoubleProperty height = mediaView.fitHeightProperty();
        width.bind(Bindings.selectDouble(alert.getDialogPane().sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(alert.getDialogPane().sceneProperty(), "height"));
        mediaView.setPreserveRatio(true);
        alert.getDialogPane().setContent(content);
        alert.setOnShowing(e -> mediaPlayer.play());
        alert.showAndWait();
    }
}
