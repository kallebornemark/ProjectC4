package projectc4.c4.client;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import static projectc4.c4.util.C4Color.*;
import static projectc4.c4.util.C4Constants.*;

/**
 * @author Jimmy Maksymiw
 */
public class GameGridAnimation extends View {

    //Todo ta bort denna och allt som har med denna att göra
    private GameGridView gameGridView;

    private GameController gameController;
    private boolean animateNewMove = false;
    private boolean animatePointer = false;
    private int offsetX;
    private int offsetY;
    private int sideOfTile;

    private int width;
    private int height;

    //Todo bara hämta rows/cols från Gameconroller, ej sätta som instansvariabler.
    private int rows;
    private int cols;

    private int col;
    private int player = PLAYER2;
    private int rowStop;
    private int currentPosY;
    private int pointerPos;

    public GameGridAnimation(Context context) {
        super(context);
    }

    public GameGridAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameGridAnimation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void addView(GameGridView gameGridView){
        this.gameGridView = gameGridView;
    }

    public void setSize(int offsetX, int offsetY, int sideOfTile){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.sideOfTile = sideOfTile;
    }

    private void updateDisplay() {
        this.post(new Runnable() {
            public void run() {
                superInvalidate();
            }
        });
    }

    private void superInvalidate() {
        super.invalidate();
    }

    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();

        RectF rect;
        if (animatePointer) {
            if (player == PLAYER1) {
                paint.setColor(YELLOW);
            } else if (player == PLAYER2) {
                paint.setColor(RED);
            }
            rect = new RectF(pointerPos-(sideOfTile/2), offsetY-sideOfTile, (sideOfTile + pointerPos)-(sideOfTile/2), offsetY);
            canvas.drawRoundRect(rect, 20, 20, paint);
            animatePointer = false;

        } else if (animateNewMove) {
            if (player == PLAYER1) {
                paint.setColor(RED);
            } else if (player == PLAYER2) {
                paint.setColor(YELLOW);
            }

            rect = new RectF(col, currentPosY, (sideOfTile + col), (sideOfTile + currentPosY));
            canvas.drawRoundRect(rect, 20, 20, paint);

            if (currentPosY < rowStop*(sideOfTile+ GRIDSPACING) + offsetY  ){
                currentPosY+=25;
                if (currentPosY >= rowStop*(sideOfTile+ GRIDSPACING) + offsetY ){
                    currentPosY = rowStop*(sideOfTile+ GRIDSPACING)+ offsetY;
                    animateNewMove = false;
//                    gameGridView.updateDisplay();
//                    gameGridView.setElement(rowStop, colStart, player);
                }
                updateDisplay();
            }
        }


    }

    public void animatePointer(int pointerPos){
        this.pointerPos = pointerPos;
        this.animatePointer = true;
        updateDisplay();
    }


    public void animateNewMove(int colStart, int rowStop, int player){
        this.col = ((colStart) * (sideOfTile + GRIDSPACING)) + offsetX;
        this.rowStop = rowStop;
        this.player = player;
        this.currentPosY = offsetY-(sideOfTile/2);
        this.animateNewMove = true;

        updateDisplay();
    }

    @Override
    protected void onMeasure(int widthMeasuredSpec, int heightMeasuredSpec) {
        width = MeasureSpec.getSize(widthMeasuredSpec);
        height = MeasureSpec.getSize(heightMeasuredSpec);

        rows = gameController.getBoardHeight();
        cols = gameController.getBoardWidth();

        // Räkna ut passande storlek för brickan
        sideOfTile = Math.min((((width - GRIDSPACING) / gameController.getBoardWidth()) - GRIDSPACING),
                (((height - GRIDSPACING) / gameController.getBoardHeight()) - GRIDSPACING));

        // Rita gameBoard mitt i canvasen i x-led
        offsetX = (width - (gameController.getBoardWidth() * (sideOfTile + GRIDSPACING) - GRIDSPACING)) / 2;

        // Rita gameBoard längst ner på canvasen i y-led
        offsetY = (height - (gameController.getBoardHeight() * (sideOfTile + GRIDSPACING)));

        System.out.println("GGA - width: " + width + " height: " + height + "\nsideOfTile: " + sideOfTile);
        setMeasuredDimension(width, height);
    }
}
