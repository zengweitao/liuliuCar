package com.cheweibao.liuliu.wxapi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.agent.ShopListActivity;
import com.cheweibao.liuliu.agent.TransitionActivity;
import com.cheweibao.liuliu.alipay.bean.AlipayConstants;
import com.cheweibao.liuliu.alipay.bean.PayResult;
import com.cheweibao.liuliu.alipay.bean.ResultInfo;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.data.ShopInfo;
import com.cheweibao.liuliu.data.UserOrderDetail;
import com.cheweibao.liuliu.data.WeixinInfo;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.used.IDCardActivity;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import carraydraw.com.coutdowntimelibrary.countdown.CountDownUtil;
import carraydraw.com.coutdowntimelibrary.countdown.CountDownView;

/**
 * 支付宝微信支付页面
 * Created by unknow on 2018/5/31.
 */

public class WXPayEntryActivity extends IDCardActivity implements IWXAPIEventHandler {
    final int RESULT_CODE = 0x101;
    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    @Bind(R.id.img_car)
    ImageView imgCar;
    @Bind(R.id.tv_car_name)
    TextView tvCarName;
    @Bind(R.id.tv_car_min_price)
    TextView tvCarMinPrice;
    @Bind(R.id.tv_car_min_down_payments)
    TextView tvCarMinDownPayments;
    @Bind(R.id.tv_car_min_down_payments_unit)
    TextView tvCarMinDownPaymentsUnit;
    @Bind(R.id.tv_car_monthly_supply)
    TextView tvCarMonthlySupply;
    @Bind(R.id.tv_event)
    TextView tvEvent;
    @Bind(R.id.is_choose)
    RelativeLayout isChoose;
    @Bind(R.id.tv_first_payment)
    TextView tvFirstPayment;
    @Bind(R.id.tv_first_payment_unit)
    TextView tvFirstPaymentUnit;
    @Bind(R.id.tv_bond)
    TextView tvBond;
    @Bind(R.id.tv_bond_unit)
    TextView tvBondUnit;
    @Bind(R.id.tv_monthly_supply_month)
    TextView tvMonthlySupplyMonth;
    @Bind(R.id.tv_monthly_supply)
    TextView tvMonthlySupply;
    @Bind(R.id.tv_monthly_supply_unit)
    TextView tvMonthlySupplyUnit;
    @Bind(R.id.tv_service_charge)
    TextView tvServiceCharge;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_near)
    TextView tvNear;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_purchaser)
    TextView tvPurchaser;
    @Bind(R.id.tv_id_card)
    TextView tvIdCard;
    @Bind(R.id.tv_mobile_numbers)
    TextView tvMobileNumbers;
    @Bind(R.id.select_alipay)
    ImageView selectAlipay;
    @Bind(R.id.select_weixin)
    ImageView selectWeixin;
    @Bind(R.id.ll_choose_pay)
    LinearLayout llChoosePay;
    @Bind(R.id.ll_wait)
    LinearLayout llWait;
    @Bind(R.id.ll_pay)
    LinearLayout llPay;
    @Bind(R.id.tv_in_review)
    TextView tvInReview;
    @Bind(R.id.tv_actual_price)
    TextView tvActualPrice;
    @Bind(R.id.tv_refresh_content)
    TextView tvRefreshContent;
    @Bind(R.id.refresh)
    View refresh;
    @Bind(R.id.ll_car_store)
    View llCarStore;
    @Bind(R.id.ll_more_shop)
    View llMoreShop;
    @Bind(R.id.btn_purchase)
    TextView btnPurchase;
    @Bind(R.id.ll_select_alipay)
    LinearLayout llSelectAlipay;
    @Bind(R.id.ll_select_weixin)
    LinearLayout llSelectWeixin;
    @Bind(R.id.waiting_include)
    View Waiting;
    @Bind(R.id.countdownhour)
    CountDownView countdownhour;
    private int style = 0;//0 我的订单，1 订单确认
    private int stylePay = 0;//0 支付宝，1 微信
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_my_order);
        ButterKnife.bind(this);
        api = WXAPIFactory.createWXAPI(this, AlipayConstants.WX_APP_ID, false);
        api.registerApp(AlipayConstants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
        style = getIntent().getIntExtra("style", 0);
        initView();
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        btnPurchase.setEnabled(false);
        switch (style) {
            case 0:
                tvTopTitle.setText(getResources().getString(R.string.my_order));
                llChoosePay.setVisibility(View.GONE);
                llPay.setVisibility(View.GONE);
                llWait.setVisibility(View.VISIBLE);
                tvInReview.setText("审核中");
                llMoreShop.setVisibility(View.INVISIBLE);
                llCarStore.setEnabled(false);
                break;
            case 1:
                tvTopTitle.setText(getResources().getString(R.string.order_sure));
                llChoosePay.setVisibility(View.VISIBLE);
                llPay.setVisibility(View.VISIBLE);
                llWait.setVisibility(View.GONE);
                llMoreShop.setVisibility(View.VISIBLE);
                llCarStore.setEnabled(true);
                break;
        }
        tvRefreshContent.setText(getResources().getString(R.string.tv_network_link_failure));
    }

    private void setView() {
        if (!TextUtils.isEmpty(info.getOrderStatus())) {
            int state = Integer.parseInt(info.getOrderStatus());
//            state=1;
            //0-待确认中(待审核待支付) 1-待支付(通过审核有效订单) 2-支付成功(已支付 首款或订金) 3-取消的订单(已失效(7天未支付),不能支付) 4-驳回的订单 5-分期还款中 6-已完结(全款或分期完成) 7-售后',
            switch (state) {
                case 0:
                    tvTopTitle.setText(getResources().getString(R.string.my_order));
                    llChoosePay.setVisibility(View.GONE);
                    llPay.setVisibility(View.GONE);
                    llWait.setVisibility(View.VISIBLE);
                    tvInReview.setText("审核中");
                    llMoreShop.setVisibility(View.INVISIBLE);
                    llCarStore.setEnabled(false);
                    break;
                case 1:
                    btnPurchase.setEnabled(true);
                    btnPurchase.setText(getResources().getString(R.string.tv_immediate_pay));
                    getverifyStatus();
                    break;
                case 2:
                case 5:
                case 6:
                case 7:
                    btnPurchase.setEnabled(false);
                    llSelectAlipay.setEnabled(false);
                    llSelectWeixin.setEnabled(false);
                    llChoosePay.setVisibility(View.GONE);
                    btnPurchase.setText("已支付");
                    break;
                case 3:
                case 4:
                    btnPurchase.setEnabled(false);
                    llSelectAlipay.setEnabled(false);
                    llSelectWeixin.setEnabled(false);
                    llChoosePay.setVisibility(View.GONE);
                    btnPurchase.setText("已失效");
                    break;
            }
        }
        if (false) {
            selectAlipay.setImageResource(R.drawable.ic_select);
            selectWeixin.setImageResource(R.drawable.ic_unselect);
            //微信
            selectWeixin.setImageResource(R.drawable.ic_select);
            selectAlipay.setImageResource(R.drawable.ic_unselect);
        }
//        btnPurchase.setEnabled(true);
        Glide.with(MyGlobal.context).load(info.getBanner()).centerCrop().into(imgCar);
        tvCarName.setText(info.getModelName());
        tvCarMinPrice.setVisibility(View.INVISIBLE);
        tvCarMinDownPayments.setText(info.getDownPayments());
        tvCarMinDownPaymentsUnit.setText("万");
        tvCarMonthlySupply.setText("月供" + info.getMonthPay() + "元");

//        tvEvent.setText("活动");
        tvFirstPayment.setText(info.getDownPayments());
        tvFirstPaymentUnit.setText("万");
        tvBond.setText(info.getBond());
        tvBondUnit.setText("元");
        tvMonthlySupply.setText(info.getMonthPay());
        tvMonthlySupplyUnit.setText("元");
        tvMonthlySupplyMonth.setText(info.getTerm() + "月");
        isChoose.setVisibility(View.GONE);
        tvServiceCharge.setText(info.getServiceFee() + "元");

        llMoreShop.setVisibility(View.INVISIBLE);
        llCarStore.setEnabled(false);
        if ("2".equals(info.getCarType())) {
            tvNear.setVisibility(View.VISIBLE);
//        } else {
//            getInfo();
        }
        tvName.setText(info.getStoreName());
        tvContent.setText(info.getAddress());

        tvPurchaser.setText(getResources().getString(R.string.tv_purchaser) + "：" + info.getRealName());
        // 用于显示的加*身份证
        String show_id = info.getIdcard().substring(0, 3) + "********" + info.getIdcard().substring(11, info.getIdcard().length());
        tvIdCard.setText(getResources().getString(R.string.tv_id_card) + "：" + show_id);
        String maskNumber = info.getMobile().substring(0, 3) + "****" + info.getMobile().substring(7, info.getMobile().length());
        tvMobileNumbers.setText(getResources().getString(R.string.tv_mobile_numbers) + "：" + maskNumber);
        tvActualPrice.setText(info.getPrice());
//        setCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContext = this;
        getModelDetail();
//        mshowNoticeDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_CODE:
                if (data != null) {
                    String position = data.getStringExtra("position");
                    ShopInfo result = (ShopInfo) data.getSerializableExtra("shop");
                    if ("0".equals(position)) {
                        tvNear.setVisibility(View.VISIBLE);
                    } else {
                        tvNear.setVisibility(View.GONE);
                    }
                    tvName.setText(result.getChannleName());
                    tvContent.setText(result.getDetailAddress());
                }
                break;
            case RESULT_REPAY:
                boolean rePay = data.getBooleanExtra("rePay", false);
                if (rePay) {
                    getPayorder(info.getTitle(), info.getDesc(), info.getPrice());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ivTopBack, R.id.ll_car_store, R.id.ll_select_alipay, R.id.ll_select_weixin, R.id.btn_purchase, R.id.rl_btn, R.id.waiting_close})
    public void onViewClicked(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.ivTopBack:
            case R.id.waiting_close:
                finish();
                break;
            case R.id.ll_car_store:
                //提车门店
                if (!ButCommonUtils.isFastDoubleClick()) {
                    it = new Intent(mContext, ShopListActivity.class);
                    it.putExtra("parentid", info.getId());
                    startActivityForResult(it, RESULT_CODE);
                }
                break;
            case R.id.btn_purchase:
                //立即支付
                if (!ButCommonUtils.isFastDoubleClick()) {
                    getPayorder(info.getTitle(), info.getDesc(), info.getPrice());
                }
                break;
            case R.id.ll_select_alipay:
                //阿里
                selectAlipay.setImageResource(R.drawable.ic_select);
                selectWeixin.setImageResource(R.drawable.ic_unselect);
                stylePay = 0;
                break;
            case R.id.ll_select_weixin:
                //微信
//                ToastUtil.showToast("暂未开通微信支付");
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
                if (progress != null && progress.isShowing()) {
                    hideProgress();
                }
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("sessionId", myglobal.user.userId);
                postMap(ServerUrl.userOrderDetail, fields, handler);
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
                            String strdata = data.toJSONString();
                            info = gson.fromJson(strdata, UserOrderDetail.class);
                            setView();
                        } else {
                            if (mContext != null) {
                                String message = result.get("desc") + "";
                                ToastUtil.showToast(message);
                                finish();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

    private void payInfoSave(String tradeNo) {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                switch (stylePay) {
                    case 0:
                        //阿里
                        fields.put("type", "1");
                        fields.put("tradeNo", tradeNo);
                        break;
                    case 1:
                        //微信
                        fields.put("type", "2");
                        fields.put("tradeNo", tradeNo);
                        break;
                }
                fields.put("outTradeNo", info.getOrderNum());
                postMap(ServerUrl.payInfoSave, fields, payInfoSavehandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
        }
    }

    private Handler payInfoSavehandler = new Handler() {
        public void handleMessage(Message msg) {
            hideProgress();
            switch (msg.what) {
                case 0://订单支付成功
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("code") + "";
                        if ("0".equals(status)) {
                            Intent it = new Intent(mContext, TransitionActivity.class);
                            it.putExtra("style", 5);
                            startActivity(it);
                            finish();
                        } else {
                            if (mContext != null) {
                                String message = result.get("desc") + "";
                                shortToast(message);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        shortToast("抱歉数据异常，请联系客服");
                    }
                    break;
                default:
                    if (mContext != null)
                        shortToast("网络不给力，请联系客服");
                    break;
            }
        }

        ;
    };

    private void getPayorder(String subject, String body, String total_amount) {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("orderNum", info.getOrderNum());
                switch (stylePay) {
                    case 0:
                        //阿里
                        postMap(ServerUrl.goAlipay, fields, Alihandler);
                        break;
                    case 1:
                        //微信
                        postMap(ServerUrl.goWxpay, fields, Wxhandler);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
            }
        } else {
            shortToast("网络连接不可用");
            hideProgress();
        }
    }

    private Handler Alihandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("code") + "";
                        if ("0".equals(status)) {
                            String strdata = result.get("result") + "";
                            payAli(strdata);
                        } else {
                            String message = result.get("message") + "";
                            shortToast(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        shortToast("抱歉数据异常");
                    }
                    break;
                default:
                    shortToast("网络不给力!");
                    break;
            }
            hideProgress();
        }
    };
    private Handler Wxhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("code") + "";
                        if ("0".equals(status)) {
                            Gson gson = new Gson();
                            JSONObject data = JSON.parseObject(gson.toJson(result.get("result")));
                            String strdata = data.toJSONString();
                            WeixinInfo weixinInfo = gson.fromJson(strdata, WeixinInfo.class);
                            payWx(weixinInfo);
                        } else {
                            String message = result.get("message") + "";
                            shortToast(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        shortToast("抱歉数据异常");
                    }
                    break;
                default:
                    shortToast("网络不给力!");
                    break;
            }
            hideProgress();
        }
    };

    private void payWx(final WeixinInfo info) {
        // 判断是否安装客户端
//        if (!api.isWXAppInstalled() && !api.isWXAppSupportAPI()) {
        if (!api.isWXAppInstalled()) {
            ToastUtil.showToast("请您先安装微信客户端！");
            return;
        }
        WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
        builder.setAppId(info.getAppid())
                .setPartnerId(info.getPartnerid())
                .setPrepayId(info.getPrepayid())
                .setNonceStr(info.getNoncestr())
                .setTimeStamp(info.getTimestamp())
                .setSign(info.getSign())
                .build().toWXPayNotSign(WXPayEntryActivity.this, info.getAppid());
    }

    public void payAli(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(WXPayEntryActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Gson gson = new Gson();
                        ResultInfo info = gson.fromJson(resultInfo, ResultInfo.class);
                        ToastUtil.showToast("支付成功");
                        //再调取后台查询该笔订单是否支付成功
                        payInfoSave(info.getAlipay_trade_app_pay_response().getTrade_no());
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.showToast("支付失败");
                        Intent it = new Intent(mContext, TransitionActivity.class);
                        it.putExtra("style", 6);
                        startActivityForResult(it, RESULT_REPAY);
                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.d(TAG, "onPayFinish,errCode=" + baseResp.errCode);
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    ToastUtil.showToast("支付成功");
                    payInfoSave("");
                    break;
                case BaseResp.ErrCode.ERR_COMM:
//                default:
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    ToastUtil.showToast("支付失败");
                    Intent it = new Intent(mContext, TransitionActivity.class);
                    it.putExtra("style", 6);
                    startActivityForResult(it, RESULT_REPAY);
                    break;
            }
        }
    }

    public  void setCount(long time) {
        Waiting.setVisibility(View.VISIBLE);
        countdownhour.setTime(time);
        countdownhour.finshTime(new CountDownUtil.CallTimeFinshBack() {
            @Override
            public void finshTime() {
                /**
                 * 倒计时结束监听
                 */
                Waiting.setVisibility(View.GONE);
                getverifyStatus();
            }
        });
    }

    private void getverifyStatus() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                if (progress != null && progress.isShowing()) {
                    hideProgress();
                }
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("id", info.getId());
                postMap(ServerUrl.verifyStatus, fields, verifyhandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
        }
    }

    private Handler verifyhandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        int status = Integer.parseInt(result.get("status") + "");
                        switch (status) {
                            case 0:
                                //未申请审核
                                mshowNoticeDialog();
                                break;
                            case 1:
                                if (mContext != null) {
                                    String message = result.get("msg") + "";
                                    ToastUtil.showToast(message);
                                    finish();
                                }
                                break;
                            case 2:
                                setCount(Long.parseLong(result.get("remainingSeconds") + "") * 1000);
                                break;
                            case 3:
                                if (mContext != null) {
                                    String message = result.get("msg") + "";
                                    ToastUtil.showToast(message);
                                    finish();
                                }
                                break;
                            case 4:
                                if (mContext != null) {
                                    String message = result.get("msg") + "";
                                    ToastUtil.showToast(message);
                                }
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    if (refresh != null) {
//                        refresh.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            hideProgress();
        }

    };
}
