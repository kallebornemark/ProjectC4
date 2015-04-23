package projectc4.c4.client;


import projectc4.c4.client.fragments.GameFragment;
import projectc4.c4.client.fragments.GamePopupFragment;
import projectc4.c4.client.fragments.LoginFragment;
import projectc4.c4.client.fragments.MatchmakingFragment;
import projectc4.c4.util.GameInfo;
import projectc4.c4.util.User;

import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark
 */
public class ClientController {
    private GameController gameController;
    private Client client;
    private int player = PLAYER1;
    private int gameMode;
    private GameFragment gameFragment;
    private MatchmakingFragment matchmakingFragment;
    private LoginFragment loginFragment;
    private GamePopupFragment gamePopupFragment;
    private String opponentName;
    private GameInfo gameInfo;
    private boolean okayToLeave = false;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public int getGameMode() {
        return this.gameMode;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client c) {
        this.client = c;
    }

    public void serverOffline() {
        loginFragment.serverOffline();
    }

    public void cancelTimer() {
        gameController.cancelTimer();
    }

    public void setGameFragment(GameFragment gameFragment) {
        this.gameFragment = gameFragment;
    }

    public void setMatchmakingFragment(MatchmakingFragment matchmakingFragment) {
        this.matchmakingFragment = matchmakingFragment;
    }

    public void setLoginFragment(LoginFragment loginFragment) {
        this.loginFragment = loginFragment;
    }

    public void setGamePopupFragment(GamePopupFragment gamePopupFragment) {
        this.gamePopupFragment = gamePopupFragment;
    }

    public GamePopupFragment getGamePopupFragment() {
        return this.gamePopupFragment;
    }

    public void connect() {
        client = new Client(this);
//        client.connect("10.2.10.36", 3450);
//        client.connect("10.2.10.36", 3450);
//        client.connect("10.1.8.135", 3450);
//        client.connect("10.2.25.13", 3450);
//        client.connect("10.1.8.135", 3450);
//        client.connect("10.1.17.111", 3450);
//        client.connect("192.168.1.57", 3450); // Kalles hemmadator
//        client.connect("192.168.0.10", 3450);
//        client.connect("10.1.17.111", 3450);
        client.connect("10.2.20.240", 3450);
//        client.connect("17
// 2.20.10.2", 3450); // Kalles hemmadator
//        client.connect("192.168.0.10", 3450);
//        client.connect("192.168.0.10", 3450);

    }

    public void newOutgoingMove(int column) {
        client.newMove(column);
    }

    public void newIncomingMove(int column) {
        System.out.println("Clientcontrollerns newIncomingMove " + column);
        if(column == EMPTYMOVE) {
            gameController.changePlayer(true);
        }else {
            gameController.newMove(column, true);
        }
    }

    public void startGameUI() {
        matchmakingFragment.startGameUI();
    }

    public void drawTile(int pos, int player) {
        System.out.println("Drawtile: drawTile(" + pos + "," + player + ")");
    }

    public void changeHighlightedPlayer(int player) {
        gameFragment.highlightPlayer(player);
    }

    public void highlightWinnerPlayerStar(int player) {
        gameFragment.highlightWinnerPlayerStar(player);
    }

    public void enableGameButton() {
        if (gameMode == MATCHMAKING) {
            gameFragment.promptRematch();
        } else if (gameMode == LOCAL) {
            gameFragment.setNewGame();
        }
    }

    public void unpromptRematch() {
        gameFragment.unpromptRematch();
    }

    public void disableBlackArrow() {
        gameFragment.disableBlackArrow();
    }

    public void enableBlackArrow() {
        gameFragment.enableBlackArrow();
    }

    public void animateBlackArrow(int direction) {
        gameFragment.animateArrow(direction);
    }

    public void draw() {
//        gameFragment.setTextViewWinner("It's a draw!");
        highlightWinnerPlayerStar(DRAW);
        changeHighlightedPlayer(DRAW);
        enableGameButton();
    }

    public void newGame(int gamemode) {
        gameController.newGame(gamemode);
    }

    public void requestRematch() {
        client.requestRematch();
    }

    public void rematch() {
        newGame(MATCHMAKING);
        unpromptRematch();
        changeHighlightedPlayer(gameController.getPlayerTurn());
        gameFragment.animateArrow(gameController.getPlayerTurn());
        gameFragment.disableStars();
    }

    public void requestGame(int gamemode) {
        client.requestGame(gamemode);
    }

    public void setWinner(int winner) {
        gameFragment.setWinner(winner);
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

    public GameController getGameController() {
        return gameController;
    }

    public void requestUsername(String username) {
        client.requestUsername(username);
    }

    public User getUser() {
        return client.getUser();
    }

    public void goToMatchmaking() {
        loginFragment.goToMatchmaking();
    }

    public void login() {
        loginFragment.requestUsername();
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    /**
     * Updates the winner's User object's game result statistics.
     *
     * @param playerTurn The winner (PLAYER1 if local player, PLAYER2 if opponent)
     * @param draw Whether or not it's a draw.
     */
    public void updateUser(int playerTurn, boolean draw) {
        System.out.println("Update user called in ClientController, winner = " + playerTurn + ", draw = " + draw);
        if (!draw) {
            if (playerTurn == player) {
                getUser().newGameResult(WIN, gameInfo.getOpponentElo());
                client.updateUser(WIN);
            }else if (playerTurn == SURRENDER) { // Force Loss
                System.out.println("FORCE LOSS");
                getUser().newGameResult(LOSS, gameInfo.getOpponentElo());
                client.updateUser(SURRENDER);
            }else if(playerTurn == WIN) { //Force win
                System.out.println("FORCE WIN");
                gameController.setButtonEnable();
                highlightWinnerPlayerStar(PLAYER1);
                stopAnimation();
                getUser().newGameResult(WIN, gameInfo.getOpponentElo());
                client.updateUser(WIN);
                setOkayToLeave(true);

            }else {
                getUser().newGameResult(LOSS, gameInfo.getOpponentElo());
                client.updateUser(LOSS);
            }
        } else {
            getUser().newGameResult(DRAW, gameInfo.getOpponentElo());
            client.updateUser(DRAW);
        }
    }

    public String getPlayerStats(boolean opponent) {
        int[] stats;
        double elo;
        String resString;
        if (opponent) {
            stats = gameInfo.getOpponentGameResults();
            elo = gameInfo.getOpponentElo();
        } else {
            stats = getUser().getGameResults();
            elo = getUser().getElo();
        }

        resString = "Total games played: " + stats[0] + "\n" +
                "Games won: " + stats[1] + "\n" +
                "Games lost: " + stats[2] + "\n" +
                "Games drawn: " + stats[3] + "\n\n" +
                "ELO: " + elo;

        return resString;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void stopAnimation() {
        gameFragment.stopAnimation();
    }

    public boolean isOkayToLeave() {
        return okayToLeave;
    }

    public void setOkayToLeave(boolean okayToLeave) {
        this.okayToLeave = okayToLeave;
    }

    public void cancelSearch() {
        client.cancelSearch();
    }
}