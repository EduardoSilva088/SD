package Guiao1;

public class BankRunner implements Runnable{
    private Bank bank;

    BankRunner(Bank b){
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
