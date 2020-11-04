package Guiao4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierEx1A {

    private int n;
    private int count = 0;
    private Lock l = new ReentrantLock();
    private Condition c = l.newCondition();

    BarrierEx1A(int N) {
        this.n = N;
    }

    void await() throws InterruptedException {
        try{
            l.lock();
            count++;
            if (count < n)
                while (count < n){
                    c.await();
                }
            else
                c.signalAll();
        } finally{
            l.unlock();
        }
    }

}
