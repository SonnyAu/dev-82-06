package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccountModel {
    private String name;
    private double balance;
    private LocalDate date;

    private static List<AccountModel> accounts = new ArrayList<>();

    // Static initializer block to pre-populate accounts
    static {
        accounts.add(new AccountModel("Savings", 1000.0, LocalDate.now()));
        accounts.add(new AccountModel("Checking", 500.0, LocalDate.now()));
        accounts.add(new AccountModel("Business", 2000.0, LocalDate.now()));
    }

    public AccountModel(String name, double balance, LocalDate date) {
        this.name = name.toLowerCase(); // Store the name in lowercase
        this.balance = balance;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDate getDate() {
        return date;
    }

    // Method to check for duplicate account names (case insensitive)
    public static boolean isDuplicateAccountName(String newName) {
        String lowerNewName = newName.toLowerCase();
        for (AccountModel account : accounts) {
            if (account.getName().equals(lowerNewName)) {
                return true;
            }
        }
        return false;
    }

    // Add this method to return data in CSV format
    public String[] getCSVData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return new String[]{name, String.valueOf(balance), date.format(formatter)};
    }

    // Static method to retrieve a unique list of account names
    public static List<String> getAccountNames() {
        Set<String> accountNames = new HashSet<>();
        for (AccountModel account : accounts) {
            accountNames.add(account.getName());
        }
        return new ArrayList<>(accountNames);
    }

    @Override
    public String toString() {
        return name + "," + balance + "," + date;
    }
}
