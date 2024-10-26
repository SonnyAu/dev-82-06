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

// pane loaded after clicking "New Account"
public class NewAccountController {

    @FXML DatePicker datePicker;
    @FXML TextField nameField;
    @FXML TextField balanceField;
    @FXML Text errorMsg;
    @FXML Button submitNewAccount;

    // initialize date with current date
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

    // method for submitting data and creating/adding to csv
    @FXML
    public void submitNewAccount() {
        errorMsg.setText("");

        // name cannot be empty or spaces
        if (nameField.getText().isEmpty() || nameField.getText().trim().isEmpty()) {
            errorMsg.setText("enter a name");
            return;
        }

        // balance cannot be empty or spaces
        if (balanceField.getText().isEmpty() || balanceField.getText().trim().isEmpty()) {
            errorMsg.setText("enter a balance");
            return;
        }

        // balance cannot be < 0
        if (Double.parseDouble(balanceField.getText()) < 0) {
            errorMsg.setText("balance must be more than 0");
            return;
        }

        // create new account and write it to csv using data controller
        AccountModel account = new AccountModel(nameField.getText(), Double.parseDouble(balanceField.getText()), datePicker.getValue());
        DataController<AccountModel> dc = new DataController<>(AccountModel.class);
        dc.writeToCSV(dc.getFilePath(), account.getCSVData());

        errorMsg.setText("submitted");
    }
}
