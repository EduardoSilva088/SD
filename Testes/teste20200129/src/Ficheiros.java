package teste20200129.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ficheiros implements FicheirosInter {
    private Lock l;
    private Condition c;
    private boolean inUse; //Determina se alguém está a modificar ficheiros. True se sim
    
    enum Estado {Using, NotUsingChanged, NotUsingNotChanged}
    private Map<String, Estado> ficheiros;

    public Ficheiros() {
        this.l = new ReentrantLock();
        this.c = l.newCondition();
        this.ficheiros = new TreeMap<>();
        this.inUse = false;
    }
    
    public void using(String path) throws InterruptedException {
        try{
            this.l.lock();
            while(inUse){
                this.c.await();
            }
            this.inUse = true;
            this.ficheiros.put(path, Estado.Using);
        } finally {
            this.l.unlock();
        }

    }
    
    public void notUsing(String path, boolean modified){
        try{
            this.l.lock();
            if (modified){
                this.ficheiros.put(path, Estado.NotUsingChanged);
                this.inUse = false;
                this.c.signalAll();
            }
            else {
                this.ficheiros.put(path, Estado.NotUsingNotChanged);
                this.inUse = false;
                this.c.signalAll();
            }

        } finally {
            this.l.unlock();
        }
    }
    
    public List<String> startBackup() throws InterruptedException {
        try {
            List<String> res = new ArrayList<>();        
            while(inUse){ //enquanto alguem tiver a usar, faz await
                this.c.await();
            }

            for (String nome : this.ficheiros.keySet()){
                if (this.ficheiros.get(nome) == Estado.NotUsingChanged){
                    res.add(nome);
                }
            }
            return res;
        } finally {
            this.l.unlock();
        }
    }

    // Assumimos que sempre que a cópia de segurança termina, damos reset à condição modificado
    public void endBackup(){
        try{
            this.l.lock();
            for (String nome : this.ficheiros.keySet()){
                this.ficheiros.put(nome, Estado.NotUsingNotChanged);
            }
        } finally {
            this.l.unlock();
        }
    }


}
