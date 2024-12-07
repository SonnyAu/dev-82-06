package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class ViewTransactionController {

    @FXML
    private TextField accountField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField paymentField;

    @FXML
    private TextField depositField;

    private TransactionModel transaction;

    public void setTransaction(TransactionModel transaction) {
        this.transaction = transaction;
        populateFields();
    }

    private void populateFields() {
        if (transaction != null) {
            accountField.setText(transaction.getAccount());
            typeField.setText(transaction.getTransactionType());
            dateField.setText(transaction.getTransactionDate().toString());
            descriptionField.setText(transaction.getTransactionDescription());
            paymentField.setText(String.valueOf(transaction.getPaymentAmount()));
            depositField.setText(String.valueOf(transaction.getDepositAmount()));
        }
    }

    @FXML
    private void goBack() {
        try {
            HBox root = RootController.getInstance().getContainer();
            root.getChildren().remove(root.getChildren().size() - 1);

            // Return to the appropriate view (can be adjusted for context)
            AnchorPane initPane = FXMLLoader.load(getClass().getResource("/resources/init.fxml"));
            root.getChildren().add(initPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
