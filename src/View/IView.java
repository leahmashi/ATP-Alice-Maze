package View;

import ViewModel.MyViewModel;
import javafx.scene.media.MediaPlayer;

public interface IView
{
    void setViewModel(MyViewModel viewModel);
    //TODO: setMusic function
    //TODO: changeScene function
    void setMediaPlayer(MediaPlayer mediaPlayer);
}
