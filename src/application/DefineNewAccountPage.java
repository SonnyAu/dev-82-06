package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DefineNewAccountPage {

    public static void display() {
        Stage accountStage = new Stage();
        accountStage.setTitle("Create New Account");

        // Layout for account creation form
        VBox accountLayout = new VBox();
        accountLayout.setSpacing(10);
        accountLayout.setPadding(new Insets(20)); // Add padding around the layout
        accountLayout.setAlignment(Pos.TOP_CENTER); // Align elements to the top-center

        // Account Name Section
        Label accountNameLabel = new Label("Account Name:");
        TextField accountNameInput = new TextField();
        VBox accountNameBox = new VBox(5, accountNameLabel, accountNameInput); // Vertical gap of 5
        accountNameBox.setAlignment(Pos.TOP_LEFT);

        // Opening Date Section
        Label openingDateLabel = new Label("Opening Date:");
        DatePicker openingDatePicker = new DatePicker();
        VBox openingDateBox = new VBox(5, openingDateLabel, openingDatePicker);
        openingDateBox.setAlignment(Pos.TOP_LEFT);

        // Opening Balance Section
        Label balanceLabel = new Label("Opening Balance:");
        TextField balanceInput = new TextField();
        VBox balanceBox = new VBox(5, balanceLabel, balanceInput);
        balanceBox.setAlignment(Pos.TOP_LEFT);

        // Error message for validation
        Text errorMessage = new Text();
        errorMessage.setFill(Color.RED);

        // Submit button with error handling
        Button submitButton = new Button("Create");
        submitButton.setOnAction(e -> {
            if (accountNameInput.getText().isEmpty() || openingDatePicker.getValue() == null || balanceInput.getText().isEmpty()) {
                errorMessage.setText("Please enter all required fields.");
            } else {
                errorMessage.setText("");
                accountStage.close(); // Close window when fields are filled
            }
        });

        // Center the button at the bottom
        HBox buttonBox = new HBox(submitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0)); // Add padding to the top of the button

        // Add elements to the account layout
        accountLayout.getChildren().addAll(
                accountNameBox,
                openingDateBox,
                balanceBox,
                errorMessage,
                buttonBox
        );

        // Make text fields and DatePicker resize dynamically with the window
        accountNameInput.prefWidthProperty().bind(accountLayout.widthProperty().multiply(0.9));
        balanceInput.prefWidthProperty().bind(accountLayout.widthProperty().multiply(0.9));

        // Forcing the DatePicker to resize consistently
        openingDatePicker.setMinWidth(0); // Allow shrinking
        openingDatePicker.setMaxWidth(Double.MAX_VALUE); // Allow growing
        openingDatePicker.prefWidthProperty().bind(accountLayout.widthProperty().multiply(0.9));

        // Wrap the VBox in a StackPane to center it within the window
        StackPane rootPane = new StackPane(accountLayout);
        rootPane.setAlignment(Pos.CENTER); // Center the VBox in the StackPane

        // Set up the scene
        Scene accountScene = new Scene(rootPane, 600, 400);
        accountStage.setScene(accountScene);
        accountStage.show();
    }
}