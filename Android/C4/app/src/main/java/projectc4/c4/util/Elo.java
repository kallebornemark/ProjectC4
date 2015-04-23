package projectc4.c4.util;

/**
 * Calculating the relative skill level of two players.
 *
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class Elo {

    public static double calculateElo(double eA, double eB) {
        System.out.println("Calculating ELO");
        System.out.println("Current ELO: " + eA + ", opponent ELO: " + eB);
        double calculatedElo;
        calculatedElo = 1 / (1 + (Math.pow(10, (eA-eB)/400)));
        calculatedElo = calculatedElo * 10;
        System.out.println("Calculated ELO: " + calculatedElo);

        // Avrunda
        return Math.round(calculatedElo * 100.0) / 100.0;
    }
}
