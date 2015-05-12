package c4.utils;

import java.io.Serializable;

/**
 * Created by Bornemark on 2015-05-11.
 */
public class GameResult implements Serializable {
    private int wins;
    private int losses;
    private int draws;
    private double elo;
    private int rank;

    public GameResult(int wins, int losses, int draws, double elo, int rank) {
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.elo = elo;
        this.rank = rank;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getElo() {
        return elo;
    }

    public void setElo(double elo) {
        this.elo = elo;
    }
}
