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
        addClip(clipStage, borderPane);
        addContinueButton(borderPane, clipStage);
        Scene clipScene = new Scene(borderPane, 900, 650);
        clipScene.getStylesheets().add("View/CSSs/MainStyle.css"); //TODO fix button size and location
        clipStage.setScene(clipScene);
        clipStage.setTitle("WhiteRabbitClip");
        clipStage.show();

        ((Node)(event.getSource())).getScene().getWindow().hide();

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                Stage chooseMazeStage = changeScene("View/FXMLs/ChooseMazeView.fxml", "chooseMazeScene", "chooseMaze");
                chooseMazeStage.setOnCloseRequest(e -> {  //TODO: event for close the window
                    System.exit(0);
                });
                clipStage.hide();
            }
        });
    }

    private void addClip(Stage clipStage, BorderPane borderPane)
    {
        Media media = new Media(new File("resources/clips/WhiteRabbitClip.mp4").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        borderPane.setCenter(mediaView);

        mediaView.fitWidthProperty().bind(clipStage.widthProperty()); //TODO fix resize view
        mediaView.fitHeightProperty().bind(clipStage.heightProperty());
        clipStage.setOnHidden(e -> mediaPlayer.stop());
        mediaPlayer.setAutoPlay(true);

        if (isOff)
            mediaPlayer.setMute(true);
    }

    private void addContinueButton(BorderPane borderPane, Stage clipStage)
    {
        Button continueButton = new Button("continue");
        continueButton.setId("continueButton");
        BorderPane borderLeft = new BorderPane();
        BorderPane borderLeftCenter = new BorderPane();
        borderLeftCenter.setCenter(continueButton);
        AnchorPane insiderAnchorPane = new AnchorPane();
        insiderAnchorPane.getChildren().add(borderLeftCenter);
        borderLeft.setCenter(insiderAnchorPane);
        continueButton.prefHeight(60);
        continueButton.prefWidth(130);
        borderPane.setBottom(borderLeft);

        continueButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                changeScene("View/FXMLs/ChooseMazeView.fxml", "chooseMazeScene", "chooseMaze");
                clipStage.hide();
            }
        });
    }
}
