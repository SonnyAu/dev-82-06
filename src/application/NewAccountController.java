package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

public class NewAccountController {

    @FXML
    public DatePicker datePicker;

    @FXML
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
        String errorMsg = "";

    }


}
