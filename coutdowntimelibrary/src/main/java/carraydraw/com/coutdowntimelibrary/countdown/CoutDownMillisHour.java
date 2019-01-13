package carraydraw.com.coutdowntimelibrary.countdown;

import android.view.View;
import android.widget.TextView;

/**
 * Created by suining on 2016/6/30 0030.
 */
public class CoutDownMillisHour extends CoutDownParent{
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private View hourView;
    public CoutDownMillisHour(long millisInFuture, long countDownInterval,View hourView) {
        super(millisInFuture, countDownInterval);
        this.hourView=hourView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(hourView instanceof TextView){
            ((TextView) hourView).setText(dealTime(millisUntilFinished/1000));
        }
    }

    @Override
    public void onFinish() {
            // cancel();
    }

    /**
     * deal time string
     *
     * @param time
     * @return
     */
    public static String dealTime(long time) {
        StringBuffer returnString = new StringBuffer();
        long hours = (time % (24 * 60 * 60)) / (60 * 60);
        String hoursStr = timeStrFormat(String.valueOf(hours));
        returnString.append(hoursStr);
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
