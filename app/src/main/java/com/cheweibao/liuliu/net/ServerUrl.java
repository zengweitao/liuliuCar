package com.cheweibao.liuliu.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 接口
 *
 * @author Administrator
 */
public class ServerUrl {

    //server
//    public static String BASE_URL = "http://www.liuliu.com/liuliuGoodCar/";//正式服务器
    public static String BASE_URL = "http://admin.zhenxinche.com/liuliuGoodCar/";//正式服务器
//    public static String BASE_URL = "http://101.132.40.201/liuliuGoodCar/";    //测试环境
//	public  static String BASE_URL = "http://192.168.0.167:8080/doctorCar/";
//	public  static String BASE_URL = "http://192.168.13.35:8080/doctorCar/";
//	public  static String BASE_URL = "http://101.132.67.227:8080/doctorCar/";
//    public static String BASE_URL = "http://192.168.1.131:8080/liuliuGoodCar/";    //测试环境
//	public  static String BASE_URL = "http://192.168.1.122:8180/liuliuGoodCar/";    //测试环境
//    public static String BASE_URL = "http://192.168.1.122:8082/liuliuGoodCar/";    //测试环境
    //public  static String BASE_URL = "http://www.huaqg.com:8080/huaqgAdmin/";
//	public  static String BASE_URL ="http://admin.huaqg.com/";     //生产环境
//	public  static String BASE_URL = "http://192.168.31.78:8080/";

    /**
     * 2.1	图书馆首页接口
     */
    public final static String pingu = "api/jingzhenggu";

    //3.1	登陆
    public final static String login = "users/login";
    //3.2	注册
    public final static String register = "users/register";//注册
    public final static String checkExists = "users/checkExists";//是否存在
    public final static String getSmsCode = "send/smsCode";//获取忘记密码验证码
    //3.3	密码找回
    public final static String resetPwd = "users/forget";
    public final static String findPwd = "LoginApiController/findPassword";
    // 退出
    public final static String logout = "users/logout";

    public final static String getBrandList = "pj/getBrandList";/* 获取车辆品牌车型*/
    public final static String getCarTypeList = "pj/getCarTypeList";
    public final static String getCarList = "pj/getCarList";
    public final static String getRegionList = "getRegionList";
    public final static String updateLocation = "updateLocation";
    public final static String updateUserInfo = "updateUserInfo";
    public final static String setOnlineStatus = "setOnlineStatus";
    public final static String verifyAppUserPage = "/mobile/loan/pre/verifyAppUserPage";
    public final static String verifybankcard = "/mobile/loan/pre/verifybankcard";
    public final static String secondSteps = "/mobile/loan/pre/secondStepsAdd";
    public final static String secondStepsUpdate = "/mobile/loan/pre/secondStepsUpdate";//第二步退回更新
    public final static String config = "/mobile/loan/pre/config";//材料配置
    public final static String secondStepsReject = "/mobile/loan/pre/secondStepsReject";//第二步审核回显
    //业务员
    public final static String getModelList = "mobile/car/getModelList";//首页推荐车列表
    public final static String getUsedModelList = "mobile/usedCar/getModelList";//首页二手车列表
    public final static String modelDetail = "mobile/car/modelDetail";//车辆详情
    public final static String modelUsedDetail = "mobile/car/usedCarModelDetail";//二手车辆详情
    public final static String getModelDoorList = "mobile/door/getModelDoorList";//车辆详情
    public final static String getAppCarSelfList = "mobile/car/getAppCarSelfList";
    public final static String getAppUsedCarSelfList = "mobile/car/getAppUsedCarSelfList";//二手车的车辆列表
    public final static String getModelConfigList = "mobile/car/getModelConfigList";

    public final static String userOrderDetail = "mobile/order/userOrderDetail";//订单详情
    public final static String verifyStatus = "usedcar/order/audit/verifyStatus";//审核状态
    public final static String checkUserInfo = "usedcar/order/audit/checkUserInfo";// 验证是否是号主
    public final static String getMyRepay = "mobile/order/getMyRepay";//订单详情
    public final static String goAlipay = "alipay/goAlipay";//获取阿里订单
    public final static String goWxpay = "alipay/wxpay";//获取微信订单
    public final static String payInfoSave = "mobile/order/payInfoSave";//支付成功

    public final static String getAgentOrderList = "Agent/pj/getOrderList";
    public final static String submitOrder = "Agent/pj/submitOrder";/*预估价格*/
    public final static String requestReport = "Agent/pj/requestReport";/*申请报告*/
    public final static String pjGetOrderInfo = "pj/getOrderInfo";/*评价订单信息*/
    public final static String pjSaveOrderInfo = "Agent/pj/saveOrderInfo";/*保存订单*/
    public final static String pjMake2ReportOrder = "Agent/pj/make2ReportOrder";/*转到申请报告*/
    //评估师
    public final static String getAppraiserOrderList = "Appraiser/pj/getOrderList";
    public final static String setPrice = "Appraiser/pj/setPrice";
    //检测师
    public final static String getCheckerOrderList = "Checker/jc/getOrderList";
    public final static String jcGetOrderInfo = "jc/getOrderInfo";
    public final static String saveBasicInfo = "jc/saveBasicInfo";
    public final static String saveBasicConf = "jc/saveBasicConf";
    public final static String saveCheckRes = "jc/saveCheckRes";
    public final static String savePhoto = "jc/savePhoto";
    public final static String saveAfterService = "jc/saveAfterService";
    public final static String finishCheck = "jc/finishCheck";
    public final static String appraisalCar = "api/jingzhenggu";
    public final static String appraisalCarHtml = "api/jingzhengu.html";
    public final static String version_path = "version/getAPPUpdateVersion";


    /**
     * 获取加密字符串，并且按照key首字母排序
     *
     * @param params 生成加密字符串数据
     * @param split  分隔符
     * @return
     */
    public static String getCodeStr(HashMap<String, String> params, String split) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);

            if (key.equals("sign")) continue;

            String value = params.get(key);

            if (value == null || value == "") continue;

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        if (prestr.substring(prestr.length() - 1).equals("&"))
            prestr = prestr.substring(0, prestr.length() - 1);

        return prestr;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String MD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {

            if ((b & 0xFF) < 0x10) hex.append("0");

            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();

    }
}
