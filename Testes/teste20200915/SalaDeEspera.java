package teste20200915;

public interface SalaDeEspera {
    boolean espera(String nome) throws InterruptedException;
    void desiste(String nome);
    String atende();    
}
