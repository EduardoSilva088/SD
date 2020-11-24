package Guiao1;

public class Ex2 {

    public static void main(String[] args) {
        int N = 10;
        Bank bank = new Bank();
        BankRunner inc = new BankRunner(bank);
        Thread tv[] = new Thread[N];

        for(int i = 0; i < N; i++)
            tv[i] = new Thread(inc);

        for (Thread t: tv){
            t.start();
        }

        for(Thread t : tv){
            try {
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(bank.balance());
    }
}
