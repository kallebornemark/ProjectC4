package c4.utils;

import java.io.Serializable;

/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class Highscore implements Serializable {
    private static final long serialVersionUID = -423423424550L;
    private String[][] top10Elo;
    private String[][] top10Wins;
    private String[][] top10Losses;
    private String[][] top10draws;

    public void setTop10Elo(String[][] top10Elo) {
        this.top10Elo = top10Elo;
    }

    public String[][] getTop10Elo() {
        return top10Elo;
    }

    public String[][] getTop10Wins() {
        return top10Wins;
    }

    public void setTop10Wins(String[][] top10Wins) {
        this.top10Wins = top10Wins;
    }

    public String[][] getTop10Losses() {
        return top10Losses;
    }

    public void setTop10Losses(String[][] top10Losses) {
        this.top10Losses = top10Losses;
    }

    public String[][] getTop10draws() {
        return top10draws;
    }

    public void setTop10draws(String[][] top10draws) {
        this.top10draws = top10draws;
    }
}
