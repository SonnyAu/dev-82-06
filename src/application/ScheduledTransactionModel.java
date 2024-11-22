package application;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduledTransactionModel {

    private static final String SCHEDULED_TRANSACTIONS_FILE = "src/data/scheduled_transactions.csv";

    private String scheduleName;
    private String account;
    private String transactionType;
    private String frequency;
    private int dueDate; // e.g., day of the month
    private double paymentAmount;

    public ScheduledTransactionModel(String scheduleName, String account, String transactionType, String frequency, int dueDate, double paymentAmount) {
        this.scheduleName = scheduleName;
        this.account = account;
        this.transactionType = transactionType;
        this.frequency = frequency;
        this.dueDate = dueDate;
        this.paymentAmount = paymentAmount;
    }

    // Getters for TableView (used in ShowScheduledTransactionsController)
    public String getScheduleName() { return scheduleName; }
    public String getAccount() { return account; }
    public String getTransactionType() { return transactionType; }
    public String getFrequency() { return frequency; }
    public int getDueDate() { return dueDate; }
    public double getPaymentAmount() { return paymentAmount; }

    // Load scheduled transactions from CSV
    public static List<ScheduledTransactionModel> getScheduledTransactions() {
        List<ScheduledTransactionModel> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SCHEDULED_TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                // Validate data length
                if (data.length < 6) {
                    System.err.println("Malformed scheduled transaction line: " + line);
                    continue; // Skip malformed rows
                }

                try {
                    transactions.add(new ScheduledTransactionModel(
                            data[0].trim(), // scheduleName
                            data[1].trim(), // account
                            data[2].trim(), // transactionType
                            data[3].trim(), // frequency
                            Integer.parseInt(data[4].trim()), // dueDate
                            Double.parseDouble(data[5].trim()) // paymentAmount
                    ));
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing scheduled transaction data: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // Save a scheduled transaction to CSV
    public static void saveScheduledTransaction(String scheduleName, String account, String transactionType, String frequency, String dueDate, String paymentAmount) {
        try {
            // Prevent duplicate entries
            if (isDuplicateScheduleName(scheduleName)) {
                System.out.println("Duplicate scheduled transaction. Not saving: " + scheduleName);
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCHEDULED_TRANSACTIONS_FILE, true))) {
                writer.write(scheduleName + "," + account + "," + transactionType + "," + frequency + "," + dueDate + "," + paymentAmount);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Check for duplicate schedule names
    public static boolean isDuplicateScheduleName(String scheduleName) {
        List<ScheduledTransactionModel> transactions = getScheduledTransactions();
        for (ScheduledTransactionModel transaction : transactions) {
            if (transaction.getScheduleName().equalsIgnoreCase(scheduleName)) {
                return true;
            }
        }
        return false;
    }

    // Populate the file with 10 default scheduled transactions
    public static void populateDefaultScheduledTransactions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCHEDULED_TRANSACTIONS_FILE))) {
            writer.write("Netflix Subscription,Savings,Entertainment,Monthly,15,15.0\n");
            writer.write("Gym Membership,Savings,Utilities,Monthly,1,50.0\n");
            writer.write("Office Rent,Business,Rent,Monthly,5,1000.0\n");
            writer.write("Weekly Groceries,Checking,Groceries,Weekly,7,80.0\n");
            writer.write("Electricity Bill,Savings,Utilities,Monthly,20,50.0\n");
            writer.write("Water Bill,Savings,Utilities,Monthly,25,30.0\n");
            writer.write("Internet Bill,Savings,Utilities,Monthly,10,60.0\n");
            writer.write("Insurance Premium,Savings,Utilities,Monthly,1,100.0\n");
            writer.write("Client Meeting,Business,Entertainment,Quarterly,1,300.0\n");
            writer.write("Yearly Subscription,Savings,Entertainment,Yearly,1,120.0\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
