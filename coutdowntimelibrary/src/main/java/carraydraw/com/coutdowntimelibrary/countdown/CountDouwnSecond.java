package carraydraw.com.coutdowntimelibrary.countdown;

import android.view.View;
import android.widget.TextView;

/**
 * Created by suining on 2016/6/30 0030.
 */
public class CountDouwnSecond extends CoutDownParent{
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private View sencondView;
    public CountDouwnSecond(long millisInFuture, long countDownInterval,View sencondView) {
        super(millisInFuture, countDownInterval);
        this.sencondView=sencondView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(sencondView instanceof TextView){
            ((TextView) sencondView).setText(dealTime(millisUntilFinished/1000));
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
        long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
        String secondStr = timeStrFormat(String.valueOf(second));
        returnString.append(secondStr);
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
