package ExercicioAula;

//Exercicios da aula 29/10

import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


interface SalaDeEspera {
    boolean espera(String nome) throws InterruptedException;

    void desiste(String nome);

    String atende();
}

public class Aula1 implements SalaDeEspera {
    enum Estado {
        ESPERA, ATENDIDA, DESISTIU
    };

    private Map<String, Estado> estados;
    private Lock l = new ReentrantLock();
    private Condition c = l.newCondition();

    public boolean espera(String nome) throws InterruptedException {
        try{
            l.lock();
            estados.put(nome, Estado.ESPERA);
            while (estados.get(nome) == Estado.ESPERA) {
                c.wait();
            }
            return estados.remove(nome) == Estado.ATENDIDA;
        } finally {
            l.unlock();
        }

    }

    public void desiste(String nome) {
        try{
            l.lock();
            if (estados.get(nome) == Estado.ESPERA){
                estados.put(nome, Estado.DESISTIU);
                c.signalAll();
            }
        }finally {
            l.unlock();
        }
        
    }

    public String atende() {
        try{
            l.lock();
            for (String nome : estados.keySet())
                if (estados.get(nome) == Estado.ESPERA) {
                    estados.put(nome, Estado.ATENDIDA);
                    c.signalAll();
                    return nome;
                }
                return null;
        } finally {
            l.unlock();
        }

    }

}
