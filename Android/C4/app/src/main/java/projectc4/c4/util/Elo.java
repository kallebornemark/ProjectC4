package projectc4.c4.util;

/**
 * Created by Emil on 2015-04-15.
 */
public class Elo {

    public static int calculateElo(int eA, int eB) {

        int calculatedElo = 0;

        calculatedElo = 1 / (1 + (10^((eB-eA)/400)));

        return calculatedElo;
    }
}
