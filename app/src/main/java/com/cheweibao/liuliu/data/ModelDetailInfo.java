package com.cheweibao.liuliu.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by unknow on 2018/6/10.
 */

public class ModelDetailInfo implements Serializable {
    public String id;
    public String channleId;//渠道ID
    public String channleName;//渠道名称
    public String detailAddress;//详情地址
    public String carType;//1集采车2渠道车
    public List<BannerInfo> image;//图片地址
    public List<ConfigInfo> config;//车辆配置数据集
    public List<FinanceInfo> finance;//分期方案数据集
    public CarModelInfo carModel;//车辆车型数据集
    public String serviceFee;//服务费

    public String getChannleId() {
        return channleId;
    }

    public void setChannleId(String channleId) {
        this.channleId = channleId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public List<BannerInfo> getImage() {
        return image;
    }

    public void setImage(List<BannerInfo> image) {
        this.image = image;
    }

    public List<ConfigInfo> getConfig() {
        return config;
    }

    public void setConfig(List<ConfigInfo> config) {
        this.config = config;
    }

    public List<FinanceInfo> getFinance() {
        return finance;
    }

    public void setFinance(List<FinanceInfo> finance) {
        this.finance = finance;
    }

    public CarModelInfo getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModelInfo carModel) {
        this.carModel = carModel;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }
}
