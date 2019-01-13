package com.cheweibao.liuliu.adapter;

/**
 * Created by Administrator on 2018/4/14.
 */

public class PaiXu {
    String time;
    Float value;

    public PaiXu(String time, Float value) {
        this.time = time;
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
