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

public class ShowScheduledTransactionsController {

    @FXML
    private TableView<ScheduledTransactionModel> scheduledTransactionTable;

    @FXML
    private TableColumn<ScheduledTransactionModel, String> scheduleNameColumn;
    @FXML
    private TableColumn<ScheduledTransactionModel, String> accountColumn;
    @FXML
    private TableColumn<ScheduledTransactionModel, String> transactionTypeColumn;
    @FXML
    private TableColumn<ScheduledTransactionModel, String> frequencyColumn;
    @FXML
    private TableColumn<ScheduledTransactionModel, Integer> dueDateColumn;
    @FXML
    private TableColumn<ScheduledTransactionModel, Double> paymentAmountColumn;

    @FXML
    private TextField searchBar;

    @FXML
    public void initialize() {
        // Set up columns with ScheduledTransactionModel fields
        scheduleNameColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleName"));
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));

        // Load and display all scheduled transactions in the table
        loadAndDisplayScheduledTransactions();

        // Add a listener to the search bar for filtering
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> searchScheduledTransactions(newValue));

        // Enable row click functionality to edit scheduled transactions
        scheduledTransactionTable.setRowFactory(tv -> {
            TableRow<ScheduledTransactionModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    openEditPane(row.getItem());
                }
            });
            return row;
        });
    }

    private void loadAndDisplayScheduledTransactions() {
        ObservableList<ScheduledTransactionModel> transactions = FXCollections.observableArrayList(ScheduledTransactionModel.getScheduledTransactions());
        // Sort transactions by Due Date in ascending order
        transactions.sort(Comparator.comparingInt(ScheduledTransactionModel::getDueDate));
        scheduledTransactionTable.setItems(transactions);
    }

    private void searchScheduledTransactions(String searchTerm) {
        ObservableList<ScheduledTransactionModel> filteredTransactions = FXCollections.observableArrayList();
        for (ScheduledTransactionModel transaction : ScheduledTransactionModel.getScheduledTransactions()) {
            if (transaction.getScheduleName().toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredTransactions.add(transaction);
            }
        }
        scheduledTransactionTable.setItems(filteredTransactions);
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openEditPane(ScheduledTransactionModel transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/editScheduledTransaction.fxml"));
            AnchorPane editPane = loader.load();

            // Pass the selected transaction to the edit controller
            EditScheduledTransactionController controller = loader.getController();
            controller.setTransaction(transaction);

            // Replace the right pane with the edit view
            HBox root = RootController.getInstance().getContainer();
            if (!root.getChildren().isEmpty()) {
                root.getChildren().remove(root.getChildren().size() - 1);
            }
            root.getChildren().add(editPane);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to open the edit pane.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void openEditPane() {
        ScheduledTransactionModel selectedTransaction = scheduledTransactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showAlert("No scheduled transaction selected for editing.", Alert.AlertType.WARNING);
        } else {
            openEditPane(selectedTransaction);
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
