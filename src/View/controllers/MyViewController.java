package View.controllers;

import View.AView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class MyViewController extends AView
{

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Media musicFile = new Media(getClass().getClassLoader().getResource("music/AliceMainWindowMusic.mp3").toString());
        setMusic(musicFile);
    }

    public void generateMaze(ActionEvent event)
    {
        Stage clipStage = new Stage();
        BorderPane borderPane = new BorderPane();
        addClip(clipStage, borderPane, "resources/clips/WhiteRabbitClip.mp4", "View/FXMLs/ChooseMazeView.fxml", "chooseMazeScene", "chooseMaze");
        addContinueButton(borderPane, clipStage, "View/FXMLs/ChooseMazeView.fxml", "chooseMazeScene", "chooseMaze");
        Scene clipScene = new Scene(borderPane, 900, 650);
        clipScene.getStylesheets().add("View/CSSs/MainStyle.css"); //TODO fix button size and location
        clipStage.setScene(clipScene);
        clipStage.setTitle("WhiteRabbitClip");
        clipStage.show();

        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
