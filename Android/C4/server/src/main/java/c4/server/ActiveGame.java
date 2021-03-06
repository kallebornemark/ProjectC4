package c4.server;

import java.io.Serializable;

import c4.utils.C4Constants;
import c4.utils.GameInfo;


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
    private int[][] gameboard;
    private boolean isActive;

    public ActiveGame(Server server, ConnectedClient c1, ConnectedClient c2) {
        this.server = server;
        this.c1 = c1;
        this.c2 = c2;
        server.newGame(c1, c2);
        this.isActive = true;
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

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void leftRematch(ConnectedClient c1) {
        newMove(c1, C4Constants.LEFT_REMATCH);
        stopRematchListener();
        System.out.println("LEFT REMATCH");
    }

    public void newMove(ConnectedClient sender, int column) {

        if (sender == c1) {
            c2.newMove(column);
        } else {
            c1.newMove(column);
        }
        int[] powerupAndCol = new Powerups().SpawnPowerupTier1();
        if(powerupAndCol != null) {
            c1.sendPowerup(powerupAndCol);
            c2.sendPowerup(powerupAndCol);
        }
    }

    public void swapPos(ConnectedClient cc) {
        if (cc.getStartPos() == C4Constants.PLAYER1) {
            cc.setStartPos(C4Constants.PLAYER2);
        } else if (cc.getStartPos() == C4Constants.PLAYER2) {
            cc.setStartPos(C4Constants.PLAYER1);
        }
    }

    /**
     * Swap starting positions and start a new game
     */
    public void rematch() {
        System.out.println("Rematch : ActiveGame");
        server.rematch(c1, c2, getGameBoard());
        isActive = true;
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
            System.out.println("Rematchlistener stopped");
        }
    }

    public void setGameboard(int[][] gameboard) {
        this.gameboard = gameboard;
    }

    public int[][] getGameBoard() {
        return gameboard;
    }

    private class RematchListener implements Runnable {

        public void run() {
            try {
                setGameboard(new Powerups().spawnPowerupTier3());
                int counter = 0;
                while (!Thread.interrupted()) {
                    System.out.println("ActiveGame: One player ready for rematch, waiting for second...");
                    counter++;
                    if (c1isReady && c2isReady) {
                        System.out.println("ActiveGame: Two players ready for rematch - starting!");
                        rematch();
                        stopRematchListener();

                    } else if (counter == 60) {
                        stopRematchListener();
                    }
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                c1isReady = false;
                c2isReady = false;
            }
        }
    }
}
