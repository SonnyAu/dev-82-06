package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// this class is a model for an account
public class AccountModel {
    private String name;
    private double balance;
    private LocalDate date;

    // generic account
    public AccountModel(String name, double balance, LocalDate date) {
        this.name = name;
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

    // returns an array of strings for a csv
    public String[] getCSVData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        return new String[]{name, String.valueOf(balance), date.format(formatter)};
    }

    @Override
    public String toString() {
        return name + "," + balance + "," + date;
    }
}
