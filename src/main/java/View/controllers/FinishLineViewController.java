package View.controllers;

import View.AView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.media.Media;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class FinishLineViewController extends AView
{
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Media finishLineMedia = new Media(new File("resources/music/finalSceneMusic.mp3").toURI().toString());
        setMusic(finishLineMedia);
    }

    @FXML
    public void finish() { System.exit(0); }

    @FXML
    public void startOver(ActionEvent actionEvent)
    {
        changeScene("FXMLs/MyView.fxml", "mainScene", "mainWindow");
        hideOldWindow(actionEvent);
    }

}
