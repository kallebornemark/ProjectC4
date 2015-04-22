package projectc4.c4.client;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.HashSet;
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
    private int playedRow, playedCol;
    private int playedTiles;
    private int gameMode;
    private HashSet<Integer> winningTiles = new HashSet<>();
    private Timer timer;
    private int time;

    public GameController(ClientController clientController) {
        this.playerTurn = PLAYER1;
        this.clientController = clientController;
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

    public int getBoardWidth() {
        return gameBoard[1].length;
    }

    public int getBoardHeight() {
        return gameBoard.length;
    }

    public void setElement(int row, int col, int player) {
        this.gameBoard[row][col] = player;
    }

    public int getElement(int row, int col) {
        return gameBoard[row][col];
    }

    public int getPlayedTiles() {
        return playedTiles;
    }

    public void resetGameBoard() {
        this.gameBoard = new int[6][7];
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setPlayerTurn(int player) {
//        System.out.println("Sätter player i gameController");
        this.playerTurn = player;
    }

    public int getGameMode(){
        return gameMode;
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
                newSecond();
            }
        }, 0, 1000);
    }

    public void newSecond() {
        --time;
        if (time == 0) {
            System.out.println("TIDEN SLUT");
            timer.cancel();
            changePlayer(false);
            clientController.newOutgoingMove(EMPTYMOVE);
        }
    }

    public void cancelTimer() {
        timer.cancel();
    }

    public void changePointerpos(int pointerCol) {
        gameGridShowPointer.changePointerPos(pointerCol);
    }

    private int calculate(int row, int col) {
        return (row * getBoardWidth()) + col;
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
            clientController.animateBlackArrow(PLAYER1); // <--
        } else {
            if (playerTurn == PLAYER1) {
                clientController.animateBlackArrow(PLAYER1); // <--
            } else {
                clientController.animateBlackArrow(PLAYER2); // -->
            }
        }
        if (!isIncoming && timer != null) {
            timer.cancel();
        }
    }

    private void highlightTiles(){
        gameGridView.setWinningTiles(winningTiles, gameBoard);
    }

    public void newGame(int gameMode) {
        resetGameBoard();

        if (gameGridView != null && gameGridShowPointer != null && gameGridForeground != null) {
            gameGridView.newGame();
            gameGridShowPointer.changePointerPos(-1);
            setButtonEnable(true);
        }
//        if(gameMode == MATCHMAKING) {
//            clientController.stopAnimation();
//        }
        gameBoard = new int[6][7];
        colSize = new int[getBoardWidth()];
        playedTiles = 0;
        winningTiles.clear();
        this.gameMode = gameMode;

        if(gameMode == LOCAL) {
            setPlayerTurn(PLAYER1);
            clientController.setPlayer(PLAYER1);
            clientController.changeHighlightedPlayer(PLAYER1);
        }
    }

    public synchronized void newMove(int x, final boolean isIncoming) {
        System.out.println("GameController - newMove(" + x + ") [ isIncoming = " + isIncoming + " ]");
        if (colSize[x] < getBoardHeight()) {
            if ((isIncoming || ( playerTurn == clientController.getPlayer()))) {
                setButtonEnable(false);

                playedRow = (getBoardHeight() - 1) - (colSize[x]);
                playedCol = x;

                final int tmpRow = (getBoardHeight() - 1) - (colSize[x]++);
                gameGridShowPointer.changePointerPos(x);

                if (gameMode == MATCHMAKING && !isIncoming) {
                    clientController.newOutgoingMove(x);
                }

//                System.out.println("Calc " + calculate(playedRow,playedCol))

                playedTiles++;

                if (gameMode == MATCHMAKING){
                    gameGridView.newMove(tmpRow, x, playerTurn);
                    checkOutcome(isIncoming);
                    // TODO Fixa animation för MM, tredje movet fuckad upp allt atm
//                    gameGridAnimation.newMove(tmpRow,x,playerTurn, isIncoming);

                } else if (gameMode == LOCAL) {
                    gameGridAnimation.newMove(tmpRow,x,playerTurn, isIncoming);
                }
            }
        }
    }

    public void setButtonEnable(boolean buttonEnable){
        gameGridForeground.setButtonEnable(buttonEnable);
    }
       //Todo dafuck händer här?
    public synchronized void finishMove(final int row, final int col, final int player, final boolean isIncoming){
        new Handler(Looper.getMainLooper()).postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                gameGridView.newMove(row, col, player);
            }
        });
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                checkOutcome(isIncoming);
            }
        }, 20);
    }

    public synchronized void checkOutcome(boolean isIncoming) {
        if (checkHorizontal() || checkVertical() || checkDiagonalRight() || checkDiagonalLeft()) {
            // If somebody won
            setButtonEnable(false);
            if(timer != null) {
                timer.cancel();
            }
            clientController.enableGameButton();
            clientController.disableBlackArrow();
            setButtonEnable(false);
            highlightTiles();
            // Put a star next to the player who won
            clientController.highlightWinnerPlayerStar(playerTurn);
            clientController.setWinner(playerTurn);

            if (gameMode == MATCHMAKING) {
                clientController.stopAnimation();
                clientController.updateUser(playerTurn, false);
            }
        } else if (playedTiles == 42) {
            // Draw
            clientController.draw();
            if (gameMode == MATCHMAKING) {
                clientController.stopAnimation();
                clientController.updateUser(playerTurn, true);
            }

        } else {
            // Regular move without any particular outcome
            changePlayer(isIncoming);
            setButtonEnable(true);
            if (gameMode == LOCAL) {
                clientController.setPlayer(playerTurn);
            }
        }
    }

    private boolean checkHorizontal() {
        winningTiles.clear();
        int counter = 0;
        for(int col = playedCol; col < getBoardWidth(); col++) {
            if(col == getBoardWidth()-1 || getElement(playedRow, col+1) != playerTurn) {
                for(int colMinus = col; colMinus >= 0; colMinus--) {
                    if(getElement(playedRow, colMinus) == playerTurn) {
                        counter++;
                        winningTiles.add(calculate(playedRow, colMinus));
                    } else {
                        return counter >= 4;
                    }

                }
                return counter >= 4;
            }
        }
        return counter >= 4;
//        int counter = 1;
//        for (int i = playedCol; i < getBoardWidth(); i++) {
//            if (i == getBoardWidth() - 1 || getElement(playedRow, i + 1) != playerTurn) {
//                counter = 1;
//                for (int j = i; j >= 0; j--) {
//                    if (j == 0 || getElement(playedRow, j - 1) != playerTurn) {
//                        return false;
//                    } else {
//                        counter++;
//                    }
//                    if (counter == 4) {
//                        System.out.println("j: " + j);
//                        for(int n = 0; n < counter; n++) {
//                            winningTiles.add(calculate(playedRow, j - 1) + n);
//                            System.out.print(calculate(playedRow, j - 1) + n);
//                        }
//                        return true;
//                    }
//                }
//            } else {
//                counter++;
//            }
//            if (counter == 4) {
//                System.out.println("i: " + i);
//                for(int n = 0; n < counter; n++) {
//                    winningTiles.add(calculate(playedRow, i + 1) - n);
//                    System.out.print(calculate(playedRow, i + 1) - n);
//                }
//                return true;
//            }
//        }
//        return false;
    }

    private boolean checkVertical(){
        winningTiles.clear();
        int counter = 0;
        for(int row = playedRow; row < getBoardHeight(); row++) {
            if(getElement(row, playedCol) == playerTurn) {
                counter++;
                winningTiles.add(calculate(row, playedCol));
            } else {
                return counter >= 4;
            }
        }
        return counter >= 4;
    }

    private boolean checkDiagonalRight() {
        winningTiles.clear();
        int counter = 0;
        for(int col = playedCol, row = playedRow; col < getBoardWidth() && row < getBoardHeight(); col++, row++) {
            if(col == getBoardWidth()-1 || row == getBoardHeight()-1 || getElement(row+1, col+1) != playerTurn) {
                for(int colMinus = col, rowMinus = row; colMinus >= 0 && rowMinus >= 0; colMinus--, rowMinus--) {
                    if(getElement(rowMinus, colMinus) == playerTurn) {
                        counter++;
                        winningTiles.add(calculate(rowMinus, colMinus));
                    } else {
                        return counter >= 4;
                    }
                }
                return counter >= 4;
            }
        }
        return counter >= 4;

//        int counter = 1;
//        for(int i = playedCol,j = playedRow; i < getBoardWidth() && j < getBoardHeight(); i++, j++) {
//            if(i == getBoardWidth() - 1 || j == getBoardHeight() -1 || getElement(j + 1, i + 1) != playerTurn) {
//                counter = 1;
//                for(int x = i, y = j; x >= 0 && y >= 0; x--, y--) {
//                    if(x == 0 || y == 0 || getElement(y - 1, x - 1) != playerTurn) {
//                        return false;
//                    }else {
//                        counter++;
//                    }
//                    if(counter == 4) {
//                        System.out.println();
//                        for(int n = 0; n <= 21; n+=7) {
//                            winningTiles.add(calculate(y - 1,x - 1) + n);
//                            System.out.print(calculate(y - 1,x - 1) + n + " ");
//                        }
//                        return true;
//                    }
//                }
//            }else {
//                counter++;
//            }
//            if(counter == 4) {
//                System.out.println()
//                ;
//                for(int n = 0; n <= 21; n+=7) {
//                    winningTiles.add(calculate(j + 1,i + 1) - n);
//                    System.out.print(calculate(j + 1,i + 1) - n + " ");
//                }
//                return true;
//            }
//        }
//        return false;
    }

    private boolean checkDiagonalLeft(){
        winningTiles.clear();
        int counter = 0;
        for (int colMinus = playedCol, rowPlus = playedRow; colMinus >= 0 && rowPlus < getBoardHeight(); colMinus--, rowPlus++) {
            if (colMinus == 0 || rowPlus == getBoardHeight()-1 || getElement(rowPlus+1, colMinus-1) != playerTurn) {
                for (int colPlus = colMinus, rowMinus = rowPlus; colPlus < getBoardWidth() && rowMinus >= 0; colPlus++, rowMinus--) {
                    if (getElement(rowMinus, colPlus) == playerTurn) {
                        counter++;
                        winningTiles.add(calculate(rowMinus, colPlus));
                    } else {
                        return counter >= 4;
                    }
                }
                return counter >= 4;
            }
        }
        return counter >= 4;

//        int counter = 1;
//        for(int i = playedCol,j = playedRow; i >= 0 && j < getBoardHeight(); i--, j++) {
//            if(i == 0 || j == getBoardHeight() -1 || getElement(j + 1, i - 1) != playerTurn) {
//                counter = 1;
//                for(int x = i, y = j; x < getBoardWidth() && y >= 0; x++, y--) {
//                    if(x == getBoardWidth() - 1 || y == 0 || getElement(y - 1, x + 1) != playerTurn) {
//                        return false;
//                    }else {
//                        counter++;
//                    }if(counter == 4) {
//                        System.out.println()
//                        ;
//                        for(int n = 0; n <= 15; n+=5) {
//                            winningTiles.add(calculate(y - 1,x + 1) + n);
//                            System.out.print(calculate(y - 1,x + 1) + n + " ");
//                        }
//                        return true;
//                    }
//                }
//            }else {
//                counter++;
//            }if(counter == 4) {
//                System.out.println()
//                ;
//                for(int n = 0; n <= 15; n+=5) {
//                    winningTiles.add(calculate(j + 1,i - 1) - n);
//                    System.out.print(calculate(j + 1,i - 1) - n + " ");
//                }
//                return true;
//            }
//        }
//        return false;
    }

    public void setButtonEnable() {
        gameGridForeground.setButtonEnable(false);
    }
}
