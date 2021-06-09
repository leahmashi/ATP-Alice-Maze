package View.controllers;

import View.AView;
import javafx.event.Event;
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
        Media finishLineMedia = new Media(new File("resources/music/veryMerryUnbirthday.mp3").toURI().toString());
        setMusic(finishLineMedia);

    }
    @FXML
    public void finish(Event e) { System.exit(0); }


    @FXML
    public void startOver(Event e)
    {
        System.out.println("hello from startOver");
        //TODO: return to main or return to choose?
    }

}
