package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;

public class NewAccountController {

    @FXML DatePicker datePicker;
    @FXML TextField nameField;
    @FXML TextField balanceField;
    @FXML Text errorMsg;
    @FXML Button submitNewAccount;

    public void initialize() {
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    public void setRightPaneAsHome() {
        // set right pane back to init pane
        URL dir = getClass().getResource("/resources/init.fxml");
        try {
            // HBox structured like [sidebar, rightpane], this code removes the last element (rightpane) and then adds a new one (init)
            HBox root = RootController.getInstance().getContainer();
            root.getChildren().remove(root.getChildren().size() - 1);

            AnchorPane newAccountPane = FXMLLoader.load(dir);
            root.getChildren().add(newAccountPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void submitNewAccount() {
        errorMsg.setText("");
        if (nameField.getText().isEmpty() || nameField.getText().trim().isEmpty()) {
            errorMsg.setText("enter a name");
            return;
        }

        if (balanceField.getText().isEmpty() || balanceField.getText().trim().isEmpty()) {
            errorMsg.setText("enter a balance");
            return;
        }

        if (Double.parseDouble(balanceField.getText()) < 0) {
            errorMsg.setText("balance must be more than 0");
            return;
        }

        AccountModel account = new AccountModel(nameField.getText(), Double.parseDouble(balanceField.getText()), datePicker.getValue());
        DataController<AccountModel> dc = new DataController<>(AccountModel.class);
        dc.writeToCSV(dc.getFilePath(), account.getCSVData());

        errorMsg.setText("submitted");
    }
}
