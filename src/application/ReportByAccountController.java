package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class ReportByAccountController {

    @FXML private ComboBox<String> accountDropdown;
    @FXML private TableView<TransactionModel> transactionTable;

    @FXML
    public void initialize() {
        // Populate the dropdown with account names
        accountDropdown.setItems(FXCollections.observableArrayList(AccountModel.getAccountNames()));

        // Load transactions for the selected account
        accountDropdown.setOnAction(event -> loadTransactionsByAccount());
    }

    private void loadTransactionsByAccount() {
        String selectedAccount = accountDropdown.getValue();
        if (selectedAccount != null) {
            ObservableList<TransactionModel> transactions = FXCollections.observableArrayList(
                    TransactionModel.getTransactionsByAccount(selectedAccount));
            transactionTable.setItems(transactions);
        }
    }

    @FXML
    public void goBack() {
        // Navigate back to the previous view
    }
}
