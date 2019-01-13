package com.cheweibao.liuliu.agent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.data.MyRepayInfo;
import com.cheweibao.liuliu.net.ServerUrl;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by unknow on 2018/5/31.
 */

public class RepaymentActivity extends BaseActivity {
    final int RESULT_CODE = 1000001;
    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    @Bind(R.id.img_car)
    ImageView imgCar;
    @Bind(R.id.tv_car_name)
    TextView tvCarName;
    @Bind(R.id.tv_car_min_down_payments)
    TextView tvCarMinDownPayments;
    @Bind(R.id.tv_car_monthly_supply)
    TextView tvCarMonthlySupply;
    @Bind(R.id.tv_first_payment)
    TextView tvFirstPayment;
    @Bind(R.id.tv_monthly_supply)
    TextView tvMonthlySupply;
    @Bind(R.id.tv_monthly_supply_unit)
    TextView tvMonthlySupplyUnit;
    @Bind(R.id.tv_bond)
    TextView tvBond;
    @Bind(R.id.select_alipay)
    ImageView selectAlipay;
    @Bind(R.id.select_weixin)
    ImageView selectWeixin;
    @Bind(R.id.ll_choose_pay)
    LinearLayout llChoosePay;
    @Bind(R.id.tvTopRight)
    TextView tvTopRight;
    @Bind(R.id.tv_state)
    TextView tvState;
    @Bind(R.id.ll_select_alipay)
    LinearLayout llSelectAlipay;
    @Bind(R.id.ll_select_weixin)
    LinearLayout llSelectWeixin;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;
    @Bind(R.id.tv_additional_conditions)
    TextView tvAdditionalConditions;
    @Bind(R.id.tv_refresh_content)
    TextView tvRefreshContent;
    @Bind(R.id.refresh)
    View refresh;
    @Bind(R.id.tv_payment_coupon)
    TextView tvPaymentCoupon;
    private int stylePay = 0;//0 支付宝，1 微信
    private MyRepayInfo info = new MyRepayInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_repayment);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTopTitle.setText(getResources().getString(R.string.my_repayment));
        tvTopRight.setText(getResources().getString(R.string.tv_early_repayment));
        tvTopRight.setVisibility(View.VISIBLE);
        tvRefreshContent.setText(getResources().getString(R.string.tv_network_link_failure));

        String str = "1.每个月<font color='#F2971B'><big>20</big></font>";
        tvPaymentCoupon.setText(Html.fromHtml(str));
        tvPaymentCoupon.append(getResources().getString(R.string.tv_payment_coupon_1));
        SpannableString clickString = new SpannableString(getResources().getString(R.string.tv_payment_coupon_tel));
        clickString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //咨询
                CalldialogShow();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.tv_must)); //设置颜色
                ds.setTextSize(40);
            }
        }, 0, clickString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPaymentCoupon.append(clickString);
        tvPaymentCoupon.append(getResources().getString(R.string.tv_payment_coupon_2));
        tvPaymentCoupon.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setView() {
        Glide.with(MyGlobal.context).load(info.getBanner()).centerCrop().into(imgCar);
        tvCarName.setText(info.getModelNmae());
        tvCarMinDownPayments.setText(getResources().getString(R.string.tv_down_payments) + info.getDownPayments() + "万");
        tvCarMonthlySupply.setText(getResources().getString(R.string.tv_date_payment) + info.getOrderPayTime());

        tvFirstPayment.setText(info.getMonthPay());
        tvMonthlySupply.setText(info.getTermNum() + "/");
        tvMonthlySupplyUnit.setText(info.getAllTermNum());
        tvAdditionalConditions.setText(getResources().getString(R.string.tv_deadline_repayment) + info.getRepaymentDay());
        //还款情况 0-正常 1-已还款 2-已结清  3-待逾期 4-逾期中
        if (!TextUtils.isEmpty(info.getMyRepayStatus())) {
            int status = Integer.parseInt(info.getMyRepayStatus());
            String strstatus = "";
            switch (status) {
                case 0:
                    strstatus = "还款中";
                    break;
                case 1:
                    strstatus = "已还款";
                    break;
                case 2:
                    strstatus = "已结清";
                    break;
                case 3:
                    strstatus = "待逾期";
                    break;
                case 4:
                    strstatus = "逾期中";
                    break;
            }
            tvState.setText(strstatus);
        }
        tvBond.setText(info.getBond());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getModelDetail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.ivTopBack, R.id.tvTopRight, R.id.ll_select_alipay, R.id.ll_select_weixin, R.id.tv_in_review, R.id.icon_question, R.id.rl_btn})
    public void onViewClicked(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.ivTopBack:
                finish();
                break;
            case R.id.tvTopRight:
                //咨询
                if (!ButCommonUtils.isFastDoubleClick()) {
                    CalldialogShow();
                }
                break;
            case R.id.tv_in_review:
                //立即还款
                if (!ButCommonUtils.isFastDoubleClick()) {
                }
                break;
            case R.id.icon_question:
                //保证金组成部分
                if (!ButCommonUtils.isFastDoubleClick()) {
                    ToastUtil.showLongToast(getResources().getString(R.string.tv_margin_cue));
                }
                break;
            case R.id.ll_select_alipay:
                //ali
                selectAlipay.setImageResource(R.drawable.ic_select);
                selectWeixin.setImageResource(R.drawable.ic_unselect);
                stylePay = 0;
                break;
            case R.id.ll_select_weixin:
                //wx
                selectWeixin.setImageResource(R.drawable.ic_select);
                selectAlipay.setImageResource(R.drawable.ic_unselect);
                stylePay = 1;
                break;
            case R.id.rl_btn:
                getModelDetail();
                refresh.setVisibility(View.GONE);
                break;
        }
    }

    private void getModelDetail() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("sessionId", myglobal.user.userId);
                postMap(ServerUrl.getMyRepay, fields, handler);
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
                        if ("0".equals(status)) {
                            Gson gson = new Gson();
                            JSONObject data = JSON.parseObject(gson.toJson(result.get("result")));
                            if (data != null && !TextUtils.isEmpty(data + "")) {
                                String strdata = data.toJSONString();
                                info = gson.fromJson(strdata, MyRepayInfo.class);
                                setView();
                            } else {
                                Intent it = new Intent(mContext, TransitionActivity.class);
                                it.putExtra("style", 7);
                                startActivity(it);
                                finish();
                            }
                        } else if ("LOANPRE00033".equals(status)) {
                            Intent it = new Intent(mContext, TransitionActivity.class);
                            it.putExtra("style", 7);
                            startActivity(it);
                            finish();
                        } else {
                            if (mContext != null) {
                                String message = result.get("desc") + "";
                                ToastUtil.showToast(message);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (refresh != null) {
                            refresh.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                default:
                    if (refresh != null) {
                        refresh.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            hideProgress();
        }

    };
}
