package c4.server;

import java.util.Random;

import c4.utils.C4Constants;


/**
 *
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class Powerups {
    /**
     * Genererar en powerup
     * @return powerup-int
     */
    public int generatePowerupTier1() {
        Random random = new Random();
        int powerup = random.nextInt(100)+1;
            //Klocka ska åka ner
        if (powerup <= 34) {
            return C4Constants.POWERUP_TIME;
            //Bomb ska åka ner
        } else if (powerup > 34 && powerup <= 65) {
            return C4Constants.POWERUP_BOMB;
            //Colorblind
        } else if (powerup > 65 && powerup <= 100) {
            return C4Constants.POWERUP_COLORBLIND;
        }
        return 0;
    }

    public int generatePowerupTier3() {
        Random random = new Random();
        int powerup = random.nextInt(3) + 1;
        //extra turn
        if (powerup <= 2) {
            return C4Constants.POWERUP_EXTRATURN;
            //Shuffle board
        } else if (powerup == 3) {
            return C4Constants.POWERUP_SHUFFLE;
        }
        return 0;
    }

    public int[] SpawnPowerupTier1() {
        Random random = new Random();
        int colRandom = random.nextInt(7);
        int chance = random.nextInt(6)+1;
        if (chance == 1) {
            int[] sendArray = new int[2];
            sendArray[1] = colRandom;
            sendArray[0] = generatePowerupTier1();
            return sendArray;
        }
        return null;
    }

    /**
     * Spawns powerups randomly in the gameboardgrid.
     * @return grid with powerups
     */


    public int[][] spawnPowerupTier3() {
        int counter = 0;
        Random random = new Random();
        int[][] gameboard = new int[6][7];
        for (int i = 0; i < gameboard.length ; i++) {
            for (int j = 0; j < gameboard[i].length ; j++) {
               if (i <= 2) {
                   int chance = random.nextInt(10)+1;
                   if (chance == 1 && counter <= 1) {
                       gameboard[i][j] = generatePowerupTier3();
                       counter ++;
                   } else {
                       gameboard[i][j] = 0;
                   }
               } else {
                   gameboard[i][j] = 0;
               }
            }
        }
        return gameboard;
    }
}
