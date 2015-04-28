package projectc4.c4.server;

import java.util.Random;

/**
 * Created by Emil on 2015-04-28.
 */
public class Powerups {
    /**
     * Genererar en powerup
     * @return powerup-int
     */
    public int generatePowerup() {
        Random random = new Random();
        int powerup = random.nextInt(51)+1;

            //Klocka
        if (powerup <= 15) {
            return 30;
            //Bomb
        } else if (powerup > 15 && powerup <= 30) {
            return 31;
            //Colorblind
        } else if (powerup > 30 && powerup <= 40) {
            return 32;
            //Extra turn
        } else if (powerup > 40 && powerup <= 45) {
            return 33;
            //Shuffle board
        } else if (powerup > 45 && powerup <= 50) {
            return 34;
        }
        return 0;
    }

    /**
     * Spawns powerups randomly in the gameboardgrid.
     * @return grid with powerups
     */
    public int[][] spawnPowerup() {
        Random random = new Random();
        int[][] gameboard = new int[6][7];

        for (int i = 0; i < gameboard.length ; i++) {
            for (int j = 0; j < gameboard[i].length ; j++) {
                int chance = random.nextInt(11)+1;
                if (chance == 1) {
                    gameboard[i][j] = generatePowerup();
                } else {
                    gameboard[i][j] = 0;
                }
            }
        }
        return gameboard;
    }
}
