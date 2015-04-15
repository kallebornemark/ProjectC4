package projectc4.c4.util;

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

    public User(String username){
        elo = 0;
        this.username = username;
        gameResults = new int[4];
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
        if (result == WIN) {
            res = 1;

        } else if (result == LOSS) {
            res = 2;
        } else {
            res = 3;
        }
        gameResults[res]++;
        gameResults[0]++;
    }

    public int[] getGameResults() {
        return gameResults;
    }
}
