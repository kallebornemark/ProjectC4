package projectc4.c4.client;

/**
 * @author Kalle Bornemark
 */
public class GameController {
    private ClientController clientController;
    private Game game;
    private int[] size = new int[6];
    private int player;
    private int row, col;
    private int playedTiles;

    public GameController(ClientController clientController) {
        this.clientController = clientController;
        game = new Game();
        player = 1;
    }

    public void newGame() {
        playedTiles = 0;
        for (int i = 0; i < size.length; i++) {
            size[i] = 0;
        }
        game.reset();
        player = 1;
        /*
        Lokalt
         */
        if(clientController.getUsername() == null) {
            clientController.setPlayer(player);
        }
    }

    public void newMove(int x) {
        //Om nuvarande spelares nummer är lika med ditt spelar-nummer och kollumnen inte är full
        if(player == clientController.getPlayer() && size[x] < 7) {
            row = (game.getHeight() - 1) - (size[x]);
            col = x;
            game.setElement((game.getHeight() - 1) - (size[x]++), x, player);
            clientController.drawTile((((game.getHeight() - 1) - (size[x]-1)) * 6) + x, player);
            playedTiles++;
            if (checkHorizontal() || checkVertical() || checkDiagonalRight() || checkDiagonalLeft()) {
                clientController.winner(player);
                System.out.println("Winner");
            } else if (playedTiles == 42) {
                clientController.draw();
                System.out.println("Draw");
            } else {
                changePlayer();
                /*
                Lokalt
                 */
                 if(clientController.getUsername() == null) {
                    clientController.setPlayer(player);
                 }
            }
        }
    }

    public void changePlayer() {
        if(player == 1) {
            player = 2;
        }else {
            player = 1;
        }
        clientController.changeHighlightedPlayer(player);
    }

    private boolean checkHorizontal() {
        int counter = 1;
        for (int i = col; i < game.getLength(); i++) {
            if (i == game.getLength() - 1 || game.getElement(row, i + 1) != player) {
                counter = 1;
                for (int j = i; j >= 0; j--) {
                    if (j == 0 || game.getElement(row, j - 1) != player) {
                        return false;
                    } else {
                        counter++;
                    }
                    if (counter == 4) {
                        return true;
                    }
                }
            } else {
                counter++;
            }
            if (counter == 4) {
                return true;
            }
        }
        return false;
    }

    private boolean checkVertical(){
        int counter = 1;
        for(int x = row; x < game.getHeight(); x++) {
            if(x == game.getHeight() - 1 || game.getElement(x + 1, col) != player) {
                return false;
            } else {
                counter++;
                if (counter == 4) {
                    return true;
                }
            }
        }
        return false;

    }

    private boolean checkDiagonalRight() {
        int counter = 1;
        for(int i = col,j = row; i < game.getLength() && j < game.getHeight(); i++, j++) {
            if(i == game.getLength() - 1 || j == game.getHeight() -1 || game.getElement(j + 1, i + 1) != player) {
                counter = 1;
                for(int x = i, y = j; x >= 0 && y >= 0; x--, y--) {
                    if(x == 0 || y == 0 || game.getElement(y - 1, x - 1) != player) {
                        return false;
                    }else {
                        counter++;
                    }
                    if(counter == 4) {
                        return true;
                    }
                }
            }else {
                counter++;
            }
            if(counter == 4) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonalLeft(){
        int counter = 1;
        for(int i = col,j = row; i >= 0 && j < game.getHeight(); i--, j++) {
            if(i == 0 || j == game.getHeight() -1 || game.getElement(j + 1, i - 1) != player) {
                counter = 1;
                for(int x = i, y = j; x < game.getLength() && y >= 0; x++, y--) {
                    if(x == game.getLength() - 1 || y == 0 || game.getElement(y - 1, x + 1) != player) {
                        return false;
                    }else {
                        counter++;
                    }if(counter == 4) {
                        return true;
                    }
                }
            }else {
                counter++;
            }if(counter == 4) {
                return true;
            }
        }
        return false;
    }
}
