package c4.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import c4.utils.C4Constants;
import c4.utils.GameInfo;
import c4.utils.GameResult;
import c4.utils.Highscore;
import c4.utils.User;


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

    public synchronized void addConnectedClient(ConnectedClient connectedClient) {
        connectedClientHashMap.put(connectedClient.getUsername(), connectedClient);
    }

    public synchronized void removeConnectedClient(ConnectedClient connectedClient) {
        connectedClientHashMap.remove(connectedClient.getUsername());
    }

    public synchronized String[] attemptLogin(String name, String password) {
        return database.attemptLogin(name, password);
    }

    public synchronized Highscore requestHighscore() {
        return database.getHighscore();
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

    /*
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

    public GameResult getGameResults(String username) {
        return database.getGameResults(username);
    }


    public void newGame(ConnectedClient c1, ConnectedClient c2) {
        // Assign random startpos to clients
        Random rand = new Random();
        int player1, player2, temp;
        temp = rand.nextInt(2)+1;
        if (temp == 1) {
            player1 = C4Constants.PLAYER1;
            player2 = C4Constants.PLAYER2;
        } else {
            player1 = C4Constants.PLAYER2;
            player2 = C4Constants.PLAYER1;
        }
        c1.setStartPos(player1);
        c2.setStartPos(player2);
        System.out.println("Startpositions set - Player 1 = " + player1 + ", Player 2 = " + player2);

        // Create new GameInfo objects and send to clients
        String user1 = c1.getUsername();
        String user2 = c2.getUsername();

        int[][] gameBoard = new Powerups().spawnPowerupTier3();

        GameResult gr1 = database.getGameResults(user1);
        GameResult gr2 = database.getGameResults(user2);
        int[] intGr1 = {gr1.getWins(), gr1.getLosses(), gr1.getDraws()};
        int[] intGr2 = {gr2.getWins(), gr2.getLosses(), gr2.getDraws()};

        c1.newGameInfo(new GameInfo(user2, c2.getFirstName(), c2.getLastName(), database.getElo(user2), intGr2, c1.getStartPos()));
        c2.newGameInfo(new GameInfo(user1, c1.getFirstName(), c1.getLastName(), database.getElo(user1), intGr1, c2.getStartPos()));

        c1.sendPowerups(gameBoard);
        c2.sendPowerups(gameBoard);

        c1.newGame();
        c2.newGame();
    }

    public Object newUser(User user) {
        return database.newUser(user);
    }

    /**
     * Update an existing user in the database
     * @param user User object with updated info to be inserted into the database
     */
    public void updateUser(User user) {
        database.updateUser(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail());
        System.out.println("User \"" + user.getUsername() + "\" updated through profile");
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
