package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditScheduledTransactionController {

    @FXML
    private TextField scheduleNameField;

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
    private Button cancelButton; // Optional, only present if defined in FXML

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

        // Check if cancelButton exists before setting its action
        if (cancelButton != null) {
            cancelButton.setOnAction(e -> cancelEditScheduledTransaction());
        }
    }

    public void setTransaction(ScheduledTransactionModel transaction) {
        if (transaction != null) {
            currentTransaction = transaction;

            // Bind all fields with the current transaction
            scheduleNameField.setText(transaction.getScheduleName());
            accountDropdown.setValue(transaction.getAccount());
            transactionTypeDropdown.setValue(transaction.getTransactionType());
            frequencyDropdown.setValue(transaction.getFrequency());
            dueDateField.setText(String.valueOf(transaction.getDueDate()));
            paymentAmountField.setText(String.valueOf(transaction.getPaymentAmount()));
        }
    }

    private void saveTransaction() {
        if (validateFields()) {
            String oldScheduleName = currentTransaction.getScheduleName(); // Save old name for matching
            String newScheduleName = scheduleNameField.getText();

            currentTransaction.setScheduleName(newScheduleName);
            currentTransaction.setAccount(accountDropdown.getValue());
            currentTransaction.setTransactionType(transactionTypeDropdown.getValue());
            currentTransaction.setFrequency(frequencyDropdown.getValue());
            currentTransaction.setDueDate(Integer.parseInt(dueDateField.getText()));
            currentTransaction.setPaymentAmount(Double.parseDouble(paymentAmountField.getText()));

            // Save or update the transaction
            ScheduledTransactionModel.updateScheduledTransaction(currentTransaction, oldScheduleName);

            showAlert("Scheduled transaction updated successfully!", Alert.AlertType.INFORMATION);
        }
    }

    private boolean validateFields() {
        if (scheduleNameField.getText().isEmpty() ||
                accountDropdown.getValue() == null ||
                transactionTypeDropdown.getValue() == null ||
                frequencyDropdown.getValue() == null ||
                dueDateField.getText().isEmpty() ||
                paymentAmountField.getText().isEmpty()) {
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

    public void cancelEditScheduledTransaction() {
        // Placeholder for cancel functionality
    }
}
