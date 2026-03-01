import java.util.*;

public class Bank implements AccountService, BankService, NotificationService {

    private HashMap<Integer, BankAccount> accounts = new HashMap<>();
    private Deque<Notification> notifications;
    @Override
    public void addAccount(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
        this.notifications = new ArrayDeque<>();
    }

    @Override
    public void deposit(int accountNumber, double amount) {
        accounts.get(accountNumber).deposit(amount);
        this.notifications = new ArrayDeque<>();
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

    public Notification getNotification(){
        return this.notifications.peek();
    }

    public void sendNotification(){
        System.out.printf("%s \n %s", notifications.peek().getTitle(), notifications.peek().getMessage());
    }

    public void pushNotification(){
        Notification n = new Notification("testMsg", "testTitle");
        this.notifications.add(n);
    }

    public void pushNotification(String msg, String title){
        Notification n = new Notification(msg, title);
        this.notifications.add(n);
    }
    public void markAsRead(Notification n){
        for(Notification i: this.notifications){
            if(i.getTitle().equals(n.getTitle())){
                i.setIsRead(true);
                return;
            }
        }
    }
    public void clearAllNotification(){
        for(Iterator itr = this.notifications.iterator(); itr.hasNext();){
            this.notifications.pop();
        }
    }
}