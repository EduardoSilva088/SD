package Guiao1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankEx3 {
    private static class Account {
        private int balance;
        Account(int balance) {
            this.balance = balance; 
        }
        int balance() { return balance; }
        boolean deposit(int value) {
            balance += value;
            return true;
        }
    }
  
    // Our single account, for now
    private Account savings = new Account(0);

    Lock lock = new ReentrantLock();
    
    // Account balance
    public int balance() {
        lock.lock();
        try{
            return savings.balance();
        } finally{
            lock.unlock();
        }
    }
  
    // Deposit
    boolean deposit(int value) {
        lock.lock();
        try{
            return savings.deposit(value);
        } finally{
            lock.unlock();
        }

    }
}
