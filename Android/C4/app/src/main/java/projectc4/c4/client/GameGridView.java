package projectc4.c4.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import java.util.HashSet;

import static projectc4.c4.util.C4Color.*;
import static projectc4.c4.util.C4Constants.*;

/**
 * @author Jimmy Maksymiw
 */
public class GameGridView extends View {

    private GameController gameController;

    private int sideOfTile;
    private int offsetX;
    private int offsetY;
    private int width;
    private int height;
    private Paint paint;
    private Bitmap bitmap;
    private Canvas c;

    public GameGridView(Context context) {
        super(context);
    }

    public GameGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
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
        this.bitmap = null;
        updateDisplay();
    }

    public void resetGameBoard(){
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        c = new Canvas(bitmap);
        paint = new Paint();
        Paint paint = new Paint();
        paint.setColor(LIGHTGRAY);
        c.drawRoundRect(offsetX, offsetY, width-offsetX, height, 20, 20, paint);
    }

    public void newMove(int row, int col, int player) {
        if (player == PLAYER1) {
            paint.setColor(RED);
        } else if (player == PLAYER2) {
            paint.setColor(YELLOW);
        }
        int posX = (col * (sideOfTile + GRIDSPACING)) + offsetX;
        int posY = (row * (sideOfTile + GRIDSPACING)) + offsetY;
        c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
        updateDisplay();
        gameController.setElement(row, col, player);
    }



    protected void onDraw(Canvas canvas) {
        if (bitmap==null){
            resetGameBoard();
        }
        if (bitmap != null && gameController != null){
            canvas.save();
            canvas.drawBitmap(bitmap, 0, 0, paint);
            canvas.restore();
        }
    }

    public void setWinningTiles(HashSet<Integer> winningTiles, int[][] gameBoard) {
        Paint erasePaint = new Paint();
        erasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        c.drawRect(0, 0, width, height, erasePaint);

        int tmpCounter = 0;
        for (int row = 0; row < gameBoard.length; row++) {
            for (int col = 0; col < gameBoard[row].length; col++) {
                int tmpPlayer = gameBoard[row][col];
                int posX = (col * (sideOfTile + GRIDSPACING)) + offsetX;
                int posY = (row * (sideOfTile + GRIDSPACING)) + offsetY;

                if (!winningTiles.contains(tmpCounter)) {
                    if (tmpPlayer == PLAYER1) {
                        paint.setColor(REDLOSERTILE);
                    } else if (tmpPlayer == PLAYER2) {
                        paint.setColor(YELLOWLOSERTILE);
                    } else {
                        paint.setColor(LIGHTGRAY);
                    }
                } else {
                    if (tmpPlayer == PLAYER1) {
                        paint.setColor(RED);
                    } else if (tmpPlayer == PLAYER2) {
                        paint.setColor(YELLOW);
                    }
                }
                c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
                tmpCounter++;
            }
        }
        paint.reset();
        updateDisplay();
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