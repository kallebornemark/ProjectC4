package projectc4.c4.server;

import projectc4.c4.util.GameInfo;
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

    public User getUser() {
        return user;
    }
    
    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public void setActiveGame(ActiveGame activeGame) {
        this.activeGame = activeGame;
//        user.setActiveGame(activeGame);
    }

    public ActiveGame getActiveGame() {
        return activeGame;
    }

    private void startCommunication() {
        int value;
        String username;
        try {
            System.out.println("ConnectedClient: Communication started");
            while (!Thread.interrupted()) {
                Object obj = ois.readObject();

                if (obj instanceof Integer) {
                    value = (Integer)obj;

                    // Hantera ints
                    if (value == MATCHMAKING) {
                        // New incoming MM game
                        System.out.println("Server: New incoming MM game");
                        server.addSearchingClient(this);

                    } else if (value >= 0 && value <= 20) {
                        // New incoming move
                        activeGame.newMove(this, value);

                    } else if (value == REMATCH) {
                        // Requested rematch
                        activeGame.setReady(this);

                    } else if (value == WIN || value == LOSS || value == DRAW) {
                        // Match ended, time to update Users
                        server.updateUser(this, value);
                    }
                    System.out.println("Server: Har fått en int: " + value);


                    // Hantera Users
                } else if (obj instanceof String) {
                    username = (String)obj;
                    System.out.println("Server: Username recieved: " + username);

                    // Check if user is online
                    if (!server.isUserOnline(username)) {
                        System.out.println("Server: No users with username '" + username + "' online, validating...");
                        // Check if user is registered
                        user = server.validateUser(username);
                        System.out.println("Server: User set to " + user.getUsername());
                        System.out.println("Attempting to add user " + user.getUsername() + " to user hashmap");
                        server.addUser(user);
                        System.out.println(user.getUsername() + " added to client hashmap");
                        server.addConnectedClient(this);
                        System.out.println(user.getUsername() + " added to cc hashmap");

                        // Send back user to client
                        oos.writeObject(user);
                        oos.flush();
                        System.out.println(user.getUsername() + " sent to client");
                    } else {
                        System.out.println("Server: Client named " + username + " already online!");
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            // Hantera om någon dissar
            System.out.println("Server: Client '" + user.getUsername() + "' disconnected");
            server.removeConnectedClient(this);
            System.out.println("Server: Client '" + user.getUsername() + "' removed from connected client list");
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        }
    }

    /**
     * Notify the client with a new game.
     *
     * @param gameInfo information about the game.
     */
    public void newGameInfo(GameInfo gameInfo) {
        try {
            System.out.println("Server: newGameInfo(" + gameInfo.toString() + ")");
            oos.writeObject(gameInfo);
            oos.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newGame() {
        try {
            oos.writeObject(MATCHMAKING);
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
        try {
            // Start streams
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            // Start listening to inputstream
            startCommunication();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
