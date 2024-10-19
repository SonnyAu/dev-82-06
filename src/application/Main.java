package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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

            // Create a title label
            Label titleLabel = new Label("Money Minder");
            titleLabel.setFont(new Font("Arial", 30)); // Set font and size
            titleLabel.setTextFill(Color.BLACK); // Set text color to black

            // Create a layout with a VBox for title and button
            VBox vbox = new VBox(20); // Vertical spacing of 20
            vbox.getChildren().addAll(titleLabel, createAccountBtn);
            vbox.setStyle("-fx-alignment: center;"); // Center-align the content

            // Create a root pane and set background color to blue from the image
            StackPane root = new StackPane(vbox);
            root.setStyle("-fx-background-color: #6A8DAD;"); // Set to blue color

            // Create the scene with root pane
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