package projectc4.c4.client;

import java.util.ArrayList;

import c4.utils.C4Constants;

/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class Powerup {
    private GameController gameController;
    private GameGridView gameGridView;


    public Powerup(GameController gameController, GameGridView gameGridView) {
        this.gameController = gameController;
        this.gameGridView = gameGridView;
    }

    public void powerupTime() {
       gameController.startTimer(10);
    }

    public void powerupColorblind() {
        gameGridView.setNoColor(true);
    }

    public void powerupExtraTurn() {
        gameController.setExtraTurn(true);
    }

    public void powerupBomb(int playedRow, int playedCol) {
        ArrayList<Integer> posList = new ArrayList<>();

        for (int i = playedRow; i < gameController.getGameBoard().length; i++) {
            posList.add(i);
            gameController.getGameBoard()[i][playedCol] = 0;
            if(gameController.getColSize()[playedCol] > 0) {
                gameController.getColSize()[playedCol]--;
            }
        }
        gameGridView.setBombTiles(true, posList, playedCol);
    }

    public void powerupShuffle() {
        for(int i = 0; i < gameController.getGameBoard()[0].length; i ++) {
            for(int j = 0; j < gameController.getGameBoard().length; j ++) {

                if(gameController.getElement(j,i) == C4Constants.PLAYER1) {
                    gameController.getGameBoard()[j][i] = C4Constants.PLAYER2;
                }
                else if(gameController.getElement(j,i) == C4Constants.PLAYER2) {
                    gameController.getGameBoard()[j][i] = C4Constants.PLAYER1;
                }
            }
        }
        gameGridView.resetGameBoard(gameController.getGameBoard());
    }
}
