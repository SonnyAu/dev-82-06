package application;

import java.time.LocalDate;
import java.util.Date;

public class AccountModel {
    private String name;
    private double balance;
    private LocalDate date;


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

    public String[] getCSVData() {
        return new String[]{name, String.valueOf(balance), date.toString()};
    }

    @Override
    public String toString() {
        return name + "," + balance + "," + date;
    }


}
