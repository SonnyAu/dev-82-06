package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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

        Label accountNameLabel = new Label("Account Name:");
        TextField accountNameInput = new TextField();

        Label openingDateLabel = new Label("Opening Date:");
        DatePicker openingDatePicker = new DatePicker();
        openingDatePicker.setPrefWidth(400);

        Label balanceLabel = new Label("Opening Balance:");
        TextField balanceInput = new TextField();

        Button submitButton = new Button("Create");
        submitButton.setOnAction(e -> {
            accountStage.close();
        });

        // System shall prevent leaving the required fields empty when saving the information or when leaving the field whichever you prefer
        Text errorMessage = new Text();
        errorMessage.setFill(Color.RED);

        submitButton.setOnAction(e -> {
            // This check if there is any empty field
            if (accountNameInput.getText().isEmpty() || openingDatePicker.getValue() == null || balanceInput.getText().isEmpty()) {
                errorMessage.setText("Please enter all required fields.");
            } else {
                // Close the window once everything is confirmed filled out
                errorMessage.setText("");
                accountStage.close();
            }
        });

        // Use HBox as a wrap in order to get the button to center
        HBox buttonBox = new HBox(submitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPrefWidth(Double.MAX_VALUE);

        // Add elements to the layout
        accountLayout.getChildren().addAll(
                accountNameLabel, accountNameInput,
                openingDateLabel, openingDatePicker,
                balanceLabel, balanceInput,
                errorMessage,
                buttonBox
        );

        Scene accountScene = new Scene(accountLayout, 400, 300);
        accountStage.setScene(accountScene);
        accountStage.show();
    }
}