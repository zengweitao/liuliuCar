package carraydraw.com.coutdowntimelibrary.countdown;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import carraydraw.com.coutdowntimelibrary.R;


/**
 * Created by suining on 2016/6/23 0023.
 */
public class CountDownView extends LinearLayout{

    private Context context;
    TextView timeView;//时间view
    TextView milLowView;//毫秒慢的view
    TextView milview;//毫秒快的view
    private int textCorlor;
    private int textSize;
    private  CountDownUtil countDownUtil;
    public CountDownView(Context context) {
        this(context,null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
      this(context, attrs,0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.CountDownView,defStyleAttr,0);
        textCorlor=array.getColor(R.styleable.CountDownView_countTextDownColor,0xfff36e21);
        textSize=array.getDimensionPixelSize(R.styleable.CountDownView_countTextDownSize,
                DisplayUtil.sp2px(context,10));
        array.recycle();
        init();
    }

    private void init() {

        timeView =new TextView(context);
        timeView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        timeView.setText("");
        timeView.setTextColor(textCorlor);
        timeView.setTextSize(textSize);
        addView(timeView);
        milLowView =new TextView(context);
        milLowView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        milLowView.setText("");
        milLowView.setTextColor(textCorlor);
        milLowView.setTextSize(textSize);
        addView(milLowView);
        milview =new TextView(context);
        milview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        milview.setText("");
        milview.setTextColor(textCorlor);
        milview.setTextSize(textSize);
        addView(milview);
    }
    //毫秒倒计时
    public void setTime(Long time){
        if(time<0){
            return;
        }
        countDownUtil =new CountDownUtil(context,time,null,timeView,milLowView,milview);
        countDownUtil.setMinuteAddsSecond();
        countDownUtil.setMillisecond();
    }

    public void finshTime(CountDownUtil.CallTimeFinshBack timeFinshBack){
        countDownUtil.finshTime(timeFinshBack);
    }
    public void cancelTime(){
        if(countDownUtil!=null){
            countDownUtil.cancelTime();
        }
    }
    public void setHourtMinuteAddsSecond(Long time){
        if(time<0){
            return;
        }
        countDownUtil =new CountDownUtil(context,time,null,timeView,milLowView,milview);
        countDownUtil.setHourandMinuteandSecond();
    }
    public void cancelHourtMinuteAddsSecondTime(){
        if(countDownUtil!=null){
            countDownUtil.cancelHourMinuteAddsSecondTime();
        }
    }
    public void finshHourtMinuteAddsSecondTime(CountDownUtil.CallTimeFinshBack timeFinshBack){
        if(countDownUtil!=null){
            countDownUtil.finshTime(timeFinshBack);
        }
    }
}
