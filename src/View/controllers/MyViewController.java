package View.controllers;

import View.AView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
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
        Media media = new Media(new File("resources/clips/WhiteRabbitClip.mp4").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        Group group = new Group();
        MediaView mediaView = new MediaView(mediaPlayer);
        group.getChildren().add(mediaView);
        Stage clipStage = new Stage();
        mediaView.fitWidthProperty().bind(clipStage.widthProperty()); //TODO fix resize view
        mediaView.fitHeightProperty().bind(clipStage.heightProperty());
        clipStage.setOnHidden(e -> mediaPlayer.stop());
        mediaPlayer.setAutoPlay(true);

        if (isOff)
            mediaPlayer.setMute(true);

        addContinueButton(group, clipStage);
        Scene clipScene = new Scene(group, 900, 650);
        clipScene.getStylesheets().add("View/CSSs/MainStyle.css"); //TODO fix button size and location
        clipStage.setScene(clipScene);
        clipStage.setTitle("WhiteRabbitClip");
        clipStage.show();

        ((Node)(event.getSource())).getScene().getWindow().hide();

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                Parent root = null;
                try
                {
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("View/FXMLs/ChooseMazeView.fxml"));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                root.setId("chooseMaze");
                Stage ChooseMazeStage = new Stage();
                Scene ChooseMazeScene = new Scene(root, 900, 650);
                ChooseMazeStage.setTitle("chooseMazeScene");
                ChooseMazeStage.setScene(ChooseMazeScene);
                ChooseMazeStage.show();

                clipStage.hide();
            }
        });
    }

    private void addContinueButton(Group group, Stage clipStage)
    {
        Button continueButton = new Button("continue");
        continueButton.setLayoutX(350);
        continueButton.setLayoutY(600);
        continueButton.setId("continueButton");
        group.getChildren().add(continueButton);

        continueButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("View/FXMLs/ChooseMazeView.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                root.setId("chooseMaze");
                Stage ChooseMazeStage = new Stage();
                Scene ChooseMazeScene = new Scene(root, 900, 650);
                ChooseMazeStage.setTitle("chooseMazeScene");
                ChooseMazeStage.setScene(ChooseMazeScene);
                ChooseMazeStage.show();

                clipStage.hide();
            }
        });
    }
}
