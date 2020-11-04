package Guiao4;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Agreement implements Runnable{

    private int epoca;
    private int count;
    private int n;
    private int maior = 0;

    private Lock l = new ReentrantLock();
    private Condition c = l.newCondition();

    
    Agreement (int N){
        this.n = N;
        this.epoca = 0;
        this.count = 0;

    }

    public int propose(int choice) throws InterruptedException {
        int e;
        this.l.lock();
        try{
            e = this.epoca;
            count++;
            if (this.maior < choice) this.maior = choice;
            if (count < n){
                while(this.epoca == e){
                c.await();
                }
            }
            else {
                c.signalAll();
                this.epoca++;
                this.count = 0;
            }
            System.out.println(this.maior); //Teste
            return this.maior;
        } finally {
            this.l.unlock();
        }
    }

    public void run(){
        Random r = new Random();
        try{
            this.propose(r.nextInt(10000));
        } catch (InterruptedException e) {}
    }


    public static void main(String[] args) {

        int N = 10;

        Thread t[] = new Thread[N];
        Agreement a = new Agreement(N);

        for (int i = 0; i < N; i++){
            t[i] = new Thread(a);
            t[i].start();
        }

        for (int i = 0; i <N; i++) 
            try {
                t[i].join();
            } catch (InterruptedException e) {}
    }
    
}
