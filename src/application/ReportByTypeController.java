package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
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

    @FXML
    private Button backButton;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        // Set up the table columns with properties from TransactionModel
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionDateColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTransactionDate().format(dateFormatter)));
        transactionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        depositAmountColumn.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));

        // Populate the transaction type dropdown
        transactionTypeDropdown.setItems(FXCollections.observableArrayList(TransactionModel.getTransactionTypes()));

        // Set up the filter action for the dropdown
        transactionTypeDropdown.setOnAction(event -> filterTransactions());
    }

    // Filter transactions based on the selected type
    private void filterTransactions() {
        String selectedType = transactionTypeDropdown.getValue();
        if (selectedType == null || selectedType.isEmpty()) {
            showAlert("Please select a transaction type.", Alert.AlertType.WARNING);
            return;
        }

        List<TransactionModel> filteredTransactions = TransactionModel.getTransactionsByType(selectedType);
        ObservableList<TransactionModel> observableTransactions = FXCollections.observableArrayList(filteredTransactions);
        transactionTable.setItems(observableTransactions);
    }

    // Navigate back to the home page
    @FXML
    private void goBack() {
        try {
            RootController root = RootController.getInstance();
        } catch (Exception e) {
            System.err.println("Error navigating back to the home page.");
            e.printStackTrace();
        }
    }


    // Display an alert
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
