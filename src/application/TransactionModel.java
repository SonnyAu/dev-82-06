package application;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionModel {
    private static final String TRANSACTIONS_FILE = "src/data/transactions.csv";
    private static final String TRANSACTION_TYPES_FILE = "src/data/transaction_types.csv";
    public static List<TransactionModel> transactionsList = new ArrayList<>();
    private static List<String> transactionTypes = new ArrayList<>();

    private String account;
    private String transactionType;
    private LocalDate transactionDate;
    private String transactionDescription;
    private double paymentAmount;
    private double depositAmount;

    // Constructor
    public TransactionModel(String account, String transactionType, LocalDate transactionDate, String transactionDescription, double paymentAmount, double depositAmount) {
        this.account = account;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.transactionDescription = transactionDescription;
        this.paymentAmount = paymentAmount;
        this.depositAmount = depositAmount;
    }

    // Getters and Setters
    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
    public String getTransactionDescription() { return transactionDescription; }
    public void setTransactionDescription(String transactionDescription) { this.transactionDescription = transactionDescription; }
    public double getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(double paymentAmount) { this.paymentAmount = paymentAmount; }
    public double getDepositAmount() { return depositAmount; }
    public void setDepositAmount(double depositAmount) { this.depositAmount = depositAmount; }

    // Save a new transaction
    public static void saveTransaction(TransactionModel transaction) {
        transactionsList.add(transaction);
        saveTransactionsToFile();
    }

    // Update an existing transaction
    public static void updateTransaction(TransactionModel updatedTransaction) {
        for (int i = 0; i < transactionsList.size(); i++) {
            if (transactionsList.get(i).equals(updatedTransaction)) {
                transactionsList.set(i, updatedTransaction);
                break;
            }
        }
        saveTransactionsToFile();
    }

    // Save transactions to file
    private static void saveTransactionsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (TransactionModel transaction : transactionsList) {
                writer.write(String.join(",",
                        transaction.getAccount(),
                        transaction.getTransactionType(),
                        transaction.getTransactionDate().toString(),
                        transaction.getTransactionDescription(),
                        String.valueOf(transaction.getPaymentAmount()),
                        String.valueOf(transaction.getDepositAmount())
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load transactions from file
    public static List<TransactionModel> getAllTransactions() {
        transactionsList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                transactionsList.add(new TransactionModel(
                        data[0].trim(),
                        data[1].trim(),
                        LocalDate.parse(data[2].trim()),
                        data[3].trim(),
                        Double.parseDouble(data[4].trim()),
                        Double.parseDouble(data[5].trim())
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(transactionsList);
    }

    // Populate Default Transactions
    public static void populateDefaultTransactions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE))) {
            writer.write("Savings,Groceries,2024-11-01,Weekly groceries,0.0,100.0\n");
            writer.write("Savings,Utilities,2024-11-02,Paid electricity bill,50.0,0.0\n");
            writer.write("Savings,Deposit,2024-11-03,Salary deposit,0.0,1500.0\n");
            writer.write("Savings,Entertainment,2024-11-04,Movie tickets,30.0,0.0\n");
            writer.write("Checking,Groceries,2024-11-05,Supermarket shopping,80.0,0.0\n");
            writer.write("Checking,Deposit,2024-11-06,Cash deposit,0.0,200.0\n");
            writer.write("Business,Groceries,2024-11-07,Office snacks,0.0,100.0\n");
            writer.write("Business,Entertainment,2024-11-08,Client dinner,150.0,0.0\n");
            writer.write("Business,Rent,2024-11-09,Office rent,1000.0,0.0\n");
            writer.write("Savings,Utilities,2024-11-10,Internet bill,40.0,0.0\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        getAllTransactions(); // Refresh the transactionsList
    }

    // Get Transaction Types
    public static List<String> getTransactionTypes() {
        if (transactionTypes.isEmpty()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_TYPES_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    transactionTypes.add(line.trim());
                }
            } catch (IOException e) {
                System.err.println("Error reading transaction types file: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return new ArrayList<>(transactionTypes);
    }

    // Add a transaction type
    public static void addTransactionType(String type) {
        if (!isDuplicateTransactionType(type)) {
            transactionTypes.add(type);
            saveTransactionTypesToFile();
        }
    }

    // Check if a transaction type is duplicate
    public static boolean isDuplicateTransactionType(String newType) {
        for (String existingType : transactionTypes) {
            if (existingType.equalsIgnoreCase(newType)) {
                return true;
            }
        }
        return false;
    }

    // Save transaction types to file
    private static void saveTransactionTypesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_TYPES_FILE))) {
            for (String type : transactionTypes) {
                writer.write(type);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving transaction types: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Equals method for comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TransactionModel)) return false;
        TransactionModel other = (TransactionModel) obj;
        return account.equals(other.account) &&
                transactionType.equals(other.transactionType) &&
                transactionDate.equals(other.transactionDate) &&
                transactionDescription.equals(other.transactionDescription);
    }
}
