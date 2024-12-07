package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
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
            // Ensure transactions are initialized
            TransactionModel.getAllTransactions(); // Load normal transactions
            ScheduledTransactionModel.getScheduledTransactions(); // Load scheduled transactions

            // Populate the dropdown with account names
            List<String> accountNames = AccountModel.getAccountNames();
            accountDropdown.setItems(FXCollections.observableArrayList(accountNames));

            // Bind table columns to TransactionModel properties
            transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
            transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
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

            // Fetch normal transactions by account
            List<TransactionModel> normalTransactions = TransactionModel.getTransactionsByAccount(selectedAccount);

            // Fetch scheduled transactions and filter by account
            List<TransactionModel> scheduledTransactions = new ArrayList<>();
            for (ScheduledTransactionModel scheduled : ScheduledTransactionModel.getScheduledTransactions()) {
                if (scheduled.getAccount().equalsIgnoreCase(selectedAccount)) {
                    // Convert ScheduledTransactionModel to TransactionModel for display
                    LocalDate calculatedDate = LocalDate.of(
                            LocalDate.now().getYear(),
                            LocalDate.now().getMonth(),
                            scheduled.getDueDate()
                    );

                    scheduledTransactions.add(new TransactionModel(
                            scheduled.getAccount(),
                            scheduled.getTransactionType(),
                            calculatedDate,
                            scheduled.getScheduleName(),
                            scheduled.getPaymentAmount(),
                            0.0 // Scheduled transactions typically lack deposits
                    ));
                }
            }

            // Combine both lists
            List<TransactionModel> allTransactions = new ArrayList<>(normalTransactions);
            allTransactions.addAll(scheduledTransactions);

            // Populate the table with combined transactions
            ObservableList<TransactionModel> observableTransactions = FXCollections.observableArrayList(allTransactions);
            transactionsTable.setItems(observableTransactions);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error loading transactions for the selected account: " + e.getMessage());
        }
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

    private void showAlert(String message) {
        System.err.println(message); // Log error
    }
}
