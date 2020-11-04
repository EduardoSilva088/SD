package Guiao4;

import java.util.Random;

class Waiter implements Runnable {
    private Agreement b;
    private int id;

    public Waiter(Agreement b, int id) {
        this.b = b;
        this.id = id;
    }

    public void run() {
        Random r = new Random();
        try {
            System.out.println(id + " in");
            int ret = b.propose(r.nextInt(10000));
            System.out.println(id + " out " + ret);

        } catch (InterruptedException e) {
        }
    }
}

public class TestAgreement {

    public static void main(String[] args) {
        int N = 6;

        Agreement b = new Agreement(3);
        Thread t[] = new Thread[N];

        for (int i = 0; i < N; i++) {
            t[i] = new Thread(new Waiter(b, i));
            t[i].start();
        }

        try {
            for (int i = 0; i < N; i++)
                t[i].join();
        } catch (InterruptedException e) {

        }

    }
}
