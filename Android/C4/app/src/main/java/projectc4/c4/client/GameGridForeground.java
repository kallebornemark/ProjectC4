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
import static projectc4.c4.util.C4Color.*;
import static projectc4.c4.util.C4Constants.*;

/**
 * @author Jimmy Maksymiw
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
    private Bitmap bitmap;
    private Paint paint;

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

    private  void paintForeground(){
//        System.out.println("GGF - create bitmap - Width: " + width + " Height: " + height);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        paint.setColor(BLACK);
        c.drawRoundRect(offsetX - GRIDSPACING, offsetY - GRIDSPACING, offsetX + (cols * (sideOfTile + GRIDSPACING)), offsetY + (rows * (sideOfTile + GRIDSPACING)), 20, 20, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                int posX = (col * (sideOfTile + GRIDSPACING)) + offsetX;
                int posY = (row * (sideOfTile + GRIDSPACING)) + offsetY;

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
        int action = event.getActionMasked();
        float touchPosX = event.getX();
        float touchPosY = event.getY();
        int x = offsetX;
        int x2 = width - offsetX;
        int y = offsetY - sideOfTile;

        if (!buttonEnable) {
            return false;
        }
        //Todo ändra så att isEnabled används istället och sätts från gameController.
        if (gameController.getGameMode() == MATCHMAKING && gameController.getPlayerTurn() == PLAYER2) {
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

                    //Todo ta bort markör eller?
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
