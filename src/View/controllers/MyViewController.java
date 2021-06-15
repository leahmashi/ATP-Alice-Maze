package View.controllers;

import View.AView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class MyViewController extends AView
{

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Media musicFile = new Media(Objects.requireNonNull(getClass().getClassLoader().getResource("music/AliceMainWindowMusic.mp3")).toString());
        setMusic(musicFile);
    }

    @FXML
    public void generateMaze(ActionEvent event)
    {
        hideOldWindow(event);
        Stage clipStage = new Stage();
        BorderPane borderPane = new BorderPane();
        Media media = new Media(new File("resources/clips/WhiteRabbitClip.mp4").toURI().toString());
        addClip(clipStage, borderPane, media, "View/FXMLs/ChooseMazeView.fxml", "chooseMazeScene", "chooseMaze");
        addContinueButton(borderPane, clipStage, "View/FXMLs/ChooseMazeView.fxml", "chooseMazeScene", "chooseMaze");
        Scene clipScene = new Scene(borderPane, 900, 650);
        clipScene.getStylesheets().add("View/CSSs/MainStyle.css");
        clipStage.setScene(clipScene);
        clipStage.setTitle("WhiteRabbitClip");
        clipStage.show();
    }

    @FXML
    public void showPropertiesButton(ActionEvent actionEvent)
    {
        Window window = ((Node)(actionEvent.getSource())).getScene().getWindow();
        window.setOnHidden(e -> mediaPlayer.stop());
        viewModel.showProperties(window, mediaPlayer, this);
        window.hide();
    }
}
