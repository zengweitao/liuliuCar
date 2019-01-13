package com.cheweibao.liuliu.data;

import android.widget.ImageView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class PjOrderListInfo {


	public String pjdId;
	public String pjdNo;
	public String pjdType;
	public String pjdPayStatus;
	public String carPrePrice;
	public String carReportPrice;
	public String pjdReportStatus;
	public String carId;
	public String carLoanPrice;
	public String lenderName;
	public String carTypeName;
	public String createTime;

	//评估师
	public String jgName;

	public ImageView ivImg;
	public PjOrderListInfo() {
		recycle();
	}

	public PjOrderListInfo(JSONObject obj) {
		if(obj == null){
			recycle();
			return;
		}
		try {
			pjdId = obj.getString("pjdId");
			pjdNo = obj.getString("pjdNo");
			pjdType = obj.getString("pjdType");
			carPrePrice = obj.getString("carPrePrice");
			pjdPayStatus = obj.getString("pjdPayStatus");
			carReportPrice = obj.getString("carReportPrice");
			pjdReportStatus = obj.getString("pjdReportStatus");
			carId = obj.getString("carId");
			carLoanPrice = obj.getString("carLoanPrice");
			lenderName = obj.getString("lenderName");
			carTypeName = obj.getString("carTypeName");
			createTime = obj.getString("createTime");
			jgName = obj.getString("jgName");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void recycle() {
		pjdId = "";
		pjdNo = "";
		carPrePrice = "";
		pjdType = "";
		pjdPayStatus = "";
		carReportPrice = "";
		pjdReportStatus = "";
		carId = "";
		carLoanPrice = "";
		lenderName = "";
		carTypeName = "";
		createTime = "";
		jgName = "";
	}
}