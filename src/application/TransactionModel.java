package application;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionModel {
    private static final String TRANSACTIONS_FILE = "src/data/transactions.csv";
    private static final String TRANSACTION_TYPES_FILE = "src/data/transaction_types.csv";
    private static List<TransactionModel> transactionsList = new ArrayList<>();
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

    // Getters
    public String getAccount() {
        return account;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    // Setters
    public void setAccount(String account) {
        this.account = account;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    // Method to check for duplicate transaction types
    public static boolean isDuplicateTransactionType(String type) {
        for (String existingType : transactionTypes) {
            if (existingType.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    // Method to add a transaction type
    public static void addTransactionType(String type) {
        if (!isDuplicateTransactionType(type)) {
            transactionTypes.add(type.trim());
            saveTransactionTypes();
        }
    }

    // Get Transaction Types
    public static List<String> getTransactionTypes() {
        return new ArrayList<>(transactionTypes);
    }

    // Save Transaction Types to File
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

    // Save Transaction
    public static void saveTransaction(String account, String transactionType, LocalDate date, String description, double paymentAmount, double depositAmount) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE));
             BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {

            // Check for duplicates in the file
            String line;
            while ((line = reader.readLine()) != null) {
                String[] transactionData = line.split(",");
                if (transactionData.length >= 6 &&
                        transactionData[0].equals(account) &&
                        transactionData[1].equals(transactionType) &&
                        transactionData[2].equals(date.toString()) &&
                        transactionData[3].equals(description)) {
                    System.out.println("Duplicate transaction found. Not saving: " + description);
                    return; // Stop saving if duplicate is found
                }
            }

            // Append the new transaction if it's unique
            String transactionLine = String.join(",",
                    account,
                    transactionType,
                    date.toString(),
                    description,
                    String.valueOf(paymentAmount),
                    String.valueOf(depositAmount)
            );
            writer.write(transactionLine);
            writer.newLine();
            System.out.println("Transaction saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Populate default transactions
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


    // Load All Transactions
    public static List<TransactionModel> getAllTransactions() {
        transactionsList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] transactionData = line.split(",");

                if (transactionData.length < 6) {
                    System.err.println("Malformed transaction line: " + line);
                    continue;
                }

                try {
                    transactionsList.add(new TransactionModel(
                            transactionData[0].trim(),
                            transactionData[1].trim(),
                            LocalDate.parse(transactionData[2].trim()),
                            transactionData[3].trim(),
                            transactionData[4].isEmpty() ? 0.0 : Double.parseDouble(transactionData[4].trim()),
                            transactionData[5].isEmpty() ? 0.0 : Double.parseDouble(transactionData[5].trim())
                    ));
                } catch (Exception e) {
                    System.err.println("Error parsing transaction line: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(transactionsList);
    }
}
