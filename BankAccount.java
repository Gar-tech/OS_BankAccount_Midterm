import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {

    private final int accountNumber;
    private double balance;
    private final ReentrantLock lock = new ReentrantLock();
    private final Statement statement;

    // We use a lock to prevent "Race Conditions" 
    // (where two threads try to change the balance at the exact same microsecond)
    public BankAccount(int accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.statement = new Statement();
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public Statement getStatement() {
        return statement;
    }

    public void deposit(double amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double amount) {
        lock.lock();
        try {
            if (balance < amount) {
                throw new IllegalArgumentException(new StringBuilder()
                    .append("Insufficient balance in account ")
                    .append(accountNumber)
                    .append(".")
                    .toString());
            }
            balance -= amount;
        } finally {
            lock.unlock();
        }
    }
}