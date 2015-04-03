package projectc4.c4.client;

import java.util.ArrayList;

import android.widget.Button;
import projectc4.c4.GameActivity;
import projectc4.c4.MatchmakingActivity;
import projectc4.c4.util.C4Color;

import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark
 */
public class ClientController {
    private static ClientController instance;
    private static GameController gameController;
    private Client client;
    private GameActivity gameActivity;
    private MatchmakingActivity matchmakingActivity;
    private int player = PLAYER1;
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

    public void setActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public void setMatchmakingActivity(MatchmakingActivity matchmakingActivity) {
        this.matchmakingActivity = matchmakingActivity;
    }

    public void connect() {
        client = new Client(this);
//        client.connect("10.2.10.36", 3450);
//        client.connect("10.1.17.111", 3450);
        client.connect("192.168.1.57", 3450); //Kalles hemmadator
//        client.connect("10.1.3.0", 3450); //Kalles laptop

    }

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public void newMove(int column) {
        if (player == getPlayerTurn()) {
            System.out.println("Clientcontrollerns newLocalMove " + column);
            gameController.newMove(column);
            if (gameMode == MATCHMAKING) {
                client.newMove(column);
            }
        }
    }

    public void newIncomingMove(int column) {
        System.out.println("Clientcontrollerns newIncomingMove " + column);
        gameController.newIncomingMove(column);
    }

    public void startGameUI() {
        matchmakingActivity.startGameUI();
    }

    public void drawTile(int pos, int player) {
//        changeHighlightedPlayer(player);
//        System.out.println("Drawtile: changeHighlight()");


        gameActivity.drawTile(pos, player);
        System.out.println("Drawtile: drawTile(" + pos + "," + player+")");
    }

    public void highLightTiles(ArrayList<Integer> pos) {
        gameActivity.highlightTiles(pos);
    }


    public void changeHighlightedPlayer(int player) {
        gameActivity.highlightPlayer(player);
    }

    public void winner(int player) {
        if (player == PLAYER1) {
            player = 1;
        } else {
            player = 2;
        }
        gameActivity.setTextViewWinner("Player " + player + " won!");
    }

    public void draw() {
        gameActivity.setTextViewWinner("It's a draw!");
    }

/*    // New Local
    public void newGame() {
        gameController.newGame(LOCAL);
        clientUI.newGame();
    }*/

    // New MM
    public void newGame(int gamemode) {
        gameController.newGame(gamemode);

        for (int i = 0; i < 42; i++) {
            gameActivity.getGrid().getChildAt(i).setBackgroundColor(C4Color.WHITE);
        }

        ArrayList<Button> buttonArrayList;
        buttonArrayList = gameActivity.getButtonArrayList();

        for (int i = 0; i < buttonArrayList.size(); i++) {
            buttonArrayList.get(i).setEnabled(true);
        }
    }

    public void requestRematch() {
        client.requestRematch();
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

    public void setPlayerTurn(int player) {
        gameController.setPlayerTurn(player);
    }

    public int getPlayerTurn() {
       return gameController.getPlayerTurn();
    }
}