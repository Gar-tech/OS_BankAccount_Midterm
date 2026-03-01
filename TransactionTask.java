public class TransactionTask implements Runnable {

    private final Bank bank;
    private final int from;
    private final int to;
    private final double amount;

    public TransactionTask(Bank bank, int from, int to, double amount) {
        this.bank = bank;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public void run() {
        bank.transfer(from, to, amount);
    }
}
