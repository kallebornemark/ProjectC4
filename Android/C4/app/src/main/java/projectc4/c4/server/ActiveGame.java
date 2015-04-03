package projectc4.c4.server;

import java.io.Serializable;
import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark
 */
public class ActiveGame implements Serializable {
    private static final long serialVersionUID = -403250971215465050L;
    private ConnectedClient c1;
    private ConnectedClient c2;
    private Thread rematchListener;
    private boolean c1isReady = false, c2isReady = false;

    public ActiveGame(Server server, ConnectedClient c1, ConnectedClient c2) {
        this.c1 = c1;
        this.c2 = c2;
        server.newGame(c1, c2);
    }

    public void newMove(ConnectedClient sender, int column) {
        if (sender == c1) {
            c2.newMove(column);
        } else {
            c1.newMove(column);
        }
    }

    public void swapPos(ConnectedClient cc) {
        if (cc.getLastPos() == PLAYER1) {
            cc.setLastPos(PLAYER2);
        } else if (cc.getLastPos() == PLAYER2) {
            cc.setLastPos(PLAYER1);
        }
    }

    public void rematch() {
        swapPos(c1);
        c1.newGame(c1.getLastPos());
        swapPos(c2);
        c2.newGame(c2.getLastPos());
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
            c1isReady = false;
            c2isReady = false;
        }
    }

    private class RematchListener implements Runnable {
        public void run() {
            while (!Thread.interrupted()) {


                if (c1isReady && c2isReady) {
                    rematch();
                    stopRematchListener();
                }
            }
        }
    }
}
