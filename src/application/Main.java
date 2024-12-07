package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    // Set the stage to the root controller
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Prepopulate the CSV files with default data if needed
        prepopulateData();

        // Initialize the root controller and set the stage
        RootController root = RootController.getInstance();
        Scene scene = new Scene(root.getContainer(), 1400, 1000);
        primaryStage.setTitle("Money Minder");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Show popup for scheduled transactions due today
        showScheduledTransactionsPopup();
    }

    public static void main(String[] args) {
        launch();
    }

    // Method to prepopulate data
    private void prepopulateData() {
        // Populate default transactions if the file is empty
        if (FileUtils.isFileEmpty("src/data/transactions.csv")) {
            TransactionModel.populateDefaultTransactions();
            System.out.println("Populated default transactions.");
        }

        // Populate default scheduled transactions if the file is empty
        if (FileUtils.isFileEmpty("src/data/scheduled_transactions.csv")) {
            ScheduledTransactionModel.populateDefaultScheduledTransactions();
            System.out.println("Populated default scheduled transactions.");
        }
    }

    // Method to show the popup for scheduled transactions due today
    private void showScheduledTransactionsPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/scheduledTransactionPopup.fxml"));
            Stage popupStage = new Stage(StageStyle.DECORATED);
            popupStage.setTitle("Scheduled Transactions Due Today");
            popupStage.setScene(new Scene(loader.load()));
            popupStage.initModality(Modality.APPLICATION_MODAL); // Ensure the popup is modal
            popupStage.initOwner(null); // Attach to the main stage (optional)
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
