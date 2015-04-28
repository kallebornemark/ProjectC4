package projectc4.c4.server;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Random;

import projectc4.c4.util.GameInfo;

import static projectc4.c4.util.C4Constants.*;

/**
 * Handles a game that is played between two clients online. The moves they do and and if they want
 * to play again.
 *
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class ActiveGame implements Serializable {
    private static final long serialVersionUID = -403250937865050L;
    private ConnectedClient c1;
    private ConnectedClient c2;
    private Thread rematchListener;
    private boolean c1isReady = false, c2isReady = false;
    private GameInfo gameInfo;
    private Server server;

    public ActiveGame(Server server, ConnectedClient c1, ConnectedClient c2) {
        this.server = server;
        this.c1 = c1;
        this.c2 = c2;
        server.newGame(c1, c2);
    }

    public ConnectedClient getOpponent(ConnectedClient connectedClient) {
        if (connectedClient == c1) {
            return c2;
        }
        return c1;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void newMove(ConnectedClient sender, int column) {
        if (sender == c1) {
            c2.newMove(column);
        } else {
            c1.newMove(column);
        }
    }

    public void swapPos(ConnectedClient cc) {
        if (cc.getStartPos() == PLAYER1) {
            cc.setStartPos(PLAYER2);
        } else if (cc.getStartPos() == PLAYER2) {
            cc.setStartPos(PLAYER1);
        }
    }

    /**
     * Swap starting positions and start a new game
     */
    public void rematch() {
        server.rematch(c1, c2);
    }

    public void setReady(ConnectedClient connectedClient) {
        if (connectedClient == c1) {
            c1isReady = true;
        } else if (connectedClient == c2) {
            c2isReady = true;
        }
        startRematchListener();
    }

    public void startRematchListener() {
        if (rematchListener == null) {
            rematchListener = new Thread(new RematchListener());
            rematchListener.start();
        }
    }

    public void stopRematchListener() {
        if (rematchListener != null) {
            rematchListener.interrupt();
            rematchListener = null;
        }
    }

    private class RematchListener implements Runnable {
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    System.out.println("ActiveGame: One player ready for rematch, waiting for second...");
                    if (c1isReady && c2isReady) {
                        System.out.println("ActiveGame: Two players ready for rematch - starting!");
                        rematch();
                        stopRematchListener();
                    }
                    rematchListener.sleep(500);
                }
            } catch (InterruptedException e) {
                c1isReady = false;
                c2isReady = false;
            }
        }
    }
}
