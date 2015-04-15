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
    private int sideOfTile;
    private int width;
    private int height;

    //Todo bara hämta rows/cols från Gameconroller, ej sätta som instansvariabler.
    private int rows;
    private int cols;

    private int offsetX;
    private int offsetY;
    private Paint paint;
    private Bitmap bitmap;

    public GameGridForeground(Context context) {
        super(context);
    }

    public GameGridForeground(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameGridForeground(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        System.out.println("GGF - create bitmap - Width: " + width + " Height: " + height);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        paint = new Paint();
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
        canvas.drawBitmap(bitmap, 0, 0, paint);

    }

    public void paintForeground(){
        updateDisplay();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        float touchPosY;
        float touchPosX;
        int x = offsetX + GRIDSPACING;
        int x2 = getWidth() - offsetX - GRIDSPACING;
        int y = offsetY - GRIDSPACING - sideOfTile;
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                //Todo Ändra position på markör
                break;
            case MotionEvent.ACTION_MOVE:
                touchPosX = event.getX();
                touchPosY = event.getY();
                if (touchPosX >= x && touchPosX <= x2 && touchPosY >= y) {
//                    System.out.println("ACTION_MOVE: x: " + event.getX() + "    -    y: " + event.getY());
//                    gameGridView.animatePointer((int)touchPosX);
                }
                break;
            case MotionEvent.ACTION_UP:

                touchPosX = event.getX();
                touchPosY = event.getY();

//                System.out.println("ACTION_UP: x: " + touchPosX + "    -    y: " + touchPosY);

                //kontrollerar att klicket är på själva spelbrädet eller en sideofTile över.
                if (touchPosX >= x && touchPosX <= x2 && touchPosY >= y) {
                    //Kollar igenom alla kolumner efter klick.
                    for (int col = 0; col < gameController.getBoardWidth(); col++) {
                        //Om x positionen är i just denna kulumnen körs performClick och bricka läggs.
                        if (touchPosX >= offsetX + GRIDSPACING + ((GRIDSPACING + sideOfTile) * col) && touchPosX <= offsetX + GRIDSPACING + sideOfTile + ((sideOfTile + GRIDSPACING) * col)) {
//                            System.out.println("onTouchEvent: touchPosX: " + touchPosX + "    -    col = " + (col));
//                            System.out.println("gameController.newOutgoingMove(col): " + col);
                            gameController.newMove(col, false);
                            break;
                        }
                    }
                } else {
//                    System.out.println("onTouchEvent: width: " + getWidth() + "    -    touchPosX: " + touchPosX + "    -    touchPosy: " + touchPosY);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                //Todo ta bort markör eller?
                break;
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
        System.out.println("GGF - width: " + width + " height: " + height + "\nsideOfTile: " + sideOfTile);
        setMeasuredDimension(width, height);
    }
}
