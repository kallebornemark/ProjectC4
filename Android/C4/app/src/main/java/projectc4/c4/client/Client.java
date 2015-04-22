package projectc4.c4.client;

import projectc4.c4.util.GameInfo;
import projectc4.c4.util.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark
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
            } catch (Exception e) {
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
            oos.writeObject(CANCELSEARCH);
            oos.flush();
            System.out.println(CANCELSEARCH + " CANCEL");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestRematch() {
        try {
            oos.writeObject(REMATCH);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestUsername(String username) {
        try {
            oos.writeObject(username);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public void updateUser(int result) {
        try {
            oos.writeObject(result);
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
        } else if (number == MATCHMAKING && !clientController.getGameInfo().isRematch()) {
            clientController.startGameUI();
        } else if (number == MATCHMAKING && clientController.getGameInfo().isRematch()) {
            clientController.rematch();
        } else if (number == SURRENDER) {
            System.out.println(":                Han andra SURRENDERA, du ska VINNA");
            clientController.updateUser(WIN, false);
        }
    }

    public void startCommunication() {
        Object obj;
        int number;
        GameInfo gameInfo;
        clientController.login();
        try {
            System.out.println("Client communication started");
            while (!Thread.interrupted()) {
                obj = ois.readObject();

                if (obj instanceof Integer) {
                    number = (Integer)obj;
                    checkNumberAndSend(number);

                } else if (obj instanceof User) {
                    user = (User)obj;
                    System.out.println("Client: User set to " + user.getUsername());
                    clientController.goToMatchmaking();

                } else if (obj instanceof GameInfo) {
                    gameInfo = (GameInfo)obj;
                    clientController.setGameInfo(gameInfo);
                    clientController.setPlayerTurn(gameInfo.getPlayerTurn());
                    System.out.println("New GameInfo received! Opponent wins: " + gameInfo.getOpponentGameResults()[1] + ", losses: " + gameInfo.getOpponentGameResults()[2]);
                }
            }
        } catch (Exception e) {

        }
    }

    public void run() {
        Object obj;
        User user;
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
            clientController.serverOffline();
            disconnect();
//        } catch (ClassNotFoundException e2) {
//            e2.printStackTrace();
        }
    }
}
