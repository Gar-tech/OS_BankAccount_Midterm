# OS_BankAccount_Midterm
The Banking System is an object-oriented application that manages accounts, transactions, statements, and notifications. The Bank class controls accounts using a HashMap, while each BankAccount supports deposit, withdrawal, and transfer. Concurrency is implemented using threads and locking to ensure safe parallel transactions and data consistency.

# OS BankAccount: Multithreaded Banking System


### 🛠 How it works
1. **The Bank**: Acts as the central hub, keeping track of everyone in a `HashMap`.
2. **The Account**: Each `BankAccount` is responsible for its own money and its own "lock."
3. **Transaction Tasks**: These are small "workers" that we throw into a thread pool to simulate real-world traffic.

### 🧪 Running the Test
use `TestBank.java`. It will set up two accounts, move $25,000 between them across 4 threads, and then print the final balances and the time it took to finish.
