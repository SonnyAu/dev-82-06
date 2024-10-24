package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        displayHome(stage);  // Start with the home page
    }

    // Method to display the Home page
    public static void displayHome(Stage stage) {
        // Create a title label for the app
        Label titleLabel = new Label("Money Minder");
        titleLabel.setFont(new Font("Arial", 40)); // Set font size for the title
        titleLabel.setTextFill(Color.WHITE); // Set text color to white

        // Create a button to navigate to Define New Account page
        Button defineNewAccountButton = new Button("Define New Account");

        // Action for the button to open DefineNewAccountPage
        defineNewAccountButton.setOnAction(event -> {
            DefineNewAccountPage defineNewAccountPage = new DefineNewAccountPage();
            defineNewAccountPage.display(stage);
        });

        // Use VBox to stack the title and button vertically
        VBox layout = new VBox(20, titleLabel, defineNewAccountButton);
        layout.setAlignment(Pos.CENTER);  // Center the title and button in the VBox

        // Set background color for the VBox
        layout.setBackground(new Background(new BackgroundFill(Color.STEELBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Create the scene and display the home page
        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Home Page");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}