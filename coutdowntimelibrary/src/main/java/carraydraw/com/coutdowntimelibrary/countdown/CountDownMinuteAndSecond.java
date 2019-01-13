package carraydraw.com.coutdowntimelibrary.countdown;

import android.view.View;
import android.widget.TextView;

/**
 * Created by suining on 2016/6/23 0023.
 * 分钟与秒的倒计时
 */
public class CountDownMinuteAndSecond extends CoutDownParent{
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private View view;
    private Callback callback;
    public CountDownMinuteAndSecond(long millisInFuture, long countDownInterval,View view,Callback callback) {
        super(millisInFuture, countDownInterval);
        this.view=view;
        this.callback=callback;
    }

    @Override
    public void onTick(long millisUntilFinished) {
       if(view instanceof TextView){
           ((TextView) view).setText(dealTime(millisUntilFinished/1000));
       }
    }

    @Override
    public void onFinish() {
        callback.onFinshBack();
    }



    interface Callback{
        void onFinshBack();
    }

    /**
     * deal time string
     *
     * @param time
     * @return
     */
    public static String dealTime(long time) {
        StringBuffer returnString = new StringBuffer();
        long day = time / (24 * 60 * 60);
        long hours = (time % (24 * 60 * 60)) / (60 * 60);
        long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
        long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
        String dayStr = String.valueOf(day);
        String hoursStr = timeStrFormat(String.valueOf(hours));
        String minutesStr = timeStrFormat(String.valueOf(minutes));
        String secondStr = timeStrFormat(String.valueOf(second));
        returnString.append(minutesStr).append(":").append(secondStr);
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
