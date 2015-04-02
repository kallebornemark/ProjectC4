package projectc4.c4.client;
import java.io.Serializable;
import java.util.ArrayList;
import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark
 */
public class GameController {
    private ClientController clientController;
    private GameGrid gameGrid;
    private int[] size = new int[6];
    private int playerToMakeNextMove;
    private int row, col;
    private int playedTiles;
    private int gameMode;
    private ArrayList<Integer> winningTiles = new ArrayList<Integer>();

    public GameController(ClientController clientController) {
        this.clientController = clientController;
        gameGrid = new GameGrid();
        playerToMakeNextMove = PLAYER1;

    }

    public int getPlayer() {
        return playerToMakeNextMove;
    }

    public void newGame(int gameMode) {
        playedTiles = 0;
        for (int i = 0; i < size.length; i++) {
            size[i] = 0;
        }
        gameGrid.reset();
        playerToMakeNextMove = PLAYER1;
        winningTiles.clear();
        this.gameMode = gameMode;
        /*
        Lokalt
         */
        if(gameMode == LOCAL) {
            clientController.setPlayer(playerToMakeNextMove);
        }
    }

    public void newMove(int x) {
        //Om nuvarande spelares nummer är lika med ditt spelar-nummer och kollumnen inte är full
        System.out.println("Innan if-satsen i newMove");
        if(playerToMakeNextMove == clientController.getPlayer() && size[x] < 7) {
            System.out.println("INNE I newMove");
            row = (gameGrid.getHeight() - 1) - (size[x]);
            col = x;
            gameGrid.setElement((gameGrid.getHeight() - 1) - (size[x]++), x, playerToMakeNextMove);
            clientController.drawTile((((gameGrid.getHeight() - 1) - (size[x]-1)) * 6) + x, playerToMakeNextMove);
            System.out.println("Calc " + calculate(row,col));
            playedTiles++;
            if (checkHorizontal() || checkVertical() || checkDiagonalRight() || checkDiagonalLeft()) {
                clientController.winner(playerToMakeNextMove);
                clientController.highLightTiles(winningTiles);
                System.out.println("Winner");
            } else if (playedTiles == 42) {
                clientController.draw();
                System.out.println("Draw");
            } else {
                changePlayer();
                /*
                Lokalt
                 */
                if(gameMode == LOCAL) {
                    clientController.setPlayer(playerToMakeNextMove);
                }
            }
        }
    }

    public void changePlayer() {
        if(playerToMakeNextMove == PLAYER1) {
            playerToMakeNextMove = PLAYER2;
        }else {
            playerToMakeNextMove = PLAYER1;
        }
        clientController.changeHighlightedPlayer(playerToMakeNextMove);
    }

    private boolean checkHorizontal() {
        int counter = 1;
        for (int i = col; i < gameGrid.getLength(); i++) {
            if (i == gameGrid.getLength() - 1 || gameGrid.getElement(row, i + 1) != playerToMakeNextMove) {
                counter = 1;
                for (int j = i; j >= 0; j--) {
                    if (j == 0 || gameGrid.getElement(row, j - 1) != playerToMakeNextMove) {
                        return false;
                    } else {
                        counter++;
                    }
                    if (counter == 4) {
                        System.out.println("j: " + j);
                        for(int n = 0; n < counter; n++) {
                            winningTiles.add(calculate(row, j - 1) + n);
                            System.out.print(calculate(row, j - 1) + n);
                        }
                        return true;
                    }
                }
            } else {

                counter++;
            }
            if (counter == 4) {
                System.out.println("i: " + i);
                for(int n = 0; n < counter; n++) {
                    winningTiles.add(calculate(row, i + 1) - n);
                    System.out.print(calculate(row, i + 1) - n);
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkVertical(){
        int counter = 1;
        for(int x = row; x < gameGrid.getHeight(); x++) {
            if(x == gameGrid.getHeight() - 1 || gameGrid.getElement(x + 1, col) != playerToMakeNextMove) {
                return false;
            } else {
                counter++;
                if (counter == 4) {
                    System.out.println();
                    for(int i = 0, j= 0; i < counter; i ++,j += 6) {
                        winningTiles.add(calculate(row,col) + j);
                    }
                    return true;
                }
            }
        }
        return false;

    }

    private boolean checkDiagonalRight() {
        int counter = 1;
        for(int i = col,j = row; i < gameGrid.getLength() && j < gameGrid.getHeight(); i++, j++) {
            if(i == gameGrid.getLength() - 1 || j == gameGrid.getHeight() -1 || gameGrid.getElement(j + 1, i + 1) != playerToMakeNextMove) {
                counter = 1;
                for(int x = i, y = j; x >= 0 && y >= 0; x--, y--) {
                    if(x == 0 || y == 0 || gameGrid.getElement(y - 1, x - 1) != playerToMakeNextMove) {
                        return false;
                    }else {
                        counter++;
                    }
                    if(counter == 4) {
                        System.out.println();
                        for(int n = 0; n <= 21; n+=7) {
                            winningTiles.add(calculate(y - 1,x - 1) + n);
                            System.out.print(calculate(y - 1,x - 1) + n + " ");
                        }
                        return true;
                    }
                }
            }else {
                counter++;
            }
            if(counter == 4) {
                System.out.println()
                ;
                for(int n = 0; n <= 21; n+=7) {
                    winningTiles.add(calculate(j + 1,i + 1) - n);
                    System.out.print(calculate(j + 1,i + 1) - n + " ");
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonalLeft(){
        int counter = 1;
        for(int i = col,j = row; i >= 0 && j < gameGrid.getHeight(); i--, j++) {
            if(i == 0 || j == gameGrid.getHeight() -1 || gameGrid.getElement(j + 1, i - 1) != playerToMakeNextMove) {
                counter = 1;
                for(int x = i, y = j; x < gameGrid.getLength() && y >= 0; x++, y--) {
                    if(x == gameGrid.getLength() - 1 || y == 0 || gameGrid.getElement(y - 1, x + 1) != playerToMakeNextMove) {
                        return false;
                    }else {
                        counter++;
                    }if(counter == 4) {
                        System.out.println()
                        ;
                        for(int n = 0; n <= 15; n+=5) {
                            winningTiles.add(calculate(y - 1,x + 1) + n);
                            System.out.print(calculate(y - 1,x + 1) + n + " ");
                        }
                        return true;
                    }
                }
            }else {
                counter++;
            }if(counter == 4) {
                System.out.println()
                ;
                for(int n = 0; n <= 15; n+=5) {
                    winningTiles.add(calculate(j + 1,i - 1) - n);
                    System.out.print(calculate(j + 1,i - 1) - n + " ");
                }
                return true;
            }
        }
        return false;
    }

    private int calculate(int row, int col) {
        int value = (row * 6) + col;
        return value;
    }
}
