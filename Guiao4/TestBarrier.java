package Guiao4;

class Test implements Runnable {

    private BarrierEx1B b;
    private int id;

    public Test(BarrierEx1B b, int id) {
        this.b = b;
        this.id = id;

    }

    @Override
    public void run() {
        try{
            System.out.println(this.id + " in");
            b.await();
            System.out.println(this.id + " out");
        } catch (InterruptedException e ){}

    }

}

public class TestBarrier {

    public static void main(String[] args) {
        int N = 9;

        BarrierEx1B barrierB = new BarrierEx1B(3);
        Thread t[] = new Thread[N];

        for (int i = 0; i < N; i++) {
            t[i] = new Thread(new Test(barrierB,i));
            t[i].start();
        }
        
        try{
            for(int i = 0; i < N; i++){
                t[i].join();
            }
        }catch (InterruptedException e){}
    }
}
