package com.cheweibao.liuliu.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.ui.MyEditText;
import com.google.gson.Gson;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.UserInfo;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.ScrollViewExt;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.etPhone)
    EditText etPhone;
    @Bind(R.id.etPwd)
    MyEditText etPwd;
    @Bind(R.id.tvLogin)
    Button tvLogin;
    @Bind(R.id.tvResetPwd)
    TextView tvResetPwd;
    @Bind(R.id.tvRegister)
    TextView tvRegister;

    String strPhone, strPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                isClick();
            }
        });
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                isClick();
            }
        });
    }

    private void isClick() {
        strPhone = etPhone.getText().toString().trim();
        strPwd = etPwd.getText().toString().trim();

        if (Utils.isEmpty(strPhone)) {
            return;
        }
        if (Utils.isEmpty(strPwd)) {
            return;
        }
        tvLogin.setEnabled(true);
    }


    @OnClick({R.id.tvLogin, R.id.tvResetPwd, R.id.tvRegister, R.id.btn_back})
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tvLogin:
                if (!ButCommonUtils.isFastDoubleClick()) {
                    login();
                }
                break;
            case R.id.tvResetPwd:
                if (!ButCommonUtils.isFastDoubleClick()) {
                    it = new Intent(mContext, ResetPwdActivity.class);
                    startActivityForResult(it, RELOGIN);
                }
                break;
            case R.id.tvRegister:
                //注册
                if (!ButCommonUtils.isFastDoubleClick()) {
                    it = new Intent(mContext, RegisterActivity.class);
                    startActivityForResult(it, RELOGIN);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RELOGIN:
                try {
                    strPhone = data.getStringExtra("strPhone");
                    strPwd = data.getStringExtra("pass");
                    if (ServerUrl.isNetworkConnected(mContext)) {
                        try {
                            showProgress();
                            HashMap<String, String> fields = new HashMap<String, String>();
                            fields.put("mobile", strPhone);
                            fields.put("password", strPwd);
//                            fields.put("password", Utils.getMD5(strPwd));
                            postMap(ServerUrl.login, fields, handler);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (mContext != null)
                            shortToast("网络连接不可用");
                    }
                } catch (Exception e) {
                }
                break;
        }
    }

    private void login() {
        strPhone = etPhone.getText().toString().trim();
        strPwd = etPwd.getText().toString().trim();

        if (Utils.isEmpty(strPhone)) {
            shortToast("请输入手机号！");
            return;
        }

        if (!Utils.checkMobileNO(strPhone)) {
            shortToast("输入的手机号不正确！");
            return;
        }

        if (Utils.isEmpty(strPwd)) {
            shortToast("请输入密码！");
            return;
        } else if (strPwd.length() < 6 || strPwd.length() > 18) {
            shortToast("请输入6到18位密码～～");
            return;
        }

        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("mobile", strPhone);
                fields.put("password", strPwd);
//                fields.put("password", Utils.getMD5(strPwd));
                postMap(ServerUrl.login, fields, handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            hideProgress();
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
//
                            myglobal.user.userId = result.get("data").toString();
                            myglobal.user.userPhone = strPhone;
//
                            Utils.setLoginType(mContext, UserInfo.LOGIN_MOBILE);
                            Utils.setLoginMobile(mContext, strPhone);
                            Utils.setLoginPwd(mContext, strPwd);

                            goMainTab();

                        } else {
                            if (mContext != null) {
                                String message = result.get("desc") + "";
                                shortToast(message);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        shortToast("抱歉数据异常");
                    }
                    break;
                default:
                    if (mContext != null)
                        shortToast("网络不给力!");
                    break;
            }
        }

        ;
    };

    private void goMainTab() {
//        Intent it = new Intent(MyConstants.LOGIN_COMPLETE);
//        LocalBroadcastManager.getInstance(mContext).sendBroadcast(it);
        finish();
    }
}
