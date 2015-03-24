package projectc4.c4;

/**
 * @author Kalle Bornemark
 */
public class GameController {
    private ClientController clientController;
    private GameGrid gameGrid;
    private int[] size = new int[6];
    private int player;
    private int row, col;

    public GameController(ClientController clientController) {
        this.clientController = clientController;
        gameGrid = new GameGrid();
        player = 1;
    }

    public void newMove(int x) {
        if(size[x] < 7) {
            row = (gameGrid.getHeight() - 1) - (size[x]);
            col = x;
            gameGrid.setElement((gameGrid.getHeight() - 1) - (size[x]++), x, player);
            clientController.drawTile((((gameGrid.getHeight() - 1) - (size[x]-1)) * 6) + (x + 1), player);
            checkOutcome();
        }
    }

    public void changePlayer() {
        if(player == 1) {
            player = 2;
        }else {
            player = 1;
        }
    }

    public void checkOutcome() {


    }

    public boolean checkHorizontal() {
        int counter = 1;

        for(int i = col; i < gameGrid.getLength(); i++) {

            if(gameGrid.getElement(row,i+1) != player) {
                counter = 1;
                for(int j = i; j >= 0; j--) {
                    if(gameGrid.getElement(row,j-1) != player) {
                        return false;
                    }
                    else {
                        counter++;
                    }
                    if (counter == 4) {
                        return true;
                    }
                }
            }
            else {
                counter++;
            }
            if(counter == 4) {
                return true;
            }
        }
        return false;
    }

}
