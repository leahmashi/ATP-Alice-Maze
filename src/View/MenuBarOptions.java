package View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.media.MediaPlayer;

public class MenuBarOptions
{

    @FXML
    public void createNewFile(ActionEvent actionEvent)
    {

    }

    @FXML
    public void saveFile(ActionEvent actionEvent)
    {

    }

    @FXML
    public void loadFile(ActionEvent actionEvent)
    {

    }

    @FXML
    public void showProperties(ActionEvent actionEvent)
    {

    }

    @FXML
    public void showSettings(ActionEvent actionEvent, MediaPlayer mediaPlayer)
    {
        mediaPlayer.stop();
        actionEvent.consume();
    }

    @FXML
    public void showHelp(ActionEvent actionEvent)
    {

    }

    @FXML
    public void showAbout(ActionEvent actionEvent)
    {

    }

    @FXML
    public void exitProgram(ActionEvent actionEvent)
    {

    }


}
