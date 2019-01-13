package com.cheweibao.liuliu.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by felix on 15/4/24.
 */
public class QuickIndexBar extends View {

    public QuickIndexBar(Context context) {
        super(context);
        init();
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //以26个字母作为索引
    String[] indexs = new String[]{"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z"};

    private Paint paint;
    private int cellWidth, cellHeight;

    private void init() {
        paint = new Paint();
        paint.setTextSize(30);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (cellHeight == 0) cellHeight = getMeasuredHeight() / indexs.length;
        if (cellWidth == 0) cellWidth = getMeasuredWidth();

        //分别画26个字母
        for (int i = 0; i < indexs.length; i++) {

            if (i == lastIndex) {               //正在touch的位置 需要更加人性化的交互
                paint.setTextSize(80);
                paint.setColor(Color.rgb(0x0d, 0xbd, 0xb2));
            } else {
                paint.setTextSize(30);
                paint.setColor(Color.WHITE);
            }

            //先测量用此画笔画字母的大小，用一个矩形把它包裹起来，这样方便计算字母的高度
            Rect bounds = new Rect();
            paint.getTextBounds(indexs[i], 0, indexs[i].length(), bounds);

            //计算画每个字母的起始坐标
            float x = cellWidth / 2 - paint.measureText(indexs[i]) / 2;
            float y = cellHeight / 2 + bounds.height() / 2 + i * cellHeight;


            canvas.drawText(indexs[i], x, y, paint);
        }

    }

    private int lastIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
//        if (x >= 0 && x <= cellWidth) {  //只对quickindexbar的触摸事件有效
//            return false;
//        }

        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (isSameIndex(y / cellHeight)) break;

                //安全检查
                if (y >= 0 && (y / cellHeight) < indexs.length) {
                    String word = indexs[((int) (y / cellHeight))];
                    lastIndex = y / cellHeight;
                    if (mIndexChangedListener != null) {
                        mIndexChangedListener.indexChanged(word);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                lastIndex = -1;
                break;
            default:
                break;
        }
        //重新调用onDraw
        invalidate();

        //自行处理触摸事件，不向上传递
        return true;
    }

    /**
     * 当前的索引位置是否和上一个相等
     */
    private boolean isSameIndex(int currIndex) {
        return lastIndex == currIndex;
    }


    private IndexChangedListener mIndexChangedListener;

    public void setIndexChangedListener(IndexChangedListener indexChangedListener) {
        mIndexChangedListener = indexChangedListener;
    }

    public interface IndexChangedListener {
        void indexChanged(String word);
    }
}
