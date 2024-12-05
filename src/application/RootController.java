package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.Stack;

public class RootController {
    private static RootController instance = null;
    private HBox root;
    private final Stack<String> navigationStack = new Stack<>(); // Track navigation history

    private RootController() {
        root = new HBox();
        try {
            // Load sidebar
            AnchorPane sidebar = FXMLLoader.load(getClass().getResource("/resources/sidebar.fxml"));
            root.getChildren().add(sidebar);

            // Load init panel (home page) and add it to the stack
            String initialPage = "/resources/init.fxml";
            navigationStack.push(initialPage); // Add home page to stack
            AnchorPane init = FXMLLoader.load(getClass().getResource(initialPage));
            root.getChildren().add(init);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Singleton pattern to ensure a single instance
    public static synchronized RootController getInstance() {
        if (instance == null) {
            instance = new RootController();
        }
        return instance;
    }

    public HBox getContainer() {
        return root;
    }

    // Load a new page and add it to the navigation stack
    public void loadPage(String fxmlPath) {
        try {
            // Check if the new page is already loaded to prevent duplicates
            if (!navigationStack.isEmpty() && navigationStack.peek().equals(fxmlPath)) {
                return; // If already on the page, do nothing
            }

            navigationStack.push(fxmlPath); // Add the current page to the stack
            AnchorPane page = FXMLLoader.load(getClass().getResource(fxmlPath));

            // Replace the current right pane with the new page
            if (root.getChildren().size() > 1) {
                root.getChildren().remove(1); // Remove the current right pane
            }
            root.getChildren().add(page);
        } catch (Exception e) {
            System.err.println("Error loading FXML file: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // Go back to the previous page
    public void goBack() {
        if (navigationStack.size() > 1) { // Ensure there's a previous page to go back to
            navigationStack.pop(); // Remove the current page
            String previousPage = navigationStack.peek(); // Get the previous page
            try {
                AnchorPane page = FXMLLoader.load(getClass().getResource(previousPage));

                // Replace the current right pane with the previous page
                if (root.getChildren().size() > 1) {
                    root.getChildren().remove(1); // Remove the current right pane
                }
                root.getChildren().add(page);
            } catch (Exception e) {
                System.err.println("Error loading the previous page: " + previousPage);
                e.printStackTrace();
            }
        } else {
            System.out.println("No previous page to go back to."); // Avoid stack underflow
        }
    }
}
