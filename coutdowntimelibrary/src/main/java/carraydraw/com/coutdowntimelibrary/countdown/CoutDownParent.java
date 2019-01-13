package carraydraw.com.coutdowntimelibrary.countdown;

import android.os.CountDownTimer;

/**
 * Created by suining on 2016/6/23 0023.
 */
public abstract class CoutDownParent extends CountDownTimer{
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */

    public CoutDownParent(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);

    }
}
