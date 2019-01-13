package carraydraw.com.coutdowntimelibrary.countdown;

import android.view.View;
import android.widget.TextView;

/**
 * Created by suining on 2016/6/23 0023.
 *  毫秒右边快的倒计时
 */
public class CoutDownMillisSecond extends CoutDownParent{
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private View view;
    public CoutDownMillisSecond(long millisInFuture, long countDownInterval,View view) {
        super(millisInFuture, countDownInterval);
        this.view=view;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //millisUntilFinished/
        if(view instanceof TextView){
            ((TextView) view).setText(millisUntilFinished%9+"");

//            if(0==millisUntilFinished%8){
//                ((TextView) view).setText(":1"+millisUntilFinished%9+"");
//            }else if(1==millisUntilFinished%8){
//                ((TextView) view).setText(":3"+millisUntilFinished%9+"");
//            }else if(2==millisUntilFinished%8){
//                ((TextView) view).setText(":3"+millisUntilFinished%9+"");
//            }else if(3==millisUntilFinished%8){
//                ((TextView) view).setText(":5"+millisUntilFinished%9+"");
//            }else if(4==millisUntilFinished%8){
//                ((TextView) view).setText(":7"+millisUntilFinished%9+"");
//            }else if(5==millisUntilFinished%8){
//                ((TextView) view).setText(":7"+millisUntilFinished%9+"");
//            }else if(6==millisUntilFinished%8){
//                ((TextView) view).setText(":7"+millisUntilFinished%9+"");
//            }else if(7==millisUntilFinished%8){
//                ((TextView) view).setText(":9"+millisUntilFinished%9+"");
//            }

        }
    }

    @Override
    public void onFinish() {
        this.start();
    }
}
