package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import java.net.URL;

public class SidebarController {

    public void initialize() {}

    @FXML
    public void newAccountButton() {
        URL dir = getClass().getResource("/resources/newAccount.fxml");
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
    public void enterTransactionsButton() {
        // Updated to load enterTransactions.fxml
        URL dir = getClass().getResource("/resources/enterTransactions.fxml");
        try {
            HBox root = RootController.getInstance().getContainer();
            root.getChildren().remove(root.getChildren().size() - 1);

            AnchorPane enterTransactionsPane = FXMLLoader.load(dir);
            root.getChildren().add(enterTransactionsPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void transactionTypesButton() {
        URL dir = getClass().getResource("/resources/transactionTypes.fxml");
        try {
            HBox root = RootController.getInstance().getContainer();
            root.getChildren().remove(root.getChildren().size() - 1);

            AnchorPane transactionTypesPane = FXMLLoader.load(dir);
            root.getChildren().add(transactionTypesPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
