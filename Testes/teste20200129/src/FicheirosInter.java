package teste20200129.src;

import java.util.List;

public interface FicheirosInter {
    void using(String path) throws InterruptedException;
    void notUsing(String path, boolean modified);
    List<String> startBackup() throws InterruptedException;
    void endBackup();
}
