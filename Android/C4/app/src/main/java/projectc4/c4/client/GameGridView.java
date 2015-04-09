package projectc4.c4.client;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * @author Jimmy Maksymiw
 */
public class GameGridView extends View {

//    läggs in i onCreate
//    gameGridView = (GameGridView)findViewById(R.id.gameGridView);
//    gameGridView.setFocusable(true);
//    gameGridView.addViews((GameGridAnimation)findViewById(R.id.gameGridAnimation), (GameGridForeground)findViewById(R.id.gameGridForeground));


    private int[][] gameBoard = new int[7][6];


    // För att nå GameGridAnimation & GameGridForeground
    private GameGridAnimation gameGridAnimation;
    private GameGridForeground gameGridForeground;

    //Todo ändra till konstanter
    private int colorPlayer1 = 0xFFF67E59, colorPlayer2 = 0xFFECD06C;

    private int gridSpacing = 10;
    private int sideOfTile;
    private int offsetX;
    private int offsetY;

    public GameGridView(Context context) {
        super(context);
    }

    public GameGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addViews(GameGridAnimation gameGridAnimation, GameGridForeground gameGridForeground) {
        gameGridForeground.setFocusable(true);
        gameGridAnimation.setFocusable(true);

        this.gameGridAnimation = gameGridAnimation;
        this.gameGridForeground = gameGridForeground;
        gameGridAnimation.addView(this);
        gameGridForeground.addView(this);
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
        gameBoard = new int[7][6];
    }

    //För testning
    public void setRandom() {
        Random rnd = new Random();
        int[][] tmp = this.gameBoard;
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                tmp[i][j] = rnd.nextInt(3);
            }
        }
        setDisplay(tmp);
    }

    //För testning
    public void setRandomBoard() {
        Random rnd = new Random();
        gameBoard = new int[rnd.nextInt(6) + 5][rnd.nextInt(6) + 5];
        updateDisplay();
    }

    public void setDisplay(int[][] board) {
        this.gameBoard = board;
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
        sideOfTile = Math.min((((getWidth() - gridSpacing) / gameBoard[0].length) - gridSpacing),
                (((getHeight() - gridSpacing) / gameBoard.length) - gridSpacing));

        // Rita gameBoard mitt i canvasen i x-led
        offsetX = (getWidth() - (gameBoard[0].length * (sideOfTile + gridSpacing) - gridSpacing)) / 2;

        // Rita gameBoard längst ner på canvasen i y-led
        offsetY = (getHeight() - (gameBoard.length * (sideOfTile + gridSpacing)));

        //Todo sätta dessa automatiskt utan att få nullpointer på smidigt sätt?
        //Skicka vidare uträkningen till GameGridAnimation & GameGridForeground
        if ((gameGridAnimation!=null) && (gameGridForeground!=null)) {
            gameGridForeground.setSize(offsetX, offsetY, sideOfTile, gridSpacing, gameBoard.length, gameBoard[0].length);
            gameGridAnimation.setSize(offsetX, offsetY, sideOfTile, gridSpacing);
            setGameGridForeground();
        }
    }

    protected void onDraw(Canvas canvas) {
        calculateSize();

        Paint paint = new Paint();

        RectF rect = new RectF(offsetX - gridSpacing, offsetY - gridSpacing, offsetX + (gameBoard[0].length * (sideOfTile + gridSpacing)), offsetY + (gameBoard.length * (sideOfTile + gridSpacing)));
        //Todo sätta rätt gridspacingfärg
        paint.setColor(Color.DKGRAY);
        canvas.drawRoundRect(rect, 20, 20, paint);
        System.out.println("____________________________________________________________________________________");
        System.out.println("STORLEK:\nwidth: " + getWidth() + "\nHeight: " + getHeight() + "\nsideOfTile: " + sideOfTile + "\noffsetX: " + offsetX + "\noffsetY: " + offsetY);

        for (int row = 0; row < gameBoard.length; row++) {
            for (int col = 0; col < gameBoard[row].length; col++) {

                //Todo sätta dit rätt konstanter för färger
                if (gameBoard[row][col] == 0) {
                    paint.setColor(0xFFa59484);
                } else if (gameBoard[row][col] == 1) {
                    paint.setColor(colorPlayer1);
                } else if (gameBoard[row][col] == 2) {
                    paint.setColor(colorPlayer2);
                }

                int posX = (col * (sideOfTile + gridSpacing)) + offsetX;
                int posY = (row * (sideOfTile + gridSpacing)) + offsetY;
                //Todo uträkning i temporära variabler istället
                rect = new RectF(posX, posY, (sideOfTile + posX), (sideOfTile + posY));
                canvas.drawRoundRect(rect, 20, 20, paint);
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
            System.out.println("WAAH?");
            gameGridAnimation.animatePointer(pointerPos);
            tmpPos = pointerPos;
        }
    }


    //För testning
    public void randomAnimation() {
        Random rnd = new Random();
        gameGridAnimation.animateNewMove(rnd.nextInt(gameBoard[0].length), rnd.nextInt(gameBoard.length), rnd.nextInt(2) + 1);
    }

    public void randomAnimation(int col) {
        System.out.println("random animation col: " + col);
        tmpPos = -1;
        Random rnd = new Random();
        gameGridAnimation.animateNewMove(col, rnd.nextInt(gameBoard.length), rnd.nextInt(2) + 1);
    }

    @Override
    protected void onMeasure(int widthMeasuredSpec, int heightMeasuredSpec) {
        int w = MeasureSpec.getSize(widthMeasuredSpec);
        int h = MeasureSpec.getSize(heightMeasuredSpec);
        System.out.println("w: " + w + " h: " + h );
        setMeasuredDimension(w, h);
    }
}