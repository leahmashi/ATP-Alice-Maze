package View;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuBarOptions
{
    private static ToggleButton toggleButton = new ToggleButton("ON");

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
    public boolean showSettings(ActionEvent actionEvent, MediaPlayer mediaPlayer)
    {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVBox = new VBox(20);
        dialogVBox.getChildren().add(toggleButton);
        toggleButton.setId("toggleButton");
        toggleButton.getStylesheets().addAll(this.getClass().getResource("CSSs/ToggleButton.css").toExternalForm());
        toggleButton.setOnAction((e) -> {
            setMusic(mediaPlayer, e);
        });
        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
        if (toggleButton.isSelected())
            return false;
        else
            return true;

    }

    private boolean setMusic(MediaPlayer mediaPlayer, ActionEvent actionEvent)
    {
        System.out.println(toggleButton.isSelected());
        if (toggleButton.isSelected()) //TODO style the toggle button and add label
        {
            mediaPlayer.stop();
            actionEvent.consume();
            toggleButton.setText("OFF");
            return true;
        }
        else
        {
            mediaPlayer.play();
            actionEvent.consume();
            toggleButton.setText("ON");
            return false;
        }
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
        System.exit(0);
    }


}
