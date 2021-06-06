package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public interface IView
{
    void setViewModel(MyViewModel viewModel);
    void setMusic(Media musicFile);
    //TODO: changeScene function
    void setMediaPlayer(MediaPlayer mediaPlayer);
    //TODO: declarations of menu needed or not?
    void createNewFile(ActionEvent actionEvent);
    void loadFile(ActionEvent actionEvent);
    void showProperties(ActionEvent actionEvent);
    void showSettings(ActionEvent actionEvent);
    void showHelp(ActionEvent actionEvent);
    void showAbout(ActionEvent actionEvent);
    void exitProgram(ActionEvent actionEvent);
}
