package carraydraw.com.coutdowntimelibrary.countdown;

/**
 * Created by suining on 2016/6/23 0023.
 *
 */
public interface CountDownInterface {
    //设置小时
    void setHour(Long hourTime);
    //设置分
    void setMinute(Long minuteTime);
    //设置秒
    void setSecond(Long secondTime);
    //设置毫秒
    void setMillisecond();
    //设置分和秒
    void setMinuteAddsSecond();
    //设置时分秒
    void setHourandMinuteandSecond();
}
