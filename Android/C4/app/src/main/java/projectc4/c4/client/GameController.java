package projectc4.c4.client;

import java.util.ArrayList;
import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark
 */
public class GameController {
    private ClientController clientController;
    private GameGridView gameGridView;
    private GameGridAnimation gameGridAnimation;
    private GameGridForeground gameGridForeground;
    private int[] colSize;
    private int playerTurn;
    private int row, col;
    private int playedTiles;
    private int gameMode;
    private ArrayList<Integer> winningTiles = new ArrayList<>();
    private boolean gameIsActive;

    public GameController(ClientController clientController) {
        playerTurn = PLAYER1;
        this.clientController = clientController;
        this.colSize = new int[7];
        gameIsActive = true;
    }

    public void setViews(GameGridView gameGridView, GameGridAnimation gameGridAnimation, GameGridForeground gameGridForeground) {
        this.gameGridView = gameGridView;
        this.gameGridAnimation = gameGridAnimation;
        this.gameGridForeground = gameGridForeground;
        gameGridForeground.setGameController(this);
        this.gameGridView.setFocusable(true);
        this.gameGridView.addViews(this.gameGridAnimation, this.gameGridForeground);
//        this.colSize = new int[gameGridView.getBoardWidth()];
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int player) {
        System.out.println("Sätter player i gameController");
        this.playerTurn = player;
    }

    public void newGame(int gameMode) {
        if (gameGridView != null) {
            gameGridView.reset();

        }
        gameIsActive = true;
        playedTiles = 0;
        for (int i = 0; i < colSize.length; i++) {
            colSize[i] = 0;
        }
        winningTiles.clear();
        this.gameMode = gameMode;

        if(gameMode == LOCAL) {
            setPlayerTurn(PLAYER1);
            clientController.setPlayer(PLAYER1);
            clientController.changeHighlightedPlayer(PLAYER1);
        }
    }

    public void newMove(int x, boolean isIncoming) {
        System.out.println("GameController - newMove(" + x + ") [ isIncoming = " + isIncoming + " ]");
        if (colSize[x] < gameGridView.getBoardHeight()) {
            if ((isIncoming || ( playerTurn == clientController.getPlayer()) && gameIsActive)) {

                row = (gameGridView.getBoardHeight() - 1) - (colSize[x]);
                col = x;
                gameGridView.setElement((gameGridView.getBoardHeight() - 1) - (colSize[x]++), x, playerTurn);
                playedTiles++;

                // Check if somebody won or if the game is drawn
                checkOutcome();

                // Send the move to opponent if it's an online game
                if (gameMode == MATCHMAKING && !isIncoming) {
                    clientController.newOutgoingMove(x);
                }
            }

        }
    }

    public void checkOutcome() {
        if (checkHorizontal() || checkVertical() || checkDiagonalRight() || checkDiagonalLeft()) {

            // Somebody won
            gameIsActive = false;
            clientController.enableGameButton();

//            TODO Göra om highlightTiles-metoden
//            clientController.highlightTiles(winningTiles);

            // Put a star next to the player who won
            clientController.highlightWinnerPlayerStar(playerTurn);
            clientController.updateUser(playerTurn);

        } else if (playedTiles == 42) {

            // Draw
            clientController.draw();

        } else {

            // Regular move without any particular outcome
            changePlayer();
            if (gameMode == LOCAL) {
                clientController.setPlayer(playerTurn);
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
        for (int i = col; i < gameGridView.getBoardWidth(); i++) {
            if (i == gameGridView.getBoardWidth() - 1 || gameGridView.getElement(row, i + 1) != playerTurn) {
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
        for(int x = row; x < gameGridView.getBoardHeight(); x++) {
            if(x == gameGridView.getBoardHeight() - 1 || gameGridView.getElement(x + 1, col) != playerTurn) {
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
        for(int i = col,j = row; i < gameGridView.getBoardWidth() && j < gameGridView.getBoardHeight(); i++, j++) {
            if(i == gameGridView.getBoardWidth() - 1 || j == gameGridView.getBoardHeight() -1 || gameGridView.getElement(j + 1, i + 1) != playerTurn) {
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
        for(int i = col,j = row; i >= 0 && j < gameGridView.getBoardHeight(); i--, j++) {
            if(i == 0 || j == gameGridView.getBoardHeight() -1 || gameGridView.getElement(j + 1, i - 1) != playerTurn) {
                counter = 1;
                for(int x = i, y = j; x < gameGridView.getBoardWidth() && y >= 0; x++, y--) {
                    if(x == gameGridView.getBoardWidth() - 1 || y == 0 || gameGridView.getElement(y - 1, x + 1) != playerTurn) {
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
