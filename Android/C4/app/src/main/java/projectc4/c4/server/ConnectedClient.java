package projectc4.c4.server;

import projectc4.c4.util.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static projectc4.c4.util.C4Constants.MATCHMAKING;

/**
 * @author Kalle Bornemark
 */
public class ConnectedClient extends Thread {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private User user;
    private Server server;
    private ActiveGame activeGame;

    public ConnectedClient(Server server, Socket socket) {
        this.socket = socket;
    }

    public void setActiveGame(ActiveGame activeGame) {
        this.activeGame = activeGame;
    }

    private void validateUser(String name){
        user = new User(name);
        try {
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startCommunication() {
        int value;
        try {
            System.out.println("Server: Kommunikationen är startad i ConnectedClient");
            while (!Thread.interrupted()) {
                System.out.println("Server: Väntar på readInt...");
                value = (Integer)ois.readObject();
                System.out.println("Server: Har fått en int: " + value);
                if (value == MATCHMAKING) {
                    server.addSearchingClient(this);
                } else {
                    activeGame.newMove(this, value);
                }
            }
        } catch (Exception e) {
            // Hantera om någon dissar
            e.printStackTrace();
        }
    }

    public void newGame(int player) {
        try {
            System.out.println("Server: WriteInt: " + player);
            oos.writeInt(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newMove(int column) {
        try {
            oos.writeInt(column);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String incomingName;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            // Read incoming name
//            incomingName = ois.readUTF();
            // Create user object & send back to client
//            validateUser(incomingName);
            // Add this connectedClient to connectedClients
//            server.addConnectedClient(incomingName, this);

            startCommunication();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
