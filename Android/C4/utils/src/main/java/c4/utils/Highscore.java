package c4.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class Highscore implements Serializable {
    private static final long serialVersionUID = -423423424550L;
    private ArrayList<HashMap<String,String>> highScoreElo;
    private ArrayList<HashMap<String,String>> highscoreWins;
    private ArrayList<HashMap<String,String>> highScoreLosses;
    private ArrayList<HashMap<String,String>> highScoreDraws;

    public ArrayList<HashMap<String, String>> getHighScoreDraws() {
        return highScoreDraws;
    }

    public void setHighScoreDraws(ArrayList<HashMap<String, String>> highScoreDraws) {
        this.highScoreDraws = highScoreDraws;
    }

    public ArrayList<HashMap<String, String>> getHighScoreElo() {
        return highScoreElo;
    }

    public void setHighScoreElo(ArrayList<HashMap<String, String>> highScoreElo) {
        this.highScoreElo = highScoreElo;
    }

    public ArrayList<HashMap<String, String>> getHighScoreLosses() {
        return highScoreLosses;
    }

    public void setHighScoreLosses(ArrayList<HashMap<String, String>> highScoreLosses) {
        this.highScoreLosses = highScoreLosses;
    }

    public ArrayList<HashMap<String, String>> getHighscoreWins() {
        return highscoreWins;
    }

    public void setHighscoreWins(ArrayList<HashMap<String, String>> highscoreWins) {
        this.highscoreWins = highscoreWins;
    }
}
