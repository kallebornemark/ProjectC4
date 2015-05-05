package projectc4.c4.client;

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
    private Thread client;
    private User user;
    private User opponentUser;
    private ClientController clientController;

    public Client(ClientController clientController) {
        this.clientController = clientController;
    }

    public void connect(String ip, int port) {
        user = null;
        this.ip = ip;
        this.port = port;
        System.out.println("Innan tråden skapas");
        if (client == null) {
            client = new Thread(this);
            client.start();
            System.out.println("Tråden har startats");
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void disconnect() {
        if (client != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.interrupt();
            clientController.setClient(null);
            client = null;

            System.out.println("Client Disconnected");
        }
    }

    public void setUsername(User user) {
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void createOpponentUser(GameInfo gameInfo) {
        opponentUser = new User(gameInfo.getOpponentUserName(), gameInfo.getOpponentFirstName(), gameInfo.getOpponentLastName(), gameInfo.getOpponentElo(), gameInfo.getOpponentGameResults());
    }

    public User getOpponentUser() {
        return opponentUser;
    }

    public void updateUser(int result) {
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
            clientController.setPlayerTurn(gameInfo.getPlayerTurn());
            clientController.setOpponentName(gameInfo.getOpponentUserName());
            System.out.println("New GameInfo received! ------- Opponent: " + clientController.getGameInfo().getOpponentUserName());
        }
    }

    public void startCommunication() {
        clientController.login();
        try {
            System.out.println("Client communication started");
            while (!Thread.interrupted()) {
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
            System.out.println("Objectoutputstream skapad");

            // User from server
//            obj = ois.readObject();
//            setUsername((User)obj);

            // Start listening
            startCommunication();
        } catch (IOException e) {
            clientController.loginErrorMessage("Server offline");
            disconnect();
//        } catch (ClassNotFoundException e2) {
//            e2.printStackTrace();
        }
    }
}
