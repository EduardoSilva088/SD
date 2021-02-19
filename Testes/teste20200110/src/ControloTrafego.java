package teste20200110.src;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ControloTrafego implements ControloTrafegoAereo {

    private int NUM;
    private int MAX;

    private Lock l;
    private Condition c;

    private boolean[] pistasOcupada; // true se sim, false se não

    private int idAterragem;
    private int idDescolagem;
    private int aterragens;
    private int descolagens;
    private int descolagensSeguidas;

    public ControloTrafego (int NUM, int MAX){

        this.NUM = NUM;
        this.MAX = MAX;
        this.l = new ReentrantLock();
        this.c = l.newCondition();

        for (int i = 0; i < NUM; i++)
            this.pistasOcupada[i] = false;
        
        this.descolagensSeguidas = 0;
        this.idAterragem = 0;
        this.idDescolagem = 0;
        this.aterragens = 0;
        this.descolagens = 0;
    }

    private Integer pistaLivre(){
        for(int i = 0; i < NUM; i++) {
            if(!this.pistasOcupada[i]);
                return i;
        }
        return null;
    }

	@Override
	public int pedirParaDescolar() throws InterruptedException {
        try{
            this.l.lock();
            int id = this.idDescolagem++;
            Integer p;
            while((p = this.pistaLivre()) == null ||
                   (this.descolagensSeguidas >= this.MAX && this.idAterragem < aterragens) ||
                   id != this.descolagens){
                this.c.await();
            }

            this.pistasOcupada[p] = true;

            return p;
        } finally {
            this.l.unlock();
        }
	}

	@Override
	public int pedirParaAterrar() throws InterruptedException {
        try{
            this.l.lock();
            Integer p;
            int id = this.idAterragem++;

            while((p = this.pistaLivre()) == null || id != aterragens)
                this.c.await();
            
            this.pistasOcupada[p] = true;
            return p;
        } finally {
            this.l.unlock();
        }

	}

	@Override
	public void descolou(int pista) {
        try{
            this.l.lock();
            

        } finally {
            this.l.unlock();
        }

		
	}

	@Override
	public void aterrou(int pista) {
		// TODO Auto-generated method stub
		
	}
    
}
