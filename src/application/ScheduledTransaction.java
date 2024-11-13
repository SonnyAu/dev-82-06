package application;

public class ScheduledTransaction {
    private String scheduleName;
    private String account;
    private String transactionType;
    private String frequency;
    private String dueDate;
    private String paymentAmount;

    public ScheduledTransaction(String scheduleName, String account, String transactionType, String frequency, String dueDate, String paymentAmount) {
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
    public String getDueDate() { return dueDate; }
    public String getPaymentAmount() { return paymentAmount; }
}
