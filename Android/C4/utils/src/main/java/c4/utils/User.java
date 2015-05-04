package c4.utils;

import android.content.Intent;

import java.io.Serializable;


/**
 * Information about the user. Contains their usernamne, name, wins, losses, elo-rating and
 * profile picture. This is sent to the user when they connect to the server.
 *
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class User implements Serializable {
    private static final long serialVersionUID = -253471345465050L;
    private final String username;
    private int[] gameResults;
    private String firstName;
    private String lastName;
    private double elo = 1000;
    private Intent profileImage;
//    private ActiveGame activeGame;

    public User(String username, String firstName, String lastName, double elo, int[] gameResults){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.elo = elo;
        this.gameResults = gameResults;
    }

    /*public void setActiveGame(ActiveGame activeGame) {
        this.activeGame = activeGame;
    }*/
/*
    public void setProfileImage(Intent profileImage) {
        this.profileImage = profileImage;
    }

    public Intent getProfileImage() {
        return this.profileImage;
    }*/

   /* public void setUsername(User user) {
        User.this = user;
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
        if (result == C4Constants.WIN) {
            res = 0;
            elo += Elo.calculateElo(elo, opponentElo);
        } else if (result == C4Constants.LOSS) {
            res = 1;
            elo -= Elo.calculateElo(opponentElo, elo);
        } else {
            res = 2;
        }
        gameResults[res]++;
        System.out.println(res + "++ = " + gameResults[res]);

        System.out.println("ELO set to " + elo);
    }

    public int[] getGameResults() {
        return gameResults;
    }
}
