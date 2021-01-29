package teste20200915.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Sala implements SalaDeEspera {
    enum Estado {ATENDIDA, DESISTIU, ESPERA};

    private Lock l;
    private Condition c;
    private Map<String, Estado> pessoas;

    public Sala(){
        this.l = new ReentrantLock();
        this.c = l.newCondition();
        this.pessoas = new HashMap<>();
    }
    
    
    public boolean espera(String nome) throws InterruptedException {
        try{
            this.l.lock();
            this.pessoas.put(nome, Estado.ESPERA);
            while(this.pessoas.get(nome) == Estado.ESPERA){
                c.await();
            }
            return (this.pessoas.remove(nome) == Estado.ATENDIDA);
        } finally {
            this.l.unlock();
        }
    }

    public void desiste(String nome){
        try{
            this.l.lock();
            if (this.pessoas.containsKey(nome)){
                if (this.pessoas.get(nome) == Estado.ESPERA){
                    this.pessoas.put(nome, Estado.DESISTIU);
                    c.signalAll();
                }
            }
        } finally {
            this.l.unlock();
        }
    
    }

    public String atende(){
        try {
            this.l.lock();
            for(String nome : this.pessoas.keySet()){
                if (this.pessoas.get(nome) == Estado.ESPERA){
                    this.pessoas.put(nome, Estado.ATENDIDA);
                    c.signalAll();
                    return nome;
                }
            }
            return null;
        } finally {
            this.l.unlock();
        }   
    }

    public List<String> atende(int n) throws InterruptedException {
        try{
            this.l.lock();
            List<String> res = new ArrayList<>();
            while (this.pessoas.size() < n )
                c.await();
            
            for(String nome : this.pessoas.keySet()){
                if (n > 0){
                    if(this.pessoas.get(nome) == Estado.ESPERA){
                        this.pessoas.put(nome, Estado.ATENDIDA);
                        n--;
                        res.add(nome);
                    }
                }
            }
            return res;
        } finally {
            this.l.unlock();
        }
    
    }
}
