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
        // Ensure all transactions are loaded at startup
        TransactionModel.getAllTransactions(); // Load normal transactions
        ScheduledTransactionModel.getScheduledTransactions(); // Load scheduled transactions

        // Set up the table columns
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionDateColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTransactionDate().format(dateFormatter)));
        transactionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        depositAmountColumn.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));

        // Populate the dropdown with transaction types
        transactionTypeDropdown.setItems(FXCollections.observableArrayList(TransactionModel.getTransactionTypes()));

        // Add a listener to the dropdown for filtering transactions
        transactionTypeDropdown.setOnAction(event -> filterTransactionsByType());
    }

    private void filterTransactionsByType() {
        String selectedType = transactionTypeDropdown.getValue();
        if (selectedType == null || selectedType.isEmpty()) {
            showAlert("Please select a transaction type.", Alert.AlertType.WARNING);
            return;
        }

        // Fetch normal transactions by type
        List<TransactionModel> normalTransactions = TransactionModel.getTransactionsByType(selectedType);

        // Fetch scheduled transactions and filter by type
        List<TransactionModel> scheduledTransactions = new ArrayList<>();
        for (ScheduledTransactionModel scheduled : ScheduledTransactionModel.getScheduledTransactions()) {
            if (scheduled.getTransactionType().equalsIgnoreCase(selectedType)) {
                // Convert ScheduledTransactionModel to TransactionModel for display
                LocalDate calculatedDate = LocalDate.of(
                        LocalDate.now().getYear(),
                        LocalDate.now().getMonth(),
                        scheduled.getDueDate() // Use the due date as the day of the month
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

        // Display the combined transactions in the table
        ObservableList<TransactionModel> observableTransactions = FXCollections.observableArrayList(allTransactions);
        transactionTable.setItems(observableTransactions);

        // Sort transactions in descending order by transaction date
        transactionTable.getItems().sort((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()));
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
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
}
