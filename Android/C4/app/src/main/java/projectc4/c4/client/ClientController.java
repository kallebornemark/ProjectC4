package projectc4.c4.client;

import java.util.ArrayList;
import projectc4.c4.LocalGameActivity;
import projectc4.c4.MatchmakingActivity;
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
    private MatchmakingActivity matchmakingActivity;
    private int player;
    private int gameMode;
//    public boolean gameIsReady = false;

    public static void initInstance()
    {
        if (instance == null)
        {
            // Create the instance
            instance = new ClientController();
            gameController = new GameController(instance);
        }
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public int getGameMode() {
        return this.gameMode;
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

    public void setMatchmakingActivity(MatchmakingActivity matchmakingActivity) {
        this.matchmakingActivity = matchmakingActivity;
    }

    public void connect() {
        client = new Client(this);
//        client.connect("10.2.10.36", 3450);
//        client.connect("10.1.17.111", 3450);
        client.connect("192.168.1.57", 3450); //Kalles hemmadator
    }

    public LocalGameActivity getLocalGameActivity() {
        return localGameActivity;
    }

    public void newLocalMove(int column) {
        System.out.println("Clientcontrollerns newMove " + column);
        gameController.newLocalMove(column);
    }

    public void newOutgoingMove(int column) {
        gameController.newLocalMove(column);
        client.newMove(gameController.getPlayer(), column);
    }

    public void newIncomingMove(int column) {
        gameController.newIncomingMove(column);
    }

    public void startGameUI() {
        matchmakingActivity.startGameUI();
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
        if (player == PLAYER1) {
            player = 1;
        } else {
            player = 2;
        }
        clientUI.winner("Player " + player + " won!");
    }

    public void draw() {
        clientUI.winner("It's a draw!");
    }

/*    // New Local
    public void newGame() {
        gameController.newGame(LOCAL);
        clientUI.newGame();
    }*/

    // New MM
    public void newGame(int gamemode) {
        gameController.newGame(gamemode);
        clientUI.newGame();
    }

    public void requestGame(int gamemode) {
        client.requestGame(gamemode);
    }

    public int getOpponent() {
        int opponent;
        if (player == PLAYER1) {
            return PLAYER2;
        }
        return PLAYER1;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
        System.out.println("Player set to: " + this.player);
    }

    public void setCurrentPlayer(int player) {
        gameController.setPlayer(player);
    }

    public int getCurrentPlayer() {
       return gameController.getPlayer();
    }
}