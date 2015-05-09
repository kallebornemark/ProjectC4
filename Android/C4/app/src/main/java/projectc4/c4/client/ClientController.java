package projectc4.c4.client;


import c4.utils.C4Constants;
import c4.utils.GameInfo;
import c4.utils.Highscore;
import c4.utils.User;
import projectc4.c4.client.fragments.GameFragment;
import projectc4.c4.client.fragments.GamePopupFragment;
import projectc4.c4.client.fragments.HighscoreFragment;
import projectc4.c4.client.fragments.LoginFragment;
import projectc4.c4.client.fragments.MatchmakingFragment;

/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class ClientController {
    private GameController gameController;
    private Client client;
    private int player = C4Constants.PLAYER1;
    private int gameMode;
    private GameFragment gameFragment;
    private MatchmakingFragment matchmakingFragment;
    private LoginFragment loginFragment;
    private GamePopupFragment gamePopupFragment;
    private HighscoreFragment highscoreFragment;
    private String opponentName;
    private GameInfo gameInfo;
    private boolean okayToLeave = false;
    private MainActivity context;

    public ClientController(MainActivity context) {
        this.context = context;
    }

    public MainActivity getContext() {
        return context;
    }

    public void setContext(MainActivity context) {
        this.context = context;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        this.client = new Client(this);
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

    public void loginErrorMessage(String msg) {
        loginFragment.loginErrorMessage(msg);
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

    public void newUser(User user) {
        client.newUser(user);
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

    public void enableLoginButton() {
        loginFragment.enableLoginButton();
    }

    public void connect() {
//          client.connect("10.2.10.38", 3450);
//        client.connect("10.2.10.36", 3450);
        client.connect("10.2.10.36", 3450);
//        client.connect("10.1.8.135", 3450);
//        client.connect("10.2.25.13", 3450);
//        client.connect("10.1.8.135", 3450);
//        client.connect("10.1.17.111", 3450);
//        client.connect("192.168.1.57", 3450); // Kalles hemmadator
//        client.connect("172.20.10.2", 3450); // Kalles hotspot
//        client.connect("192.168.1.210", 3450); // Kalles macbook hemma
//        client.connect("192.168.0.10", 3450);
//        client.connect("10.1.17.111", 3450);
//        client.connect("10.2.20.240", 3450);
//        client.connect("192.168.0.10", 3450);
//        client.connect("192.168.1.225", 3450);

    }

    public void newOutgoingMove(int column) {
        client.newMove(column);
    }

    public void newIncomingMove(int column) {
        System.out.println("Clientcontrollerns newIncomingMove " + column);
        if(column == C4Constants.EMPTYMOVE) {
            gameController.changePlayer(true);
        } else {
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

    public void enableGameButton(boolean gameInProgress) {
        if (gameMode == C4Constants.MATCHMAKING) {
            gameFragment.promptRematch();
        } else if (gameMode == C4Constants.LOCAL) {
            gameFragment.setNewGame(gameInProgress);
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

    public void draw(boolean gameInProgress) {
//        gameFragment.setTextViewWinner("It's a draw!");
        highlightWinnerPlayerStar(C4Constants.DRAW);
        changeHighlightedPlayer(C4Constants.DRAW);
        enableGameButton(gameInProgress);
    }

    public void newGame(int gamemode) {
        gameController.newGame(gamemode);
    }

    public void requestRematch() {
        client.requestRematch();
    }

    public void rematch() {
        gameController.swapPlayerTurn();
        newGame(C4Constants.MATCHMAKING);
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

    public void setTimeLimit(boolean timeLimit) {
        gameFragment.setTimeLimit(timeLimit);
    }

    public int getOpponent() {
        int opponent;
        if (player == C4Constants.PLAYER1) {
            return C4Constants.PLAYER2;
        }
        return C4Constants.PLAYER1;
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

    public GameFragment getGameFragment() {
        return gameFragment;
    }

    public void login(String username, String password) {
        client.login(username, password);
    }

    public void setHighscoreFragment(HighscoreFragment highscoreFragment){
        this.highscoreFragment = highscoreFragment;
    }

    public void showHighscore(Highscore highscore){
        highscoreFragment.setHighscore(highscore);
    }

    public User getUser() {
        return client.getUser();
    }

    public User getOpponentUser() {
        return client.getOpponentUser();
    }

    public void goToMatchmaking() {
        loginFragment.goToMatchmaking();
    }

    public void login() {
        loginFragment.login();
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
                getUser().newGameResult(C4Constants.WIN, getOpponentUser().getElo());
                getOpponentUser().newGameResult(C4Constants.LOSS, getUser().getElo());

                client.updateUser(C4Constants.WIN);
            } else if (playerTurn == C4Constants.SURRENDER) { // Force Loss
                System.out.println("FORCE LOSS");
                getUser().newGameResult(C4Constants.LOSS, gameInfo.getOpponentElo());
                client.updateUser(C4Constants.SURRENDER);
            } else if (playerTurn == C4Constants.WIN) { //Force win
                System.out.println("FORCE WIN");
                gameController.setButtonEnable(false);
                highlightWinnerPlayerStar(C4Constants.PLAYER1);
                stopAnimation();
                getUser().newGameResult(C4Constants.WIN, gameInfo.getOpponentElo());
                getOpponentUser().newGameResult(C4Constants.LOSS, getUser().getElo());
                setOkayToLeave(true);

            } else {
                getUser().newGameResult(C4Constants.LOSS, getOpponentUser().getElo());
                getOpponentUser().newGameResult(C4Constants.WIN, getUser().getElo());
            }
        } else {
            getUser().newGameResult(C4Constants.DRAW, gameInfo.getOpponentElo());
            client.updateUser(C4Constants.DRAW);
        }
        gameFragment.setElos();
    }

    public void setElos() {
        gameFragment.setElos();
    }

    public String getPlayerStats(boolean opponent) {
        int[] stats;
        double elo;
        String resString;
        if (opponent) {
            stats = client.getOpponentUser().getGameResults();
            elo = client.getOpponentUser().getElo();
        } else {
            stats = getUser().getGameResults();
            elo = getUser().getElo();
        }
        String eloString = String.format("%.2f", elo);

        resString = "Total games played: " + (stats[0] + stats[1] + stats[2]) + "\n" +
                    "Games won: " + stats[0] + "\n" +
                    "Games lost: " + stats[1] + "\n" +
                    "Games drawn: " + stats[2] + "\n\n" +
                    "Rating: " + eloString;

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