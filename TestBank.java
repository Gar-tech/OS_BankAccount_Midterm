import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestBank {

    private static int maxDepthReached = 0;

    public static void main(String[] args) throws InterruptedException {
        /*
         * This test simulates 5,000 transfers of $5 each from Account A to Account B, 
         * using a thread pool of 4 workers to perform the transfers in parallel.
         * After all transfers are complete, it checks the final balances of both accounts.
        */
        System.out.println("=== Concurrent Transfers Test ===");

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

        /*
         * This second part of the test demonstrates the behavior of the Statement class, 
         * which is responsible for recording and formatting transaction history. 
         * It tests both the pruning of old transactions and the formatting of recent transactions.
        */
        System.out.println("\n=== Statement Behavior Demo ===");

        // 1. Setup a bank with one account for the prune demo
        Bank pruneDemoBank = new Bank();
        pruneDemoBank.addAccount(new BankAccount(100, 10000));
        ExecutorService executor2 = Executors.newFixedThreadPool(4);
        
        long pruneStart = System.currentTimeMillis();
        
        // 2. Create 100 old transactions (4+ months old) to test pruning
        for (int i = 0; i < 100; i++) {
            executor2.execute(() -> {
                pruneDemoBank.deposit(100, 50, LocalDateTime.now().minusMonths(4));
            });
        }
        
        // 3. Wait for all old transactions to complete
        executor2.shutdown();
        while (!executor2.isTerminated()) {
            Thread.sleep(5);
        }
        
        long pruneEnd = System.currentTimeMillis();

        // 4. Display results - old transactions should be pruned automatically
        System.out.println("Prune check:");
        System.out.println("Execution Time: " + (pruneEnd - pruneStart) + " ms");
        pruneDemoBank.showStatement(100);

        // 5. Setup a second demo bank with two accounts for statement formatting
        Bank statementDemoBank = new Bank();
        statementDemoBank.addAccount(new BankAccount(101, 10000));
        statementDemoBank.addAccount(new BankAccount(102, 5000));

        ExecutorService executor3 = Executors.newFixedThreadPool(4);
        
        long statementStart = System.currentTimeMillis();
        
        // 6. Create 99 recent transactions (mix of deposits, withdrawals, and transfers)
        for (int i = 0; i < 33; i++) {
            executor3.execute(() -> statementDemoBank.deposit(101, 10));
        }
        
        for (int i = 0; i < 33; i++) {
            
            executor3.execute(() -> statementDemoBank.withdraw(101, 5));
        }

        for (int i = 0; i < 33; i++) {
            executor3.execute(new TransactionTask(statementDemoBank, 101, 102, 15));
        }
        
        // 7. Wait for all recent transactions to complete
        executor3.shutdown();
        while (!executor3.isTerminated()) {
            Thread.sleep(5);
        }

        long statementEnd = System.currentTimeMillis();

        // 8. Display formatted statements for both accounts
        // Balance 101 should be 9,670 and Balance 102 should be 5,495 after all transactions
        System.out.println("\nRecent transactions:");
        System.out.println("Execution Time: " + (statementEnd - statementStart) + " ms");
        statementDemoBank.showStatement(101);
        statementDemoBank.showStatement(102);

        /* 
         * This part of the test is designed to intentionally cause a StackOverflowError by performing 
         * recursive transfers between two accounts. The test verifies that the error is caught and that 
         * the balances remain consistent after the error occurs.
        */
        System.out.println("\n=== StackOverflow Test (Bank Class) ===");
        runBankStackOverflowTest();
    }

    private static void runBankStackOverflowTest() {
        Bank overflowBank = new Bank();
        overflowBank.addAccount(new BankAccount(201, 1_000_000_000));
        overflowBank.addAccount(new BankAccount(202, 0));

        try {
            recursiveBankTransfer(overflowBank, 1);
        } catch (StackOverflowError e) {
            System.out.println("StackOverflowError caught successfully.");
            System.out.println("Max recursion depth reached: " + maxDepthReached);
            System.out.println("Balance 201: " + overflowBank.checkBalance(201));
            System.out.println("Balance 202: " + overflowBank.checkBalance(202));
        }
    }

    private static void recursiveBankTransfer(Bank bank, int depth) {
        maxDepthReached = depth;
        bank.transfer(201, 202, 1);
        recursiveBankTransfer(bank, depth + 1);
    }
}
