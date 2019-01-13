package carraydraw.com.coutdowntimelibrary.countdown;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by suining on 2016/6/23 0023.
 */
public  class CountDownUtil implements CountDownInterface{
    //整体时间戳
    private Long time;
    //假的毫秒时间戳
    private Long millisecond;
    //假的毫秒显示view
    private View millisecondView;
    //整体时间戳view
    private View timeView;
    private Context context;
    private View millLowView;
    private  CoutDownMillisSecond coutDownMillisSecond;
    private   CoutDownMillisSecondSlow coutDownMillisSecondSlow;
    private  CountDownMinuteAndSecond countDownMinuteAndSecond;
    //时分秒倒计时
    private  CountDownHourMinuteSecond countDownHourMinuteSecond;
    //小时倒计时
    private CoutDownMillisHour coutDownMillisHour;
    //分钟倒计时
    private CountDownMinute countDownMinute;
    //秒倒计时
    CountDouwnSecond countDouwnSecond;
   private CallTimeFinshBack timeFinshBack;
    public CountDownUtil(Context context,Long time, Long millisecond,View timeView,View millLowView ,View millisecondView) {
        this.context=context;
        this.time = time;
        if(millisecond==null){
            this.millisecond=99l;
        }else{
            this.millisecond = millisecond;
        }
        this.timeView=timeView;
        this.millisecondView=millisecondView;
        this.millLowView=millLowView;
    }


   //设置小时的倒计时
    @Override
    public void setHour(Long hourTime) {
        coutDownMillisHour=new CoutDownMillisHour(hourTime,1000,timeView);
        coutDownMillisHour.start();
    }
    //设置分的倒计时
    @Override
    public void setMinute(Long minuteTime) {
        countDownMinute=new CountDownMinute(minuteTime,1000,millLowView);
        countDownMinute.start();
    }
    //设置秒的倒计时
    @Override
    public void setSecond(Long secondTime) {
        countDouwnSecond=new CountDouwnSecond(secondTime,1000,millisecondView);
        countDouwnSecond.start();
    }

    public void cancleHourTime(){
        coutDownMillisHour.cancel();
    }

    public void cancleMinuteTime(){
        countDouwnSecond.cancel();
    }

    public void calcleSecondTime(){
        countDouwnSecond.cancel();
    }

    //设置毫秒倒计时
    @Override
    public void setMillisecond() {
        coutDownMillisSecond =new CoutDownMillisSecond(99,1,millisecondView);
        coutDownMillisSecond.start();
        coutDownMillisSecondSlow  =new CoutDownMillisSecondSlow(1000,100,millLowView);
        coutDownMillisSecondSlow.start();
    }
   //设置分钟与秒的倒计时
    @Override
    public void setMinuteAddsSecond() {
        countDownMinuteAndSecond =new CountDownMinuteAndSecond(time, 1000, timeView, new CountDownMinuteAndSecond.Callback() {
            @Override
            public void onFinshBack() {
                if(coutDownMillisSecond!=null){
                    coutDownMillisSecond.cancel();
                }
                if(coutDownMillisSecondSlow!=null){
                    coutDownMillisSecondSlow.cancel();
                }
                if(millisecondView instanceof TextView){
                    ((TextView) millisecondView).setText("0");
                }
                if(millLowView instanceof TextView){
                    ((TextView) millLowView).setText(":0");
                }
                if(timeView instanceof TextView) {
                    ((TextView) timeView).setText("00:00");
                }
                if(timeFinshBack!=null){
                    timeFinshBack.finshTime();
                }

            }
        });
        countDownMinuteAndSecond.start();
    }

    @Override
    public void setHourandMinuteandSecond() {
        countDownHourMinuteSecond=new CountDownHourMinuteSecond(time, 1000, timeView, new CountDownHourMinuteSecond.Callback() {
            @Override
            public void onFinshBack() {
                if(timeFinshBack!=null){
                    timeFinshBack.finshTime();
                }
            }
        });
        countDownHourMinuteSecond.start();
    }

    public void finshTime(CallTimeFinshBack timeFinshBack){
        this.timeFinshBack=timeFinshBack;
    }



    public interface CallTimeFinshBack{
        void finshTime();
    }

    public void cancelTime(){
        coutDownMillisSecond.cancel();
        coutDownMillisSecondSlow.cancel();
        countDownMinuteAndSecond.cancel();
    }

    public void cancelMillisecondTime(){
        coutDownMillisSecond.cancel();
        coutDownMillisSecondSlow.cancel();
    }
    public void cancelMinuteAddsSecondTime(){
        countDownMinuteAndSecond.cancel();
    }
    public void cancelHourMinuteAddsSecondTime(){
        countDownHourMinuteSecond.cancel();
    }

}
