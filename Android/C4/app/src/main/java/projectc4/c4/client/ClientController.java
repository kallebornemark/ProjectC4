package projectc4.c4.client;

import projectc4.c4.MainActivity;
import projectc4.c4.util.C4Color;

/**
 * @author Kalle Bornemark
 */
public class ClientController {
    private GameController gameController;
    private ClientUI clientUI;
    private MainActivity mainActivity;
    private int player = 1;


    public ClientController(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        gameController = new GameController(this);
        clientUI = new ClientUI(this);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void newMove(int column) {
        System.out.println(column);
        gameController.newMove(column);
    }

    public void drawTile(int pos, int player) {
        clientUI.highlightPlayer(player);
        if (player == 1) {
            player = C4Color.RED;
        } else {
            player = C4Color.YELLOW;
        }
        System.out.println("pos i drawtile" + pos);
        clientUI.drawTile(pos, player);
    }

    public void changeHighlightedPlayer(int player) {
        clientUI.highlightPlayer(player);
    }

    public void winner(int player) {
        clientUI.winner("Player " + player + " won!");
    }

    public void draw() {
        clientUI.winner("It's a draw!");
    }

    public void newGame() {
        gameController.newGame();
        clientUI.newGame();
    }
    /*
    Lade till setters och getters för player
     */
    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public String getUsername() {
        return null;
    }

}