package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// This class is a model for an account
public class AccountModel {
    private String name;
    private double balance;
    private LocalDate date;

    // Static list to simulate stored accounts
    private static List<AccountModel> accounts = new ArrayList<>();

    // Generic account
    public AccountModel(String name, double balance, LocalDate date) {
        this.name = name;
        this.balance = balance;
        this.date = date;
        accounts.add(this); // Add this account to the list
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

    // Returns an array of strings for a CSV
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
        return new ArrayList<>(accountNames); // Convert back to list
    }

    @Override
    public String toString() {
        return name + "," + balance + "," + date;
    }
}
