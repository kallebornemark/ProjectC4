package c4.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import c4.utils.C4Constants;
import c4.utils.GameInfo;
import c4.utils.GameResult;
import c4.utils.User;


/**
 * Handles the incoming/outgoing traffic between the server and the client.
 *
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class ConnectedClient extends Thread implements Serializable {
    private static final long serialVersionUID = -4032345715465050L;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String username;
    private String firstName;
    private String lastName;
    private Server server;
    private ActiveGame activeGame;
    private int startPos;
    private Thread heartbeatReader;
    private long heartbeatTimeoutInterval;
    private long lastRead;
    private int nbrOfTries;
    private boolean heartbeat;

    public ConnectedClient(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public void startHeartbeat() {
        if (heartbeatReader == null) {
            heartbeat = true;
            heartbeatTimeoutInterval = 2000;
            lastRead = System.currentTimeMillis();
            heartbeatReader = new Thread(new Heartbeat());
            heartbeatReader.start();
        }
    }

    public void stopHeartbeat() {
        if (heartbeatReader != null) {
            heartbeat = false;
            heartbeatReader.interrupt();
            heartbeatReader = null;
        }
    }

    public void disconnect() {
        stopHeartbeat();
        try {
            if (socket != null) { socket.close(); socket = null; }
            if (ois != null) { ois.close(); ois = null; }
            if (oos != null) { oos.close(); oos = null; }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (server.isUserOnline(username)) {
            server.removeConnectedClient(ConnectedClient.this);
        }
        System.out.println("Connected Client disconnected");
        this.interrupt();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public void setActiveGame(ActiveGame activeGame) {
        this.activeGame = activeGame;
//        username.setActiveGame(activeGame);
    }

    public ActiveGame getActiveGame() {
        return activeGame;
    }

    public void requestHighscore() {
        try {
            oos.writeObject(server.requestHighscore());
            oos.flush();
            System.out.println("Sent Highscore from server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void validateUser(String username, String password) {
        try {
            String returnMsg = "";
            String[] res = server.attemptLogin(username, password);
            if (res[0] == null) {
                if (!server.isUserOnline(username)) {
                    System.out.println("Server: No users with username '" + username + "' online, validating...");

                    int[] gameResults = {Integer.parseInt(res[5]), Integer.parseInt(res[6]), Integer.parseInt(res[7])};

                    // Re-create User with username, firstname, lastname, elo, gameresults and email
                    User returnUser = new User(res[1], res[2], res[3], Double.parseDouble(res[4]), gameResults, false, res[8]);
                    startHeartbeat();
                    oos.writeObject(returnUser);
                    oos.flush();
                    this.username = username;
                    this.firstName = res[2];
                    this.lastName = res[3];
                    server.addConnectedClient(this);
                } else {
                    returnMsg = "User " + username + " already online!";
                    System.out.println("Server: User " + username + " already online!");
                }
            } else {
                returnMsg = res[0];
            }
            oos.writeObject(returnMsg);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startCommunication() {
        int value;
        String username, password;
        try {
            System.out.println("ConnectedClient: Communication started");
            while (!Thread.interrupted()) {
                Object obj = ois.readObject();

                if (obj instanceof Integer) {
                    // Hantera ints
                    value = (Integer)obj;

                    if (value == C4Constants.MATCHMAKING) {
                        // New incoming MM game
                        System.out.println("Server: New incoming MM game");
                        server.addSearchingClient(this);

                    } else if (value >= 0 && value <= 20) {
                        // New incoming move
                        activeGame.newMove(this, value);

                    } else if (value == C4Constants.REMATCH) {
                        // Requested rematch
                        activeGame.setReady(this);

                    } else if (value == C4Constants.CANCELSEARCH) {
                        System.out.println("CANCEL SEARCH !!!!");
                        server.cancelSearch(this);
                    } else if (value == C4Constants.WIN || value == C4Constants.LOSS || value == C4Constants.DRAW || value == C4Constants.SURRENDER) {
                        activeGame.setIsActive(false);
                        if (value == C4Constants.WIN) {
                            server.newGameResult(this.getUsername(), this.getActiveGame().getOpponent(this).getUsername(), value);
//                            server.updateUser(this, value);
                        }
                        // Match ended, time to update Users, if the username surrender you should force loss
                        else if (value == C4Constants.SURRENDER) {
                            System.out.println("Server: En klient har SURRENDERAT, skicka vinst till han andra");
                            server.newGameResult(this.getUsername(), this.getActiveGame().getOpponent(this).getUsername(), C4Constants.LOSS);

                            // Skicka SURRENDER till den andra klienten
                            activeGame.newMove(this, C4Constants.SURRENDER);
                            System.out.println("Skickat SURRENDER till klient 2");
                        }
                    } else if  (value == C4Constants.HIGHSCORE) {
                        requestHighscore();
                    } else if (value == C4Constants.LEFT_REMATCH) {
                        System.out.println("     FÅTT EMOT LEFT REMATCH ERIK");
                        activeGame.leftRematch(this);
                    } else if (value == C4Constants.HEARTBEAT) {
                        lastRead = System.currentTimeMillis();
//                        System.out.println("Heartbeat received, lastRead set to " + lastRead);
                    } else if (value == C4Constants.GAMERESULT) {
                        System.out.println("GAMERESULT received");
                        GameResult gr = server.getGameResults(this.username);
                        try {
                            oos.writeObject(gr);
                            oos.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (obj instanceof User) {
                    User user = (User)obj;

                    if (user.isNewUser()) {
                        // Register new user
                        Object result = server.newUser(user);
                        if (result instanceof User) {
                            this.username = user.getUsername();
                            this.firstName = user.getFirstName();
                            this.lastName = user.getLastName();
                        }
                        oos.writeObject(result);
                        oos.flush();
                    } else {
                        // Update profile settings
                        server.updateUser(user);
                    }

                }
                else if (obj instanceof String) {
                    username = (String)obj;
                    System.out.println("Server: Username recieved: " + username);
                    obj = ois.readObject();
                    password = (String)obj;
                    System.out.println("Server: Password recieved: " + password);

                    validateUser(username, password);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            // Hantera om någon dissar
            System.out.println("Server: Client '" + this.username + "' disconnected");

            if(activeGame != null && activeGame.getIsActive() ) {
                System.out.println("Server: En klient har SURRENDERAT, skicka vinst till han andra");
                server.newGameResult(this.getUsername(), this.getActiveGame().getOpponent(this).getUsername(), C4Constants.LOSS);

                // Skicka SURRENDER till den andra klienten
                activeGame.newMove(this, C4Constants.SURRENDER);
                System.out.println("Skickat SURRENDER till klient 2");
                activeGame.setIsActive(false);
            } else if (activeGame != null) {
                activeGame.newMove(this, C4Constants.LEFT_REMATCH);
            }
            heartbeatReader.interrupt();
            server.removeConnectedClient(this);
            System.out.println("Server: Client '" + this.username + "' removed from connected client list");

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
            System.out.println("Server: newGameInfo sent to opponent. playerTurn : " + gameInfo.getPlayerTurn());
            oos.writeObject(gameInfo);
            oos.reset();
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPowerup(int[] powerupAndCol) {
        try {
            oos.writeObject(powerupAndCol);
            oos.flush();
            System.out.println("Sent powerup and col from server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPowerups(int[][] powerups) {
        try {
            oos.writeObject(powerups);
            oos.flush();
            System.out.println("Sent powerup grid--------------------------------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newGame() {
        try {
            oos.writeObject(C4Constants.MATCHMAKING);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newMove(int column) {
        try {
            oos.writeObject(column);
            oos.flush();
            System.out.println(":            Server: CC " + this.toString() + " sending a new move: " + column);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            // Start streams
            oos = new ObjectOutputStream(socket.getOutputStream());
//            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            nbrOfTries = 0;
            lastRead = System.currentTimeMillis();

            // Start listening to inputstream
            startCommunication();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Heartbeat extends Thread {
        public void run() {
            while (heartbeat) {
                try {
                    heartbeatReader.sleep(heartbeatTimeoutInterval);
                    if ((System.currentTimeMillis() - lastRead) > heartbeatTimeoutInterval) {
                        nbrOfTries++;
                        System.out.println("Wait time longer than allowed, nbrOfTries set to " + nbrOfTries);
                        if (nbrOfTries > 2) {
                            disconnect();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
