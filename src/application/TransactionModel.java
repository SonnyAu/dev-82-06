package application;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionModel {
    private static List<String> transactionTypes = new ArrayList<>();
    private static List<String[]> transactions = new ArrayList<>();
    private static final String TRANSACTION_TYPES_FILE = "src/data/transaction_types.csv";
    private static final String TRANSACTIONS_FILE = "src/data/transactions.csv";

    static {
        loadTransactionTypes();
        loadTransactions();
    }

    public static List<String> getTransactionTypes() {
        return new ArrayList<>(transactionTypes);
    }

    public static boolean isDuplicateTransactionType(String type) {
        String lowerType = type.toLowerCase();
        for (String existingType : transactionTypes) {
            if (existingType.toLowerCase().equals(lowerType)) {
                return true;
            }
        }
        return false;
    }

    public static void addTransactionType(String type) {
        transactionTypes.add(type.trim());
        saveTransactionTypes();
    }

    public static void saveTransaction(String account, String transactionType, LocalDate date, String description, String payment, String deposit) {
        transactions.add(new String[]{account, transactionType, date.toString(), description, payment, deposit});
        saveTransactions();
    }

    private static void saveTransactionTypes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_TYPES_FILE))) {
            for (String type : transactionTypes) {
                writer.write(type);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadTransactionTypes() {
        transactionTypes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_TYPES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transactionTypes.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveTransactions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (String[] transaction : transactions) {
                writer.write(String.join(",", transaction));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadTransactions() {
        transactions.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transactions.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

