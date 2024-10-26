package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    // set stage to the root controller
    @Override
    public void start(Stage primaryStage) throws IOException {
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
