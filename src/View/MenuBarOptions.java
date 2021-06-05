package View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuBarOptions
{
    ToggleButton toggleButton = new ToggleButton("OFF");
    ToggleSwitch toggleSwitch = new ToggleSwitch();
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
//        dialog.initOwner();
        VBox dialogVBox = new VBox(20);
//        dialogVBox.getChildren().add(toggleSwitch);
        dialogVBox.getChildren().add(toggleButton);
        toggleButton.setId("toggleButton");
        toggleButton.getStylesheets().addAll(this.getClass().getResource("CSSs/ToggleButton.css").toExternalForm());
//        toggleButton.getStyleClass().add("CSSs/ToggleButton.red-button");
        toggleButton.setOnAction((e) -> {
            if (toggleButton.isSelected()) //TODO style the toggle button and add label
        {
            System.out.println(toggleButton.isSelected());
            mediaPlayer.stop();
            actionEvent.consume();
            toggleButton.setText("ON");
        }
        else
        {
            System.out.println(toggleButton.isSelected());
            mediaPlayer.play();
            actionEvent.consume();
            toggleButton.setText("OFF");
        }
        });
        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();


//        if (toggleSwitch.switchOnProperty().getValue())
//        {
//            System.out.println(toggleSwitch.switchOnProperty().getValue());
//            mediaPlayer.stop();
//            actionEvent.consume();
//            return true;
//        }
//        else
//        {
//            System.out.println(toggleSwitch.switchOnProperty().getValue());
//            mediaPlayer.play();
//            actionEvent.consume();
//            return false;
//        }
        return true;
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
