package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public abstract class AView implements IView, Observer, Initializable
{
    protected MyViewModel viewModel;
    protected MenuBarOptions menuBarOptions = new MenuBarOptions();
    protected MediaPlayer mediaPlayer;
    protected static boolean isOff;

    public void setViewModel(MyViewModel viewModel)
    {
        this.viewModel = viewModel;
        this.viewModel.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        //TODO: update function
    }

    @FXML
    public void createNewFile(ActionEvent actionEvent) { menuBarOptions.createNewFile(actionEvent); }
    @FXML
    public void saveFile(ActionEvent actionEvent) { menuBarOptions.saveFile(actionEvent); }
    @FXML
    public void loadFile(ActionEvent actionEvent) { menuBarOptions.loadFile(actionEvent); }
    @FXML
    public void showProperties(ActionEvent actionEvent) { menuBarOptions.showProperties(actionEvent); }
    @FXML
    public void showSettings(ActionEvent actionEvent) { isOff = menuBarOptions.showSettings(actionEvent, mediaPlayer); }
    @FXML
    public void showHelp(ActionEvent actionEvent) { menuBarOptions.showHelp(actionEvent); }
    @FXML
    public void showAbout(ActionEvent actionEvent) { menuBarOptions.showAbout(actionEvent); }
    @FXML
    public void exitProgram(ActionEvent actionEvent) { menuBarOptions.exitProgram(actionEvent); }

    public void setMediaPlayer(MediaPlayer mediaPlayer) { this.mediaPlayer = mediaPlayer; }

    public void setMusic(Media musicFile)
    {
//        Media musicFile = new Media(getClass().getClassLoader().getResource(mp3File).toString());
        mediaPlayer = new MediaPlayer(musicFile);
        setMediaPlayer(mediaPlayer);
        if (!isOff)
        {
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.play();
                }
            });
        }
    }

}
