package projectc4.c4.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

import projectc4.c4.util.GameInfo;
import projectc4.c4.util.User;

import static projectc4.c4.util.C4Constants.*;

/**
 * Handles all the clients that are connected to the server. A new thread for every new connected
 * client is set up.
 *
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class Server implements Runnable {
    private ServerSocket serverSocket;
    private Thread server;
    private HashMap<String, ConnectedClient> connectedClientHashMap;
    private SearchQueue searchingForGame;
    private Database database;
//    private ArrayList<ActiveGame> activeGames; // används inte just nu

    public Server(int port) {
        connectedClientHashMap = new HashMap<>();
        database = new Database();
        try {
            serverSocket = new ServerSocket(port);
            server = new Thread(this);
            server.start();
            searchingForGame = new SearchQueue(this, 10);
//            activeGames = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server started");
    }

    public void addUser(User user) {
    }

    public synchronized void addConnectedClient(ConnectedClient connectedClient) {
        connectedClientHashMap.put(connectedClient.getUsername(), connectedClient);
    }

    public synchronized void removeConnectedClient(ConnectedClient connectedClient) {
        connectedClientHashMap.remove(connectedClient.getUsername());
    }

    public synchronized String[] attemptLogin(String name, String password) {
        return database.attemptLogin(name, password);
    }

    public void newGameResult(String username, String opponentUsername, int result) {
        database.newGameResult(username, opponentUsername, result);
    }

    public synchronized boolean isUserOnline(String name) {
        return connectedClientHashMap.containsKey(name);
    }

    /* Behövs inte just nu
    public void addActiveGame(ActiveGame g) {
        activeGames.add(g);
    } */

    public void addSearchingClient(ConnectedClient connectedClient) {
        try {
            searchingForGame.put(connectedClient);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update User's database with new result,
     * send new GameInfo object to opponent with updated ELO:s and game statistics
     * @param c1 The ConnectedClient to update in database
     * @param value Win/loss/draw
     */
/*    public void updateUser(ConnectedClient c1, int value) {
        String user1 = c1.getUsername();
        ConnectedClient opponent = c1.getActiveGame().getOpponent(c1);
        String user2 = opponent.getUsername();
        opponent.newGameInfo(new GameInfo(database.getElo(user2), database.getElo(user1), true));
    }*/


    public void newGame(ConnectedClient c1, ConnectedClient c2) {
        // Assign random startpos to clients
        Random rand = new Random();
        int player1, player2, temp;
        temp = rand.nextInt(2)+1;
        if (temp == 1) {
            player1 = PLAYER1;
            player2 = PLAYER2;
        } else {
            player1 = PLAYER2;
            player2 = PLAYER1;
        }
        c1.setStartPos(player1);
        c2.setStartPos(player2);
        System.out.println("Startpositions set - Player 1 = " + player1 + ", Player 2 = " + player2);

        // Create new GameInfo objects and send to clients
        String user1 = c1.getUsername();
        String user2 = c2.getUsername();

        int[][] gameBoard = new Powerups().spawnPowerupTier3();

        c1.newGameInfo(new GameInfo(user2, c2.getFirstName(), c2.getLastName(), database.getElo(user2), database.getGameResults(user2), c1.getStartPos()));
        c2.newGameInfo(new GameInfo(user1, c1.getFirstName(), c1.getLastName(), database.getElo(user1), database.getGameResults(user1), c2.getStartPos()));

        c1.sendPowerups(gameBoard);
        c2.sendPowerups(gameBoard);

        c1.newGame();
        c2.newGame();
    }

    public void rematch(ConnectedClient c1, ConnectedClient c2, int[][] powerups) {
        System.out.println("Rematch : Server");
        c1.sendPowerups(powerups);
        c2.sendPowerups(powerups);

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

    public void cancelSearch(ConnectedClient c) {
        try {
            searchingForGame.remove(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(3450);
    }
}
