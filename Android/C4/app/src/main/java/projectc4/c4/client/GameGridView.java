package projectc4.c4.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;

import c4.utils.C4Color;
import c4.utils.C4Constants;
import projectc4.c4.R;


/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class GameGridView extends View {

    private GameController gameController;

    private int sideOfTile;
    private int offsetX;
    private int offsetY;
    private int width;
    private int height;
    private Paint paint;
    private Paint strokePaint;
    private Canvas c;
    private int rows, cols;
    private Drawable clock = getResources().getDrawable(R.drawable.c4_rush);
    private Drawable bomb = getResources().getDrawable(R.drawable.c4_bomb);
    private Drawable colorblind = getResources().getDrawable(R.drawable.c4_blind);
    private Drawable extraturn = getResources().getDrawable(R.drawable.c4_extra);
    private Drawable shuffle = getResources().getDrawable(R.drawable.c4_switch);
    private boolean noColor = false, drawColor = false;
    private Bitmap bitmap;
    private boolean newGame = true, create = true, bombTiles = false;
    private ArrayList<Integer> tempPosList;
    private int tempPlayedCol;

    public GameGridView(Context context) {
        super(context);
        init();
    }

    public GameGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public void init(){
        paint = new Paint();
        strokePaint = new Paint();
        strokePaint.setStrokeWidth(5);
        strokePaint.setColor(C4Color.WHITE);
        strokePaint.setStyle(Paint.Style.STROKE);


    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        rows = gameController.getBoardHeight();
        cols = gameController.getBoardWidth();
    }

    public void updateDisplay() {
        this.post(new Runnable() {
            public void run() {
                superInvalidate();
            }
        });
    }

    private void superInvalidate() {
        super.invalidate();
    }

    public void newGame(){
        newGame = true;
        updateDisplay();
    }

    public void createBitmap() {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        c = new Canvas(bitmap);
    }

    public void resetGameBoard(int[][] array){
        System.out.println();
        for (int row = 0; row < rows; row++) {
            System.out.println();
            for (int col = 0; col < cols; col++) {
                System.out.print(array[row][col] + "  ");
            }
        }
                if(create) {
              createBitmap();
              create = false;
          }
        paint.setColor(C4Color.LIGHTGRAY);
        c.drawRoundRect(offsetX, offsetY, getWidth()-offsetX, getHeight(), 20, 20, paint);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int posX = (col * (sideOfTile + C4Constants.GRIDSPACING)) + offsetX;
                int posY = (row * (sideOfTile + C4Constants.GRIDSPACING)) + offsetY;
                if (array[row][col] == C4Constants.POWERUP_TIME) { //Time icon draw
                    clock.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                    clock.draw(c);
                } else if (array[row][col] == C4Constants.POWERUP_BOMB){
                    bomb.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                    bomb.draw(c);
                } else if (array[row][col] == C4Constants.POWERUP_COLORBLIND){
                    colorblind.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                    colorblind.draw(c);
                } else if (array[row][col] == C4Constants.POWERUP_EXTRATURN){
                    extraturn.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                    extraturn.draw(c);
                } else if (array[row][col] == C4Constants.POWERUP_SHUFFLE){
                    shuffle.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                    shuffle.draw(c);
                } else if (array[row][col] == C4Constants.PLAYER1) {
                    paint.setColor(C4Color.RED);
                    c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
                } else if (array[row][col] == C4Constants.PLAYER2){
                    paint.setColor(C4Color.YELLOW);
                    c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
            }
          }
        }
        paint.setColor(C4Color.LIGHTGRAY);
        updateDisplay();
    }

    public void dropPowerup(int powerup, int col, int[] colSize) {

        if (colSize[col] < gameController.getBoardHeight()) {
            int row = (gameController.getBoardHeight() - 1) - (colSize[col]);
            int posX = (col * (sideOfTile + C4Constants.GRIDSPACING)) + offsetX;
            int posY = (row * (sideOfTile + C4Constants.GRIDSPACING)) + offsetY;
            if (gameController.getGameBoard()[row][col] == 0 && powerup == 30) { //Time icon draw
                clock.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                clock.draw(c);
                gameController.setElement(row,col,C4Constants.POWERUP_TIME);
            } else if (gameController.getGameBoard()[row][col] == 0 && powerup == 31){
                bomb.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                bomb.draw(c);
                gameController.setElement(row,col,31);
            } else if (gameController.getGameBoard()[row][col] == 0 && powerup == 32){
                colorblind.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                colorblind.draw(c);
                gameController.setElement(row,col,32);
            } else if (gameController.getGameBoard()[row][col] == 0 && powerup == 33){
                extraturn.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                extraturn.draw(c);
                gameController.setElement(row,col,33);
            } else if (gameController.getGameBoard()[row][col] == 0 && powerup == 34){
                shuffle.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                shuffle.draw(c);
                gameController.setElement(row,col,34);
            }
        }
        updateDisplay();
    }

    public void newMove(int row, int col) {
        if (gameController.getPlayerTurn() == C4Constants.PLAYER1) {
            paint.setColor(C4Color.RED);
        } else if (gameController.getPlayerTurn() == C4Constants.PLAYER2) {
            paint.setColor(C4Color.YELLOW);
        }
        int posX = (col * (sideOfTile + C4Constants.GRIDSPACING)) + offsetX;
        int posY = (row * (sideOfTile + C4Constants.GRIDSPACING)) + offsetY;
        c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
        if(drawColor) {
            drawTiles();
        } else if(noColor) {
            drawTilesGray();
            drawColor = true;
        }
        if(bombTiles) {
            bombTiles(tempPosList, tempPlayedCol);
        }
        updateDisplay();
    }

    protected void onDraw(Canvas canvas) {
//        if (bitmap==null){
        if(newGame) {
            if (!isInEditMode()) {
                resetGameBoard(gameController.getGameBoard());
                newGame = false;
            }
        }
//        if (bitmap != null && gameController != null){
        if(!newGame) {
            canvas.save();
            canvas.drawBitmap(bitmap, 0, 0, paint);
            canvas.restore();
        }
    }

    public void printArray(int[][] array) {
        System.out.println();
        for (int row = 0; row < array.length; row++) {
            System.out.println();
            for (int col = 0; col < array[0].length; col++) {
                System.out.print(array[row][col] + "  ");
            }
        }
    }

    public void setWinningTiles(HashSet<Integer> winningTiles, int[][] gameBoard) {
        printArray(gameBoard);

        int tmpCounter = 0;
        for (int row = 0; row < gameBoard.length; row++) {
            for (int col = 0; col < gameBoard[row].length; col++) {
                int tmpPlayer = gameBoard[row][col];
                int posX = (col * (sideOfTile + C4Constants.GRIDSPACING)) + offsetX;
                int posY = (row * (sideOfTile + C4Constants.GRIDSPACING)) + offsetY;

                if (tmpPlayer != 0) {
                    if (!winningTiles.contains(tmpCounter)) {
                        if (tmpPlayer == C4Constants.PLAYER1) {
                            paint.setColor(C4Color.REDLOSERTILE);
                        } else if (tmpPlayer == C4Constants.PLAYER2) {
                            paint.setColor(C4Color.YELLOWLOSERTILE);
                        } else {
                            paint.setColor(C4Color.LIGHTGRAY);
                        }
                        c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
                    } else {
                        if (tmpPlayer == C4Constants.PLAYER1) {
                            paint.setColor(C4Color.RED);
                        } else if (tmpPlayer == C4Constants.PLAYER2) {
                            paint.setColor(C4Color.YELLOW);
                        }
                        c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
                        c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, strokePaint);
                    }
                }
                tmpCounter++;
            }
        }
        paint.reset();
        updateDisplay();
    }

    public void drawTilesGray() {
        Paint paint = new Paint();
        paint.setColor(C4Color.TEST);
        for (int row = 0; row < gameController.getGameBoard().length; row++) {
            for (int col = 0; col < gameController.getGameBoard()[row].length; col++) {
                int posX = (col * (sideOfTile + C4Constants.GRIDSPACING)) + offsetX;
                int posY = (row * (sideOfTile + C4Constants.GRIDSPACING)) + offsetY;
                if(gameController.getElement(row,col) == C4Constants.PLAYER1 || gameController.getElement(row, col) == C4Constants.PLAYER2) {
                    c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);

                }

            }
        }
        paint = null;
        updateDisplay();
        noColor = false;
    }

    public void setNoColor(Boolean noColor) {
        this.noColor = noColor;
    }

    public void drawTiles() {
        Paint paint = new Paint();
        for (int row = 0; row < gameController.getGameBoard().length; row++) {
            for (int col = 0; col < gameController.getGameBoard()[row].length; col++) {
                int posX = (col * (sideOfTile + C4Constants.GRIDSPACING)) + offsetX;
                int posY = (row * (sideOfTile + C4Constants.GRIDSPACING)) + offsetY;
                if(gameController.getElement(row,col) == C4Constants.PLAYER1) {
                    paint.setColor(C4Color.RED);
                    c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
                }
                else if (gameController.getElement(row, col) == C4Constants.PLAYER2) {
                    paint.setColor(C4Color.YELLOW);
                    c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
                }

            }
        }
        paint = null;
        drawColor = false;

    }

    @Override
    protected void onMeasure(int widthMeasuredSpec, int heightMeasuredSpec) {
        width = MeasureSpec.getSize(widthMeasuredSpec);
        height = MeasureSpec.getSize(heightMeasuredSpec);
        if (gameController != null) {
            // Räkna ut passande storlek för brickan
            sideOfTile = Math.min((((width - C4Constants.GRIDSPACING) / gameController.getBoardWidth()) - C4Constants.GRIDSPACING),
                    (((height - C4Constants.GRIDSPACING) / gameController.getBoardHeight()) - C4Constants.GRIDSPACING));

            // Rita gameBoard mitt i canvasen i x-led
            offsetX = (width - (gameController.getBoardWidth() * (sideOfTile + C4Constants.GRIDSPACING) - C4Constants.GRIDSPACING)) / 2;

            // Rita gameBoard längst ner på canvasen i y-led
            offsetY = (height - (gameController.getBoardHeight() * (sideOfTile + C4Constants.GRIDSPACING)));
        }
//        System.out.println("GGW - getWidth(): " + width + " getHeight(): " + height + "\nsideOfTile: " + sideOfTile);
        setMeasuredDimension(width, height);
    }

    public void bombTiles(ArrayList<Integer> list, int playedCol) {
        printArray(gameController.getGameBoard());

        int posX = (playedCol * (sideOfTile + C4Constants.GRIDSPACING)) + offsetX;
        paint.setColor(C4Color.LIGHTGRAY);

        for (int i = 0; i < list.size(); i ++) {
            int posY = ((list.get(i)) * (sideOfTile + C4Constants.GRIDSPACING)) + offsetY;
            c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
        }
        gameController.getGameBoard()[gameController.getPlayedRow()][playedCol] = 0;
        bombTiles = false;
        tempPosList = null;
    }

    public void setBombTiles(Boolean bombTiles, ArrayList<Integer> posList, int playedCol) {
        this.tempPosList = posList;
        this.tempPlayedCol = playedCol;
        this.bombTiles = bombTiles;
    }
}