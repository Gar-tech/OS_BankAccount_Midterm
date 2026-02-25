
import java.util.HashMap;

public class Bank implements AccountService, BankService {

    private HashMap<Integer, BankAccount> accounts = new HashMap<>();

    @Override
    public void addAccount(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public void deposit(int accountNumber, double amount) {
        accounts.get(accountNumber).deposit(amount);
    }

    @Override
    public void withdraw(int accountNumber, double amount) {
        accounts.get(accountNumber).withdraw(amount);
    }

    @Override
    public double checkBalance(int accountNumber) {
        return accounts.get(accountNumber).getBalance();
    }

    @Override
    public void transfer(int fromAcc, int toAcc, double amount) {
        BankAccount from = accounts.get(fromAcc);
        BankAccount to = accounts.get(toAcc);

        from.withdraw(amount);
        to.deposit(amount);
    }
}