package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class RootController {
    private static RootController instance = null;
    private HBox root;

    private RootController() {
        // creates HBox with 2 items, [sidebar, init]
        root = new HBox();
        try {
            AnchorPane sidebar = (AnchorPane) FXMLLoader.load(getClass().getResource("/resources/sidebar.fxml"));
            root.getChildren().add(sidebar);

            AnchorPane init = (AnchorPane) FXMLLoader.load(getClass().getResource("/resources/init.fxml"));
            root.getChildren().add(init);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized RootController getInstance() {
        if (instance == null) {
            instance = new RootController();
        }
        return instance;
    }

    public HBox getContainer() {
        return root;
    }

}
