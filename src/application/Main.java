package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    // set stage to the root controller
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
}
