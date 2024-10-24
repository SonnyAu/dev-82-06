package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DefineNewAccountPage {

    private TextField accountNameField;
    private TextField openingBalanceField;
    private DatePicker openingDateField;
    private ObservableList<Account> accountsList;
    private String filePath = "accounts.csv";  // Use CSV file

    public DefineNewAccountPage() {
        accountsList = FXCollections.observableArrayList();
        loadAccountsFromFile();  // Load accounts from file when the program starts
    }

    // Save account to a CSV file
    private void saveAccountToFile(Account account) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(account.getAccountName() + "," + account.getOpeningBalance() + "," + account.getOpeningDate());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load accounts from the CSV file
    private void loadAccountsFromFile() {
        try {
            // Check if the file exists; if not, create it
            if (!Files.exists(Paths.get(filePath))) {
                Files.createFile(Paths.get(filePath));  // Automatically create CSV file if not present
            }

            // Load accounts from the CSV file
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String accountName = parts[0];
                    String openingBalance = parts[1];
                    LocalDate openingDate = LocalDate.parse(parts[2]);
                    accountsList.add(new Account(accountName, openingBalance, openingDate));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void display(Stage stage) {
        // Create form elements
        Label accountNameLabel = new Label("Account Name:");
        accountNameField = new TextField();

        Label openingBalanceLabel = new Label("Opening Balance:");
        openingBalanceField = new TextField();

        Label openingDateLabel = new Label("Opening Date:");
        openingDateField = new DatePicker(LocalDate.now());

        // Layout for form
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(accountNameLabel, 0, 0);
        gridPane.add(accountNameField, 1, 0);
        gridPane.add(openingBalanceLabel, 0, 1);
        gridPane.add(openingBalanceField, 1, 1);
        gridPane.add(openingDateLabel, 0, 2);
        gridPane.add(openingDateField, 1, 2);

        // Buttons for Save, Back, View Accounts, and Clear
        Button saveButton = new Button("Save");
        Button backButton = new Button("Back");
        Button viewAccountsButton = new Button("View Accounts");
        Button clearButton = new Button("Clear");

        HBox buttonBox = new HBox(10, saveButton, viewAccountsButton, clearButton, backButton);
        buttonBox.setPadding(new Insets(10));

        VBox layout = new VBox(10, gridPane, buttonBox);
        layout.setPadding(new Insets(10));

        // Set the scene and show the stage
        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Create New Account");
        stage.setScene(scene);
        stage.show();

        // Handle Back button click
        backButton.setOnAction(event -> Main.displayHome(stage));

        // Handle Save button click
        saveButton.setOnAction(event -> {
            String accountName = accountNameField.getText();
            String openingBalance = openingBalanceField.getText();
            LocalDate openingDate = openingDateField.getValue();

            if (accountName.isEmpty() || openingBalance.isEmpty() || openingDate == null) {
                System.out.println("Please fill out all fields.");
                return;
            }

            Account newAccount = new Account(accountName, openingBalance, openingDate);
            accountsList.add(newAccount);

            // Save the new account to the CSV file
            saveAccountToFile(newAccount);

            System.out.println("Account saved: " + newAccount.getAccountName());
        });

        // Handle View Accounts button click
        viewAccountsButton.setOnAction(event -> displayAccountsTable(stage));

        // Handle Clear button click to allow another entry
        clearButton.setOnAction(event -> {
            accountNameField.clear();
            openingBalanceField.clear();
            openingDateField.setValue(LocalDate.now());
        });
    }

    private void displayAccountsTable(Stage stage) {
        TableView<Account> accountTable = new TableView<>();

        TableColumn<Account, String> nameColumn = new TableColumn<>("Account Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().accountNameProperty());

        TableColumn<Account, String> balanceColumn = new TableColumn<>("Opening Balance");
        balanceColumn.setCellValueFactory(cellData -> cellData.getValue().openingBalanceProperty());

        TableColumn<Account, LocalDate> dateColumn = new TableColumn<>("Opening Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().openingDateProperty());

        accountTable.setItems(accountsList);
        accountTable.getColumns().addAll(nameColumn, balanceColumn, dateColumn);

        VBox tableLayout = new VBox(accountTable);
        Scene tableScene = new Scene(tableLayout, 400, 300);
        stage.setScene(tableScene);
        stage.setTitle("Stored Accounts");
        stage.show();
    }

    public List<Account> getAccountsList() {
        return new ArrayList<>(accountsList);
    }
}