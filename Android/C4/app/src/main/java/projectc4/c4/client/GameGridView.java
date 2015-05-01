package projectc4.c4.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import java.util.HashSet;

import projectc4.c4.R;

import static projectc4.c4.util.C4Color.*;
import static projectc4.c4.util.C4Constants.*;

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
    private Drawable clock = getResources().getDrawable(R.drawable.clock);
    private Drawable bomb = getResources().getDrawable(R.drawable.bomb);
    private Drawable colorblind = getResources().getDrawable(R.drawable.colorblind);
    private Drawable extraturn = getResources().getDrawable(R.drawable.extraturn);
    private Drawable shuffle = getResources().getDrawable(R.drawable.shuffle);
    private boolean noColor = false;
    private int counter = 0;
    private Bitmap bitmap;
    private boolean newGame = true, create = true;

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
        strokePaint.setColor(WHITE);
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
          if(create) {
              createBitmap();
              create = false;
          }
        paint.setColor(LIGHTGRAY);
        c.drawRoundRect(offsetX, offsetY, getWidth()-offsetX, getHeight(), 20, 20, paint);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int posX = (col * (sideOfTile + GRIDSPACING)) + offsetX;
                int posY = (row * (sideOfTile + GRIDSPACING)) + offsetY;
                if (array[row][col] == POWERUP_TIME) { //Time icon draw
                    clock.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                    clock.draw(c);
                } else if (array[row][col] == POWERUP_BOMB){
                    bomb.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                    bomb.draw(c);
                } else if (array[row][col] == POWERUP_COLORBLIND){
                    colorblind.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                    colorblind.draw(c);
                } else if (array[row][col] == POWERUP_EXTRATURN){
                    extraturn.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                    extraturn.draw(c);
                } else if (array[row][col] == POWERUP_SHUFFLE){
                    shuffle.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                    shuffle.draw(c);
                }
            }
        }
        updateDisplay();
    }

    public void dropPowerup(int powerup, int col, int[] colSize) {

        if (colSize[col] < gameController.getBoardHeight()) {
            int row = (gameController.getBoardHeight() - 1) - (colSize[col]);
            int posX = (col * (sideOfTile + GRIDSPACING)) + offsetX;
            int posY = (row * (sideOfTile + GRIDSPACING)) + offsetY;
            if (gameController.getGameBoard()[row][col] == 0 && powerup == 30) { //Time icon draw
                clock.setBounds(posX, posY, sideOfTile + posX, sideOfTile + posY);
                clock.draw(c);
                gameController.setElement(row,col,POWERUP_TIME);
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
        if (gameController.getPlayerTurn() == PLAYER1) {
            paint.setColor(RED);
        } else if (gameController.getPlayerTurn() == PLAYER2) {
            paint.setColor(YELLOW);
        }
        int posX = (col * (sideOfTile + GRIDSPACING)) + offsetX;
        int posY = (row * (sideOfTile + GRIDSPACING)) + offsetY;
        c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
        if(noColor) {
            counter++;
            if(counter == 2) {
                drawTiles();
                counter = 0;
            }

        }
        updateDisplay();
    }

    protected void onDraw(Canvas canvas) {
//        if (bitmap==null){
        if(newGame) {
            resetGameBoard(gameController.getGameBoard());
            newGame = false;
        }
//        if (bitmap != null && gameController != null){
        if(!newGame) {
            canvas.save();
            canvas.drawBitmap(bitmap, 0, 0, paint);
            canvas.restore();
        }
    }

    public void setWinningTiles(HashSet<Integer> winningTiles, int[][] gameBoard) {
        int tmpCounter = 0;
        for (int row = 0; row < gameBoard.length; row++) {
            for (int col = 0; col < gameBoard[row].length; col++) {
                int tmpPlayer = gameBoard[row][col];
                int posX = (col * (sideOfTile + GRIDSPACING)) + offsetX;
                int posY = (row * (sideOfTile + GRIDSPACING)) + offsetY;

                if (tmpPlayer != 0) {
                    if (!winningTiles.contains(tmpCounter)) {
                        if (tmpPlayer == PLAYER1) {
                            paint.setColor(REDLOSERTILE);
                        } else if (tmpPlayer == PLAYER2) {
                            paint.setColor(YELLOWLOSERTILE);
                        } else {
                            paint.setColor(LIGHTGRAY);
                        }
                        c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
                    } else {
                        if (tmpPlayer == PLAYER1) {
                            paint.setColor(RED);
                        } else if (tmpPlayer == PLAYER2) {
                            paint.setColor(YELLOW);
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
        paint.setColor(Color.BLACK);
        for (int row = 0; row < gameController.getGameBoard().length; row++) {
            for (int col = 0; col < gameController.getGameBoard()[row].length; col++) {
                int posX = (col * (sideOfTile + GRIDSPACING)) + offsetX;
                int posY = (row * (sideOfTile + GRIDSPACING)) + offsetY;
                if(gameController.getElement(row,col) == PLAYER1 || gameController.getElement(row, col) == PLAYER2) {
                    c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);

                }

            }
        }
        paint = null;
        updateDisplay();
        noColor = true;
    }
    public void drawTiles() {
        Paint paint = new Paint();
        for (int row = 0; row < gameController.getGameBoard().length; row++) {
            for (int col = 0; col < gameController.getGameBoard()[row].length; col++) {
                int posX = (col * (sideOfTile + GRIDSPACING)) + offsetX;
                int posY = (row * (sideOfTile + GRIDSPACING)) + offsetY;
                if(gameController.getElement(row,col) == PLAYER1) {
                    paint.setColor(RED);
                    c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
                }
                else if (gameController.getElement(row, col) == PLAYER2) {
                    paint.setColor(YELLOW);
                    c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
                }

            }
        }
        paint = null;
        noColor = false;
    }

    @Override
    protected void onMeasure(int widthMeasuredSpec, int heightMeasuredSpec) {
        width = MeasureSpec.getSize(widthMeasuredSpec);
        height = MeasureSpec.getSize(heightMeasuredSpec);
        if (gameController != null) {
            // Räkna ut passande storlek för brickan
            sideOfTile = Math.min((((width - GRIDSPACING) / gameController.getBoardWidth()) - GRIDSPACING),
                    (((height - GRIDSPACING) / gameController.getBoardHeight()) - GRIDSPACING));

            // Rita gameBoard mitt i canvasen i x-led
            offsetX = (width - (gameController.getBoardWidth() * (sideOfTile + GRIDSPACING) - GRIDSPACING)) / 2;

            // Rita gameBoard längst ner på canvasen i y-led
            offsetY = (height - (gameController.getBoardHeight() * (sideOfTile + GRIDSPACING)));
        }
//        System.out.println("GGW - getWidth(): " + width + " getHeight(): " + height + "\nsideOfTile: " + sideOfTile);
        setMeasuredDimension(width, height);
    }
}