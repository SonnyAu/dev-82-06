package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ShowScheduledTransactionsController {

    @FXML
    private TableView<ScheduledTransaction> scheduledTransactionTable;

    @FXML
    private TableColumn<ScheduledTransaction, String> scheduleNameColumn;
    @FXML
    private TableColumn<ScheduledTransaction, String> accountColumn;
    @FXML
    private TableColumn<ScheduledTransaction, String> transactionTypeColumn;
    @FXML
    private TableColumn<ScheduledTransaction, String> frequencyColumn;
    @FXML
    private TableColumn<ScheduledTransaction, String> dueDateColumn;
    @FXML
    private TableColumn<ScheduledTransaction, String> paymentAmountColumn;

    @FXML
    public void initialize() {
        scheduleNameColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleName"));
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));

        ObservableList<ScheduledTransaction> transactions = FXCollections.observableArrayList(ScheduledTransactionModel.getScheduledTransactions());
        scheduledTransactionTable.setItems(transactions);
    }
}
