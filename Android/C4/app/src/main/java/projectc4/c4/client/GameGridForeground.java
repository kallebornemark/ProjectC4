package projectc4.c4.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import c4.utils.C4Color;
import c4.utils.C4Constants;

/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class GameGridForeground extends View {
    private GameController gameController;

    private boolean buttonEnable = true;
    private int sideOfTile;
    private int width;
    private int height;
    private int pointerCol = -1;
    private int rows;
    private int cols;
    private int offsetX;
    private int offsetY;
    private Paint paint;
    private Canvas c;
    private Bitmap bitmap;

    public GameGridForeground(Context context) {
        super(context);
        init();
    }

    public GameGridForeground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameGridForeground(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init(){

        paint = new Paint();
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
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

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap==null){
            paintForeground();
        }
        canvas.save();
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.restore();
    }


    private void paintForeground(){
//        System.gc();
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        c = new Canvas(bitmap);
        paint.setColor(C4Color.BLACK);
        c.drawRoundRect(offsetX - C4Constants.GRIDSPACING, offsetY - C4Constants.GRIDSPACING, offsetX + (cols * (sideOfTile + C4Constants.GRIDSPACING)), offsetY + (rows * (sideOfTile + C4Constants.GRIDSPACING)), 20, 20, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int posX = (col * (sideOfTile + C4Constants.GRIDSPACING)) + offsetX;
                int posY = (row * (sideOfTile + C4Constants.GRIDSPACING)) + offsetY;
                c.drawRoundRect(posX, posY, (sideOfTile + posX), (sideOfTile + posY), 20, 20, paint);
            }
        }

        paint = new Paint();
    }

    public void setButtonEnable(boolean setButtonEnable){
        this.buttonEnable = setButtonEnable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!buttonEnable) {
            return false;
        }
        int action = event.getActionMasked();
        float touchPosX = event.getX();
        float touchPosY = event.getY();
        int x = offsetX;
        int x2 = width - offsetX;
        int y = offsetY - sideOfTile;

        if (gameController.getGameMode() == C4Constants.MATCHMAKING && gameController.getPlayerTurn() == C4Constants.PLAYER2) {
            return false;
        }

        if (touchPosX >= x && touchPosX <= x2 && touchPosY >= y) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
//                    System.out.println("ACTION_DOWN: - x: " + touchPosX + " y: " + touchPosY);

                    int tmpPoniterCol = (int) (touchPosX / (width / gameController.getBoardWidth()));
                    pointerCol = tmpPoniterCol;
                    gameController.changePointerpos(pointerCol);

                    break;
                case MotionEvent.ACTION_MOVE:
//                    System.out.println("ACTION_MOVE: - x: " + touchPosX + " y: " + touchPosY);

                    tmpPoniterCol = (int)(touchPosX / (width / gameController.getBoardWidth()));

//                    System.out.println("tmp col: " + tmpPoniterCol);
                    if (tmpPoniterCol != pointerCol){
                        pointerCol = tmpPoniterCol;
                        gameController.changePointerpos(pointerCol);
                    }

                    break;
                case MotionEvent.ACTION_UP:
//                    System.out.println("ACTION_UP - x: " + touchPosX + " y: " + touchPosY);

                    int newMoveCol = (int) (touchPosX / (width / gameController.getBoardWidth()));
                    gameController.newMove(newMoveCol, false);

                    break;
                case MotionEvent.ACTION_CANCEL:
//                    System.out.println("ACTION_CANCEL - x: " + touchPosX + " y: " + touchPosY);
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasuredSpec, int heightMeasuredSpec) {
        width = MeasureSpec.getSize(widthMeasuredSpec);
        height = MeasureSpec.getSize(heightMeasuredSpec);

        if (gameController != null) {
            rows = gameController.getBoardHeight();
            cols = gameController.getBoardWidth();

            // Räkna ut passande storlek för brickan
            sideOfTile = Math.min((((width - C4Constants.GRIDSPACING) / gameController.getBoardWidth()) - C4Constants.GRIDSPACING),
                    (((height - C4Constants.GRIDSPACING) / gameController.getBoardHeight()) - C4Constants.GRIDSPACING));

            // Rita gameBoard mitt i canvasen i x-led
            offsetX = (width - (gameController.getBoardWidth() * (sideOfTile + C4Constants.GRIDSPACING) - C4Constants.GRIDSPACING)) / 2;

            // Rita gameBoard längst ner på canvasen i y-led
            offsetY = (height - (gameController.getBoardHeight() * (sideOfTile + C4Constants.GRIDSPACING)));
        }
//        System.out.println("GGF - width: " + width + " height: " + height + "\nsideOfTile: " + sideOfTile);
        setMeasuredDimension(width, height);
    }
}
