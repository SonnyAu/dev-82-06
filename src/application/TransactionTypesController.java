package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TransactionTypesController {

    @FXML
    private TextField transactionTypeField;

    @FXML
    private Label statusLabel;

    @FXML
    private Button saveButton;

    @FXML
    private void saveTransactionType() {
        String newType = transactionTypeField.getText().trim();

        // Check if the input is empty
        if (newType.isEmpty()) {
            statusLabel.setText("Transaction type cannot be empty.");
            return;
        }

        // Check for duplicate transaction type (case-insensitive)
        if (TransactionModel.isDuplicateTransactionType(newType)) {
            statusLabel.setText("Transaction type already exists. Please choose a different name.");
            return;
        }

        // Add the new transaction type if it is not a duplicate
        TransactionModel.addTransactionType(newType);
        statusLabel.setText("Transaction type added successfully.");

        // Clear the field after saving
        transactionTypeField.clear();
    }
}