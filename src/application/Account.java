package application;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class Account {
    private final SimpleStringProperty accountName;
    private final SimpleStringProperty openingBalance;
    private final SimpleObjectProperty<LocalDate> openingDate;

    public Account(String accountName, String openingBalance, LocalDate openingDate) {
        this.accountName = new SimpleStringProperty(accountName);
        this.openingBalance = new SimpleStringProperty(openingBalance);
        this.openingDate = new SimpleObjectProperty<>(openingDate);
    }

    public String getAccountName() {
        return accountName.get();
    }

    public SimpleStringProperty accountNameProperty() {
        return accountName;
    }

    public String getOpeningBalance() {
        return openingBalance.get();
    }

    public SimpleStringProperty openingBalanceProperty() {
        return openingBalance;
    }

    public LocalDate getOpeningDate() {
        return openingDate.get();
    }

    public SimpleObjectProperty<LocalDate> openingDateProperty() {
        return openingDate;
    }
}