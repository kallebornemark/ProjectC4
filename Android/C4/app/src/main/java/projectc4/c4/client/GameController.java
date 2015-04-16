package projectc4.c4.client;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark
 */
public class GameController {
    private ClientController clientController;
    private GameGridView gameGridView;
    private GameGridShowPointer gameGridShowPointer;
    private GameGridForeground gameGridForeground;
    private GameGridAnimation gameGridAnimation;
    private int[][] gameBoard;
    private int[] colSize;
    private int playerTurn;
    private int row, col;
    private int playedTiles;
    private int gameMode;
    private ArrayList<Integer> winningTiles = new ArrayList<>();
    private boolean gameIsActive;
    private Timer timer;
    private int time;

    public GameController(ClientController clientController) {
        this.playerTurn = PLAYER1;
        this.clientController = clientController;
        this.gameIsActive = true;
    }

    public int getBoardWidth() {
        return gameBoard[1].length;
    }

    public int getBoardHeight() {
        return gameBoard.length;
    }

    public void setElement(int row, int col, int player) {
        this.gameBoard[row][col] = player;
    }

    public int getElement(int i, int j) {
        return gameBoard[i][j];
    }

    public Timer getTimer() {
        return timer;
    }

    public void resetGameBoard() {
        this.gameBoard = new int[6][7];
    }

    public void setViews(GameGridView gameGridView, GameGridAnimation gameGridAnimation, GameGridShowPointer gameGridShowPointer, GameGridForeground gameGridForeground) {
        this.gameGridView = gameGridView;
        this.gameGridView.setGameController(this);
        this.gameGridView.setFocusable(true);

        this.gameGridAnimation = gameGridAnimation;
        this.gameGridAnimation.setGameController(this);

        this.gameGridShowPointer = gameGridShowPointer;
        this.gameGridShowPointer.setGameController(this);

        this.gameGridForeground = gameGridForeground;
        this.gameGridForeground.setGameController(this);

    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int player) {
        System.out.println("Sätter player i gameController");
        this.playerTurn = player;
    }

    public int getGameMode(){
        return gameMode;
    }


    public void changePointerpos(int pointerCol) {
        gameGridShowPointer.changePointerPos(pointerCol);
    }

    public void newGame(int gameMode) {
        resetGameBoard();
        if (gameGridView != null) {
            gameGridView.newGame();
            gameGridShowPointer.changePointerPos(-1);
        }

        gameBoard = new int[6][7];
        colSize = new int[getBoardWidth()];
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

    public void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        time = 30;
        timer = new Timer(); //Kanske slösar minne
        System.out.println("TIMER STARTAD");
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                toDO();
            }
        }, 0, 1000);


}

    public void newMove(int x, boolean isIncoming) {
        System.out.println("GameController - newMove(" + x + ") [ isIncoming = " + isIncoming + " ]");
        if (colSize[x] < getBoardHeight()) {
            if ((isIncoming || ( playerTurn == clientController.getPlayer()) && gameIsActive)) {

                row = (getBoardHeight() - 1) - (colSize[x]);
                col = x;

                int tmpRow = (getBoardHeight() - 1) - (colSize[x]++);
                gameGridShowPointer.changePointerPos(x);
                gameGridView.newMove(tmpRow, x, playerTurn);

                //för animation, fungerar inte just nu...
//                gameGridAnimation.newMove(tmpRow, x, playerTurn);
//                System.out.println("Calc " + calculate(row,col))
                playedTiles++;

                // Check if somebody won or if the game is drawn
                checkOutcome(isIncoming);

                // Send the move to opponent if it's an online game
                if (gameMode == MATCHMAKING && !isIncoming) {
                    clientController.newOutgoingMove(x);
                }
            }

        }
    }

    public void checkOutcome(boolean isIncoming) {
        if (checkHorizontal() || checkVertical() || checkDiagonalRight() || checkDiagonalLeft()) {

            gameGridAnimation.setClickable(false);
            // Somebody won
            if(timer != null) {
                timer.cancel();
            }
            gameIsActive = false;
            clientController.enableGameButton();

//            TODO Göra om highlightTiles-metoden
//            clientController.highlightTiles(winningTiles);

            // Put a star next to the player who won
            clientController.highlightWinnerPlayerStar(playerTurn);

            if (gameMode == MATCHMAKING) {
                clientController.updateUser(playerTurn, false);
            }

        } else if (playedTiles == 42) {

            // Draw
            clientController.draw();
            if (gameMode == MATCHMAKING) {
                clientController.updateUser(playerTurn, true);
            }

        } else {

            // Regular move without any particular outcome
            changePlayer(isIncoming);
            if (gameMode == LOCAL) {
                clientController.setPlayer(playerTurn);
            }

        }
    }

    public void changePlayer(boolean isIncoming) {
        if (playerTurn == PLAYER1) {
            playerTurn = PLAYER2;
        } else {
            playerTurn = PLAYER1;
        }
        clientController.changeHighlightedPlayer(playerTurn);
        if (isIncoming) {
            startTimer();
        }
        if (!isIncoming && timer != null) {
            timer.cancel();
        }
    }

    private boolean checkHorizontal() {
        int counter = 1;
        for (int i = col; i < getBoardWidth(); i++) {
            if (i == getBoardWidth() - 1 || getElement(row, i + 1) != playerTurn) {
                counter = 1;
                for (int j = i; j >= 0; j--) {
                    if (j == 0 || getElement(row, j - 1) != playerTurn) {
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
        for(int x = row; x < getBoardHeight(); x++) {
            if(x == getBoardHeight() - 1 || getElement(x + 1, col) != playerTurn) {
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
        for(int i = col,j = row; i < getBoardWidth() && j < getBoardHeight(); i++, j++) {
            if(i == getBoardWidth() - 1 || j == getBoardHeight() -1 || getElement(j + 1, i + 1) != playerTurn) {
                counter = 1;
                for(int x = i, y = j; x >= 0 && y >= 0; x--, y--) {
                    if(x == 0 || y == 0 || getElement(y - 1, x - 1) != playerTurn) {
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
        for(int i = col,j = row; i >= 0 && j < getBoardHeight(); i--, j++) {
            if(i == 0 || j == getBoardHeight() -1 || getElement(j + 1, i - 1) != playerTurn) {
                counter = 1;
                for(int x = i, y = j; x < getBoardWidth() && y >= 0; x++, y--) {
                    if(x == getBoardWidth() - 1 || y == 0 || getElement(y - 1, x + 1) != playerTurn) {
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

    public void cancelTimer() {
        timer.cancel();
    }

    public void toDO() {
        --time;
        if (time == 0) {
            System.out.println("TIDEN SLUT");
            timer.cancel();
            changePlayer(false);
            clientController.newOutgoingMove(EMPTYMOVE);
        }
    }
}
