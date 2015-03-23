package projectc4.c4;

/**
 * @author Kalle Bornemark
 */
public class ClientController {
    private GameController gameController;
    private ClientUI clientUI;

    public ClientController() {
        gameController = new GameController(this);
        clientUI = new ClientUI(this);
    }

}