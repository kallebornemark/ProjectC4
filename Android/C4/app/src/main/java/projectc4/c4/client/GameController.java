package projectc4.c4.client;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import static projectc4.c4.util.C4Constants.*;

/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
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
    private Powerup powerup;

    public GameController(ClientController clientController) {
        this.playerTurn = PLAYER1;
        this.clientController = clientController;
        this.gameBoard = new int[6][7];
        this.powerup = new Powerup(this);
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
        this.playerTurn = player;
    }

    public int getGameMode(){
        return gameMode;
    }

    public void startTimer(int time) {
        if (timer != null) {
            timer.cancel();
        }
        this.time = time;
        timer = new Timer();
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
            if(timer == null) {
                startTimer(30);
            }
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
        setElement(3,0,POWERUP_TIME);
        if (gameGridView != null && gameGridShowPointer != null && gameGridForeground != null) {
            gameGridView.newGame();
            gameGridShowPointer.changePointerPos(-1);
            setButtonEnable(true);
        }
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

    public void checkIfPowerup(int tile, boolean isIncoming) {
        if(tile == POWERUP_TIME) {
            if(isIncoming) {
                System.out.println("POWERUP TIME");
                powerup.powerupTime();
            }
           clientController.setTimeLimit(true);
        }
    }


    public void newMove(int col, boolean isIncoming) {
        System.out.println("GameController - newMove(" + col + ") [ isIncoming = " + isIncoming + " ]");
        if (colSize[col] < getBoardHeight()) {
            System.out.println(getElement((getBoardHeight() - 1) - (colSize[col]),col));
            checkIfPowerup(getElement((getBoardHeight() - 1) - (colSize[col]),col), isIncoming);
            setButtonEnable(false);
            this.playedCol = col;
            this.playedRow = (getBoardHeight() - 1) - (colSize[playedCol]++);
            setElement(playedRow, playedCol, playerTurn);
            playedTiles++;
            gameGridShowPointer.changePointerPos(playedCol);
            gameGridAnimation.newMove(playedRow, playedCol, isIncoming);
        }
    }

    public void setButtonEnable(boolean buttonEnable){
        gameGridForeground.setButtonEnable(buttonEnable);
    }

    public void finishMove(boolean isIncoming){
        gameGridView.newMove(playedRow, playedCol);

        if (gameMode == MATCHMAKING && !isIncoming) {
            clientController.newOutgoingMove(playedCol);
        }
        checkOutcome(isIncoming);
    }

    public void checkOutcome(boolean isIncoming) {
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
                clientController.setOkayToLeave(true);
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
    }
}
