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
        // Populate dropdowns with data
        accountDropdown.getItems().clear();
        accountDropdown.getItems().addAll(AccountModel.getAccountNames());
        accountDropdown.getSelectionModel().selectFirst(); // Default selection

        transactionTypeDropdown.getItems().clear();
        transactionTypeDropdown.getItems().addAll(TransactionModel.getTransactionTypes());
        transactionTypeDropdown.getSelectionModel().selectFirst(); // Default selection

        transactionDate.setValue(LocalDate.now()); // Default date

        // Restrict input to valid numeric values for payment and deposit
        restrictToDoubleInput(paymentAmount);
        restrictToDoubleInput(depositAmount);

        // Save button action
        saveButton.setOnAction(e -> saveTransaction());
    }

    private void restrictToDoubleInput(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                textField.setText(oldValue); // Revert to old value if input is invalid
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
            try {
                String account = accountDropdown.getValue();
                String transactionType = transactionTypeDropdown.getValue();
                LocalDate date = transactionDate.getValue();
                String description = transactionDescription.getText();
                double payment = paymentAmount.getText().isEmpty() ? 0.0 : Double.parseDouble(paymentAmount.getText());
                double deposit = depositAmount.getText().isEmpty() ? 0.0 : Double.parseDouble(depositAmount.getText());

                // Save the transaction
                TransactionModel.saveTransaction(account, transactionType, date, description, payment, deposit);

                showAlert("Transaction saved successfully!", Alert.AlertType.INFORMATION);
                resetForm();
            } catch (NumberFormatException e) {
                showAlert("Invalid numeric values for Payment Amount or Deposit Amount.", Alert.AlertType.ERROR);
            }
        }
    }

    private boolean validateFields() {
        // Check if all required fields are filled
        if (accountDropdown.getValue() == null || transactionTypeDropdown.getValue() == null ||
                transactionDate.getValue() == null || transactionDescription.getText().isEmpty()) {
            showAlert("Please fill all required fields.", Alert.AlertType.ERROR);
            return false;
        }

        // Validate at least one of Payment or Deposit is filled
        if (paymentAmount.getText().isEmpty() && depositAmount.getText().isEmpty()) {
            showAlert("Please enter either a Payment Amount or a Deposit Amount.", Alert.AlertType.ERROR);
            return false;
        }

        // Validate both Payment and Deposit are not filled simultaneously
        if (!paymentAmount.getText().isEmpty() && !depositAmount.getText().isEmpty()) {
            showAlert("Please fill either Payment Amount or Deposit Amount, not both.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
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
