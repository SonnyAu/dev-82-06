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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportByTypeController {

    @FXML
    private ComboBox<String> transactionTypeDropdown;

    @FXML
    private TableView<TransactionModel> transactionTable;

    @FXML
    private TableColumn<TransactionModel, String> accountColumn;

    @FXML
    private TableColumn<TransactionModel, String> transactionDateColumn;

    @FXML
    private TableColumn<TransactionModel, String> transactionDescriptionColumn;

    @FXML
    private TableColumn<TransactionModel, Double> paymentAmountColumn;

    @FXML
    private TableColumn<TransactionModel, Double> depositAmountColumn;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        try {
            initializeTransactions();
            initializeDropdown();
            initializeTableColumns();
        } catch (Exception e) {
            logAndShowError("Error initializing Report By Type page: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void initializeTransactions() {
        TransactionModel.getAllTransactions(); // Load normal transactions
        ScheduledTransactionModel.getScheduledTransactions(); // Load scheduled transactions
    }

    private void initializeDropdown() {
        transactionTypeDropdown.setItems(FXCollections.observableArrayList(TransactionModel.getTransactionTypes()));
        transactionTypeDropdown.setOnAction(event -> filterTransactionsByType());
    }

    private void initializeTableColumns() {
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionDateColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTransactionDate().format(dateFormatter)));
        transactionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        depositAmountColumn.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));

        // Add double-click listener to open a transaction view
        transactionTable.setRowFactory(tv -> {
            TableRow<TransactionModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    openTransactionView(row.getItem());
                }
            });
            return row;
        });
    }

    private void filterTransactionsByType() {
        String selectedType = transactionTypeDropdown.getValue();
        if (selectedType == null || selectedType.isEmpty()) {
            logAndShowError("Please select a transaction type.", Alert.AlertType.WARNING);
            return;
        }

        try {
            ObservableList<TransactionModel> allTransactions = FXCollections.observableArrayList();
            allTransactions.addAll(getNormalTransactions(selectedType));
            allTransactions.addAll(getScheduledTransactions(selectedType));

            transactionTable.setItems(allTransactions);
            transactionTable.getItems().sort((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()));
        } catch (Exception e) {
            logAndShowError("Error filtering transactions by type: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private List<TransactionModel> getNormalTransactions(String transactionType) {
        return TransactionModel.getTransactionsByType(transactionType);
    }

    private List<TransactionModel> getScheduledTransactions(String transactionType) {
        List<TransactionModel> scheduledTransactions = new ArrayList<>();
        for (ScheduledTransactionModel scheduled : ScheduledTransactionModel.getScheduledTransactions()) {
            if (scheduled.getTransactionType().equalsIgnoreCase(transactionType)) {
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
