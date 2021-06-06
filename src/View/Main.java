package View;

import Model.IModel;
import Model.MyModel;
import View.controllers.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/FXMLs/MyView.fxml"));
        Parent root = fxmlLoader.load();
        root.setId("mainWindow");
        Scene mainScene = new Scene(root, 900, 650);

        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
        viewModel.addObserver(view);

        primaryStage.setTitle("mainScene");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    public static void main(String[] args) { launch(args); }
}
