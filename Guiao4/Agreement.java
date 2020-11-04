package Guiao4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static java.lang.Math.max;

public class Agreement{

    private class Instance{
        int result = Integer.MIN_VALUE;
        int c = 0;
    }

    private int n;

    private Lock l;
    private Condition c;
    private Instance instance;

    
    Agreement (int N){
        this.n = N;
        this.l = new ReentrantLock();
        this.c = l.newCondition();
        this.instance = new Instance();
    }

    public int propose(int choice) throws InterruptedException {
        this.l.lock();
        try{
            Instance instance = this.instance;
            instance.c++;
            instance.result = max(instance.result,choice);
            if (instance.c < n){
                while(instance == this.instance){
                c.await();
                }
            }
            else {
                c.signalAll();
                this.instance = new Instance();
            }
            return instance.result;
        } finally {
            this.l.unlock();
        }
    }
}
