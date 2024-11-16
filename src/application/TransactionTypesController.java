package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;

public class TransactionTypesController {

    @FXML
    private TextField transactionTypeField;

    @FXML
    private Label statusLabel;

    @FXML
    private Button saveButton;

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
    private void saveTransactionType() {
        String newType = transactionTypeField.getText().trim();

        if (newType.isEmpty()) {
            statusLabel.setText("Transaction type cannot be empty.");
            return;
        }

        if (TransactionModel.isDuplicateTransactionType(newType)) {
            statusLabel.setText("Transaction type already exists.");
            return;
        }

        if (TransactionModel.getTransactionTypes().size() >= 4) {
            statusLabel.setText("Cannot add more than 4 transaction types. Please delete an existing one.");
            return;
        }

        // Add the new transaction type
        TransactionModel.addTransactionType(newType);
        statusLabel.setText("Transaction type added successfully.");
        transactionTypeField.clear();
    }

}