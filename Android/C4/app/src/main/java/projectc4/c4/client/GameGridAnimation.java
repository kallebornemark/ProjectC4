package projectc4.c4.client;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;

import static projectc4.c4.util.C4Color.*;
import static projectc4.c4.util.C4Constants.*;

/**
 * Created by Jimmy on 2015-04-15.
 */
public class GameGridAnimation extends View {
    private GameController gameController;

    private boolean animateNewMove = false;
    private int offsetX;
    private int offsetY;
    private int sideOfTile;
    private int width;
    private int height;

    private int col;
    private int rowStop;
    private int currentPosY ;
    private int pointerPos;
    private Paint paint = new Paint();

    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = true;

            if (needNewFrame) {
                if (pointerPos > getHeight()){
                    pointerPos = 0;
                } else {
                    pointerPos +=20;
                }
                System.out.println("inne i runnable");
                postDelayed(this, 15);
                invalidate();

            }
        }
    };

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

    public void testAnimation(Canvas canvas){
//    paint.setColor(YELLOW);
        int x = offsetX + ((GRIDSPACING + sideOfTile) * col);
        int x2 = offsetX + sideOfTile + ((GRIDSPACING + sideOfTile) * col);
        int y2 = pointerPos + sideOfTile;

        canvas.drawRoundRect(x, pointerPos, x2, y2, 20, 20, paint);

    }

    public void newMove(int row, int col, int player) {
        if (player == PLAYER1) {
            paint.setColor(RED);
        } else if (player == PLAYER2) {
            paint.setColor(YELLOW);
        }
        this.rowStop = row;
        this.col = col;
        animateNewMove = true;
        removeCallbacks(animator);
        post(animator);
    }

    private void drawTile(Canvas canvas){
        if (gameController.getPlayerTurn() == PLAYER1) {
            paint.setColor(RED);
        } else {
            paint.setColor(YELLOW);
        }
        int x = offsetX + ((GRIDSPACING + sideOfTile) * col) ;
        int x2 = offsetX + sideOfTile + ((GRIDSPACING + sideOfTile) * col);
        int y2= pointerPos+sideOfTile;

        canvas.drawRoundRect(x, pointerPos, x2, y2, 20, 20, paint);
    }

    protected void onDraw(Canvas canvas) {
        if (animateNewMove) {
            canvas.save();
//            drawTile(canvas);
            testAnimation(canvas);
            canvas.restore();
        }
    }

    @Override
    protected void onMeasure(int widthMeasuredSpec, int heightMeasuredSpec) {
        width = View.MeasureSpec.getSize(widthMeasuredSpec);
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
//        System.out.println("GGF - width: " + width + " height: " + height + "\nsideOfTile: " + sideOfTile);
        setMeasuredDimension(width, height);
    }
}
