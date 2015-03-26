package projectc4.c4;

/**
 * @author Kalle Bornemark
 */
public class ClientController {
    private GameController gameController;
    private ClientUI clientUI;
    private MainActivity mainActivity;

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
        System.out.println(pos);
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

}