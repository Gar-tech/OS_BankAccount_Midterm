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
        lock.lock();
        try {
            transactionQueue.add(new Transaction(date, detail, type, amount, balance));
            pruneStatement();
        } finally {
            lock.unlock();
        }
    }

    public void pruneStatement() {
        lock.lock();
        try {
            LocalDateTime threshold = LocalDateTime.now().minusMonths(3);
            while (true) {
            Transaction head = transactionQueue.peek();
            if (head != null && head.getDate().isBefore(threshold)) {
                transactionQueue.poll();
            } else {
                break;
            }
        }
        } finally {
            lock.unlock();
        }
    }

    // Generate a table format string from all transactions currently in the queue.
    public String formatStatement() {
        lock.lock();
        try {
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

            // Print each transaction in the queue
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
        } finally {
            lock.unlock();
        }
    }
}