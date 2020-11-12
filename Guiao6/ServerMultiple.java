package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ServerThread implements Runnable {
    Socket s;

    public ServerThread(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());

            int res = 0;
            int i = 0;

            String line;
            while ((line = in.readLine()) != null) {
                try {
                    res += Integer.parseInt(line);
                    i++;
                } catch (NumberFormatException e) {

                }
                out.println(res);
                out.flush();
            }
            out.println((double) res / i);
            out.flush();
            this.s.shutdownOutput();
            this.s.shutdownInput();
            this.s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class ServerMultiple {

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(12345);

        while (true) {
            Socket socket = ss.accept();
            System.out.println("Assigning a new thread for this server.");
            Thread t = new Thread(new ServerThread(socket));
            t.start();
        }
    }

}
