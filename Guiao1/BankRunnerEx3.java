package Guiao1;

public class BankRunnerEx3 implements Runnable {
    private BankEx3 bank;

    BankRunnerEx3(BankEx3 b){
        this.bank = b;
    }

    public void run(){
        int I = 1000;
        int V = 100;

        for(int i = 0; i < I; i++){
            this.bank.deposit(V);
        }

    }
    
}
