package Guiao1;

/**
 * A diferença deste exercicio para o 2 está
 * na classe Bank.java. Foi adicionado um 
 * Lock para criar uma exclusão mútua.
 */
public class Ex3 {
    public static void main(String[] args) {
        int N = 10;
        BankEx3 bank = new BankEx3();
        BankRunnerEx3 inc = new BankRunnerEx3(bank);
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
