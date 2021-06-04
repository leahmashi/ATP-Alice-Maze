package View.controllers;


import Model.IModel;
import Model.MyModel;
import View.AView;
import View.IView;
import ViewModel.MyViewModel;

import algrorithms.mazeGenerators.AMazeGenerator;
import algrorithms.mazeGenerators.MyMazeGenerator;
import algrorithms.mazeGenerators.SimpleMazeGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class chooseMazeViewController extends AView implements Initializable
{
    private static MediaPlayer mediaPlayer;
    private int rows;
    private int cols;
    private AMazeGenerator mg;
    @FXML
    public TextField EnterRowsText;
    @FXML
    public TextField EnterColsText;
    @FXML
    public ChoiceBox primChoice;


    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Media musicFile = new Media(new File("resources/AliceChooseMazeMusic.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(musicFile);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });

    }

    public void raiseErrorWindow(String text)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(text);
        alert.show();
    }

    public void generateMaze(ActionEvent actionEvent) throws IOException
    {
        String colsInput = EnterColsText.getText();
        String rowsInput = EnterRowsText.getText();
        try {
            rows = Integer.parseInt(rowsInput);
            cols = Integer.parseInt(colsInput);
            String mazeChoice = (String) primChoice.getValue();
            if (mazeChoice == null)
                raiseErrorWindow("Please choose a maze type");
            else
                mg = ChooseMaze(mazeChoice);
        }
        catch (Exception e)
        {
            raiseErrorWindow("One or more inputs isn't a positive number, please try again");
        }

        Stage MazeWindowStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/FXMLs/MazeView.fxml"));
        Parent root = fxmlLoader.load();
        MazeViewController controller = fxmlLoader.getController();
        controller.initData(rows, cols, mg);
        IModel model = new MyModel(mg);
        MyViewModel viewModel = new MyViewModel(model);
        IView view = fxmlLoader.getController();
        view.setViewModel(viewModel);
        Scene MazeWindowScene = new Scene(root);
        MazeWindowStage.setScene(MazeWindowScene);
        MazeWindowStage.show();

        ((Node)(actionEvent.getSource())).getScene().getWindow().setOnHidden(e -> mediaPlayer.stop());
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/FXMLs/MyView.fxml"));
        root.setId("mainWindow");
        Stage MainWindowStage = new Stage();
        Scene MainWindowScene = new Scene(root, 900, 650);
        MainWindowStage.setTitle("mainScene");
        MainWindowStage.setScene(MainWindowScene);
        MainWindowStage.show();

        ((Node)(actionEvent.getSource())).getScene().getWindow().setOnHidden(e -> mediaPlayer.stop());
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        Media musicFile = new Media(getClass().getClassLoader().getResource("AliceMainWindowMusic.mp3").toString());
        mediaPlayer = new MediaPlayer(musicFile);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });
    }

    private AMazeGenerator ChooseMaze(String mazeChoice)
    {
        switch (mazeChoice) {
            case "Simple" -> mg = new SimpleMazeGenerator();
            case "Randomized Prim" -> mg = new MyMazeGenerator();
        }
        return mg;
    }

}
