package Guiao3;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BankEx1 {

    private static class Account {
        private int balance;
        private Lock l = new ReentrantLock();

        Account(int balance) {
            this.balance = balance;
        }

        int balance() {
            return balance;
        }

        boolean deposit(int value) {
            balance += value;
            return true;
        }

        boolean withdraw(int value) {
            if (value > balance)
                return false;
            balance -= value;
            return true;
        }
    }

    private Map<Integer, Account> map = new HashMap<Integer, Account>();

    private int nextId = 0; // Id de conta atribuido

    Lock l = new ReentrantLock();

    // create account and return account id
    public int createAccount(int balance) {
        Account c = new Account(balance);
        l.lock();
        try {
            int id = nextId;
            nextId += 1;
            map.put(id, c);
            return id;
        } finally {
            l.unlock();
        }

    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
        Account c = map.remove(id);
        l.lock();
        try {
            if (c == null)
                return 0;
            c.l.lock(); // Lock sobre a conta
        } finally {
            l.unlock();
        }
        // Neste ponto temos o lock global livre mas a conta está em lock
        //ou seja, ainda está em memória
        try {
            return c.balance();
        } finally {
            c.l.unlock(); // Unlock da conta
        }
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        Account c = map.get(id);
        l.lock();
        try {
            if (c == null)
                return 0;
            c.l.lock();
        } finally {
            l.unlock();
        }
        try {
            return c.balance();
        } finally {
            c.l.unlock();
        }
    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        Account c = map.get(id);
        l.lock();
        try {
            if (c == null)
                return false;
            c.l.lock();
        } finally {
            l.unlock();
        }
        try {
            return c.deposit(value);
        } finally {
            c.l.unlock();
        }
    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        Account c = map.get(id);
        l.lock();
        try {
            if (c == null)
                return false;
            c.l.lock();
        } finally {
            l.unlock();
        }
        try {
            return c.withdraw(value);
        } finally {
            c.l.unlock();
        }
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        Account cfrom, cto;
        l.lock();
        try {
            cfrom = map.get(from);
            cto = map.get(to);
            if (cfrom == null || cto == null)
                return false;
            cfrom.l.lock();
            cto.l.lock();
        } finally {
            l.unlock();
        }
        try {
            try {
                if (!cfrom.withdraw(value))
                    return false;
            } finally {
                cfrom.l.unlock();
            }
            return cto.deposit(value);
        } finally {
            cto.l.unlock();
        }
    }

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance(int[] ids) {
        int total = 0;
        for (int i : ids) {
            Account c = map.get(i);
            if (c == null)
                return 0;
            total += c.balance();
        }
        return total;
    }

}