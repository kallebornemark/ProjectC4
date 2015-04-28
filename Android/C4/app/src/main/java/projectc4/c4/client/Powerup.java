package projectc4.c4.client;

public class Powerup {
    GameController gameController;

    public Powerup(GameController gameController) {
        this.gameController = gameController;
    }

    public void powerupTime() {
       gameController.startTimer(10);
    }
}
