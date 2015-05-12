package projectc4.c4.client;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;

import c4.utils.C4Constants;
import c4.utils.GameInfo;
import c4.utils.Highscore;
import c4.utils.User;


/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class Client implements Runnable, Serializable {
    private static final long serialVersionUID = -4032343768965050L;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String ip;
    private int port;
    private User user;
    private User opponentUser;
    private ClientController clientController;
    private Thread client;
    private Thread heartbeat;

    public Client(ClientController clientController) {
        this.clientController = clientController;
    }

    public void connect(String ip, int port) {
        if (client == null) {
            this.ip = ip;
            this.port = port;
            System.out.println("Innan tråden skapas");
            client = new Thread(this);
            client.start();
            System.out.println("Tråden har startats");
        }
    }

    public void disconnect() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = clientController.getContext().getFragmentManager();
//                fragmentManager.beginTransaction();
                fragmentManager.popBackStackImmediate("Menu", 0);
            }
        });

        stopHeartbeat();

        try {
            if (ois     != null) { ois.close();         ois = null; }
            if (oos     != null) { oos.close();         oos = null; }
            if (socket  != null) { socket.close();      socket = null; }
            if (user    != null) { this.user = null; }
            if (client  != null) { client.interrupt();  client = null; }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Client Disconnected");
    }

    public void newMove(int value) {
        try {
            oos.writeObject(value);
            oos.flush();
            System.out.println("Client sending new move: " + value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelSearch() {
        try {
            oos.writeObject(C4Constants.CANCELSEARCH);
            oos.flush();
            System.out.println(C4Constants.CANCELSEARCH + " CANCEL");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestRematch() {
        try {
            oos.writeObject(C4Constants.REMATCH);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password) {
        try {
            oos.writeObject(username);
            oos.writeObject(password);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newUser(User user) {
        try {
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public void createOpponentUser(GameInfo gameInfo) {
        opponentUser = new User(
                gameInfo.getOpponentUserName(),
                gameInfo.getOpponentFirstName(),
                gameInfo.getOpponentLastName(),
                gameInfo.getOpponentElo(),
                gameInfo.getOpponentGameResults(),
                false);
    }

    public User getOpponentUser() {
        return opponentUser;
    }

    public void updateUserWithResult(int result) {
        try {
            oos.writeObject(result);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUserObject(User user) {
        try {
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestGame(int gamemode) {
        try {
            oos.writeObject(gamemode);
            oos.flush();
            System.out.println("Request game " + gamemode);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void requestHighscore(int highscore) {
        try {
            oos.writeObject(highscore);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startHeartbeat() {
        if (heartbeat == null) {
            heartbeat = new Thread(new Heartbeat());
            heartbeat.start();
            System.out.println("Heartbeat started!");
        }
    }

    public void stopHeartbeat() {
        if (heartbeat != null) {
            heartbeat.interrupt();
            heartbeat = null;
            System.out.println("Heartbeat stopped!");
        }
    }

    public void checkNumberAndSend(int number) {
        if (number >= 0 && number <= 20) {
            System.out.println(this.toString() + " får ett inkommande move: " + number);
            clientController.newIncomingMove(number);
        } else if (number == C4Constants.MATCHMAKING && !clientController.getGameInfo().isRematch()) {
            clientController.startGameUI();
        } else if (number == C4Constants.MATCHMAKING && clientController.getGameInfo().isRematch()) {
            System.out.println("REMATCH REMATCH REMATCH");
            clientController.rematch();
        } else if (number == C4Constants.SURRENDER) {
            System.out.println(":                Han andra SURRENDERA, du ska VINNA");
            clientController.updateUser(C4Constants.WIN, false);
        }
    }

    private void checkObject(Object obj) {
        int number;
        GameInfo gameInfo;
        if (obj instanceof Integer) {
            number = (Integer)obj;
            checkNumberAndSend(number);
        } else if (obj instanceof String) {
            // Login attempt failed on server side, display error in login fragment
            clientController.loginErrorMessage((String) obj);
            clientController.enableLoginButton();
            if (client != null) { client.interrupt(); client = null; }
        } else if (obj instanceof int[][]){
            int[][] gameBoard = (int[][])obj;
            clientController.getGameController().setPowerups(gameBoard);
            System.out.println("Tagit emot powerups"); //TEMP

        } else if (obj instanceof int[]){
            int[] powerupAndCol = (int[])obj;
            clientController.getGameController().dropPowerup(powerupAndCol);
            System.out.println("Tagit emot powerup tier 1 och col"); //TEMP

        } else if (obj instanceof User) {
            user = (User)obj;
            System.out.println("Client: User set to " + user.getUsername());
            clientController.goToMatchmaking();

        } else if (obj instanceof GameInfo) {
            // Sent the first time a series of games is started
            gameInfo = (GameInfo)obj;
            createOpponentUser(gameInfo);
            clientController.setGameInfo(gameInfo);
            clientController.setPlayer(C4Constants.PLAYER1);
            clientController.setPlayerTurn(gameInfo.getPlayerTurn());
            clientController.setOpponentName(gameInfo.getOpponentUserName());

            System.out.println("New GameInfo received! ------- Opponent: " + clientController.getGameInfo().getOpponentUserName());
        } else if (obj instanceof Highscore){
            Highscore highscore = (Highscore)obj;
            System.out.println(" HIGHSCORE RECIEVED");
            clientController.showHighscore(highscore);
        }
    }

    public void startCommunication() {
        try {
            System.out.println("Client communication started");
            while (true) {
                final Object obj = ois.readObject();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        checkObject(obj);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Clienttråd börjad");
        try {
            System.out.println("Försöker skapa socket...");
            Socket socket = new Socket();
            this.socket = socket;
            socket.connect(new InetSocketAddress(ip, port), 4000);
            System.out.println("Socket skapad");
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            System.out.println("Streams skapad");

        } catch (IOException e) {
            e.printStackTrace();
        }

        clientController.login();

        // Start sending heartbeats
        startHeartbeat();

        // Start listening on ois
        startCommunication();
    }

    private class Heartbeat extends Thread {
        int nbrOfTries = 0;
        public void run() {
            while (heartbeat != null) {
                try {
                    oos.writeObject(C4Constants.HEARTBEAT);
                    oos.flush();
                    System.out.println("Heartbeat sent to server, sleeping 1000ms...");
                    Thread.sleep(1000);

                } catch (IOException e) {
                    // If server doesn't respond

                    while (true) {
                        nbrOfTries++;
                        if (nbrOfTries > 3) {
                            disconnect();
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
