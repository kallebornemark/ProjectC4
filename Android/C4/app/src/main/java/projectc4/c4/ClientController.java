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

}