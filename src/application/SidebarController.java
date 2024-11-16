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
        loadPage("/resources/newAccount.fxml");
    }

    @FXML
    public void transactionTypesButton() {
        loadPage("/resources/transactionTypes.fxml");
    }

    @FXML
    public void enterTransactionsButton() {
        loadPage("/resources/enterTransactions.fxml");
    }

    @FXML
    public void showTransactionsButton() {
        loadPage("/resources/showTransactions.fxml");
    }

    @FXML
    public void enterScheduledTransactionsButton() {
        loadPage("/resources/enterScheduledTransactions.fxml");
    }

    @FXML
    public void showScheduledTransactionsButton() {
        loadPage("/resources/showScheduledTransactions.fxml");
    }

    private void loadPage(String fxmlPath) {
        try {
            URL dir = getClass().getResource(fxmlPath);
            HBox root = RootController.getInstance().getContainer();
            root.getChildren().remove(root.getChildren().size() - 1);

            AnchorPane pane = FXMLLoader.load(dir);
            root.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
