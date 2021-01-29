package teste20200129;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.List;

public class Worker implements Runnable{
    private Socket s;
    private Ficheiros f;
    

    public Worker(Socket s, Ficheiros ficheiros){
        this.s = s;
        this.f = ficheiros;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
            PrintWriter out = new PrintWriter(this.s.getOutputStream());
            
            String value;
            while((value = in.readLine()) != null){
                String[] args = value.split(" ");
                try{
                    switch(args[0]){
                        case "using" :
                            this.f.using(args[1]);
                            break;
                        case "notUsing" :
                            if (args[2].equals("modified"))
                                this.f.notUsing(args[1], true);
                            else
                                this.f.notUsing(args[1], false);
                            break;
                        case "startBackup":
                            List<String> res = this.f.startBackup();

                            StringBuilder send = new StringBuilder();
                            for(String s : res){
                                send.append(s).append("\n");
                            }
                            out.write(send.toString());
                            out.flush();

                            break;
                        case "endBackup":
                            this.f.endBackup();
                            break;
                    }

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        

    }
}
