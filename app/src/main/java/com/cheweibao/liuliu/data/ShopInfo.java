package com.cheweibao.liuliu.data;

import java.io.Serializable;

/**
 * Created by unknow on 2018/7/6.
 */

public class ShopInfo implements Serializable {
    public String id;//渠道id
    public String channleName;//渠道名称
    public String detailAddress;//详情地址
    public String channelLon;//门店经度
    public String channelLat;//门店纬度

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannleName() {
        return channleName;
    }

    public void setChannleName(String channleName) {
        this.channleName = channleName;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getChannelLon() {
        return channelLon;
    }

    public void setChannelLon(String channelLon) {
        this.channelLon = channelLon;
    }

    public String getChannelLat() {
        return channelLat;
    }

    public void setChannelLat(String channelLat) {
        this.channelLat = channelLat;
    }
}
