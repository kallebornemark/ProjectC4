package projectc4.c4.client;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import c4.utils.C4Color;
import c4.utils.C4Constants;

/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class GameGridAnimation extends RelativeLayout {
    private GameController gameController;

    private Tile redTile;
    private Tile yellowTile;

    private int sideOfTile;
    private int offsetX;
    private int offsetY;

    TranslateAnimation animate;

    private BounceInterpolator bounce;

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
        init();
    }

    public void init(){
        bounce = new BounceInterpolator();

        this.redTile = new Tile(getContext(), C4Color.RED);
        addView(redTile);
        redTile.setVisibility(INVISIBLE);

        this.yellowTile = new Tile(getContext(), C4Color.YELLOW);
        addView(yellowTile);
        yellowTile.setVisibility(INVISIBLE);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isInEditMode()) {
            sideOfTile = Math.min((((getWidth() - C4Constants.GRIDSPACING) / gameController.getBoardWidth()) - C4Constants.GRIDSPACING),
                    (((getHeight() - C4Constants.GRIDSPACING) / gameController.getBoardHeight()) - C4Constants.GRIDSPACING));
            offsetX = (getWidth() - (gameController.getBoardWidth() * (sideOfTile + C4Constants.GRIDSPACING) - C4Constants.GRIDSPACING)) / 2;
            offsetY = (getHeight() - (gameController.getBoardHeight() * (sideOfTile + C4Constants.GRIDSPACING)));

            System.out.println("AMIN: w:" + getWidth() + " h: " + getHeight()+ " sideoftile: " + sideOfTile);

            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if ( child instanceof Tile){
                    child.layout(0, 0, sideOfTile, sideOfTile);
                }
            }
        }
    }


    public void newMove(int row, int col, final boolean isIncoming) {
        int xPos = offsetX + (col * (C4Constants.GRIDSPACING + sideOfTile));
        int yStop = offsetY + (row * (C4Constants.GRIDSPACING + sideOfTile));

        animate = new TranslateAnimation(xPos,xPos,offsetY-sideOfTile,yStop);
        animate.setInterpolator(bounce);
        animate.setDuration(250);
        animate.setFillAfter(false);
        animate.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animate) {}
            public void onAnimationRepeat(Animation animate) {}
            public void onAnimationEnd(Animation animate) {
                gameController.finishMove(isIncoming);
            }
        });
        if (gameController.getPlayerTurn() == C4Constants.PLAYER1) {
            redTile.startAnimation(animate);
        } else if (gameController.getPlayerTurn() == C4Constants.PLAYER2) {
            yellowTile.startAnimation(animate);
        }
    }

    /**
     * Privat klass
     */
    private class Tile extends View {

        private Paint paint = new Paint();

        public Tile(Context context,int color) {
            super(context);
            paint.setColor(color);
        }

        public Tile(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public Tile(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRoundRect(0, 0, getWidth(), getHeight(), 20, 20, paint);
        }
    }
}
