package c4.utils;

import java.io.Serializable;

/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class GameInfo implements Serializable {
    private static final long serialVersionUID = -403280971345465050L;
    private int playerTurn;
    private double elo;
    private String opponentUserName;
    private String opponentFirstName;
    private String opponentLastName;
    private double opponentElo;
    private int[] opponentGameResults;
    private boolean rematch;

    public GameInfo(String opponentUserName, String opponentFirstName, String opponentLastName, double opponentElo, int[] opponentGameResults, int playerTurn) {
        this.opponentUserName = opponentUserName;
        this.opponentFirstName = opponentFirstName;
        this.opponentLastName = opponentLastName;
        this.opponentElo = opponentElo;
        this.opponentGameResults = opponentGameResults;
        this.playerTurn = playerTurn;
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

    public String getOpponentFirstName() {
        return opponentFirstName;
    }

    public String getOpponentLastName() {
        return opponentLastName;
    }
}
