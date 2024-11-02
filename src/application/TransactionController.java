package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;

public class TransactionController {

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
    public void initialize() {
        // Clear any existing items to prevent duplicates
        accountDropdown.getItems().clear();
        transactionTypeDropdown.getItems().clear();

        // Populate accountDropdown and transactionTypeDropdown with unique data
        accountDropdown.getItems().addAll(AccountModel.getAccountNames());
        accountDropdown.getSelectionModel().selectFirst(); // Default selection

        transactionTypeDropdown.getItems().addAll(TransactionModel.getTransactionTypes());
        transactionTypeDropdown.getSelectionModel().selectFirst(); // Default selection

        transactionDate.setValue(LocalDate.now()); // Default date

        // Set save button action
        saveButton.setOnAction(e -> saveTransaction());

        // Add listeners to restrict input to numeric values (double) for paymentAmount and depositAmount
        restrictToDoubleInput(paymentAmount);
        restrictToDoubleInput(depositAmount);
    }

    private void restrictToDoubleInput(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                textField.setText(oldValue); // Revert to old value if input is not valid
            }
        });
    }

    @FXML
    public void setRightPaneAsHome() {
        URL dir = getClass().getResource("/resources/init.fxml");
        try {
            HBox root = RootController.getInstance().getContainer();
            root.getChildren().remove(root.getChildren().size() - 1);

            AnchorPane initPane = FXMLLoader.load(dir);
            root.getChildren().add(initPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveTransaction() {
        if (validateFields()) {
            String account = accountDropdown.getValue();
            String transactionType = transactionTypeDropdown.getValue();
            LocalDate date = transactionDate.getValue();
            String description = transactionDescription.getText();
            String payment = paymentAmount.getText();
            String deposit = depositAmount.getText();

            // Simulate saving the transaction
            TransactionModel.saveTransaction(account, transactionType, date, description, payment, deposit);

            showAlert("Transaction saved successfully!", Alert.AlertType.INFORMATION);
            resetForm();
        } else {
            showAlert("Please fill all required fields correctly.", Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields() {
        if (accountDropdown.getValue() == null || transactionTypeDropdown.getValue() == null ||
                transactionDate.getValue() == null || transactionDescription.getText().isEmpty()) {
            return false;
        }

        if (transactionTypeDropdown.getValue() == null || transactionTypeDropdown.getValue().isEmpty()) {
            return false;
        }

        if ((paymentAmount.getText().isEmpty() && depositAmount.getText().isEmpty()) ||
                (!paymentAmount.getText().isEmpty() && !depositAmount.getText().isEmpty())) {
            showAlert("Please fill either Payment Amount or Deposit Amount, not both.", Alert.AlertType.ERROR);
            return false;
        }

        if (!paymentAmount.getText().isEmpty() && !isNumeric(paymentAmount.getText())) {
            showAlert("Payment Amount must be a number.", Alert.AlertType.ERROR);
            return false;
        }
        if (!depositAmount.getText().isEmpty() && !isNumeric(depositAmount.getText())) {
            showAlert("Deposit Amount must be a number.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void resetForm() {
        transactionDescription.clear();
        paymentAmount.clear();
        depositAmount.clear();
        transactionDate.setValue(LocalDate.now());
        accountDropdown.getSelectionModel().selectFirst();
        transactionTypeDropdown.getSelectionModel().selectFirst();
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
