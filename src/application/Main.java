package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Button to Navigate to Account Creation Page
            Button createAccountBtn = new Button("Define New Account");
            createAccountBtn.setOnAction(e -> {
                // Navigate to Define New Account page
                DefineNewAccountPage.display();
            });
            StackPane root = new StackPane();
            root.getChildren().add(createAccountBtn);

            // This makes the scene for the homepage
            Scene homeScene = new Scene(root, 600, 400);
            primaryStage.setTitle("Home Page");
            primaryStage.setScene(homeScene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}