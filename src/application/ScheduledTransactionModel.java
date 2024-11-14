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
    private int dueDate;
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
                transactions.add(new ScheduledTransactionModel(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        Integer.parseInt(data[4]),
                        Double.parseDouble(data[5])
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // Save a scheduled transaction to CSV
    public static void saveScheduledTransaction(String scheduleName, String account, String transactionType, String frequency, String dueDate, String paymentAmount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCHEDULED_TRANSACTIONS_FILE, true))) {
            writer.write(scheduleName + "," + account + "," + transactionType + "," + frequency + "," + dueDate + "," + paymentAmount);
            writer.newLine();
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
}
