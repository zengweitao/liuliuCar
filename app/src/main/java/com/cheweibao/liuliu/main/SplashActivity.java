package com.cheweibao.liuliu.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.data.UserInfo;
import com.google.gson.Gson;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.Loadinfo;
import com.cheweibao.liuliu.dialog.LoadDialog;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.net.UpdateAppManager;

import java.util.HashMap;

public class SplashActivity extends BaseActivity {

    int isStartNewVersion = 0; // 0- no new version, 1- upgrade, 2- first install

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!isTaskRoot()) {
            finish();
            return;
        }

        isStartNewVersion = Utils.isStartNewVersion(mContext);

        Utils.deleteUserinfo(mContext);
        myglobal.user = Utils.getUserInfo(mContext);

//        检查更新
//        getVersion();
        date();
    }

    private void date() {
        boolean isfir = Utils.getBooleanPreferences1(mContext, "isfir");
        if (isfir) {
            //第一次进入跳转
            new FirstThread().start();
        } else {
            //第二次进入跳转
            autoLogin();
        }
    }

    public class FirstThread extends Thread {
        //继承Thread类，并改写其run方法
        public void run() {
            try {
                Thread.sleep(1000);
                mHandler.sendEmptyMessage(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class NextThread extends Thread {
        //继承Thread类，并改写其run方法
        public void run() {
            try {
                Thread.sleep(1000);
                mHandler.sendEmptyMessage(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    nextPage();
                    break;
                case 200:
                    Intent in = new Intent(mContext, WelcomeActivity.class);
                    startActivity(in);
                    finish();
                    break;
            }
        }
    };

    private void nextPage() {
        //        goLoginPage1();
        //进入主界面
        goMainTab();
    }

    private void goMainTab() {
        Intent it = new Intent(mContext, MainActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    public void autoLogin() {
        String strPhone = Utils.getLoginMobile(mContext);
        String strPwd = Utils.getLoginPwd(mContext);

        if (TextUtils.isEmpty(strPhone) || TextUtils.isEmpty(strPwd)) {
            new NextThread().start();
            return;
        }

        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("mobile", strPhone);
                fields.put("password", strPwd);
//                fields.put("password", Utils.getMD5(strPwd));
                postMap(ServerUrl.login, fields, autoLoginHandler);
            } catch (Exception e) {
                e.printStackTrace();
                nextPage();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
            nextPage();
        }

    }

    private Handler autoLoginHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            shortToast("登录成功！");
//                            Gson gson = new Gson();
//                            JSONObject data = JSON.parseObject(gson.toJson(result.get("data")));
//                            JSONObject userInfo = data.getJSONObject("userInfo");
//                            Utils.setUserInfo(mContext, userInfo.toJSONString());

                            myglobal.user.userId = result.get("data").toString();
                            myglobal.user.userPhone = Utils.getLoginMobile(mContext);
                        } else {
                            if (mContext != null) {
                                String message = result.get("message") + "";
                            }
                            logout();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        logout();
                    }
                    break;
                default:
                    logout();
                    if (mContext != null)
                        shortToast("自动登录失败!");
                    break;
            }

            nextPage();
        }

        ;
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
