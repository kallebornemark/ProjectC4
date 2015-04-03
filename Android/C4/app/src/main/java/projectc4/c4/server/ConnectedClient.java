package projectc4.c4.server;

import projectc4.c4.util.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark
 */
public class ConnectedClient extends Thread implements Serializable {
    private static final long serialVersionUID = -4032345715465050L;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private User user;
    private Server server;
    private ActiveGame activeGame;
    private int startPos;

    public ConnectedClient(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }
    
    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
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

                // Input int
                Object obj = ois.readObject();
                value = (Integer)obj;

                System.out.println("Server: Har fått en int: " + value);

                if (value == MATCHMAKING) {
                    // New incoming MM game
                    System.out.println("Server: New incoming MM game");
                    server.addSearchingClient(this);

                } else if (value >= 0 && value <= 5) {
                    // New incoming move
                    activeGame.newMove(this, value);

                } else if (value == REMATCH) {
                    // Requested rematch
                    activeGame.setReady(this);
                }
            }
        } catch (Exception e) {
            // Hantera om någon dissar
            e.printStackTrace();
        }
    }

    /**
     * Notify the client with a new game.
     *
     * @param player Player 1 or 2.
     */
    public void newGame(int player) {
        startPos = player;
        try {
            System.out.println("Server: newGame(" + player + ")");
            oos.writeObject(player);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newMove(int column) {
        try {
            oos.writeObject(column);
            oos.flush();
            System.out.println("Server: CC " + this.toString() + " sending a new move: " + column);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String incomingName;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

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
