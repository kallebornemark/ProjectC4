package projectc4.c4.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Kalle Bornemark
 */
public class Server implements Runnable {
    private ServerSocket serverSocket;
    private Thread server;
    private ArrayList<ClientHandler> connectedClients;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            server = new Thread(this);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (!Thread.interrupted()){
            try {
                Socket socket = serverSocket.accept();
                connectedClients.add(new ClientHandler(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        private void startCommunication() {
            try {
                while (!Thread.interrupted()) {
                    // Communication
                }
            } catch (Exception e) {
                // Hantera om någon dissar
            }
        }

        public void run() {
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
                startCommunication();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
