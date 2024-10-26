package application;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// the inital right pane loaded when starting the application
public class InitController {

    @FXML TableView<AccountModel> accountTable;
    @FXML TableColumn<AccountModel, String> nameCol;
    @FXML TableColumn<AccountModel, Double> balanceCol;
    @FXML TableColumn<AccountModel, LocalDate> dateCol;

    // initializes the pane with a table full of accounts
    public void initialize() {
        // populating table with the values from the accounts csv
        DataController dc = new DataController("accounts.csv");
        ArrayList<String[]> accounts = dc.readFromCSV();

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        ObservableList<AccountModel> data = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        // iterate through the arrays in accounts
        for (String[] row: accounts) {
            try {
                String name = row[0];
                double balance = Double.parseDouble(row[1]);
                LocalDate date = LocalDate.parse(row[2], formatter);

                AccountModel account = new AccountModel(name, balance, date);
                data.add(account);

            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        // fill the accounts table
        accountTable.setItems(data);
        // sort by descending dates
        dateCol.setSortType(TableColumn.SortType.DESCENDING);
        accountTable.getSortOrder().add(dateCol);
    }
}
