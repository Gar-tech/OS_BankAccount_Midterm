
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestBank {

	public static void main(String[] args) throws InterruptedException {

        Bank bank = new Bank();
        bank.addAccount(new BankAccount(1, 500000));
        bank.addAccount(new BankAccount(2, 0));

        ExecutorService executor = Executors.newFixedThreadPool(4);

        long start = System.currentTimeMillis();

        for (int i = 0; i < 5000; i++) {
            executor.execute(new TransactionTask(bank, 1, 2, 5));
        }

        executor.shutdown();

        while (!executor.isTerminated()) {
            Thread.sleep(5);
        }

        long end = System.currentTimeMillis();

        System.out.println("Execution Time: " + (end - start) + " ms");
        System.out.println("Balance A: " + bank.checkBalance(1));
        System.out.println("Balance B: " + bank.checkBalance(2));
    }
}