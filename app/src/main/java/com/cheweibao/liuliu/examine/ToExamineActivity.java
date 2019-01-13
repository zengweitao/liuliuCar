package com.cheweibao.liuliu.examine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BankCardUtils;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.AppCarSelfListInfo;
import com.cheweibao.liuliu.data.OrderInfo;
import com.cheweibao.liuliu.db.LocalCityTable;
import com.cheweibao.liuliu.main.MainActivity;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.MyEditText;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToExamineActivity extends BaseActivity {

    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    @Bind(R.id.tvTopRight)
    TextView tvTopRight;
    @Bind(R.id.et_name)
    MyEditText etName;
    @Bind(R.id.et_id_card)
    EditText etIdCard;
    @Bind(R.id.et_bank_card)
    EditText etBankCard;
    @Bind(R.id.et_mobile_numbers)
    EditText etMobileNumbers;
    String name, Idcard, bankCard, mobileNumbers;
    @Bind(R.id.clear_name)
    ImageView clearName;
    @Bind(R.id.clear_id_card)
    ImageView clearIdCard;
    @Bind(R.id.clear_bank_card)
    ImageView clearBankCard;
    @Bind(R.id.clear_mobile_numbers)
    ImageView clearMobileNumbers;
    private OrderInfo orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_examine);
        ButterKnife.bind(this);
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        tvTopTitle.setText("贷前审核");
        tvTopRight.setText("下一步");
        tvTopRight.setTextColor(getResources().getColor(R.color.color_grey_999999));
        tvTopRight.setVisibility(View.VISIBLE);
        tvTopRight.setEnabled(false);
        //获取登录手机号
        etMobileNumbers.setText(myglobal.user.userPhone);
        clearMobileNumbers.setVisibility(View.VISIBLE);

        etName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    clearName.setVisibility(View.VISIBLE);
                } else {
                    clearName.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                setTvTopRight();
            }
        });
        etIdCard.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    clearIdCard.setVisibility(View.VISIBLE);
                } else {
                    clearIdCard.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                setTvTopRight();
            }
        });
        etBankCard.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    clearBankCard.setVisibility(View.VISIBLE);
                } else {
                    clearBankCard.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                setTvTopRight();
            }
        });
        etMobileNumbers.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    clearMobileNumbers.setVisibility(View.VISIBLE);
                } else {
                    clearMobileNumbers.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                setTvTopRight();
            }
        });
    }

    private void setTvTopRight() {
        if (clearName.getVisibility() == View.VISIBLE
                && clearIdCard.getVisibility() == View.VISIBLE
                && clearBankCard.getVisibility() == View.VISIBLE
                && clearMobileNumbers.getVisibility() == View.VISIBLE) {
            tvTopRight.setTextColor(getResources().getColor(R.drawable.top_right_selectable));
            tvTopRight.setEnabled(true);
        }
    }


    @OnClick({R.id.ivTopBack, R.id.tvTopRight, R.id.clear_name, R.id.clear_id_card, R.id.clear_bank_card, R.id.clear_mobile_numbers})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivTopBack:
                finish();
                break;
            case R.id.tvTopRight:
                //下一步
                if (!ButCommonUtils.isFastDoubleClick3s()) {
                    setValidate();
                }
                break;
            case R.id.clear_name:
                etName.setText("");
                break;
            case R.id.clear_id_card:
                etIdCard.setText("");
                break;
            case R.id.clear_bank_card:
                etBankCard.setText("");
                break;
            case R.id.clear_mobile_numbers:
                etMobileNumbers.setText("");
                break;
        }
    }

    private void setValidate() {
        name = etName.getText().toString().trim();
        Idcard = etIdCard.getText().toString().trim();
        bankCard = etBankCard.getText().toString().trim();
        mobileNumbers = etMobileNumbers.getText().toString().trim();

        if (!BankCardUtils.IDCardValidate(Idcard)) {
            ToastUtil.showLongToast("身份证无效，不是合法的身份证号码，请重新输入");
            return;
        }
        if (!BankCardUtils.checkBankCard(bankCard)) {
            ToastUtil.showLongToast("银行卡号无效，不是合法的银行卡号码，请重新输入");
            return;
        }
        if (!Utils.checkMobileNO(mobileNumbers)) {
            ToastUtil.showLongToast("手机号格式有误，请重新输入");
            return;
        }
        getAppCarSelfList();
    }

    private void getAppCarSelfList() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("realname", name + "");
                fields.put("idcard", Idcard + "");
                fields.put("mobile", mobileNumbers);
                fields.put("bankcard", bankCard);
                fields.put("sessionId", myglobal.user.userId);
                postMap(ServerUrl.verifybankcard, fields, handler);
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

            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("code") + "";
                        if ("SYS0008".equals(status)) {
                            baselogout();
                        } else if ("0".equals(status)) {
                            Intent Intent = new Intent(mContext, ToExamineOneActivity.class);
                            Intent.putExtra("orderInfo", orderInfo);
                            startActivity(Intent);
                            finish();
                        } else {
                            String message = result.get("desc") + "";
                            ToastUtil.showLongToast(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    ToastUtil.showToast(getResources().getString(R.string.error_msg_content));
                    break;
            }
            hideProgress();
        }

    };
}
