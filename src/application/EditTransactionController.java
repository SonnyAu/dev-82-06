package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;

public class EditTransactionController {

    @FXML
    private ComboBox<String> accountDropdown;

    @FXML
    private ComboBox<String> transactionTypeDropdown;

    @FXML
    private DatePicker transactionDate;

    @FXML
    private TextField transactionDescription;

    @FXML
    private TextField paymentAmount;

    @FXML
    private TextField depositAmount;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private TransactionModel currentTransaction; // To hold the transaction being edited

    // Initialize the controller
    @FXML
    public void initialize() {
        // Populate dropdowns
        accountDropdown.getItems().addAll(AccountModel.getAccountNames());
        transactionTypeDropdown.getItems().addAll(TransactionModel.getTransactionTypes());

        // Restrict numeric inputs
        restrictToDoubleInput(paymentAmount);
        restrictToDoubleInput(depositAmount);

        // Set save button action
        saveButton.setOnAction(e -> saveTransaction());

        // Set cancel button action
        cancelButton.setOnAction(e -> closeEditPane());
    }

    // Restrict input fields to numeric values
    private void restrictToDoubleInput(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*(\\.\\d*)?")) {
                textField.setText(oldValue);
            }
        });
    }

    // Set the transaction to be edited
    public void setTransaction(TransactionModel transaction) {
        if (transaction != null) {
            currentTransaction = transaction;
            accountDropdown.setValue(transaction.getAccount());
            transactionTypeDropdown.setValue(transaction.getTransactionType());
            transactionDate.setValue(transaction.getTransactionDate());
            transactionDescription.setText(transaction.getTransactionDescription());

            // For paymentAmount and depositAmount, set default value as 0.0 if they are not populated
            paymentAmount.setText(transaction.getPaymentAmount() != 0.0 ? String.valueOf(transaction.getPaymentAmount()) : "");
            depositAmount.setText(transaction.getDepositAmount() != 0.0 ? String.valueOf(transaction.getDepositAmount()) : "");
        }
    }

    // Save the edited transaction
    private void saveTransaction() {
        if (currentTransaction == null) {
            showAlert("No transaction selected to edit.", Alert.AlertType.ERROR);
            return;
        }

        if (validateFields()) {
            // Update the transaction
            currentTransaction.setAccount(accountDropdown.getValue());
            currentTransaction.setTransactionType(transactionTypeDropdown.getValue());
            currentTransaction.setTransactionDate(transactionDate.getValue());
            currentTransaction.setTransactionDescription(transactionDescription.getText());

            String paymentText = paymentAmount.getText().trim();
            String depositText = depositAmount.getText().trim();

            currentTransaction.setPaymentAmount(paymentText.isEmpty() ? 0.0 : Double.parseDouble(paymentText));
            currentTransaction.setDepositAmount(depositText.isEmpty() ? 0.0 : Double.parseDouble(depositText));

            // Save updates to data source
            TransactionModel.updateTransaction(currentTransaction);

            showAlert("Transaction updated successfully!", Alert.AlertType.INFORMATION);
            closeEditPane();
        }
    }

    // Validate input fields
    private boolean validateFields() {
        if (accountDropdown.getValue() == null || transactionTypeDropdown.getValue() == null ||
                transactionDate.getValue() == null || transactionDescription.getText().isEmpty()) {
            showAlert("All fields must be filled!", Alert.AlertType.ERROR);
            return false;
        }
        if (paymentAmount.getText().trim().isEmpty() && depositAmount.getText().trim().isEmpty()) {
            showAlert("Please enter either a Payment Amount or a Deposit Amount.", Alert.AlertType.ERROR);
            return false;
        }
        try {
            if (!paymentAmount.getText().trim().isEmpty()) {
                Double.parseDouble(paymentAmount.getText());
            }
            if (!depositAmount.getText().trim().isEmpty()) {
                Double.parseDouble(depositAmount.getText());
            }
        } catch (NumberFormatException e) {
            showAlert("Amounts must be valid numbers.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    // Close the edit pane
    private void closeEditPane() {
        try {
            // Access the parent container of the current pane
            Pane parent = (Pane) saveButton.getScene().getRoot();

            if (parent instanceof HBox) {
                // If the root layout is HBox, remove the edit pane
                ((HBox) parent).getChildren().remove(this.saveButton.getParent());
            } else if (parent instanceof AnchorPane) {
                // If the root layout is AnchorPane, clear the children
                ((AnchorPane) parent).getChildren().clear();
            } else {
                System.err.println("Unsupported layout type for root: " + parent.getClass().getName());
            }
        } catch (Exception e) {
            showAlert("Failed to close the edit pane. Ensure the layout is properly set up.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Show alert dialog
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
