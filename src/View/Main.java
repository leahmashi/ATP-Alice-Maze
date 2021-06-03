package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {

    MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Media musicFile = new Media(getClass().getClassLoader().getResource("AliceMainWindowMusic.mp3").toString());
        mediaPlayer = new MediaPlayer(musicFile);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });

        primaryStage.setOnHidden(e -> mediaPlayer.stop());

//        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/FXMLs/MyView.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/FXMLs/MyView.fxml"));
        Parent root = fxmlLoader.load();
        root.setId("mainWindow");
        Scene mainScene = new Scene(root, 900, 650);
        mainScene.getStylesheets().addAll(this.getClass().getResource("CSSs/MainStyle.css").toExternalForm());
        primaryStage.setTitle("mainScene");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    public static void main(String[] args) { launch(args); }
}
