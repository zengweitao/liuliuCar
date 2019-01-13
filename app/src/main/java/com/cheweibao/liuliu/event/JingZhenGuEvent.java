package com.cheweibao.liuliu.event;

import com.cheweibao.liuliu.data.Citys;

/**
 * Created by sj on 2016/10/26.
 * 设置主页面的显示的也没
 */
public class JingZhenGuEvent {
    public Citys s;

    public JingZhenGuEvent(Citys s) {
        this.s = s;
    }

    public Citys getPosition() {
        return s;
    }

    public void setPosition(Citys s) {
        this.s = s;
    }
}
