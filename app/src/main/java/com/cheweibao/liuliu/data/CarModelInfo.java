package com.cheweibao.liuliu.data;

import java.io.Serializable;

/**
 * Created by unknow on 2018/6/10.
 */

public class CarModelInfo implements Serializable {
    public String id;//主键ID
    public String sourcesId;//主键ID(源ID)
    public String carTypeName;//车辆型号
    public String carPrice;//车辆购车价格
    public String purchaseTax;//购置税
    public String purchaseNum;//进购数量
    public String insurancePremium;//保险费
    public String isIndex;//是否置顶首页0：否  1:是
    public String applyStatus;//上架状态(1：待上架  2:已上架  3：已下架)
    public String commission;//佣金
    public String carType;//车来源(默认1 1:集采车,2:渠道商)
    public String serviceFee;//服务费
    public String downPayments;//首付
    public String bond;//保证金
    public String term;//分期数
    public String monthPay;//月供
    public String name;
    public String banner;//主图
    public String carImage;//车型图片
    public String down_payments;//首付金额
    public String guidePrice;//指导价格
    public String report;//车辆基本信息,json字符串
    public String inspectList;//检测项信息,json字符串

    public String getInspectList() {
        return inspectList;
    }

    public void setInspectList(String inspectList) {
        this.inspectList = inspectList;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getDown_payments() {
        return down_payments;
    }

    public void setDown_payments(String down_payments) {
        this.down_payments = down_payments;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourcesId() {
        return sourcesId;
    }

    public void setSourcesId(String sourcesId) {
        this.sourcesId = sourcesId;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(String carPrice) {
        this.carPrice = carPrice;
    }

    public String getPurchaseTax() {
        return purchaseTax;
    }

    public void setPurchaseTax(String purchaseTax) {
        this.purchaseTax = purchaseTax;
    }

    public String getPurchaseNum() {
        return purchaseNum;
    }

    public void setPurchaseNum(String purchaseNum) {
        this.purchaseNum = purchaseNum;
    }

    public String getInsurancePremium() {
        return insurancePremium;
    }

    public void setInsurancePremium(String insurancePremium) {
        this.insurancePremium = insurancePremium;
    }

    public String getIsIndex() {
        return isIndex;
    }

    public void setIsIndex(String isIndex) {
        this.isIndex = isIndex;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getGuidePrice() {
        return guidePrice;
    }

    public void setGuidePrice(String guidePrice) {
        this.guidePrice = guidePrice;
    }

}
