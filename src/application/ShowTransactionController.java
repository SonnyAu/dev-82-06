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

    @FXML
    public void initialize() {
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        transactionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        depositAmountColumn.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        loadAndDisplayTransactions();
    }

    private void loadAndDisplayTransactions() {
        ObservableList<TransactionModel> transactions = FXCollections.observableArrayList(TransactionModel.getAllTransactions());
        // Sort transactions in descending order by Transaction Date
        transactions.sort(Comparator.comparing(TransactionModel::getTransactionDate).reversed());
        transactionTable.setItems(transactions);
    }

}
