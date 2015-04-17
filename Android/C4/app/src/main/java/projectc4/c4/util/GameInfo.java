package projectc4.c4.util;

import java.io.Serializable;

/**
 * Created by Emil on 2015-04-15.
 */
public class GameInfo implements Serializable {
    private static final long serialVersionUID = -403280971345465050L;
    private int playerTurn;
    private double elo;
    private String opponentUserName;
    private double opponentElo;
    private int[] opponentGameResults;
    private boolean rematch;

    public GameInfo(int playerTurn, String opponentUserName, double elo, double opponentElo, int[] opponentGameResults, boolean rematch) {
        this.playerTurn = playerTurn;
        this.opponentUserName = opponentUserName;
        this.elo = elo;
        this.opponentElo = opponentElo;
        this.opponentGameResults = opponentGameResults;
        this.rematch = rematch;
    }

    public double getElo() {
        return elo;
    }

    public void setRematch(boolean rematch) {
        this.rematch = rematch;
    }

    public boolean isRematch() {
        return rematch;
    }

    public double getOpponentElo() {
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

    public int[] getOpponentGameResults() {
        return opponentGameResults;
    }
}
