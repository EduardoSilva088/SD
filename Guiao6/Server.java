package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);

            int res = 0;
            int i = 0;

            while (true) {
                try {
                    Socket socket = ss.accept();

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream());

                    String line;
                    while ((line = in.readLine()) != null) {
                        res += Integer.parseInt(line);
                        i++;
                        out.println(res);
                        out.flush();
                    }
                    out.println((float) res / i);
                    out.flush();

                    socket.shutdownOutput();
                    socket.shutdownInput();
                    socket.close();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
