package projectc4.c4.client;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Jimmy Maksymiw
 *
 */
public class GridView extends ViewGroup{

    private PaintForeground paintForeground;

    private int width;
    private int height;


    public GridView(Context context) {
        super(context);
        init();
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        paintForeground = new PaintForeground(getContext());

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        System.out.println("GridView - w: " + width + " h: " + height );
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        System.out.println("onLayout - w: " + width + " h: " + height );
    }


    private class Animation {

    }

    private class PaintForeground extends View {

        public PaintForeground(Context context) {
            super(context);
        }
    }
}
