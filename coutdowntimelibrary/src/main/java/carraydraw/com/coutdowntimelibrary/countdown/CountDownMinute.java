package carraydraw.com.coutdowntimelibrary.countdown;

import android.view.View;
import android.widget.TextView;

/**
 * Created by suining on 2016/6/30 0030.
 * 分钟倒计时
 */
public class CountDownMinute extends CoutDownParent{

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private View minuteView;
    public CountDownMinute(long millisInFuture, long countDownInterval,View minuteView) {
        super(millisInFuture, countDownInterval);
        this.minuteView=minuteView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(minuteView instanceof TextView){
            ((TextView) minuteView).setText(dealTime(millisUntilFinished/1000));
        }
    }

    @Override
    public void onFinish() {

    }
    /**
     * deal time string
     *
     * @param time
     * @return
     */
    public static String dealTime(long time) {
        StringBuffer returnString = new StringBuffer();
        long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
        String minutesStr = timeStrFormat(String.valueOf(minutes));
        returnString.append(minutesStr);
        return returnString.toString();
    }
    /**
     * format time
     *
     * @param timeStr
     * @return
     */
    private static String timeStrFormat(String timeStr) {
        switch (timeStr.length()) {
            case 1:
                timeStr = "0" + timeStr;
                break;
        }
        return timeStr;
    }
}
