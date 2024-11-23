package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class EditScheduledTransactionController {

    @FXML
    private ComboBox<String> accountDropdown;

    @FXML
    private ComboBox<String> transactionTypeDropdown;

    @FXML
    private ComboBox<String> frequencyDropdown;

    @FXML
    private TextField dueDateField;

    @FXML
    private TextField paymentAmountField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private ScheduledTransactionModel currentTransaction; // Holds the transaction being edited

    @FXML
    public void initialize() {
        // Populate dropdowns
        accountDropdown.getItems().addAll(AccountModel.getAccountNames());
        transactionTypeDropdown.getItems().addAll(TransactionModel.getTransactionTypes());
        frequencyDropdown.getItems().addAll("Daily", "Weekly", "Monthly", "Yearly");

        // Restrict inputs to valid formats
        restrictToIntegerInput(dueDateField);
        restrictToDoubleInput(paymentAmountField);

        // Set button actions
        saveButton.setOnAction(e -> saveTransaction());
        cancelButton.setOnAction(e -> closeEditPane());
    }

    public void setTransaction(ScheduledTransactionModel transaction) {
        if (transaction != null) {
            currentTransaction = transaction;
            accountDropdown.setValue(transaction.getAccount());
            transactionTypeDropdown.setValue(transaction.getTransactionType());
            frequencyDropdown.setValue(transaction.getFrequency());
            dueDateField.setText(String.valueOf(transaction.getDueDate()));
            paymentAmountField.setText(String.valueOf(transaction.getPaymentAmount()));
        }
    }

    private void saveTransaction() {
        if (validateFields()) {
            // Update the transaction
            currentTransaction.setAccount(accountDropdown.getValue());
            currentTransaction.setTransactionType(transactionTypeDropdown.getValue());
            currentTransaction.setFrequency(frequencyDropdown.getValue());
            currentTransaction.setDueDate(Integer.parseInt(dueDateField.getText()));
            currentTransaction.setPaymentAmount(Double.parseDouble(paymentAmountField.getText()));

            // Save the updated transaction
            ScheduledTransactionModel.saveScheduledTransaction(
                    currentTransaction.getScheduleName(),
                    currentTransaction.getAccount(),
                    currentTransaction.getTransactionType(),
                    currentTransaction.getFrequency(),
                    String.valueOf(currentTransaction.getDueDate()),
                    String.valueOf(currentTransaction.getPaymentAmount())
            );

            showAlert("Scheduled transaction updated successfully!", Alert.AlertType.INFORMATION);
            closeEditPane();
        }
    }

    private boolean validateFields() {
        if (accountDropdown.getValue() == null || transactionTypeDropdown.getValue() == null ||
                frequencyDropdown.getValue() == null || dueDateField.getText().isEmpty() || paymentAmountField.getText().isEmpty()) {
            showAlert("All fields must be filled correctly!", Alert.AlertType.ERROR);
            return false;
        }
        try {
            Integer.parseInt(dueDateField.getText()); // Validate due date as an integer
            Double.parseDouble(paymentAmountField.getText()); // Validate payment amount as a double
        } catch (NumberFormatException e) {
            showAlert("Invalid numeric values!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void closeEditPane() {
        if (saveButton.getScene().getRoot() instanceof AnchorPane) {
            AnchorPane parent = (AnchorPane) saveButton.getScene().getRoot();
            parent.getChildren().clear();
        } else if (saveButton.getScene().getRoot() instanceof HBox) {
            HBox parent = (HBox) saveButton.getScene().getRoot();
            parent.getChildren().remove(saveButton.getParent());
        }
    }

    private void restrictToIntegerInput(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*")) {
                textField.setText(oldValue);
            }
        });
    }

    private void restrictToDoubleInput(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*(\\.\\d*)?")) {
                textField.setText(oldValue);
            }
        });
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
