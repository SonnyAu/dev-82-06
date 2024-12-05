package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

// the base scene
// contains a single HBox populated with sidebar and another element
public class RootController {
    private static RootController instance = null;
    private HBox root;

    private RootController() {
        // creates HBox with 2 items, [sidebar, init]
        root = new HBox();
        try {
            // add sidebar
            AnchorPane sidebar = (AnchorPane) FXMLLoader.load(getClass().getResource("/resources/sidebar.fxml"));
            root.getChildren().add(sidebar);

            // add init panel
            AnchorPane init = (AnchorPane) FXMLLoader.load(getClass().getResource("/resources/init.fxml"));
            root.getChildren().add(init);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // since this is a singleton class we never instantiate it more than once
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