package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.time.LocalDate;

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
            paymentAmount.setText(String.valueOf(transaction.getPaymentAmount()));
            depositAmount.setText(String.valueOf(transaction.getDepositAmount()));
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
            currentTransaction.setPaymentAmount(Double.parseDouble(paymentAmount.getText()));
            currentTransaction.setDepositAmount(Double.parseDouble(depositAmount.getText()));

            // Save to file
            TransactionModel.saveTransaction(
                    currentTransaction.getAccount(),
                    currentTransaction.getTransactionType(),
                    currentTransaction.getTransactionDate(),
                    currentTransaction.getTransactionDescription(),
                    currentTransaction.getPaymentAmount(),
                    currentTransaction.getDepositAmount()
            );

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
        if (paymentAmount.getText().isEmpty() && depositAmount.getText().isEmpty()) {
            showAlert("Please enter either a Payment Amount or a Deposit Amount.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    // Close the edit pane
    private void closeEditPane() {
        if (saveButton.getScene() == null || saveButton.getScene().getRoot() == null) {
            showAlert("Unable to close the edit pane. No root detected.", Alert.AlertType.ERROR);
            return;
        }

        if (saveButton.getScene().getRoot() instanceof AnchorPane) {
            AnchorPane parent = (AnchorPane) saveButton.getScene().getRoot();
            parent.getChildren().clear(); // Removes the edit pane
        } else if (saveButton.getScene().getRoot() instanceof HBox) {
            HBox parent = (HBox) saveButton.getScene().getRoot();
            parent.getChildren().remove(this.saveButton.getParent()); // Removes the edit pane from HBox
        } else {
            System.err.println("Root layout is not AnchorPane or HBox. Cannot close the edit pane.");
        }
    }


    // Show alert dialog
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}