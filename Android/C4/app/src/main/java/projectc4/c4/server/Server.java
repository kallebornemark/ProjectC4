package projectc4.c4.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import projectc4.c4.util.User;
/**
 * @author Kalle Bornemark
 */
public class Server implements Runnable {
    private ServerSocket serverSocket;
    private Thread server;
    private HashMap<String , ClientHandler> connectedClients;
    private HashMap<String , User> searchingForGame;

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
//                connectedClients.put(new ClientHandler(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void validateUser(String name){

       connectedClients.put()
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private User user;

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
                e.printStackTrace();
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

    public static void main(String[] args) {
        Server server = new Server(3450);
    }
}
