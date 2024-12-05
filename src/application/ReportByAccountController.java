package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class ReportByAccountController {

    @FXML
    private ComboBox<String> accountDropdown;

    @FXML
    private TableView<TransactionModel> transactionsTable;

    @FXML
    private TableColumn<TransactionModel, String> transactionTypeColumn;

    @FXML
    private TableColumn<TransactionModel, String> transactionDateColumn;

    @FXML
    private TableColumn<TransactionModel, String> descriptionColumn;

    @FXML
    private TableColumn<TransactionModel, Double> paymentAmountColumn;

    @FXML
    private TableColumn<TransactionModel, Double> depositAmountColumn;

    @FXML
    public void initialize() {
        try {
            // Populate the dropdown with account names
            List<String> accountNames = AccountModel.getAccountNames();
            accountDropdown.setItems(FXCollections.observableArrayList(accountNames));

            // Bind table columns to TransactionModel properties
            transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
            transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate")); // Ensure date is formatted properly in TransactionModel
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));
            paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
            depositAmountColumn.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));

            // Add listener to the dropdown for loading transactions
            accountDropdown.setOnAction(event -> loadTransactionsByAccount());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error initializing Report By Account page: " + e.getMessage());
        }
    }

    private void loadTransactionsByAccount() {
        try {
            // Get the selected account from the dropdown
            String selectedAccount = accountDropdown.getValue();
            if (selectedAccount == null || selectedAccount.isEmpty()) {
                showAlert("Please select an account.");
                return;
            }

            // Fetch transactions for the selected account
            List<TransactionModel> transactions = TransactionModel.getTransactionsByAccount(selectedAccount);

            // Populate the table with transactions
            ObservableList<TransactionModel> observableTransactions = FXCollections.observableArrayList(transactions);
            transactionsTable.setItems(observableTransactions);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error loading transactions for the selected account: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        System.err.println(message); // Log error
    }
}