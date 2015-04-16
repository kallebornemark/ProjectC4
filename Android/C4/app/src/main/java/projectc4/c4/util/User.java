package projectc4.c4.util;

import projectc4.c4.server.ActiveGame;

import java.io.Serializable;
import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark
 */
public class User implements Serializable {
    private static final long serialVersionUID = -253471345465050L;
    private final String username;
    private int[] gameResults;
    private String firstName;
    private String lastName;
    private double elo = 1000;
//    private ActiveGame activeGame;

    public User(String username){
        this.username = username;
        gameResults = new int[4];
        elo = 0;
    }

    /*public void setActiveGame(ActiveGame activeGame) {
        this.activeGame = activeGame;
    }*/

    public double getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * 0 = Total games played
     * 1 = Games won
     * 2 = Games lost
     * 3 = Games drawn
     * @param result The result which counter should be increased.
     */
    public void newGameResult(int result, double opponentElo) {
        System.out.println("New game result inc, current ELO: " + elo + ", opponent ELO: " + opponentElo);
        int res;
        if (result == WIN) {
            res = 1;
            elo += Elo.calculateElo(elo, opponentElo);
        } else if (result == LOSS) {
            res = 2;
            elo -= Elo.calculateElo(opponentElo, elo);
        } else {
            res = 3;
        }
        gameResults[res]++;
        gameResults[0]++;
        System.out.println(res + "++ = " + gameResults[res]);

        System.out.println("ELO set to " + elo);
    }

    public int[] getGameResults() {
        return gameResults;
    }
}
