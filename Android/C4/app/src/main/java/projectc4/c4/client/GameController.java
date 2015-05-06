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
    private int startingPlayer = C4Constants.PLAYER1;
    private int playedRow, playedCol;
    private int playedTiles;
    private int gameMode;
    private HashSet<Integer> winningTiles = new HashSet<>();
    private Timer timer;
    private int time;
    private Powerup powerup;
    private Boolean extraTurn = false;
    private int rows = 6, cols = 7;
    private int winsSize = 4;
    private int rounds = 1;
    private int player1Points, player2Points;

    public GameController(ClientController clientController) {
        this.playerTurn = C4Constants.PLAYER1;
        this.clientController = clientController;
        this.gameBoard = new int[rows][cols];
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

    public void setArraySize(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        if(gameGridView != null)
            gameGridView.setRowsCols(rows, cols);
        if(rows < 10) {
            setWinsSize(4);
        } else if(rows >= 10) {
            setWinsSize(5);
        }
    }

    public void setWinsSize(int winsSize) {
        this.winsSize = winsSize;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public void setPlayer1Points(int player1Points) {
        this.player1Points = player1Points;
    }

    public void setPlayer2Points(int player2Points) {
        this.player2Points = player2Points;
    }

    public void setStartingPlayer(int startingPlayer) {
        this.startingPlayer = startingPlayer;
    }
    public int getPlayedRow() {
        return playedRow;
    }

    public void resetGameBoard() {
        System.out.println("RESET GAMEBOARD");
        this.gameBoard = new int[rows][cols];
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

    public void swapPlayerTurn() {
        if (playerTurn == C4Constants.PLAYER1) {
            playerTurn = C4Constants.PLAYER2;
        } else {
            playerTurn = C4Constants.PLAYER1;
        }
    }

    public void changePlayer(boolean isIncoming) {
        if(!extraTurn) {
            swapPlayerTurn();
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
        } else {
            setWinsSize(4);
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
            if(player1Points == 0 && player2Points == 0) {
                setPlayerTurn(startingPlayer);
                clientController.setPlayer(startingPlayer);
                clientController.changeHighlightedPlayer(startingPlayer);
                clientController.getGameFragment().animateArrow(playerTurn);
            } else if (playerTurn == C4Constants.PLAYER1) {
                setPlayerTurn(C4Constants.PLAYER2);
                clientController.setPlayer(C4Constants.PLAYER2);
                clientController.changeHighlightedPlayer(C4Constants.PLAYER2);
                clientController.getGameFragment().animateArrow(C4Constants.PLAYER2);
            } else {
                setPlayerTurn(C4Constants.PLAYER1);
                clientController.setPlayer(C4Constants.PLAYER1);
                clientController.changeHighlightedPlayer(C4Constants.PLAYER1);
                clientController.getGameFragment().animateArrow(C4Constants.PLAYER1);
            }

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
            clientController.disableBlackArrow();
            setButtonEnable(false);
            highlightTiles();
            // Put a star next to the player who won
            if (gameMode == C4Constants.LOCAL) {
                if(playerTurn == C4Constants.PLAYER1) {
                    player1Points++;
                    //Öka poängen i UI-med +1
                } else {
                    player2Points++;
                    //Öka poängen i UI-med +1
                }
                if (player1Points == (rounds / 2 +1) || player2Points == (rounds / 2 +1)) {
                    clientController.enableGameButton(false);
                    clientController.highlightWinnerPlayerStar(playerTurn);
                    clientController.setWinner(playerTurn);
                    player1Points = 0;
                    player2Points = 0;
                    //Switch to selectFragment or just run the same game again
                } else {
                    clientController.enableGameButton(true);
                }
            } else {
                clientController.enableGameButton(false);
                clientController.highlightWinnerPlayerStar(playerTurn);
                clientController.setWinner(playerTurn);
            }

            if (gameMode == C4Constants.MATCHMAKING) {
                clientController.stopAnimation();
                clientController.updateUser(playerTurn, false);
                clientController.getGameInfo().setRematch(true);
                clientController.setOkayToLeave(true);
            }

        } else if (playedTiles == rows*cols) {
            // Draw
            clientController.draw(true);
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
                        return counter >= winsSize;
                    }

                }
                return counter >= winsSize;
            }
        }
        return counter >= winsSize;
    }

    private boolean checkVertical(){
        winningTiles.clear();
        int counter = 0;
        for(int row = playedRow; row < getBoardHeight(); row++) {
            if(getElement(row, playedCol) == playerTurn) {
                counter++;
                winningTiles.add(calculate(row, playedCol));
            } else {
                return counter >= winsSize;
            }
        }
        return counter >= winsSize;
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
                        return counter >= winsSize;
                    }
                }
                return counter >= winsSize;
            }
        }
        return counter >= winsSize;
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
                        return counter >= winsSize;
                    }
                }
                return counter >= winsSize;
            }
        }
        return counter >= winsSize;
    }
}
