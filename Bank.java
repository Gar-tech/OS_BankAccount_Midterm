import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Iterator;

public class Bank implements AccountService, BankService, NotificationService {

    private final HashMap<Integer, BankAccount> accounts = new HashMap<>();
    private final Deque<Notification> notifications = new ArrayDeque<>();

    /*
    Implementation of BankService methods, which handle the core banking operations.
    */

    @Override
    public void addAccount(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public void showStatement(int accountNumber) {
        BankAccount account = accounts.get(accountNumber);
        System.out.println(
            new StringBuilder().append("Bank Statement for Account ")
            .append(accountNumber).append(":\n")
            .append(account.getStatement().formatStatement())
            .toString());
    }

    @Override
    public void transfer(int fromAcc, int toAcc, double amount) {
        BankAccount from = accounts.get(fromAcc);
        BankAccount to = accounts.get(toAcc);

        try {
            from.withdraw(amount);
            to.deposit(amount);
            from.getStatement().addTransaction(
                LocalDateTime.now(),
                new StringBuilder().append("Transfer to account ").append(toAcc).toString(),
                "Withdraw",
                amount,
                from.getBalance()
            );
            to.getStatement().addTransaction(
                LocalDateTime.now(),
                new StringBuilder().append("Received from account ").append(fromAcc).toString(),
                "Deposit",
                amount,
                to.getBalance()
            );
        } catch (IllegalArgumentException e) {
            System.out.println(new StringBuilder().append("Error: Transfer failed. ").append(e.getMessage()).toString());
        }
    }

    /*
    Implementation of AccountService methods, which handle account-specific operations.
    */

    @Override
    public void deposit(int accountNumber, double amount) {
        accounts.get(accountNumber).deposit(amount);
        accounts.get(accountNumber).getStatement().addTransaction(
            LocalDateTime.now(),
            new StringBuilder().append("Deposit of ").append(amount).toString(),
            "Deposit",
            amount,
            accounts.get(accountNumber).getBalance()
        );
    }

    @Override
    public void withdraw(int accountNumber, double amount) {
        try {
            accounts.get(accountNumber).withdraw(amount);
            accounts.get(accountNumber).getStatement().addTransaction(
                LocalDateTime.now(),
                new StringBuilder().append("Withdrawal of ").append(amount).toString(),
                "Withdraw",
                amount,
                accounts.get(accountNumber).getBalance()
            );
        } catch (IllegalArgumentException e) {
            System.out.println(new StringBuilder().append("Error: ").append(e.getMessage()).toString());
        }
    }

    @Override
    public double checkBalance(int accountNumber) {
        return accounts.get(accountNumber).getBalance();
    }

    /*
    * Overloaded methods for testing pruneStatement() with specific dates.
    */

    public void transfer(int fromAcc, int toAcc, double amount, LocalDateTime date) {
        BankAccount from = accounts.get(fromAcc);
        BankAccount to = accounts.get(toAcc);

        try {
            from.withdraw(amount);
            to.deposit(amount);
            from.getStatement().addTransaction(
                date,
                new StringBuilder().append("Transfer to account ").append(toAcc).toString(),
                "Withdraw",
                amount,
                from.getBalance()
            );
            to.getStatement().addTransaction(
                date,
                new StringBuilder().append("Received from account ").append(fromAcc).toString(),
                "Deposit",
                amount,
                to.getBalance()
            );
        } catch (IllegalArgumentException e) {
            System.out.println(new StringBuilder().append("Error: Transfer failed. ").append(e.getMessage()).toString());
        }
    }

    public void deposit(int accountNumber, double amount, LocalDateTime date) {
        accounts.get(accountNumber).deposit(amount);
        accounts.get(accountNumber).getStatement().addTransaction(
            date,
            new StringBuilder().append("Deposit of ").append(amount).toString(),
            "Deposit",
            amount,
            accounts.get(accountNumber).getBalance()
        );
    }

    public void withdraw(int accountNumber, double amount, LocalDateTime date) {
        try {
            accounts.get(accountNumber).withdraw(amount);
            accounts.get(accountNumber).getStatement().addTransaction(
                date,
                new StringBuilder().append("Withdrawal of ").append(amount).toString(),
                "Withdraw",
                amount,
                accounts.get(accountNumber).getBalance()
            );
        } catch (IllegalArgumentException e) {
            System.out.println(new StringBuilder().append("Error: ").append(e.getMessage()).toString());
        }
    }

    /*
    Implementation of NotificationService methods, which handle user notifications.
    */
    
    @Override
    public Notification getNotification(){
        return this.notifications.peek();
    }

    @Override
    public void sendNotification(){
        System.out.printf("%s \n %s", notifications.peek().getTitle(), notifications.peek().getMessage());
    }

    @Override
    public void pushNotification(){
        Notification n = new Notification("testMsg", "testTitle");
        this.notifications.add(n);
    }

    @Override
    public void pushNotification(String msg, String title){
        Notification n = new Notification(msg, title);
        this.notifications.add(n);
    }

    @Override
    public void markAsRead(Notification n){
        for(Notification i: this.notifications){
            if(i.getTitle().equals(n.getTitle())){
                i.setIsRead(true);
                return;
            }
        }
    }

    @Override
    public void clearAllNotification(){
        for(Iterator itr = this.notifications.iterator(); itr.hasNext();){
            this.notifications.pop();
        }
    }
}