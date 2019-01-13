package carraydraw.com.coutdowntimelibrary.countdown;

import android.view.View;
import android.widget.TextView;

/**
 * Created by suining on 2016/6/23 0023.
 * 毫秒左边慢的倒计时
 */
public class CoutDownMillisSecondSlow extends CoutDownParent{
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private View viewTime;
    private View view;
    public CoutDownMillisSecondSlow(long millisInFuture, long countDownInterval,View viewTime) {
        super(millisInFuture, countDownInterval);
        this.viewTime=viewTime;
        this.view=view;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(viewTime instanceof TextView){
            ((TextView) viewTime).setText(":"+String.valueOf(millisUntilFinished).substring(0,1));
        }
    }

    @Override
    public void onFinish() {
     start();
    }
}
