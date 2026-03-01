import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Statement {
    
    // Thread-safe queue to hold transaction data
    private final ConcurrentLinkedQueue<Transaction> transactionQueue;
    private final ReentrantLock lock = new ReentrantLock();

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

        // Fast check: if the queue is empty or the oldest transaction is recent enough, skip pruning
        Transaction head = transactionQueue.peek();
        if (head == null || !head.getDate().isBefore(threeMonthsAgo)) {
            // No pruning needed
            return;
        }

        lock.lock();
        try {
            // Remove records older than three months, safely handling concurrent modifications
            while (true) {
                head = transactionQueue.peek();
                if (head != null && head.getDate().isBefore(threeMonthsAgo)) {
                    // Old transaction found, remove it
                    transactionQueue.poll();
                }
                else {
                    // No more old transactions to prune
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    // Generate a table format string from all transactions currently in the queue.
    public String formatStatement() {
        if (transactionQueue.isEmpty()) {
            return "No transactions found.";
        }

        StringBuilder sb = new StringBuilder();

        String separator = "-".repeat(100);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // Header
        sb.append(separator).append("\n");
        sb.append(String.format("%-25s %-25s %15s %15s %15s\n",
            "Date", "Detail", "Withdraw", "Deposit", "Balance"));
        sb.append(separator).append("\n");

        // Process the queue
        for (Transaction t : transactionQueue) {
            String withdraw = "Withdraw".equals(t.getType()) ? String.format("%.2f", t.getAmount()) : "";
            String deposit = "Deposit".equals(t.getType()) ? String.format("%.2f", t.getAmount()) : "";

            sb.append(String.format("%-25s %-25s %15s %15s %15.2f\n",
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