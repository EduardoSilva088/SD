package Guiao2;

import java.util.Random;

class MoverEx3 implements Runnable {
    BankEx3 b;
    int s; // Number of accounts

    public MoverEx3(BankEx3 b2, int s) {
        this.b = b2;
        this.s = s;
    }

    public void run() {
        final int moves = 100000;
        int from, to;
        Random rand = new Random();

        for (int m = 0; m < moves; m++) {
            from = rand.nextInt(s); // Get one
            while ((to = rand.nextInt(s)) == from)
                ; // Slow way to get distinct
            b.transfer(from, to, 1);
        }
    }
}

class BankTestEx3 {
    public static void main(String[] args) throws InterruptedException {
        final int N = 10;

        BankEx3 b = new BankEx3(N);

        for (int i = 0; i < N; i++)
            b.deposit(i, 1000);

        System.out.println(b.totalBalance());

        Thread t1 = new Thread(new MoverEx3(b, 10));
        Thread t2 = new Thread(new MoverEx3(b, 10));

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(b.totalBalance());
    }
}