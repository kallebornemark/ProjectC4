package projectc4.c4.client;

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

    public void powerupsColorblind() {
        gameGridView.setNoColor(true);
    }

    public void powerupBomb(int playedRow, int playedCol) {
        if (playedCol == 0 && playedRow == 0) {
            gameController.setElement(playedRow, playedCol+1, 0);
            gameController.setElement(playedRow+1,playedCol+1, 0);
            gameController.setElement(playedRow-1,playedCol , 0);
            gameController.setElement(playedRow, playedCol, 0);

        } else if (playedCol == 6 && playedRow == 5) {
            gameController.setElement(playedRow-1,playedCol, 0);
            gameController.setElement(playedRow-1,playedCol-1,0);
            gameController.setElement(playedRow+1,playedCol-1,0);
            gameController.setElement(playedRow, playedCol, 0);

        } else if (playedCol == 6 && playedRow == 0) {
            gameController.setElement(playedRow,playedCol-1, 0);
            gameController.setElement(playedRow+1,playedCol-1, 0);
            gameController.setElement(playedRow+1,playedCol, 0);
            gameController.setElement(playedRow, playedCol, 0);

        } else if (playedCol == 0 && playedRow == 5) {
            gameController.setElement(playedRow-1,playedCol, 0);
            gameController.setElement(playedRow-1,playedCol+1, 0);
            gameController.setElement(playedRow,playedCol+1, 0);
            gameController.setElement(playedRow, playedCol, 0);

        } else if (playedCol == 0) {
            gameController.setElement(playedRow-1,playedCol, 0);
            gameController.setElement(playedRow-1,playedCol+1, 0);
            gameController.setElement(playedRow,playedCol+1, 0);
            gameController.setElement(playedRow+1,playedCol+1, 0);
            gameController.setElement(playedRow+1,playedCol, 0);
            gameController.setElement(playedRow, playedCol, 0);

        } else if (playedCol == 6) {
            gameController.setElement(playedRow-1,playedCol, 0);
            gameController.setElement(playedRow-1,playedCol-1, 0);
            gameController.setElement(playedRow,playedCol-1, 0);
            gameController.setElement(playedRow+1,playedCol-1, 0);
            gameController.setElement(playedRow+1,playedCol, 0);
            gameController.setElement(playedRow, playedCol, 0);

        } else if (playedRow == 0) {
           gameController.setElement(playedRow,playedCol-1, 0);
           gameController.setElement(playedRow+1,playedCol-1, 0);
           gameController.setElement(playedRow+1,playedCol, 0);
           gameController.setElement(playedRow+1,playedCol+1, 0);
           gameController.setElement(playedRow,playedCol+1, 0);
           gameController.setElement(playedRow, playedCol, 0);

        } else if (playedRow == 5) {
          gameController.setElement(playedRow,playedCol-1, 0);
          gameController.setElement(playedRow-1,playedCol-1, 0);
          gameController.setElement(playedRow-1,playedCol, 0);
          gameController.setElement(playedRow-1,playedCol+1, 0);
          gameController.setElement(playedRow,playedCol+1, 0);
          gameController.setElement(playedRow, playedCol, 0);

        } else {
           gameController.setElement(playedRow,playedCol-1, 0);
           gameController.setElement(playedRow-1,playedCol-1, 0);
           gameController.setElement(playedRow-1,playedCol, 0);
           gameController.setElement(playedRow-1,playedCol+1, 0);
           gameController.setElement(playedRow,playedCol+1, 0);
           gameController.setElement(playedRow+1,playedCol-1, 0);
           gameController.setElement(playedRow+1,playedCol, 0);
           gameController.setElement(playedRow+1,playedCol+1, 0);
           gameController.setElement(playedRow, playedCol, 0);
        }
        gameGridView.bombTiles(playedRow, playedCol);
    }
}
