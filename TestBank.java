import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestBank {

	public static void main(String[] args) throws InterruptedException {
		// 1. Setup the bank and some test accounts
        Bank bank = new Bank();
        bank.addAccount(new BankAccount(1, 500000));
        bank.addAccount(new BankAccount(2, 0));
        
        // 2. Create a "Thread Pool" with 4 workers to handle tasks in parallel
        ExecutorService executor = Executors.newFixedThreadPool(4);

        long start = System.currentTimeMillis();
        
        // 3. Try 5,000 transfers of $5 each
        for (int i = 0; i < 5000; i++) {
            executor.execute(new TransactionTask(bank, 1, 2, 5));
        }
        
        // 4. Tell the workers to stop taking new jobs and wait for them to finish
        executor.shutdown();

        while (!executor.isTerminated()) {
            Thread.sleep(5);
        }

        long end = System.currentTimeMillis();
        
        // 5. Results! Balance A should be 475,000 and Balance B should be 25,000
        System.out.println("Execution Time: " + (end - start) + " ms");
        System.out.println("Balance A: " + bank.checkBalance(1));
        System.out.println("Balance B: " + bank.checkBalance(2));
    }

}
