package Guiao2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankEx3 {

    // Lock à conta invés de ao Banco

    private static class Account {
        Lock lock = new ReentrantLock();
        private int balance;

        Account(int balance) {
            this.balance = balance;
        }

        int balance() {
            lock.lock();
            try {
                return balance;
            } finally {
                lock.unlock();
            }
        }

        boolean deposit(int value) {
            lock.lock();
            try {
                balance += value;
                return true;
            } finally {
                lock.unlock();
            }
        }

        boolean withdraw(int value) {
            if (value > balance)
                return false;
            lock.lock();
            try {
                balance -= value;
                return true;
            } finally {
                lock.unlock();
            }
        }
    }

    // Bank slots and vector of accounts
    private int slots;
    private Account[] av;

    public BankEx3(int n) {
        slots = n;
        av = new Account[slots];
        for (int i = 0; i < slots; i++)
            av[i] = new Account(0);
    }

    // Account balance
    public int balance(int id) {
        if (id < 0 || id >= slots)
            return 0;
        return av[id].balance();
    }

    // Deposit
    boolean deposit(int id, int value) {
        if (id < 0 || id >= slots)
            return false;
        return av[id].deposit(value);
    }

    // Withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        if (id < 0 || id >= slots)
            return false;
        return av[id].withdraw(value);
    }

    public boolean transfer(int from, int to, int value) {
        return (withdraw(from, value) && deposit(to, value));
    }

    public int totalBalance() {
        int res = 0;
        for (Account acc : av) {
            res += acc.balance();
        }
        return res;
    }

}
