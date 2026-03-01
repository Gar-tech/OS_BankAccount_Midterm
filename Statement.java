import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Statement {
    
    // Thread-safe queue to hold transaction data
    private final Queue<Transaction> transactionQueue;

    public Statement() {
        this.transactionQueue = new ConcurrentLinkedQueue<>();
    }

    // Add a transaction to the queue to be processed later and prune old transactions.
    public void addTransaction(LocalDateTime date, String detail, String type, double amount, double balance) {
        transactionQueue.add(new Transaction(date, detail, type, amount, balance));
        pruneStatement();
    }

    public void pruneStatement() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

        // Remove records older than three months, safely handling concurrent modifications
        while (true) {
            Transaction head = transactionQueue.poll();
            if (head == null) {
                // Queue is empty; nothing more to prune
                break;
            }
            if (head.getDate().isBefore(threeMonthsAgo)) {
                // Old record, drop it and continue pruning
                continue;
            }
            // Not old enough: re-add to the queue and stop, as subsequent records will be newer
            transactionQueue.add(head);
            break;
        }

    }

    // Generate a table format string from all transactions currently in the queue.
    public String formatStatement() {
        if (transactionQueue.isEmpty()) {
            return "No transactions found.";
        }

        StringBuilder sb = new StringBuilder();

        String separator = "-".repeat(87);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // Header
        sb.append(separator).append("\n");
        sb.append(String.format("%-20s %-22s %12s %12s %12s\n",
                "Date", "Detail", "Withdraw", "Deposit", "Balance"));
        sb.append(separator).append("\n");

        // Process the queue
        for (Transaction t : transactionQueue) {
            String withdraw = "Withdraw".equals(t.getType()) ? String.format("%.2f", t.getAmount()) : "";
            String deposit = "Deposit".equals(t.getType()) ? String.format("%.2f", t.getAmount()) : "";

            sb.append(String.format("%-20s %-22s %12s %12s %12.2f\n",
                    t.getDate().format(formatter),
                    t.getDetail(),
                    withdraw,
                    deposit,
                    t.getBalance()));
        }

        sb.append(separator).append("\n");

        return sb.toString();
    }
}