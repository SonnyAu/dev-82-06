package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;

public class NewAccountController {

    @FXML DatePicker datePicker;
    @FXML TextField nameField;
    @FXML TextField balanceField;
    @FXML Text errorMsg;
    @FXML Button submitNewAccount;

    // initialize date with current date
    public void initialize() {
        datePicker.setValue(LocalDate.now());

        // Restrict nameField to alphabetic characters only (no digits or numbers)
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) { // Allow only letters and spaces
                nameField.setText(oldValue); // Revert to old value if invalid input is detected
            }
        });

        // Restrict balanceField to numeric values with an optional decimal point
        balanceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) { // Allow only numbers and a single decimal point
                balanceField.setText(oldValue); // Revert to old value if invalid input is detected
            }
        });
    }

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

    private boolean compareData(String[] existingRow, String[] newRow) {
        if (existingRow.length != newRow.length) return false;
        for (int i = 0; i < existingRow.length; i++) {
            if (!existingRow[i].equals(newRow[i])) {
                return false;
            }
        }
        return true;
    }

    @FXML
    public void submitNewAccount() {
        errorMsg.setText("");

        // Validate that name is not empty
        if (nameField.getText().isEmpty() || nameField.getText().trim().isEmpty()) {
            errorMsg.setText("Enter a valid name.");
            return;
        }

        // Check for duplicate account name
        if (AccountModel.getAccountNames().contains(nameField.getText().trim())) {
            errorMsg.setText("Account name already exists. Please choose a different name.");
            return;
        }

        // Validate that balance is not empty and is a valid number
        if (balanceField.getText().isEmpty() || balanceField.getText().trim().isEmpty()) {
            errorMsg.setText("Enter a balance.");
            return;
        }

        double balance;
        try {
            balance = Double.parseDouble(balanceField.getText());
            if (balance < 0) {
                errorMsg.setText("Balance must be more than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            errorMsg.setText("Invalid balance format.");
            return;
        }

        // Create a new account and save it using DataController
        AccountModel account = new AccountModel(nameField.getText(), balance, datePicker.getValue());
        DataController<AccountModel> dc = new DataController<>(AccountModel.class);
        dc.writeToCSV(dc.getFilePath(), account.getCSVData());

        errorMsg.setText("Submitted successfully.");
    }

}
