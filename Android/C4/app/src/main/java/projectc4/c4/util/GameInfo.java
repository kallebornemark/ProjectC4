package projectc4.c4.util;

import java.io.Serializable;

/**
 * Created by Emil on 2015-04-15.
 */
public class GameInfo implements Serializable {
    private static final long serialVersionUID = -403280971345465050L;
    private int playerTurn;
    private String opponentUserName;
    private int elo;
    private int opponentElo;

    public GameInfo(int playerTurn, String opponentUserName, int elo, int opponentElo) {
        this.playerTurn = playerTurn;
        this.opponentUserName = opponentUserName;
        this.opponentElo = opponentElo;
    }

    public int getElo() {
        return elo;
    }

    public int getOpponentElo() {
        return opponentElo;
    }

    public void setOpponentElo(int opponentElo) {
        this.opponentElo = opponentElo;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public String getOpponentUserName() {
        return opponentUserName;
    }

    public void setOpponentUserName(String opponentUserName) {
        this.opponentUserName = opponentUserName;
    }




}
