package projectc4.c4.util;

import projectc4.c4.server.ActiveGame;

import java.io.Serializable;
import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark
 */
public class User implements Serializable {
    private final String username;
    private int[] gameResults;
    private String firstName;
    private String lastName;
    private int elo;
    private ActiveGame activeGame;

    public User(String username){
        elo = 0;
        this.username = username;
        gameResults = new int[4];
    }

    public void setActiveGame(ActiveGame activeGame) {
        this.activeGame = activeGame;
    }

    public int getElo() {
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
    public void newGameResult(int result) {
        int res;
        System.out.println("Attempting to calculate elo");
        if (result == WIN) {
            res = 1;
            elo += Elo.calculateElo(getElo(), activeGame.getGameInfo().getOpponentElo());
        } else if (result == LOSS) {
            res = 2;
            elo -= Elo.calculateElo(getElo(), activeGame.getGameInfo().getOpponentElo());

        } else {
            res = 3;
        }
        System.out.println("Elo calculated");
        gameResults[res]++;
        gameResults[0]++;
    }

    public int[] getGameResults() {
        return gameResults;
    }
}
