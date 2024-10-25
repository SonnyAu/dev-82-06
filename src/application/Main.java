package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/sidebar.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
//        primaryStage.setTitle("Money Minder");
//        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
//        primaryStage.show();

        RootController root = RootController.getInstance();
        Scene scene = new Scene(root.getContainer(), 1400, 1000);
        primaryStage.setTitle("Money Minder");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
