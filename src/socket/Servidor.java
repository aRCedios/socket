
package socket;

/*
 aRCee
*/

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import javax.swing.*;

public class ServerForm extends javax.swing.JFrame {
    private static ServerSocket serverSocket;
    private static ExecutorService pool = Executors.newCachedThreadPool();

    public ServerForm() {
        initComponents();
        this.setVisible(true);
        startServer();
    }

    private void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(9999);
                jTextArea1.append("Servidor iniciado en el puerto 9999...\n");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    jTextArea1.append("Cliente conectado\n");
                    pool.execute(new ClientHandler(clientSocket));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Conectado al servidor");
                
                String message;
                while ((message = in.readLine()) != null) {
                    jTextArea1.append("Cliente: " + message + "\n");
                    out.println("Servidor recibió: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

