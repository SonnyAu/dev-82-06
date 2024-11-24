package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;

public class EnterScheduledTransactionsController {

    @FXML private TextField scheduleNameField;
    @FXML private ComboBox<String> accountDropdown;
    @FXML private ComboBox<String> transactionTypeDropdown;
    @FXML private ComboBox<String> frequencyDropdown;
    @FXML private TextField dueDateField;
    @FXML private TextField paymentAmountField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    @FXML
    public void setRightPaneAsHome() {
        URL dir = getClass().getResource("/resources/init.fxml");
        try {
            HBox root = RootController.getInstance().getContainer();
            root.getChildren().remove(root.getChildren().size() - 1);

            AnchorPane newAccountPane = FXMLLoader.load(dir);
            root.getChildren().add(newAccountPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Populate dropdowns
        accountDropdown.getItems().addAll(AccountModel.getAccountNames());
        accountDropdown.getSelectionModel().selectFirst();

        transactionTypeDropdown.getItems().addAll(TransactionModel.getTransactionTypes());
        transactionTypeDropdown.getSelectionModel().selectFirst();

        frequencyDropdown.getItems().add("Monthly");  // Default frequency option
        frequencyDropdown.getSelectionModel().selectFirst();

        // Real-time validation for Schedule Name (only letters, spaces, and hyphens)
        scheduleNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s-]*")) { // Allows only letters, spaces, and hyphens
                scheduleNameField.setText(oldValue);
            }
        });

        // Real-time validation for Due Date (only integers, no further checks)
        dueDateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Allows only digits
                dueDateField.setText(oldValue);
            }
        });

        // Real-time validation for Payment Amount (only doubles)
        paymentAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) { // Allows only numbers and one decimal point
                paymentAmountField.setText(oldValue);
            }
        });
    }


    @FXML
    private void saveScheduledTransaction() {
        String scheduleName = scheduleNameField.getText();
        String account = accountDropdown.getValue();
        String transactionType = transactionTypeDropdown.getValue();
        String frequency = frequencyDropdown.getValue();
        String dueDate = dueDateField.getText();
        String paymentAmount = paymentAmountField.getText();

        // Validate fields
        if (validateFields(scheduleName, dueDate, paymentAmount)) {
            if (!ScheduledTransactionModel.isDuplicateScheduleName(scheduleName)) {
                // Save the scheduled transaction
                ScheduledTransactionModel.saveScheduledTransaction(scheduleName, account, transactionType, frequency, dueDate, paymentAmount);
                showAlert("Scheduled transaction saved successfully!", Alert.AlertType.INFORMATION);
                resetForm();
            } else {
                showAlert("Schedule name already exists. Please use a unique name.", Alert.AlertType.ERROR);
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
            // Convert dueDate and paymentAmount to their required formats without additional range checks
            Integer.parseInt(dueDate); // Ensure due date is an integer
            Double.parseDouble(paymentAmount); // Ensure payment amount is a double
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
