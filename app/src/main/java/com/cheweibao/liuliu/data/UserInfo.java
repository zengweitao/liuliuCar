package com.cheweibao.liuliu.data;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class UserInfo implements Serializable {
    public static final int LOGIN_NO = 0;
    public static final int LOGIN_MOBILE = 1;
    public static final int LOGIN_WEIXIN = 2;


    public String userAvatar;
    public String userPhone;
    public String userId;//sessionId
    public String token;//
    public String orgId;
    public String userName;
    public String userType; // 4-业务员,5-评估员,6-检测员
    public String isOnline;
    public String checkCount;
    public String setPriceCount;
    public String orgName;

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(String checkCount) {
        this.checkCount = checkCount;
    }

    public String getSetPriceCount() {
        return setPriceCount;
    }

    public void setSetPriceCount(String setPriceCount) {
        this.setPriceCount = setPriceCount;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public UserInfo() {
        recycle();
    }

    public UserInfo(JSONObject obj) {
        if (obj == null) return;

        try {
            userAvatar = obj.getString("userAvatar");
            userType = obj.getString("userType");
            userPhone = obj.getString("userPhone");
            userId = obj.getString("userId");
            token = obj.getString("token");
            orgId = obj.getString("orgId");
            userName = obj.getString("userName");
            isOnline = obj.getString("isOnline");
            checkCount = obj.getString("checkCount");
            setPriceCount = obj.getString("setPriceCount");
            orgName = obj.getString("orgName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void recycle() {
        userAvatar = "";
        userPhone = "";
        userId = "";
        orgId = "";
        userName = "";
        userType = "";
        isOnline = "";
        checkCount = "0";
        setPriceCount = "0";
        token = "";
        orgName = "";
    }
}