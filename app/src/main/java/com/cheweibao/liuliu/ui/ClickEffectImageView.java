package com.cheweibao.liuliu.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.cheweibao.liuliu.R;


public class ClickEffectImageView extends ImageView {

	boolean m_bOutOf = false;
	
	private Rect rect;    // Variable rect to hold the bounds of the view
	
	public int maskColor = 0x30000000;
	
	public ClickEffectImageView(Context context) {
        super(context);
    }
	
	public ClickEffectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
	}
	
	public ClickEffectImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// get custom attrs
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClickEffectImageView);
		if (a != null){
			maskColor = a.getColor(R.styleable.ClickEffectImageView_ceivMaskColor, maskColor);
		}
		
/*		setClickable(true);
		setEnabled(true);
		setFocusable(true);
		setFocusableInTouchMode(true);*/
	}

	public void setMaskColor(int color){
		maskColor = color;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.getDrawable()==null) return false;
		switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            //overlay is black with transparency of 0x77 (119)
            this.getDrawable().setColorFilter(maskColor,android.graphics.PorterDuff.Mode.SRC_ATOP);
            this.invalidate();
            rect = new Rect(getLeft(), getTop(), getRight(), getBottom());
            m_bOutOf = false;
            return true;
            
        case MotionEvent.ACTION_CANCEL:
            //clear the overlay
            this.getDrawable().clearColorFilter();
            this.invalidate();
            return true;
        case MotionEvent.ACTION_MOVE:
            //clear the overlay
        	if (m_bOutOf)  	return false;
        	
        	if(!rect.contains(getLeft() + (int) event.getX(), getTop() + (int) event.getY())){
        		this.getDrawable().clearColorFilter();
        		this.invalidate();
        		m_bOutOf = true;
        	}
        	return true;
        case MotionEvent.ACTION_UP:
            //clear the overlay
            this.getDrawable().clearColorFilter();
            this.invalidate();
            if (m_bOutOf == false) 
            	performClick();
            return true;
		}
		
		return false;
	}
}
