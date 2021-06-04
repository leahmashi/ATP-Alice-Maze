package View;

import View.controllers.MazeViewController;
import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public abstract class AView implements IView, Observer
{
    protected MyViewModel viewModel;

    protected void changeScene(String fxmlName)
    {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlName));
//        Parent root = fxmlLoader.load();
//        AView newView = fxmlLoader.getController();
//        newView.setViewModel(this.viewModel);
    }

    public void setViewModel(MyViewModel viewModel)
    {
        this.viewModel = viewModel;
        this.viewModel.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg)
    {

    }
}
