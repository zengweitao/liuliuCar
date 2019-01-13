package com.cheweibao.liuliu.data;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class BankInfo {


	public String bankId;
	public String bankName;
	public String bankCode;

	public BankInfo() {
		recycle();
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public BankInfo(JSONObject obj) {
		if(obj == null) return;
		
		try {

			bankId = obj.getString("bankId");
			bankName = obj.getString("bankName");
			bankCode = obj.getString("bankCode");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void recycle() {
		bankId = "";
		bankName = "";
		bankCode = "";
	}
}