package com.cheweibao.liuliu.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideLetterDingWeiBar extends View {
  private static final String[] b = {
       "定位", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
      "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
  };
  private int choose = -1;
  private Paint paint = new Paint();
  private boolean showBg = false;
  private OnLetterChangedListener onLetterChangedListener;
  private TextView overlay;

  private int fontColor = Color.parseColor("#8c8c8c");
  private int fontSelectedColor = Color.parseColor("#5c5c5c");

  public SideLetterDingWeiBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public SideLetterDingWeiBar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SideLetterDingWeiBar(Context context) {
    super(context);
  }

  /**
   * 设置悬浮的textview
   */
  public void setOverlay(TextView overlay) {
    this.overlay = overlay;
  }

  public void setFontColor(int fontColor) {
    this.fontColor = fontColor;
    invalidate();
  }

  public void setFontSelectedColor(int fontSelectedColor) {
    this.fontSelectedColor = fontSelectedColor;
    invalidate();
  }

  @SuppressWarnings("deprecation") @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (showBg) {
      canvas.drawColor(Color.TRANSPARENT);
    }

    int height = getHeight();
    int width = getWidth();
    int singleHeight = height / b.length;
    for (int i = 0; i < b.length; i++) {
      paint.setTextSize(30);
      paint.setColor(fontColor);
      paint.setAntiAlias(true);
      if (i == choose) {
        paint.setColor(fontSelectedColor);
      }
      float xPos = width / 2 - paint.measureText(b[i]) / 2;
      float yPos = singleHeight * i + singleHeight;
      canvas.drawText(b[i], xPos, yPos, paint);
      paint.reset();
    }
  }

  @Override public boolean dispatchTouchEvent(MotionEvent event) {
    final int action = event.getAction();
    final float y = event.getY();
    final int oldChoose = choose;
    final OnLetterChangedListener listener = onLetterChangedListener;
    final int c = (int) (y / getHeight() * b.length);

    switch (action) {
      case MotionEvent.ACTION_DOWN:
        showBg = true;
        if (oldChoose != c && listener != null) {
          if (c >= 0 && c < b.length) {
            listener.onLetterChanged(b[c]);
            choose = c;
            invalidate();
            if (overlay != null) {
              overlay.setVisibility(VISIBLE);
              overlay.setText(b[c]);
            }
          }
        }

        break;
      case MotionEvent.ACTION_MOVE:
        if (oldChoose != c && listener != null) {
          if (c >= 0 && c < b.length) {
            listener.onLetterChanged(b[c]);
            choose = c;
            invalidate();
            if (overlay != null) {
              overlay.setVisibility(VISIBLE);
              overlay.setText(b[c]);
            }
          }
        }
        break;
      case MotionEvent.ACTION_UP:
        showBg = false;
        choose = -1;
        invalidate();
        if (overlay != null) {
          overlay.setVisibility(GONE);
        }
        break;
        default:
          break;
    }
    return true;
  }

  public void setOnLetterChangedListener(OnLetterChangedListener onLetterChangedListener) {
    this.onLetterChangedListener = onLetterChangedListener;
  }

  public interface OnLetterChangedListener {
    void onLetterChanged(String letter);
  }
}
