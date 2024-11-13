package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class EnterScheduledTransactionsController {

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
    public void initialize() {
        // Populate dropdowns
        accountDropdown.getItems().addAll(AccountModel.getAccountNames());
        accountDropdown.getSelectionModel().selectFirst();

        transactionTypeDropdown.getItems().addAll(TransactionModel.getTransactionTypes());
        transactionTypeDropdown.getSelectionModel().selectFirst();

        frequencyDropdown.getItems().add("Monthly");  // Default frequency option
        frequencyDropdown.getSelectionModel().selectFirst();
    }

    @FXML
    private void saveScheduledTransaction() {
        String scheduleName = scheduleNameField.getText().trim();
        String account = accountDropdown.getValue();
        String transactionType = transactionTypeDropdown.getValue();
        String frequency = frequencyDropdown.getValue();
        String dueDate = dueDateField.getText().trim();
        String paymentAmount = paymentAmountField.getText().trim();

        // Validate fields
        if (validateFields(scheduleName, dueDate, paymentAmount)) {
            if (ScheduledTransactionModel.isDuplicateScheduleName(scheduleName)) {
                showAlert("Duplicate schedule name. Please enter a unique name.", Alert.AlertType.ERROR);
            } else {
                // Save the scheduled transaction
                ScheduledTransactionModel.saveScheduledTransaction(scheduleName, account, transactionType, frequency, dueDate, paymentAmount);
                showAlert("Scheduled transaction saved successfully!", Alert.AlertType.INFORMATION);
                resetForm();
            }
        } else {
            showAlert("Please fill all fields correctly.", Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields(String scheduleName, String dueDate, String paymentAmount) {
        if (scheduleName.isEmpty() || dueDate.isEmpty() || paymentAmount.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(dueDate);  // Ensure due date is an integer
            Double.parseDouble(paymentAmount);  // Ensure payment amount is a double
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void resetForm() {
        scheduleNameField.clear();
        dueDateField.clear();
        paymentAmountField.clear();
        accountDropdown.getSelectionModel().selectFirst();
        transactionTypeDropdown.getSelectionModel().selectFirst();
        frequencyDropdown.getSelectionModel().selectFirst();
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

