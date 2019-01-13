package com.cheweibao.liuliu.data;

import java.io.Serializable;

/**
 * Created by unknow on 2018/6/10.
 */

public class FinanceInfo implements Serializable {
    public String id;
    public String downPayments;//首付
    public String bond;//保证金
    public String term;//分期数
    public String monthPay;//月供

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
