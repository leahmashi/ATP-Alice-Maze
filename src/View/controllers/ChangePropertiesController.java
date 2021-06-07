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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
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
                    raisePopupWindow("Please enter an integer value for number of threads", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.INFORMATION);
                    return;
                }


                configurations.setThreadPoolSize(threadsNum);

                Properties p = new Properties();
                p.load(new FileInputStream("resources/config.properties"));

                if (p.getProperty("threadPoolSize").equals(threadsNum))
                    successSetThreadNum = true;
            }
        }
        catch (Exception e)
        {
            raisePopupWindow("Please enter an integer value for number of threads", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.INFORMATION);

//            raiseErrorWindow("Please enter an integer value for number of threads");
            return;
        }
        if ((solvingAlgoChoice == null || solvingAlgoChoice == "") && (mazeGeneratorChoice == null || mazeGeneratorChoice == "") && threadsNum == "")
            raisePopupWindow("Please enter an integer value for number of threads", "resources/clips/offWithTheirHeads.mp4", Alert.AlertType.INFORMATION);

//        raiseErrorWindow("Please enter a property value you want to change");
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
            raisePopupWindow("the properties were saved successfully", "resources/clips/success.mp4", Alert.AlertType.INFORMATION);

//        raiseSetSuccessWindow();
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
        Media media = new Media(new File("resources/clips/success.mp4").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(200);
        alert.getDialogPane().setMinWidth(200);
        Label label = new Label("success!");
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

    public void raiseErrorWindow(String text)
    {
        Media media = new Media(new File("resources/clips/offWithTheirHeads.mp4").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(200);
        alert.getDialogPane().setMinWidth(200);
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


    public void raisePopupWindow(String text, String path, Alert.AlertType type)
    {
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        Alert alert = new Alert(type);
        alert.getDialogPane().setMinHeight(200);
        alert.getDialogPane().setMinWidth(200);
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
