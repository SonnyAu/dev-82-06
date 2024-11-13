package application;

import java.util.ArrayList;
import java.util.List;

public class ScheduledTransactionModel {
    private static List<ScheduledTransaction> scheduledTransactions = new ArrayList<>();

    public static boolean isDuplicateScheduleName(String scheduleName) {
        return scheduledTransactions.stream()
                .anyMatch(transaction -> transaction.getScheduleName().equalsIgnoreCase(scheduleName));
    }

    public static void saveScheduledTransaction(String scheduleName, String account, String transactionType, String frequency, String dueDate, String paymentAmount) {
        if (!isDuplicateScheduleName(scheduleName)) {
            ScheduledTransaction transaction = new ScheduledTransaction(scheduleName, account, transactionType, frequency, dueDate, paymentAmount);
            scheduledTransactions.add(transaction);
            // Add logic here if you want to save it permanently, e.g., to a file.
        }
    }

    public static List<ScheduledTransaction> getScheduledTransactions() {
        return new ArrayList<>(scheduledTransactions);
    }
}
