package application;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduledTransactionModel implements Model{

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

    public String getScheduleName() { return scheduleName; }
    public String getAccount() { return account; }
    public String getTransactionType() { return transactionType; }
    public String getFrequency() { return frequency; }
    public int getDueDate() { return dueDate; }
    public double getPaymentAmount() { return paymentAmount; }

    public void setScheduleName(String scheduleName) { this.scheduleName = scheduleName; }
    public void setAccount(String account) { this.account = account; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setDueDate(int dueDate) { this.dueDate = dueDate; }
    public void setPaymentAmount(double paymentAmount) { this.paymentAmount = paymentAmount; }

    // Load all scheduled transactions
    public static List<ScheduledTransactionModel> getScheduledTransactions() {
        List<ScheduledTransactionModel> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SCHEDULED_TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) {
                    System.err.println("Malformed scheduled transaction line: " + line);
                    continue;
                }

                try {
                    transactions.add(new ScheduledTransactionModel(
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            Integer.parseInt(data[4].trim()),
                            Double.parseDouble(data[5].trim())
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

    // Save a new scheduled transaction
    public static void saveScheduledTransaction(String scheduleName, String account, String transactionType, String frequency, String dueDate, String paymentAmount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCHEDULED_TRANSACTIONS_FILE, true))) {
            writer.write(String.join(",",
                    scheduleName,
                    account,
                    transactionType,
                    frequency,
                    dueDate,
                    paymentAmount
            ));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save a new scheduled transaction using the model object
    public static void saveScheduledTransaction(ScheduledTransactionModel transaction) {
        saveScheduledTransaction(
                transaction.getScheduleName(),
                transaction.getAccount(),
                transaction.getTransactionType(),
                transaction.getFrequency(),
                String.valueOf(transaction.getDueDate()),
                String.valueOf(transaction.getPaymentAmount())
        );
    }

    public static void updateScheduledTransaction(ScheduledTransactionModel updatedTransaction, String oldScheduleName) {
        List<ScheduledTransactionModel> transactions = getScheduledTransactions();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCHEDULED_TRANSACTIONS_FILE))) {
            for (ScheduledTransactionModel transaction : transactions) {
                if (transaction.getScheduleName().equalsIgnoreCase(oldScheduleName)) {
                    writer.write(String.join(",",
                            updatedTransaction.getScheduleName(),
                            updatedTransaction.getAccount(),
                            updatedTransaction.getTransactionType(),
                            updatedTransaction.getFrequency(),
                            String.valueOf(updatedTransaction.getDueDate()),
                            String.valueOf(updatedTransaction.getPaymentAmount())
                    ));
                } else {
                    writer.write(String.join(",",
                            transaction.getScheduleName(),
                            transaction.getAccount(),
                            transaction.getTransactionType(),
                            transaction.getFrequency(),
                            String.valueOf(transaction.getDueDate()),
                            String.valueOf(transaction.getPaymentAmount())
                    ));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // Prevent duplicate schedule names
    public static boolean isDuplicateScheduleName(String scheduleName) {
        List<ScheduledTransactionModel> transactions = getScheduledTransactions();
        return transactions.stream().anyMatch(t -> t.getScheduleName().equalsIgnoreCase(scheduleName));
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
