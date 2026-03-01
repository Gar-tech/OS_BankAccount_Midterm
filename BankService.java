public interface BankService {
    void transfer(int fromAcc, int toAcc, double amount);
    void addAccount(BankAccount account);
    void showStatement(int accountNumber);
}