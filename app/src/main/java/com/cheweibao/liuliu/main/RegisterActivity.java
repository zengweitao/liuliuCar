package com.cheweibao.liuliu.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.agent.WebViewActivity;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.ClickEffectImageView;
import com.cheweibao.liuliu.ui.MyEditText;
import com.cheweibao.liuliu.ui.ScrollViewExt;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    String strPhone;
    private String pass;
    String smsCode;
//    boolean isSmsCode = false;

    int timeLimit = 60, leftTime = 0;
    boolean isAgree = false;

    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    @Bind(R.id.ivTopBack)
    ClickEffectImageView ivTopBack;
    @Bind(R.id.tvTopRight)
    TextView tvTopRight;
    @Bind(R.id.etPhone)
    EditText etPhone;
    @Bind(R.id.etConfirm)
    EditText etSms;
    @Bind(R.id.tvGetConfirm)
    TextView tvGetConfirm;
    @Bind(R.id.tvSendedConfirm)
    TextView tvSendedConfirm;
    @Bind(R.id.tvNext)
    Button tvNext;
    @Bind(R.id.svMain)
    ScrollViewExt svMain;
    @Bind(R.id.etPwd)
    MyEditText etPwd;
    @Bind(R.id.etPwdConfirm)
    EditText etPwdConfirm;

    @Bind(R.id.cbDisplayPassword)
    CheckBox cbDisplayPassword;
    @Bind(R.id.user_service_protocol)
    TextView userServiceProtocol;
    @Bind(R.id.cb_user_service_protocol)
    CheckBox cbUserServiceProtocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initView();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void initView() {
        tvTopTitle.setText(getResources().getString(R.string.register));
        cbDisplayPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: " + isChecked);
                if (isChecked) {
                    //选择状态 显示明文--设置为可见的密码
                    //mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    /**
                     * 第二种
                     */
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    //mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    /**
                     * 第二种
                     */
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        cbUserServiceProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: " + isChecked);
                if (isChecked) {
                    //选择状态 已同意协议
                    isAgree = false;
                } else {
                    //默认状态 不同意协议
                    isAgree = true;
                }
            }
        });
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
        etSms.addTextChangedListener(new TextWatcher() {
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
        String pass = etPwd.getText().toString().trim();
        smsCode = etSms.getText().toString().trim();

        if (Utils.isEmpty(strPhone)) {
            return;
        }
        if (Utils.isEmpty(smsCode)) {
            return;
        }
        if (Utils.isEmpty(pass)) {
            return;
        }
        tvNext.setEnabled(true);
    }


    public void next() {
        pass = etPwd.getText().toString().trim();
        String confirmpass = etPwdConfirm.getText().toString().trim();
        smsCode = etSms.getText().toString().trim();

        if (isAgree) {
            shortToast("请阅读并同意《用户服务协议》～～");
            return;
        }
        if (Utils.isEmpty(strPhone)) {
            shortToast("请输入手机号～～");
            return;
        }

        if (!Utils.checkMobileNO(strPhone)) {
            shortToast("手机号不正确～～");
            return;
        }

//        if (!isSmsCode) {
//            shortToast("请获取验证码～～");
//            return;
//        } else
        if (Utils.isEmpty(smsCode)) {
            etSms.requestFocus();
            shortToast("请输入验证码～～");
            return;
        } else if (smsCode.length() > 6) {
            shortToast("验证码不会超过6位～～");
            return;
        }
        if (Utils.isEmpty(pass)) {
            shortToast("请输入密码～～");
            etPwd.requestFocus();
            return;
        } else if (pass.length() < 6 || pass.length() > 18) {
            shortToast("请输入6到18位密码～～");
            return;
        }
//        if (Utils.isEmpty(confirmpass)) {
//            shortToast("请再输入密码～～");
//            etPwdConfirm.requestFocus();
//            return;
//        }
//        if (!pass.equals(confirmpass)) {
//            shortToast("密码不匹配，请再输入密码～～");
//            etPwdConfirm.requestFocus();
//            return;
//        }

        if (ServerUrl.isNetworkConnected(this)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("mobile", strPhone);
                fields.put("password", pass);
//                fields.put("password", Utils.getMD5(pass));
                fields.put("code", smsCode);
                setThread_flag(true);
                postMap(ServerUrl.register, fields, pwdHandler);
                showProgress();
            } catch (Exception e) {
                setThread_flag(false);
                e.printStackTrace();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
        }
    }

    private Handler pwdHandler = new Handler() {

        public void handleMessage(Message msg) {
            setThread_flag(false);
            hideProgress();

            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            shortToast("注册成功");
                            Intent data = new Intent();
                            data.putExtra("strPhone", strPhone);
                            data.putExtra("pass", pass);
                            setResult(RELOGIN, data);
                            finish();
                        } else {
                            String message = result.get("desc") + "";
                            shortToast(message);
                        }
                    } catch (Exception e) {
                        shortToast("抱歉数据异常");
                    }
                    break;
                default:
                    if (mContext != null)
                        shortToast("网络不给力!");
                    break;
            }
        }

    };


    public void getConfirm() {
        if (getThread_flag()) return;
        if (leftTime > 0) return;
        setThread_flag(true);
        getValidCode();
    }

    private void isCheckExists() {
        strPhone = etPhone.getText().toString().trim();

        if (Utils.isEmpty(strPhone)) {
            shortToast("请输入手机号～～");
            setThread_flag(false);
            return;
        }

        if (!Utils.checkMobileNO(strPhone)) {
            shortToast("手机号不正确～～");
            setThread_flag(false);
            return;
        }

        if (ServerUrl.isNetworkConnected(this)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("mobile", strPhone);
                postMap(ServerUrl.checkExists, fields, isCheckExistsHandler);
            } catch (Exception e) {
                e.printStackTrace();
                setThread_flag(false);
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
            setThread_flag(false);
        }

    }

    private Handler isCheckExistsHandler = new Handler() {

        public void handleMessage(Message msg) {
            setThread_flag(false);
            hideProgress();
            switch (msg.what) {
                case 0:
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            String data = result.get("data") + "";
                            if ("1".equals(data)) {
                                getConfirm();
                            } else {
                                String message = result.get("message") + "";
                                shortToast(message);
                            }
                        } else if (mContext != null) {
                            String message = result.get("message") + "";
                            shortToast(message);
                        }
                    } catch (Exception e) {
                        shortToast("抱歉数据异常");
                    }
                    break;
                default:
                    if (mContext != null)
                        shortToast("网络不给力!");
                    break;
            }
        }
    };

    private void getValidCode() {
        strPhone = etPhone.getText().toString().trim();

        if (Utils.isEmpty(strPhone)) {
            shortToast("请输入手机号～～");
            setThread_flag(false);
            return;
        }

        if (!Utils.checkMobileNO(strPhone)) {
            shortToast("手机号不正确～～");
            setThread_flag(false);
            return;
        }

        if (ServerUrl.isNetworkConnected(this)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("phone", strPhone);
                postMap(ServerUrl.getSmsCode + "/register", fields, getValidCodeHandler);
            } catch (Exception e) {
                e.printStackTrace();
                setThread_flag(false);
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
            setThread_flag(false);
        }

    }

    private Handler getValidCodeHandler = new Handler() {

        public void handleMessage(Message msg) {
            setThread_flag(false);
            hideProgress();
            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("status") + "";
                        if ("0".equals(status)) {
//                            isSmsCode = true;
                            shortToast("验证码已发送～～");
                            tvSendedConfirm.setText("我们将发送验证码短信到号码：" + strPhone);
                            tvSendedConfirm.setVisibility(View.VISIBLE);
                            countTime(timeLimit);
                        } else {
                            if (mContext != null) {
                                String message = result.get("message") + "";
                                shortToast(message);
                            }
                        }
                    } catch (Exception e) {
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

    private void countTime(int time) {
        if (time < 1) {
            tvGetConfirm.setText("获取验证码");
            return;
        }

        tvGetConfirm.setText(time + "S");
        time--;
        leftTime = time;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                countTime(leftTime);
            }
        }, 1000);
    }

    @OnClick({R.id.ivTopBack, R.id.tvGetConfirm, R.id.tvNext, R.id.user_service_protocol})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivTopBack:
                finish();
                break;
            case R.id.tvGetConfirm:
                if (!ButCommonUtils.isFastDoubleClick()) {
                    isCheckExists();
                }
                break;
            case R.id.tvNext:
                if (!ButCommonUtils.isFastDoubleClick()) {
                    next();
                }
                break;
            case R.id.user_service_protocol:
                //查看协议
                if (!ButCommonUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("state", 0);
                    startActivity(intent);
                }
                break;
        }
    }

}
