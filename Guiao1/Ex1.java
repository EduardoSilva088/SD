package Guiao1;


public class Ex1 {

    public static void main(String[] args) throws InterruptedException {
        int N = 10;
        Incrementer inc = new Incrementer();
        Thread tv[] = new Thread[N];

        for(int i = 0; i < N; i++){
            tv[i] = new Thread(inc);
        }
        
        for(Thread t: tv){
            t.start();
        }

        for(Thread t : tv){
            try{
                t.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        System.out.println("fim");

    }
    
}
