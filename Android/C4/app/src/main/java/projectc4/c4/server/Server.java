package projectc4.c4.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * @author Kalle Bornemark
 */
public class Server implements Runnable {
    private ServerSocket serverSocket;
    private Thread server;
    private HashMap<String , ConnectedClient> connectedClients;
    private SearchQueue searchingForGame;
    private ArrayList<ActiveGame> activeGames;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            server = new Thread(this);
            server.start();
            searchingForGame = new SearchQueue(this, 10);
            activeGames = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addActiveGame(ActiveGame g) {
        activeGames.add(g);
    }

    public void addToQueue(ConnectedClient connectedClient) {
        try {
            searchingForGame.put(connectedClient);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addConnectedClient(String name, ConnectedClient connectedClient) {
        connectedClients.put(name, connectedClient);
    }

    public void addSearchingClient(ConnectedClient connectedClient) {
        try {
            searchingForGame.put(connectedClient);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void removeConnectedClient(String name) {
        connectedClients.remove(name);
    }

    public void newGame(ConnectedClient c1, ConnectedClient c2) {
        Random rand = new Random();
        int player1 = rand.nextInt(1)+1;
        int player2 = 3-player1;
        c1.newGame(player1);
        c2.newGame(player2);
    }

    public void run() {
        while (!Thread.interrupted()){
            try {
                Socket socket = serverSocket.accept();
                System.out.println("serversocket accepted");
                new ConnectedClient(this, socket).start();
                System.out.println("ny connectedclient skapad");
//                connectedClients.put(new ClientHandler(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(3450);
    }
}
