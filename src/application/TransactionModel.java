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

    // Static initializer block to pre-populate transaction types
    static {
        transactionTypes.add("Groceries");
        transactionTypes.add("Utilities");
        transactionTypes.add("Rent");
        transactionTypes.add("Entertainment");

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
        if (!isDuplicateTransactionType(type)) {
            transactionTypes.add(type.trim());
            saveTransactionTypes();
        }
    }

    private String account;
    private String transactionType;
    private LocalDate transactionDate;
    private String transactionDescription;
    private double paymentAmount;
    private double depositAmount;

    public TransactionModel(String account, String transactionType, LocalDate transactionDate, String transactionDescription, double paymentAmount, double depositAmount) {
        this.account = account;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.transactionDescription = transactionDescription;
        this.paymentAmount = paymentAmount;
        this.depositAmount = depositAmount;
    }
    public static void saveTransaction(String account, String transactionType, LocalDate date, String description, String payment, String deposit) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            writer.write(String.join(",", account, transactionType, date.toString(), description, payment, deposit));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // getters used in TransactionModel
    public String getAccount() { return account; }
    public String getTransactionType() { return transactionType; }
    public String getTransactionDescription() { return transactionDescription; }
    public double getPaymentAmount() { return paymentAmount; }
    public double getDepositAmount() { return depositAmount; }
    public LocalDate getTransactionDate() { return transactionDate; }

    // Load scheduled transactions from CSV
    public static List<TransactionModel> getAllTransactions() {
        List<TransactionModel> transactionsList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] transactionData = line.split(",");

                // Validate line length
                if (transactionData.length < 6) {
                    System.err.println("Malformed transaction line: " + line);
                    continue; // Skip this line
                }

                try {
                    transactionsList.add(new TransactionModel(
                            transactionData[0].trim(), // account
                            transactionData[1].trim(), // transactionType
                            LocalDate.parse(transactionData[2].trim()), // transactionDate
                            transactionData[3].trim(), // transactionDescription
                            transactionData[4].isEmpty() ? 0.0 : Double.parseDouble(transactionData[4].trim()), // paymentAmount
                            transactionData[5].isEmpty() ? 0.0 : Double.parseDouble(transactionData[5].trim())  // depositAmount
                    ));
                } catch (Exception e) {
                    System.err.println("Error parsing transaction line: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactionsList;
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
