package projectc4.c4.client;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import c4.utils.C4Constants;


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
    private Boolean extraTurn = false;

    public GameController(ClientController clientController) {
        this.playerTurn = C4Constants.PLAYER1;
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

        this.powerup = new Powerup(this, gameGridView);
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

    public int getPlayedRow() {
        return playedRow;
    }

    public void resetGameBoard() {
        System.out.println("RESET GAMEBOARD");
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

    public void dropPowerup(int[] powerupAndCol) {
        int powerup = powerupAndCol[0], col = powerupAndCol[1];
        gameGridView.dropPowerup(powerup, col, colSize);
    }

    public int[] getColSize() {
        return colSize;
    }

    public void startTimer(int time) {
        if (timer != null) {
            timer.cancel();
            timer = null;
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
            timer = null;
            changePlayer(false);
            clientController.newOutgoingMove(C4Constants.EMPTYMOVE);
        }
    }

    public void cancelTimer() {
        timer.cancel();
        timer = null;
    }

    public void changePointerpos(int pointerCol) {
        gameGridShowPointer.changePointerPos(pointerCol);
    }

    private int calculate(int row, int col) {
        return (row * getBoardWidth()) + col;
    }

    public void changePlayer(boolean isIncoming) {
        if(!extraTurn) {
            if (playerTurn == C4Constants.PLAYER1) {
                playerTurn = C4Constants.PLAYER2;

            } else {
                playerTurn = C4Constants.PLAYER1;
            }
            clientController.changeHighlightedPlayer(playerTurn);
            if (isIncoming) {
                if (timer == null) {
                    startTimer(30);
                }
                clientController.animateBlackArrow(C4Constants.PLAYER1); // <--
            } else {
                if (playerTurn == C4Constants.PLAYER1) {
                    clientController.animateBlackArrow(C4Constants.PLAYER1); // <--
                } else {
                    clientController.animateBlackArrow(C4Constants.PLAYER2); // -->
                }
            }
            if (!isIncoming && timer != null) {
                timer.cancel();
                timer = null;
            }
        } else {
            setExtraTurn(false);
        }
    }

    private void highlightTiles(){
        gameGridView.setWinningTiles(winningTiles, gameBoard);
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public void setExtraTurn(Boolean extraTurn) {
        this.extraTurn = extraTurn;
    }

    public void newGame(int gameMode) {
        if(gameMode == C4Constants.LOCAL) {
          resetGameBoard();
        }

        if (gameGridView != null && gameGridShowPointer != null && gameGridForeground != null) {
            gameGridView.newGame();
            gameGridShowPointer.changePointerPos(-1);
            setButtonEnable(true);
        }
        colSize = new int[getBoardWidth()];
        playedTiles = 0;
        winningTiles.clear();
        this.gameMode = gameMode;
        if(gameMode == C4Constants.LOCAL) {
            setPlayerTurn(C4Constants.PLAYER1);
            clientController.setPlayer(C4Constants.PLAYER1);
            clientController.changeHighlightedPlayer(C4Constants.PLAYER1);
        }
    }

    public void checkIfPowerup(int tile, int playedRow, int playedCol, boolean isIncoming) {
        if (tile == C4Constants.POWERUP_TIME) {
            if(isIncoming) {
                System.out.println("POWERUP TIME");
                powerup.powerupTime();
            }
           clientController.setTimeLimit(true);
        }
        if (tile == C4Constants.POWERUP_COLORBLIND) {
            if(isIncoming) {
                powerup.powerupColorblind();
            }
        }
        if (tile == C4Constants.POWERUP_BOMB) {
            powerup.powerupBomb(playedRow, playedCol);
        }
        if (tile == C4Constants.POWERUP_EXTRATURN) {
            powerup.powerupExtraTurn();
        }
        if (tile == C4Constants.POWERUP_SHUFFLE) {
            powerup.powerupShuffle();
        }
    }

    public void reDraw() {
        System.out.println("ReDraw");
//        gameGridForeground.paintForeground(gameBoard);
    }


    public void newMove(int col, boolean isIncoming) {
//        gameGridForeground.removeIcon(3,0);
        System.out.println("GameController - newMove(" + col + ") [ isIncoming = " + isIncoming + " ]");
        if (colSize[col] < getBoardHeight()) {
            this.playedCol = col;
            this.playedRow = (getBoardHeight() - 1) - (colSize[playedCol]);
            System.out.println(getElement((getBoardHeight() - 1) - (colSize[col]),col));
            checkIfPowerup(getElement((getBoardHeight() - 1) - (colSize[col]++),col), playedRow, playedCol, isIncoming);
            setButtonEnable(false);
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

        if (gameMode == C4Constants.MATCHMAKING && !isIncoming) {
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

            if (gameMode == C4Constants.MATCHMAKING) {
                clientController.stopAnimation();
                clientController.updateUser(playerTurn, false);
                clientController.getGameInfo().setRematch(true);
                clientController.setOkayToLeave(true);
            }

        } else if (playedTiles == 42) {
            // Draw
            clientController.draw();
            if (gameMode == C4Constants.MATCHMAKING) {
                clientController.stopAnimation();
                clientController.updateUser(playerTurn, true);
            }

        } else {
            // Regular move without any particular outcome
            changePlayer(isIncoming);
            setButtonEnable(true);
            if (gameMode == C4Constants.LOCAL) {
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

    public void setPowerups(int[][] gameBoard) {
        this.gameBoard = null;
        this.gameBoard = gameBoard;
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
