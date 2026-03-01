public interface AccountService {
    void deposit(int accountNumber, double amount);
    void withdraw(int accountNumber, double amount);
    double checkBalance(int accountNumber);
}
