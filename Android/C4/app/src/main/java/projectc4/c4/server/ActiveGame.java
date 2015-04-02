package projectc4.c4.server;

import java.io.Serializable;

/**
 * @author Kalle Bornemark
 */
public class ActiveGame implements Serializable {
    private static final long serialVersionUID = -403250971215465050L;
    private ConnectedClient c1;
    private ConnectedClient c2;

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
}
