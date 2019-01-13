package com.cheweibao.liuliu.alipay.bean;

import java.io.Serializable;

/**
 * Created by unknow on 2018/7/25.
 */

public class ResultInfo implements Serializable {
    public PayResponse alipay_trade_app_pay_response;
    public String sign;
    public String sign_type;

    public PayResponse getAlipay_trade_app_pay_response() {
        return alipay_trade_app_pay_response;
    }

    public void setAlipay_trade_app_pay_response(PayResponse alipay_trade_app_pay_response) {
        this.alipay_trade_app_pay_response = alipay_trade_app_pay_response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }
}
