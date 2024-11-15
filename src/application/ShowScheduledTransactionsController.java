package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

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
    public void setRightPaneAsHome() {
        URL dir = getClass().getResource("/resources/init.fxml");
        try {
            HBox root = RootController.getInstance().getContainer();
            root.getChildren().remove(root.getChildren().size() - 1);

            AnchorPane newAccountPane = FXMLLoader.load(dir);
            root.getChildren().add(newAccountPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        scheduleNameColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleName"));
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));

        loadAndDisplayTransactions();
    }

    private void loadAndDisplayTransactions() {
        ObservableList<ScheduledTransactionModel> transactions = FXCollections.observableArrayList(ScheduledTransactionModel.getScheduledTransactions());
        transactions.sort(Comparator.comparingInt(ScheduledTransactionModel::getDueDate)); // Sort ascending by due date
        scheduledTransactionTable.setItems(transactions);
    }
}
