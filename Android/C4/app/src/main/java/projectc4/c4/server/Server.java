package projectc4.c4.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import projectc4.c4.util.GameInfo;
import projectc4.c4.util.User;

import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark
 */
public class Server implements Runnable {
    private ServerSocket serverSocket;
    private Thread server;
    private HashMap<String , User> userHashMap;
    private HashMap<String, ConnectedClient> connectedClientHashMap;
    private SearchQueue searchingForGame;
    private ArrayList<ActiveGame> activeGames;

    public Server(int port) {
        userHashMap = new HashMap<>();
        connectedClientHashMap = new HashMap<>();
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

    public void addUser(User user) {
        userHashMap.put(user.getUsername(), user);
    }

    public void addConnectedClient(ConnectedClient connectedClient) {
        connectedClientHashMap.put(connectedClient.getUser().getUsername(), connectedClient);
    }

    public void removeConnectedClient(ConnectedClient connectedClient) {
        connectedClientHashMap.remove(connectedClient.getUser().getUsername());
    }

    public User validateUser(String name) {
        if (userHashMap.containsKey(name)) {
            return userHashMap.get(name);
        } else {
            User user = new User(name);
            addUser(user);
            return user;
        }
    }

    public boolean isUserOnline(String name) {
        return connectedClientHashMap.containsKey(name);
    }

    public void addActiveGame(ActiveGame g) {
        activeGames.add(g);
    }

    public void addSearchingClient(ConnectedClient connectedClient) {
        try {
            searchingForGame.put(connectedClient);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void newGame(ConnectedClient c1, ConnectedClient c2, boolean rematch) {
        if (!rematch) {
            Random rand = new Random();
            int player1, player2;
            player1 = (rand.nextInt(1)+1)*(-10);
            player2 = -30 - player1;
            c1.setStartPos(player1);
            c2.setStartPos(player2);
            System.out.println("Startposes swapped, C1 = " + player1 + " och C2 = " + player2);
        }

        GameInfo gameInfoC1 = new GameInfo(c1.getStartPos(),c2.getUser().getUsername(),c1.getUser().getElo(), c2.getUser().getElo(), c2.getUser().getGameResults());
        GameInfo gameInfoC2 = new GameInfo(c2.getStartPos(),c1.getUser().getUsername(),c2.getUser().getElo(), c1.getUser().getElo(), c2.getUser().getGameResults());
        System.out.println("New GameInfo objects created");

//        c1.getActiveGame().setGameInfo(gameInfoC1);
        System.out.println("GameInfo set to " + gameInfoC1 + " in c1");
//        c2.getActiveGame().setGameInfo(gameInfoC2);
        System.out.println("GameInfo set to " + gameInfoC2 + " in c1");

        c1.newGame(gameInfoC1);
        System.out.println("c1.newGame()");
        c2.newGame(gameInfoC2);
        System.out.println("c2.newGame()");
    }

    public void run() {
        while (!Thread.interrupted()){
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Serversocket accepted");
                new ConnectedClient(this, socket).start();
                System.out.println("Ny connectedclient skapad");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Server(3450);
    }
}
