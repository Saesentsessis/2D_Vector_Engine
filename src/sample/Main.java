package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Unity 0.1alpha");
        Scene main = new Scene(root, 800, 440);
        Controller.Scene = main;
        primaryStage.setScene(main);
        primaryStage.show();
        Controller.Instance.Start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
