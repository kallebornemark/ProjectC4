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

    public void powerupColorblind() {
        gameGridView.setNoColor(true);
    }

    public void powerupExtraTurn() {
        gameController.setExtraTurn(true);
    }
}
