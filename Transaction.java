import java.time.LocalDateTime;

public class Transaction {
        
    private final LocalDateTime date;
    private final String detail;
    private final String type;
    private final double amount;
    private final double balance;

    public Transaction(LocalDateTime date, String detail, String type, double amount, double balance) {
        this.date = date;
        this.detail = detail;
        this.type = type;
        this.amount = amount;
        this.balance = balance;
    }

    public LocalDateTime getDate() { return date; }
    public String getDetail() { return detail; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalance() { return balance; }
}