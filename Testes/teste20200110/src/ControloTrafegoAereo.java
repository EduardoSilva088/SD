package teste20200110.src;

public interface ControloTrafegoAereo{
    int pedirParaDescolar() throws InterruptedException;
    int pedirParaAterrar() throws InterruptedException;
    void descolou(int pista);
    void aterrou(int pista);
}