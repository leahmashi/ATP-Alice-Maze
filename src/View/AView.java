package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public abstract class AView implements IView, Observer, Initializable
{
    protected static MyViewModel viewModel;
    protected MenuBarOptions menuBarOptions = new MenuBarOptions();
    protected MediaPlayer mediaPlayer;
    protected static boolean isOff;
    protected Configurations configurations;


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
    public void createNewFile(ActionEvent actionEvent) { menuBarOptions.createNewFile(actionEvent, mediaPlayer); }
    @FXML
    public void loadFile(ActionEvent actionEvent) { menuBarOptions.loadFile(actionEvent, mediaPlayer, viewModel); } //TODO: check what happens in scenes before Maze view + problem with audio
    @FXML
    public void showProperties(ActionEvent actionEvent) { menuBarOptions.showProperties(actionEvent, viewModel, mediaPlayer); }
    @FXML
    public void showSettings(ActionEvent actionEvent) { isOff = menuBarOptions.showSettings(mediaPlayer); }
    @FXML
    public void showHelp(ActionEvent actionEvent) { menuBarOptions.showHelp(); }
    @FXML
    public void showAbout(ActionEvent actionEvent) { menuBarOptions.showAbout(); }
    @FXML
    public void exitProgram(ActionEvent actionEvent) { menuBarOptions.exitProgram(); }

    public void setMediaPlayer(MediaPlayer mediaPlayer) { this.mediaPlayer = mediaPlayer; }

    public void setMusic(Media musicFile)
    {
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

    public Stage changeScene(String fxmlFile, String stageTitle, String rootID)
    {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFile));
        try
        {
            root = fxmlLoader.load();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        root.setId(rootID);
        AView controller = fxmlLoader.getController();
        Stage stage = new Stage();
        Scene scene = new Scene(root, 900, 650);
        viewModel.addObserver(controller);
        stage.setTitle(stageTitle);
        stage.setScene(scene);
        stage.show();
        return stage;
    }

}
