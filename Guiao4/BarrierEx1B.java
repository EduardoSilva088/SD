package Guiao4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BarrierEx1B {
    

    private int n;
    private int count = 0;
    private int epoch = 0;

    private Lock l = new ReentrantLock();
    private Condition c = l.newCondition();

    BarrierEx1B(int N) {
        this.n = N;
    }

    void await() throws InterruptedException {
        int e;
        try{
            l.lock();
            e = this.epoch;
            count++;
            if (count < n)
                while (this.epoch == e){
                    c.await();
                }
            else{
                c.signalAll();
                this.epoch++;
                count = 0;
            }
        } finally{
            l.unlock();
        }
    }
    
}
