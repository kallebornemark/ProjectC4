package projectc4.c4.client;

import java.util.ArrayList;

import projectc4.c4.MainActivity;
import projectc4.c4.util.C4Color;

/**
 * @author Kalle Bornemark
 */
public class ClientController {
    private GameController gameController;
    private ClientUI clientUI;
    private Client client;
    private MainActivity mainActivity;
    private int player = 1;


    public ClientController(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        gameController = new GameController(this);
        clientUI = new ClientUI(this);
    }

    public void connect() {
        client = new Client(this);
        client.connect("10.1.3.0", 3450);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void newMove(int column) {
        System.out.println(column);
        gameController.newMove(column);
        client.newMove(gameController.getPlayer(), column);
    }

    public void drawTile(int pos, int player) {
        clientUI.highlightPlayer(player);

        clientUI.drawTile(pos, player);
    }

//    public void highLightTiles(ArrayList pos) {
//        clientUI.highLightTiles(pos);
//    }

    public void changeHighlightedPlayer(int player) {
        clientUI.highlightPlayer(player);
    }

    public void winner(int player) {
        clientUI.winner("Player " + player + " won!");
    }

    public void draw() {
        clientUI.winner("It's a draw!");
    }

    // New Local
    public void newGame() {
        gameController.newGame(0);
        clientUI.newGame();
    }

    // New MM
    public void newGame(int player, int gamemode) {
        this.player = player;
        gameController.newGame(gamemode);
        clientUI.newGame();
    }

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