package com.cheweibao.liuliu.data;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class JcOrderListInfo {


    public String jcdId;
    public String jcdNo;
    public String carBrand;
    public String carYear;
    public String carNumber;
    public String cityName;
    public String detailAddr;
    public String carTypeName;
    public String startTime;

    public JcOrderListInfo() {
        recycle();
    }

    public JcOrderListInfo(JSONObject obj) {
        recycle();
        if (obj == null) {
            return;
        }
        try {
            if (obj.containsKey("jcdId"))
                jcdId = obj.getString("jcdId");
            if (obj.containsKey("jcdNo"))
                jcdNo = obj.getString("jcdNo");
            if (obj.containsKey("carBrand"))
                carBrand = obj.getString("carBrand");
            if (obj.containsKey("carYear"))
                carYear = obj.getString("carYear");
            if (obj.containsKey("carNumber"))
                carNumber = obj.getString("carNumber");
            if (obj.containsKey("cityName"))
                cityName = obj.getString("cityName");
            if (obj.containsKey("detailAddr"))
                detailAddr = obj.getString("detailAddr");
            if (obj.containsKey("startTime"))
                startTime = obj.getString("startTime");
            if (obj.containsKey("carTypeName"))
                carTypeName = obj.getString("carTypeName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void recycle() {
        jcdId = "";
        jcdNo = "";
        carBrand = "";
        carYear = "";
        carNumber = "";
        cityName = "";
        detailAddr = "";
        startTime = "";
        carTypeName = "";
    }
}