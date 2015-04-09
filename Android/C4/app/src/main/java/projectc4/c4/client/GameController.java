package projectc4.c4.client;

import java.util.ArrayList;
import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark
 */
public class GameController {
    private ClientController clientController;
    private GameGridView gameGridView;
    private int[] size = new int[6];
    private int playerTurn;
    private int row, col;
    private int playedTiles;
    private int gameMode;
    private ArrayList<Integer> winningTiles = new ArrayList<>();

    public GameController(ClientController clientController, GameGridView gameGridView) {
        playerTurn = PLAYER1;
        this.clientController = clientController;
        //Todo fixa implementering
//        gameGridView = new GameGrid();
        this.gameGridView = gameGridView;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int player) {
        this.playerTurn = player;
    }

    public void newGame(int gameMode) {
        playedTiles = 0;
        for (int i = 0; i < size.length; i++) {
            size[i] = 0;
        }
        gameGridView.reset();
        winningTiles.clear();
        this.gameMode = gameMode;
        /*
        Lokalt
         */
        if(gameMode == LOCAL) {
            setPlayerTurn(PLAYER1);
            clientController.setPlayer(PLAYER1);
            clientController.changeHighlightedPlayer(PLAYER1);
        }
    }

    public void newMove(int x) {
        // Move from local player
        System.out.println("GameController - newMove(" + x + ")");
        if(playerTurn == clientController.getPlayer() && size[x] < 7) {
            System.out.println("GameController: newMove accepted");
            row = (gameGridView.getWidth() - 1) - (size[x]);
            col = x;


            gameGridView.setElement((gameGridView.getWidth() - 1) - (size[x]++), x, playerTurn);
//            clientController.drawTile((((gameGridView.getWidth() - 1) - (size[x]-1)) * 6) + x, playerTurn);



            System.out.println("Calc " + calculate(row,col));
            playedTiles++;
            if (checkHorizontal() || checkVertical() || checkDiagonalRight() || checkDiagonalLeft()) {
                clientController.winner(playerTurn);

                /*
                    TODO Göra om highlighttiles metoden
                 */
//                clientController.highLightTiles(winningTiles);



                System.out.println("Winner");
            } else if (playedTiles == 42) {
                clientController.draw();
                System.out.println("Draw");
            } else {
                changePlayer();
                if (gameMode == LOCAL) {
                    clientController.setPlayer(playerTurn);
                }
            }
        }
    }

    public void newIncomingMove(int x) {
        // Move from local player
        System.out.println("GameController - newIncomingMove(" + x + ")");
        if(size[x] < 7) {
            System.out.println("GameController: newIncomingMove accepted");
            row = (gameGridView.getHeight() - 1) - (size[x]);
            col = x;


            gameGridView.setElement((gameGridView.getHeight() - 1) - (size[x]++), x, playerTurn);
//            clientController.drawTile((((gameGridView.getHeight() - 1) - (size[x]-1)) * 6) + x, playerTurn);


            System.out.println("GameController - newIncomingMove: DrawTile by " + playerTurn);
            System.out.println("Calc " + calculate(row,col));
            playedTiles++;
            if (checkHorizontal() || checkVertical() || checkDiagonalRight() || checkDiagonalLeft()) {
                clientController.winner(playerTurn);
//                clientController.highLightTiles(winningTiles);
                System.out.println("Winner");
            } else if (playedTiles == 42) {
                clientController.draw();
                System.out.println("Draw");
            } else {
                changePlayer();
            }
        }
    }


    public void changePlayer() {
        if (playerTurn == PLAYER1) {
            playerTurn = PLAYER2;
        } else {
            playerTurn = PLAYER1;
        }
        clientController.changeHighlightedPlayer(playerTurn);
    }

    private boolean checkHorizontal() {
        int counter = 1;
        for (int i = col; i < gameGridView.getWidth(); i++) {
            if (i == gameGridView.getWidth() - 1 || gameGridView.getElement(row, i + 1) != playerTurn) {
                counter = 1;
                for (int j = i; j >= 0; j--) {
                    if (j == 0 || gameGridView.getElement(row, j - 1) != playerTurn) {
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
        for(int x = row; x < gameGridView.getHeight(); x++) {
            if(x == gameGridView.getHeight() - 1 || gameGridView.getElement(x + 1, col) != playerTurn) {
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
        for(int i = col,j = row; i < gameGridView.getWidth() && j < gameGridView.getHeight(); i++, j++) {
            if(i == gameGridView.getWidth() - 1 || j == gameGridView.getHeight() -1 || gameGridView.getElement(j + 1, i + 1) != playerTurn) {
                counter = 1;
                for(int x = i, y = j; x >= 0 && y >= 0; x--, y--) {
                    if(x == 0 || y == 0 || gameGridView.getElement(y - 1, x - 1) != playerTurn) {
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
        for(int i = col,j = row; i >= 0 && j < gameGridView.getHeight(); i--, j++) {
            if(i == 0 || j == gameGridView.getHeight() -1 || gameGridView.getElement(j + 1, i - 1) != playerTurn) {
                counter = 1;
                for(int x = i, y = j; x < gameGridView.getWidth() && y >= 0; x++, y--) {
                    if(x == gameGridView.getWidth() - 1 || y == 0 || gameGridView.getElement(y - 1, x + 1) != playerTurn) {
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
