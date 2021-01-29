package teste20200129;

import java.util.List;

public interface FicheirosInter {
    void using(String path);
    void notUsing(String path, boolean modified);
    List<String> startBackup();
    void endBackup();
}
