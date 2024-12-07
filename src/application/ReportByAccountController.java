package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
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
            initializeTransactions();
            initializeAccountDropdown();
            initializeTableColumns();
        } catch (Exception e) {
            logAndShowError("Error initializing Report By Account page: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void initializeTransactions() {
        TransactionModel.getAllTransactions(); // Load normal transactions
        ScheduledTransactionModel.getScheduledTransactions(); // Load scheduled transactions
    }

    private void initializeAccountDropdown() {
        List<String> accountNames = AccountModel.getAccountNames();
        accountDropdown.setItems(FXCollections.observableArrayList(accountNames));
        accountDropdown.setOnAction(event -> loadTransactionsByAccount());
    }

    private void initializeTableColumns() {
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        depositAmountColumn.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));

        // Add row double-click listener
        transactionsTable.setRowFactory(tv -> {
            TableRow<TransactionModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    openTransactionView(row.getItem());
                }
            });
            return row;
        });
    }

    private void loadTransactionsByAccount() {
        String selectedAccount = accountDropdown.getValue();
        if (selectedAccount == null || selectedAccount.isEmpty()) {
            logAndShowError("Please select an account.", Alert.AlertType.WARNING);
            return;
        }

        try {
            ObservableList<TransactionModel> allTransactions = FXCollections.observableArrayList();
            allTransactions.addAll(getNormalTransactions(selectedAccount));
            allTransactions.addAll(getScheduledTransactions(selectedAccount));
            transactionsTable.setItems(allTransactions);
        } catch (Exception e) {
            logAndShowError("Error loading transactions for the selected account: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private List<TransactionModel> getNormalTransactions(String account) {
        return TransactionModel.getTransactionsByAccount(account);
    }

    private List<TransactionModel> getScheduledTransactions(String account) {
        List<TransactionModel> scheduledTransactions = new ArrayList<>();
        for (ScheduledTransactionModel scheduled : ScheduledTransactionModel.getScheduledTransactions()) {
            if (scheduled.getAccount().equalsIgnoreCase(account)) {
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
        return scheduledTransactions;
    }

    private void openTransactionView(TransactionModel transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/viewTransaction.fxml"));
            AnchorPane viewTransactionPane = loader.load();

            // Pass transaction data to the controller
            ViewTransactionController controller = loader.getController();
            controller.setTransaction(transaction);

            // Replace current view with the transaction view
            HBox root = RootController.getInstance().getContainer();
            root.getChildren().set(root.getChildren().size() - 1, viewTransactionPane);
        } catch (Exception e) {
            logAndShowError("Failed to open transaction view.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void setRightPaneAsHome() {
        try {
            URL dir = getClass().getResource("/resources/init.fxml");
            HBox root = RootController.getInstance().getContainer();
            root.getChildren().set(root.getChildren().size() - 1, FXMLLoader.load(dir));
        } catch (Exception e) {
            logAndShowError("Error navigating to Home.", Alert.AlertType.ERROR);
        }
    }

    private void logAndShowError(String message, Alert.AlertType alertType) {
        System.err.println(message); // Log error
        showAlert(message, alertType);
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
