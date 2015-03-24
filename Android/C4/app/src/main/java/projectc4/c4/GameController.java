package projectc4.c4;

/**
 * @author Kalle Bornemark
 */
public class GameController {
    private ClientController clientController;
    private GameGrid gameGrid;
    private int[] size = new int[6];
    private int player;
    private ClientUI clientUI;

    public GameController(ClientController clientController) {
        this.clientController = clientController;
        gameGrid = new GameGrid();
        player = 1;
        clientUI = clientController.getClientUI();
    }

    public void insertTile(int x) {
        if(size[x] < 7) {
            gameGrid.setElement((gameGrid.getHeight() - 1) - (size[x]++), x, player);
            clientUI.drawTile((((gameGrid.getHeight() - 1) - (size[x]-1)) * 6) + (x + 1), player);
            //Vinstkoll
            if(player == 1) {
                player = 2;
            }else {
                player = 1;
            }
        }
    }


    public static void main(String[] args) {
//        GameController con = new GameController();
    }


}
