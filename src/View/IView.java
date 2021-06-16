package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public interface IView
{
    void setViewModel(MyViewModel viewModel);
    void setMusic(Media musicFile);
    Stage changeScene(String fxmlFile, String stageTitle, String rootID);
    void raisePopupWindow(String text, String path, Alert.AlertType type);
    void setMediaPlayer(MediaPlayer mediaPlayer);
    void createNewFile(ActionEvent actionEvent);
    void loadFile(ActionEvent actionEvent);
    void showProperties(ActionEvent actionEvent);
    void showSettings(ActionEvent actionEvent);
    void showHelp(ActionEvent actionEvent);
    void showAbout(ActionEvent actionEvent);
    void exitProgram(ActionEvent actionEvent);
    MediaPlayer getMediaPlayer();
}
