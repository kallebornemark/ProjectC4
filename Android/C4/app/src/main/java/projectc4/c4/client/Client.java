package projectc4.c4.client;

import android.content.Intent;

import projectc4.c4.LocalGameActivity;
import projectc4.c4.MatchmakingActivity;
import projectc4.c4.util.User;
import static projectc4.c4.util.C4Constants.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * @author Kalle Bornemark
 */
public class Client implements Runnable{
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
        this.ip = ip;
        this.port = port;
        System.out.println("Innan tråden skapas");
        if (client == null) {
            client = new Thread(this);
            client.start();
            System.out.println("Tråden har startats");
        }
    }

    public void disconnect() {
        if (client != null) {
            client.interrupt();
            client = null;
        }
    }

    public void setUsername(User user) {
        this.user = user;
    }

    public void newMove(int player, int value) {
        try {
            oos.writeObject(value);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestUsername(String username) {
        try {
            oos.writeUTF(username);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkNumberAndSend(int number) {
        if (number == MATCHMAKING) {
            System.out.println("Klienten får tillbaks ett gamemode " + number);
            clientController.newGame(number);
        } else if (number == PLAYER1 || number == PLAYER2) {
            System.out.println("Klienten får tillbaks en PLAYER " + number);
            clientController.setPlayer(number);
            clientController.gameIsReady = true;
            clientController.startGameUI();
        } else {
            System.out.println("clientController.newMove(" + number + ")");
            clientController.newMove(number);
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

    public void startCommunication() {
        Object obj;
        int number;
        try {
            while (!Thread.interrupted()) {
                number = (Integer)ois.readObject();
                checkNumberAndSend(number);
            }
        } catch (Exception e) {}
    }

    public void run() {
        Object obj;
        User user;
        System.out.println("Clienttråd börjad");
        try {
            System.out.println("Försöker skapa socket...");
            socket = new Socket(ip, port);
            System.out.println("Socket skapad");
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("Objectoutputstream skapad");

            // User from server
//            obj = ois.readObject();
//            setUsername((User)obj);

            // Start listening
            startCommunication();
        } catch (IOException e) {
            e.printStackTrace();
//        } catch (ClassNotFoundException e2) {
//            e2.printStackTrace();
//        }
        }
    }
}
