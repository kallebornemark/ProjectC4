package projectc4.c4.client;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import static projectc4.c4.util.C4Color.*;


/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class GameGridViewGroup extends RelativeLayout{

    private GameController gameController;

    private GameGridView gameGridView;
    private GameGridForeground gameGridForeground;
    private RelativeLayout relativeLayout;

    private Tile redTile, yellowTile;

    private Paint paint;

    public GameGridViewGroup(Context context) {
        super(context);
//        init();
    }

    public GameGridViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init();
    }

    public GameGridViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        init();
    }

    public void setGameController(GameController gameController) {
        System.out.println("Setting gameController");
        this.gameController = gameController;
        init();
    }

    public void init(){
        System.out.println("init");

        this.paint = new Paint();

        this.gameGridView = new GameGridView(getContext());
        addView(gameGridView,getWidth(),getHeight());
        gameGridView.setFocusable(true);

        this.redTile = new Tile(getContext());
        addView(redTile, getWidth(), getHeight());
        redTile.setVisibility(INVISIBLE);



        this.relativeLayout = new RelativeLayout(getContext());

        addView(relativeLayout);

        this.gameGridForeground = new GameGridForeground(getContext());
        addView(gameGridForeground);


    }

    public void newMove() {
        System.out.println("animate");
        TranslateAnimation animate = new TranslateAnimation(0,0,0,600);
//        animate.setInterpolator(new AccelerateInterpolator());
        BounceInterpolator bounce = new BounceInterpolator();
        animate.setInterpolator(bounce);
        animate.setDuration(350);
        animate.setFillAfter(true);
        redTile.startAnimation(animate);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        newMove();
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("width: "+ widthMeasureSpec + " height: " + heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        System.out.println("width: "+ getWidth() + " height: " + getWidth());
        System.out.println("onLayout: getChildCount(): " + getChildCount());

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if ( child instanceof Tile){
                System.out.println("TILE");
                child.layout(0, 0, 100, 100);
            } else {
                System.out.println("ELSE");
                child.layout(0, 0, getWidth(), getHeight());
            }

        }
    }

    /**
     *
     */
    public class Tile extends View {

        private int tileSize;
        public Tile(Context context) {
            super(context);
//            paint.setColor(color);
//            this.tileSize = size;
        }

        public Tile(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public Tile(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            System.out.println("TILE ONDRAW");
            Paint paint = new Paint();
            paint.setColor(YELLOW);
            canvas.drawRoundRect(0, 0, getWidth(), getHeight(), 20, 20, paint);
        }
    }

    /**
     *
     */
    private class Test extends View {

        private Paint paint = new Paint();

        public Test(Context context,int color) {
            super(context);
            paint.setColor(color);
            System.out.println("TILE: color: " + color);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRoundRect(0, 0, getWidth(), getHeight(), 20, 20, paint);
        }
    }

}
