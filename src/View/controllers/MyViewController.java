package View.controllers;

import View.AView;
import View.IView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;



public class MyViewController extends AView
{
    public void generateMaze(ActionEvent event) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/FXMLs/ChooseMazeView.fxml"));
        root.setId("chooseMaze");
        Stage ChooseMazeStage = new Stage();
        Scene ChooseMazeScene = new Scene(root, 900, 650);
        ChooseMazeStage.setTitle("chooseMazeScene");
        ChooseMazeStage.setScene(ChooseMazeScene);
        ChooseMazeStage.show();

        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public void solveMaze(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
//        viewModel.solveMaze();
    }
}
