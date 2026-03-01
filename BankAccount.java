import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {

    private int accountNumber;
    private double balance;
    private final ReentrantLock lock = new ReentrantLock();
    // We use a lock to prevent "Race Conditions" 
    // (where two threads try to change the balance at the exact same microsecond)
    public BankAccount(int accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
    

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
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
            if (balance >= amount) {
                balance -= amount;
            }
        } finally {
            lock.unlock();
        }
    }

}
