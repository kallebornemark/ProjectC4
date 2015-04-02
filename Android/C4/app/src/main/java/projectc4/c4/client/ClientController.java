package projectc4.c4.client;

import java.io.Serializable;
import java.util.ArrayList;

import projectc4.c4.LocalGameActivity;

import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark
 */
public class ClientController {
    private static ClientController instance;
    private static GameController gameController;
    private ClientUI clientUI;
    private Client client;
    private LocalGameActivity localGameActivity;
    private int player;
    public int gameMode;
    public boolean gameIsReady = false;

    public static void initInstance()
    {
        if (instance == null)
        {
            // Create the instance
            instance = new ClientController();
            gameController = new GameController(instance);
        }
    }

    public static ClientController getInstance()
    {
        // Return the instance
        return instance;
    }

    private ClientController() {
//        gameController = new GameController(this);
    }

    public void createClientUI() {
        clientUI = new ClientUI(this);
    }

    public void setActivity(LocalGameActivity localGameActivity) {
        this.localGameActivity = localGameActivity;
    }

    public void connect() {
        client = new Client(this);
        client.connect("10.2.10.36", 3450);
    }

    public LocalGameActivity getLocalGameActivity() {
        return localGameActivity;
    }

    public void newMove(int column) {
        System.out.println("Clientcontrollerns newMove " + column);
        gameController.newMove(column);
        if(client != null) {
            client.newMove(gameController.getPlayer(), column);
        }
    }

    public void drawTile(int pos, int player) {
        clientUI.highlightPlayer(player);

        clientUI.drawTile(pos, player);
    }

    public void highLightTiles(ArrayList<Integer> pos) {
        clientUI.highLightTiles(pos);
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

    // New Local
    public void newGame() {
        gameController.newGame(LOCAL);
        clientUI.newGame();
    }

    public void requestGame(int gamemode) {
        client.requestGame(gamemode);
    }

    // New MM
    public void newGame(int gamemode) {
        gameController.newGame(gamemode);
        clientUI.newGame();
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void setCurrentPlayer(int player) {
        gameController.setPlayer(player);
    }

    public int getCurrentPlayer() {
       return gameController.getPlayer();
    }
}