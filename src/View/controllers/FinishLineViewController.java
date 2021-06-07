package View.controllers;

import View.AView;
import javafx.event.Event;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class FinishLineViewController extends AView
{
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }
    @FXML
    public void finish(Event e)
    {
        System.exit(0);
    }


    @FXML
    public void startOver(Event e)
    {
        System.out.println("hallo from startOver");
    }

}
