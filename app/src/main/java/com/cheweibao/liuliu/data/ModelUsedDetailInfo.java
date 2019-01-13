package com.cheweibao.liuliu.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by unknow on 2018/6/10.
 */

public class ModelUsedDetailInfo implements Serializable {
    public String id;//二手车id
    public String brandName;//品牌
    public String carTypeName;//车型
    public String carImage;//车型图片
    public String vinCode;//车架号
    public String licensePlate;//车牌号
    public String carPrice;//车型价格
    public String isIndex;//
    public String carResult;//
    public String applyStatus;//
    public String carType;//
    public String channelId;//
    public String createUser;//
    public String createTime;//
    public String updateUser;//
    public String updateTime;//
    public String settopTime;//
    public String adoptTime;//
    public String applyTime;//
    public String isDelete;//
    public String downPayments;//首付金额
    public String bond;//保证金
    public String term;//期数
    public String monthPay;//月供
    public String reportId;//
    public String storeProvinceName;//省-名称
    public String storeProvinceCode;//
    public String storeCityName;//市-名称
    public String storeCityCode;//
    public String storeDistrictName;//区-编码
    public String storeDistrictCode;//
    public String storeName;//门店名称
    public String storePhone;//门店联系方式
    public String storeAddress;//门店联系方式

    public String carInfo;//
    public String imgList;//
    public String inspectList;//检测项,json字符串
    public String report;//车辆基本信息,json字符串

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(String carPrice) {
        this.carPrice = carPrice;
    }

    public String getIsIndex() {
        return isIndex;
    }

    public void setIsIndex(String isIndex) {
        this.isIndex = isIndex;
    }

    public String getCarResult() {
        return carResult;
    }

    public void setCarResult(String carResult) {
        this.carResult = carResult;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSettopTime() {
        return settopTime;
    }

    public void setSettopTime(String settopTime) {
        this.settopTime = settopTime;
    }

    public String getAdoptTime() {
        return adoptTime;
    }

    public void setAdoptTime(String adoptTime) {
        this.adoptTime = adoptTime;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getDownPayments() {
        return downPayments;
    }

    public void setDownPayments(String downPayments) {
        this.downPayments = downPayments;
    }

    public String getBond() {
        return bond;
    }

    public void setBond(String bond) {
        this.bond = bond;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getMonthPay() {
        return monthPay;
    }

    public void setMonthPay(String monthPay) {
        this.monthPay = monthPay;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getStoreProvinceName() {
        return storeProvinceName;
    }

    public void setStoreProvinceName(String storeProvinceName) {
        this.storeProvinceName = storeProvinceName;
    }

    public String getStoreProvinceCode() {
        return storeProvinceCode;
    }

    public void setStoreProvinceCode(String storeProvinceCode) {
        this.storeProvinceCode = storeProvinceCode;
    }

    public String getStoreCityName() {
        return storeCityName;
    }

    public void setStoreCityName(String storeCityName) {
        this.storeCityName = storeCityName;
    }

    public String getStoreCityCode() {
        return storeCityCode;
    }

    public void setStoreCityCode(String storeCityCode) {
        this.storeCityCode = storeCityCode;
    }

    public String getStoreDistrictName() {
        return storeDistrictName;
    }

    public void setStoreDistrictName(String storeDistrictName) {
        this.storeDistrictName = storeDistrictName;
    }

    public String getStoreDistrictCode() {
        return storeDistrictCode;
    }

    public void setStoreDistrictCode(String storeDistrictCode) {
        this.storeDistrictCode = storeDistrictCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public String getImgList() {
        return imgList;
    }

    public void setImgList(String imgList) {
        this.imgList = imgList;
    }

    public String getInspectList() {
        return inspectList;
    }

    public void setInspectList(String inspectList) {
        this.inspectList = inspectList;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
