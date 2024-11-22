package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;

public class ShowTransactionController {

    @FXML
    private TableView<TransactionModel> transactionTable;

    @FXML
    private TableColumn<TransactionModel, String> accountColumn;
    @FXML
    private TableColumn<TransactionModel, String> transactionTypeColumn;
    @FXML
    private TableColumn<TransactionModel, String> transactionDescriptionColumn;
    @FXML
    private TableColumn<TransactionModel, Double> paymentAmountColumn;
    @FXML
    private TableColumn<TransactionModel, Double> depositAmountColumn;
    @FXML
    private TableColumn<TransactionModel, String> transactionDateColumn;

    @FXML
    private TextField searchBar;

    @FXML
    public void initialize() {
        // Set up columns with the TransactionModel fields
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        transactionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        depositAmountColumn.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        // Load and display all transactions in the table
        loadAndDisplayTransactions();

        // Add a listener to the search bar for filtering
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> searchTransactions(newValue));

        // Enable row click functionality to edit transactions
        transactionTable.setRowFactory(tv -> {
            TableRow<TransactionModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    openEditPane(row.getItem());
                }
            });
            return row;
        });
    }

    private void loadAndDisplayTransactions() {
        ObservableList<TransactionModel> transactions = FXCollections.observableArrayList(TransactionModel.getAllTransactions());
        // Sort transactions in descending order by Transaction Date
        transactions.sort(Comparator.comparing(TransactionModel::getTransactionDate).reversed());
        transactionTable.setItems(transactions);
    }

    private void searchTransactions(String searchTerm) {
        ObservableList<TransactionModel> filteredTransactions = FXCollections.observableArrayList();
        for (TransactionModel transaction : TransactionModel.getAllTransactions()) {
            if (transaction.getTransactionDescription().toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredTransactions.add(transaction);
            }
        }
        transactionTable.setItems(filteredTransactions);
    }

    private void openEditPane(TransactionModel transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/editTransaction.fxml"));
            AnchorPane editPane = loader.load();

            // Pass the selected transaction to the edit controller
            EditTransactionController controller = loader.getController();
            controller.setTransaction(transaction);

            // Replace the current view with the edit pane
            HBox root = RootController.getInstance().getContainer();
            root.getChildren().remove(root.getChildren().size() - 1);
            root.getChildren().add(editPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void openEditPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/editTransaction.fxml"));
            AnchorPane editPane = loader.load();

            // Get the controller of the edit view
            EditTransactionController controller = loader.getController();

            // Pass data to the controller if necessary
            TransactionModel selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
            if (selectedTransaction != null) {
                controller.setTransaction(selectedTransaction);
            } else {
                showAlert("No transaction selected for editing.", Alert.AlertType.WARNING);
                return;
            }

            // Replace the right pane with the edit view
            HBox root = RootController.getInstance().getContainer();
            if (!root.getChildren().isEmpty()) {
                root.getChildren().remove(root.getChildren().size() - 1);
            }
            root.getChildren().add(editPane);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to open the edit pane.", Alert.AlertType.ERROR);
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
}
