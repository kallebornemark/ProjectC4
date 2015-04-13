package projectc4.c4.client;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import static projectc4.c4.util.C4Color.*;
import static projectc4.c4.util.C4Constants.*;

/**
 * @author Jimmy Maksymiw
 */
public class GameGridView extends View {

    private int[][] gameBoard;

    // För att nå GameGridAnimation & GameGridForeground
    private GameGridAnimation gameGridAnimation;
    private GameGridForeground gameGridForeground;

    private int sideOfTile;
    private int offsetX;
    private int offsetY;

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

    public void addViews(GameGridAnimation gameGridAnimation, GameGridForeground gameGridForeground) {
        gameGridForeground.setFocusable(true);
        gameGridAnimation.setFocusable(true);

        this.gameGridAnimation = gameGridAnimation;
        this.gameGridForeground = gameGridForeground;
        gameGridAnimation.addView(this);
        gameGridForeground.addView(this);
    }

    public void init() {
        gameBoard = new int[6][7];
        updateDisplay();
    }

    public int getElement(int i, int j) {
        return gameBoard[i][j];
    }

    public int getBoardWidth() {
        return gameBoard[1].length;
    }

    public int getBoardHeight() {
        return gameBoard.length;
    }

    public void reset() {
        gameBoard = new int[6][7];
        updateDisplay();
    }

    public void setElement(int row, int col, int player) {
        this.gameBoard[row][col] = player;
        gameGridAnimation.animateNewMove(col, row, player);
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

    private void calculateSize() {
        // Räkna ut passande storlek för brickan
        sideOfTile = Math.min((((getWidth() - GRIDSPACING) / gameBoard[0].length) - GRIDSPACING),
                (((getHeight() - GRIDSPACING) / gameBoard.length) - GRIDSPACING));

        // Rita gameBoard mitt i canvasen i x-led
        offsetX = (getWidth() - (gameBoard[0].length * (sideOfTile + GRIDSPACING) - GRIDSPACING)) / 2;

        // Rita gameBoard längst ner på canvasen i y-led
        offsetY = (getHeight() - (gameBoard.length * (sideOfTile + GRIDSPACING)));

        //Todo sätta dessa automatiskt utan att få nullpointer på smidigt sätt?
        //Skicka vidare uträkningen till GameGridAnimation & GameGridForeground
        if ((gameGridAnimation!=null)) {
            gameGridForeground.setSize(offsetX, offsetY, sideOfTile, gameBoard.length, gameBoard[0].length);
            gameGridAnimation.setSize(offsetX, offsetY, sideOfTile);
            setGameGridForeground();
        }
    }



    protected void onDraw(Canvas canvas) {
        calculateSize();
        Paint paint = new Paint();
        for (int row = 0; row < gameBoard.length; row++) {
            for (int col = 0; col < gameBoard[row].length; col++) {

                if (gameBoard[row][col] == 0) {
                    paint.setColor(LIGHTGRAY);
                } else if (gameBoard[row][col] == PLAYER1) {
                    paint.setColor(RED);
                } else if (gameBoard[row][col] == PLAYER2) {
                    paint.setColor(YELLOW);
                }

                int posX = (col * (sideOfTile + GRIDSPACING)) + offsetX;
                int posY = (row * (sideOfTile + GRIDSPACING)) + offsetY;
                canvas.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
            }
        }
    }

    public void setGameGridForeground() {
        gameGridForeground.paintForeground();
    }

    //Todo skicka in vinnande brickor på ett smidigt sätt
    public void setWinningTiles() {

    }

    //Todo visa pekare efter position, måste finslipas
    private int tmpPos = -1;
    public void animatePointer(int pointerPos){
        if (tmpPos == -1){
            tmpPos = pointerPos;
            gameGridAnimation.animatePointer(pointerPos);
        }
        if (pointerPos < tmpPos-15 || pointerPos > tmpPos+15 ){
            gameGridAnimation.animatePointer(pointerPos);
            tmpPos = pointerPos;
        }
    }

    @Override
    protected void onMeasure(int widthMeasuredSpec, int heightMeasuredSpec) {
        int w = MeasureSpec.getSize(widthMeasuredSpec);
        int h = MeasureSpec.getSize(heightMeasuredSpec);
        System.out.println("ggView - w: " + w + " h: " + h );
        setMeasuredDimension(w, h);
    }
}