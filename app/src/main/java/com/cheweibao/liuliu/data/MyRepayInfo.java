package com.cheweibao.liuliu.data;

import java.io.Serializable;

/**
 * Created by unknow on 2018/7/10.
 */

public class MyRepayInfo implements Serializable {
    public String id;//订单当前分期流水ID
    public String modelNmae;//车型名称
    public String banner;//车型banner图片
    public String downPayments;//首付
    public String bond;//保证金
    public String orderId;//订单主键ID
    public String orderNum;//订单号
    public String termNum;//期数
    public String allTermNum;//总共分期数
    public String monthPay;//本期应还
    public String repaymentDay;//还款截止日期
    public String orderPayTime;//订单首付支付日期
    public String myRepayStatus;//'还款情况 0-正常 1-已还款 2-已结清  3-待逾期 4-逾期中

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelNmae() {
        return modelNmae;
    }

    public void setModelNmae(String modelNmae) {
        this.modelNmae = modelNmae;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTermNum() {
        return termNum;
    }

    public void setTermNum(String termNum) {
        this.termNum = termNum;
    }

    public String getAllTermNum() {
        return allTermNum;
    }

    public void setAllTermNum(String allTermNum) {
        this.allTermNum = allTermNum;
    }

    public String getMonthPay() {
        return monthPay;
    }

    public void setMonthPay(String monthPay) {
        this.monthPay = monthPay;
    }

    public String getRepaymentDay() {
        return repaymentDay;
    }

    public void setRepaymentDay(String repaymentDay) {
        this.repaymentDay = repaymentDay;
    }

    public String getOrderPayTime() {
        return orderPayTime;
    }

    public void setOrderPayTime(String orderPayTime) {
        this.orderPayTime = orderPayTime;
    }

    public String getMyRepayStatus() {
        return myRepayStatus;
    }

    public void setMyRepayStatus(String myRepayStatus) {
        this.myRepayStatus = myRepayStatus;
    }
}
