package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.net.URL;

public class SidebarController {

    public void initialize() {

    }


    @FXML
    public void newAccountButton() {
        // HBox structured like [sidebar, rightpane], this code removes the last element (rightpane) and then adds a new one (newaccountpane)
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


}
