package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.time.LocalDate;
import java.util.List;

public class ScheduledTransactionPopupController {

    @FXML
    private TableView<ScheduledTransactionModel> dueTransactionsTable;

    @FXML
    private TableColumn<ScheduledTransactionModel, String> scheduleNameColumn;

    @FXML
    private TableColumn<ScheduledTransactionModel, String> accountColumn;

    @FXML
    private TableColumn<ScheduledTransactionModel, String> transactionTypeColumn;

    @FXML
    private TableColumn<ScheduledTransactionModel, Double> paymentAmountColumn;

    @FXML
    private Button closeButton;

    public void initialize() {
        // Set up table columns
        scheduleNameColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleName"));
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));

        // Use Platform.runLater to ensure the popup is fully loaded
        Platform.runLater(() -> {
            // Load due transactions
            List<ScheduledTransactionModel> dueTransactions = getDueTransactions();
            if (!dueTransactions.isEmpty()) {
                ObservableList<ScheduledTransactionModel> observableList = FXCollections.observableArrayList(dueTransactions);
                dueTransactionsTable.setItems(observableList);
            } else {
                // If no transactions are due, close the popup automatically
                closePopup();
            }
        });

        // Set close button action
        closeButton.setOnAction(event -> closePopup());
    }

    private List<ScheduledTransactionModel> getDueTransactions() {
        LocalDate today = LocalDate.now();
        return ScheduledTransactionModel.getScheduledTransactions().stream()
                .filter(transaction -> transaction.getDueDate() == today.getDayOfMonth())
                .toList();
    }

    private void closePopup() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
}
