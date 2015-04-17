package projectc4.c4.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import projectc4.c4.util.GameInfo;
import projectc4.c4.util.User;

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

    /**
     * Sent when rematch is called.
     * @param c1
     * @param value
     */
    public void updateUser(ConnectedClient c1, int value) {
        User user = c1.getUser();
        ConnectedClient opponent = c1.getActiveGame().getOpponent(c1);
        user.newGameResult(value, opponent.getUser().getElo());

        opponent.getActiveGame().swapPos(opponent);
        opponent.newGameInfo(new GameInfo(opponent.getStartPos(), user.getUsername(), opponent.getUser().getElo(), user.getElo(), user.getGameResults(), true));
        System.out.println("Sent new GameInfo to opponent. My wins: " + user.getGameResults()[1]);
    }

    public void newGame(ConnectedClient c1, ConnectedClient c2) {
        // Assign random startpos to clients
        Random rand = new Random();
        int player1, player2;
        player1 = (rand.nextInt(1)+1)*(-10);
        player2 = -30 - player1;
        c1.setStartPos(player1);
        c2.setStartPos(player2);
        System.out.println("Startposes swapped, C1 = " + player1 + " och C2 = " + player2);
        c1.setStartPos(player1);
        c2.setStartPos(player2);

        // Create new GameInfo objects and send to clients
        User user1 = c1.getUser();
        User user2 = c2.getUser();

        c1.newGameInfo(new GameInfo(c1.getStartPos(),user2.getUsername(),user1.getElo(), user2.getElo(), user2.getGameResults(), false));
        c2.newGameInfo(new GameInfo(c2.getStartPos(),user1.getUsername(),user2.getElo(), user1.getElo(), user1.getGameResults(), false));
        c1.newGame();
        c2.newGame();
    }

    public void rematch(ConnectedClient c1, ConnectedClient c2) {
        c1.newGame();
        c2.newGame();
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
