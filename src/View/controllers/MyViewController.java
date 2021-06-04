package View.controllers;

import View.AView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MyViewController extends AView implements Initializable
{
    private static MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
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

    public void generateMaze(ActionEvent event)
    {
        Media media = new Media(new File("resources/WhiteRabbitClip.mp4").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        Group group = new Group();
        group.getChildren().add(mediaView);
        Stage clipStage = new Stage();
        Scene clipScene = new Scene(group, 900, 650);
        clipStage.setScene(clipScene);
        mediaView.fitWidthProperty().bind(clipStage.widthProperty());
        mediaView.fitHeightProperty().bind(clipStage.heightProperty());
        mediaPlayer.setAutoPlay(true);
        clipStage.setOnHidden(e -> mediaPlayer.stop());
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

    public void solveMaze(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
//        viewModel.solveMaze();
    }
}
