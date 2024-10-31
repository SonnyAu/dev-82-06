package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionModel {
    // Static list to simulate transaction types
    private static List<String> transactionTypes = new ArrayList<>();
    private static List<String[]> transactions = new ArrayList<>(); // Simulated storage for transactions

    static {
        // Adding default transaction types
        transactionTypes.add("House");
        transactionTypes.add("Car");
        transactionTypes.add("Kids");
        transactionTypes.add("Education");
    }

    // Method to get transaction types
    public static List<String> getTransactionTypes() {
        return transactionTypes;
    }

    // Method to save a transaction
    public static void saveTransaction(String account, String transactionType, LocalDate date, String description, String payment, String deposit) {
        // Simulate saving by adding to the transactions list
        transactions.add(new String[]{account, transactionType, date.toString(), description, payment, deposit});
        System.out.println("Transaction saved: " + account + ", " + transactionType + ", " + date + ", " + description + ", " + payment + ", " + deposit);
    }
}
