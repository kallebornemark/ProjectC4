package c4.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
//        calculatedElo *= 10;
        System.out.println("Calculated ELO: " + calculatedElo);

        // Avrunda
        /*DecimalFormat df = new DecimalFormat("#.##");
        double res = Double.valueOf(df.format(calculatedElo));
        return res;*/
        return calculatedElo;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
