package com.cheweibao.liuliu.agent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.adapter.GridViewDefaultAdapter;
import com.cheweibao.liuliu.adapter.ProgrammeAdapter;
import com.cheweibao.liuliu.adapter.VehicleConfigurationAdapter;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.ImagePagerActivity;
import com.cheweibao.liuliu.common.PrefUtils;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.BannerInfo;
import com.cheweibao.liuliu.data.CarModelInfo;
import com.cheweibao.liuliu.data.DefaultInfo;
import com.cheweibao.liuliu.data.FinanceInfo;
import com.cheweibao.liuliu.data.ModelDetailInfo;
import com.cheweibao.liuliu.data.OrderInfo;
import com.cheweibao.liuliu.data.ShopInfo;
import com.cheweibao.liuliu.examine.ToExamineActivity;
import com.cheweibao.liuliu.examine.ToExamineOneActivity;
import com.cheweibao.liuliu.main.LoginActivity;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.BaseGridView;
import com.cheweibao.liuliu.ui.BaseListView;
import com.cheweibao.liuliu.ui.ClickEffectImageView;
import com.cheweibao.liuliu.ui.GlideImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by unknow on 2018/5/31.
 */

public class InfoCarActivity extends BaseActivity implements OnBannerListener {
    final int RESULT_CODE = 0x101;
    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.ivTopRight)
    ClickEffectImageView ivTopRight;
    @Bind(R.id.programme_layout)
    View programmeLayout;
    @Bind(R.id.view_programme_recyclerview)
    ListView viewProgrammeRecyclerview;
    @Bind(R.id.tv_car_name)
    TextView tvCarName;
    @Bind(R.id.tv_car_guiding_price)
    TextView tvCarGuidingPrice;
    @Bind(R.id.staging_scheme)
    TextView stagingScheme;
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
    @Bind(R.id.tv_additional_conditions)
    TextView tvAdditionalConditions;
    @Bind(R.id.tv_service_charge)
    TextView tvServiceCharge;
    @Bind(R.id.tv_view_all)
    TextView tvViewAll;
    @Bind(R.id.tv_car_store)
    TextView tvCarStore;
    @Bind(R.id.rv_list)
    BaseListView rvList;
    @Bind(R.id.tv_view_car_all)
    TextView tvViewCarAll;
    @Bind(R.id.gv_car_purchase_steps)
    BaseListView gvCarPurchaseSteps;
    @Bind(R.id.gv_car_purchase_notice)
    BaseListView gvCarPurchaseNotice;
    @Bind(R.id.btn_purchase)
    TextView btnPurchase;
    @Bind(R.id.is_first)
    ImageView isFirst;
    @Bind(R.id.refresh)
    View refresh;
    @Bind(R.id.ll_car_store)
    View llCarStore;
    @Bind(R.id.tv_refresh_content)
    TextView tvContent;
    @Bind(R.id.img_car_store)
    ImageView imgCarStore;
    private ProgrammeAdapter programmeAdapter;
    private VehicleConfigurationAdapter vcAdapter;

    CarModelInfo info;
    ModelDetailInfo modelDetailinfo;
    private OrderInfo orderInfo = new OrderInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_info_car);
        ButterKnife.bind(this);

        info = (CarModelInfo) getIntent().getSerializableExtra("info");
        initView();
        getModelDetail();
    }

    private void getModelDetail() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("id", info.getId());
                postMap(ServerUrl.modelDetail, fields, handler);
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
                            modelDetailinfo = gson.fromJson(strdata, ModelDetailInfo.class);
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

    private void setView() {
        if (modelDetailinfo.getImage().size() > 0) {
            setBanner(modelDetailinfo.getImage());
        }
        if ("2".equals(modelDetailinfo.getCarType())) {
            llCarStore.setEnabled(false);
            ShopInfo shopinfo = new ShopInfo();
            shopinfo.setChannleName(modelDetailinfo.getChannleName());
            shopinfo.setId(modelDetailinfo.getChannleId());
            shopinfo.setDetailAddress(modelDetailinfo.getDetailAddress());
            imgCarStore.setVisibility(View.GONE);
            setCity(shopinfo);
        } else {
            llCarStore.setEnabled(true);
            getInfo();
        }
        //多方案
        if (modelDetailinfo.getFinance().size() > 0) {
            final List<FinanceInfo> list = new ArrayList<>();
            list.addAll(modelDetailinfo.getFinance());
            programmeAdapter = new ProgrammeAdapter(mContext, list);
            viewProgrammeRecyclerview.setAdapter(programmeAdapter);
            viewProgrammeRecyclerview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!ButCommonUtils.isFastDoubleClick()) {
                        if (position == 0) {
                            isFirst.setVisibility(View.VISIBLE);
                        } else {
                            isFirst.setVisibility(View.GONE);
                        }
                        programmeAdapter.setCheckItem(position);
                        programmeLayout.setVisibility(View.GONE);
                        tvFirstPayment.setText(list.get(position).getDownPayments());
                        tvFirstPaymentUnit.setText("万");
                        tvBond.setText(list.get(position).getBond());
                        tvBondUnit.setText("元");
                        tvMonthlySupply.setText(list.get(position).getMonthPay());
                        tvMonthlySupplyUnit.setText("元");
                        tvMonthlySupplyMonth.setText("月供(" + list.get(position).getTerm() + "月)");
                        orderInfo.setProductId(list.get(position).getId());
                    }
                }
            });
        }

        if (modelDetailinfo.getConfig().size() > 0) {
            vcAdapter = new VehicleConfigurationAdapter(mContext, modelDetailinfo.getConfig().get(0).getParam());
            rvList.setAdapter(vcAdapter);
            Utils.setListViewHeight(rvList);
        }

        tvCarName.setText(modelDetailinfo.getCarModel().getName());
        tvCarGuidingPrice.setText("指导价 " + modelDetailinfo.getCarModel().getGuidePrice() + "万元");
        if (!TextUtils.isEmpty(modelDetailinfo.getServiceFee())) {
            tvServiceCharge.setText(Utils.getPrice(modelDetailinfo.getServiceFee()));
//            tvAdditionalConditions.setVisibility(View.VISIBLE);
//            tvServiceCharge.setVisibility(View.VISIBLE);
        } else {
            tvServiceCharge.setText(Utils.getPrice("0"));
//            tvAdditionalConditions.setVisibility(View.GONE);
//            tvServiceCharge.setVisibility(View.GONE);
        }

        tvFirstPayment.setText(modelDetailinfo.getFinance().get(0).getDownPayments());
        tvFirstPaymentUnit.setText("万");
        tvBond.setText(modelDetailinfo.getFinance().get(0).getBond());
        tvBondUnit.setText("元");
        tvMonthlySupply.setText(modelDetailinfo.getFinance().get(0).getMonthPay());
        tvMonthlySupplyUnit.setText("元");
        tvMonthlySupplyMonth.setText("月供(" + modelDetailinfo.getFinance().get(0).getTerm() + "月)");
        orderInfo.setProductId(modelDetailinfo.getFinance().get(0).getId());
    }

    int[] CarPurchaseStepsname = {R.string.car_purchase_steps_title_1, R.string.car_purchase_steps_title_2, R.string.car_purchase_steps_title_3, R.string.car_purchase_steps_title_4};
    int[] CarPurchaseStepscontent = {R.string.car_purchase_steps_content_1, R.string.car_purchase_steps_content_2, R.string.car_purchase_steps_content_3, R.string.car_purchase_steps_content_4};
    int[] CarPurchaseStepsimg = {R.drawable.ic_choose_car, R.drawable.ic_reviewed, R.drawable.ic_contract, R.drawable.ic_pickup_car};
    int[] CarPurchaseNoticename = {R.string.car_purchase_notice_title_1, R.string.car_purchase_notice_title_2, R.string.car_purchase_notice_title_3, R.string.car_purchase_notice_title_4,
            R.string.car_purchase_notice_title_5, R.string.car_purchase_notice_title_6, R.string.car_purchase_notice_title_7, R.string.car_purchase_notice_title_8, R.string.car_purchase_notice_title_9};
    int[] CarPurchaseNoticecontent = {R.string.car_purchase_notice_content_1, R.string.car_purchase_notice_content_2, R.string.car_purchase_notice_content_3, R.string.car_purchase_notice_content_4,
            R.string.car_purchase_notice_content_5, R.string.car_purchase_notice_content_6, R.string.car_purchase_notice_content_7, R.string.car_purchase_notice_content_8, R.string.car_purchase_notice_content_9};
    int[] CarPurchaseNoticeimg = {R.drawable.ic_car_source, R.drawable.ic_data, R.drawable.ic_vehicle_ownership, R.drawable.ic_tax, R.drawable.ic_insurance_details,
            R.drawable.ic_overdue, R.drawable.ic_on_the_cards, R.drawable.ic_repayment, R.drawable.ic_bank_card};

    private void initView() {
        tvTopTitle.setText(getResources().getString(R.string.car_info));
        ivTopRight.setImageResource(R.drawable.ic_navbar);
        ivTopRight.setVisibility(View.VISIBLE);
        tvContent.setText(getResources().getString(R.string.tv_network_link_failure));
        List<DefaultInfo> CarPurchaseSteps = new ArrayList<>();
        for (int i = 0; i < CarPurchaseStepsname.length; i++) {
            DefaultInfo info = new DefaultInfo();
            info.setName(getResources().getString(CarPurchaseStepsname[i]));
            info.setContent(getResources().getString(CarPurchaseStepscontent[i]));
            info.setUrl(CarPurchaseStepsimg[i]);
            CarPurchaseSteps.add(info);
        }
        gvCarPurchaseSteps.setAdapter(new GridViewDefaultAdapter(mContext, CarPurchaseSteps, 0));
        Utils.setListViewHeight(gvCarPurchaseSteps);
        List<DefaultInfo> CarPurchaseNotice = new ArrayList<>();
        for (int i = 0; i < CarPurchaseNoticename.length; i++) {
            DefaultInfo info = new DefaultInfo();
            info.setName(getResources().getString(CarPurchaseNoticename[i]));
            info.setContent(getResources().getString(CarPurchaseNoticecontent[i]));
            info.setUrl(CarPurchaseNoticeimg[i]);
            CarPurchaseNotice.add(info);
        }
        gvCarPurchaseNotice.setAdapter(new GridViewDefaultAdapter(mContext, CarPurchaseNotice, 1));
        Utils.setListViewHeight(gvCarPurchaseNotice);
    }

    private void setBanner(List<BannerInfo> bannerlist) {
        //设置图片集合
        List<String> imgs = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < bannerlist.size(); i++) {
            imgs.add(bannerlist.get(i).getImageUrl());
            titles.add("");
        }
        banner.setImages(imgs).setBannerStyle(BannerConfig.NUM_INDICATOR)
                .setDelayTime(4000)
                .setBannerAnimation(Transformer.Tablet).setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_CODE:
                if (data != null) {
                    ShopInfo result = (ShopInfo) data.getSerializableExtra("shop");
                    tvCarStore.setText(result.getChannleName());
                    orderInfo.setStoreId(result.getId());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ivTopBack, R.id.ivTopRight, R.id.tv_view_all, R.id.ll_car_store,
            R.id.tv_view_car_all, R.id.ll_consult, R.id.btn_purchase,
            R.id.view_programme_close, R.id.view_programme_none, R.id.rl_btn, R.id.programme_layout})
    public void onViewClicked(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.ivTopBack:
                finish();
                break;
            case R.id.tv_view_all:
                //查看全部分期方案
                if (!ButCommonUtils.isFastDoubleClick()) {
//                    if (modelDetailinfo.getFinance().size() > 1) {
                    programmeLayout.setVisibility(View.VISIBLE);
//                    }
                }
                break;
            case R.id.ll_car_store:
                //提车门店
                if (!ButCommonUtils.isFastDoubleClick()) {
                    it = new Intent(mContext, ShopListActivity.class);
                    it.putExtra("parentid", info.getSourcesId());
                    startActivityForResult(it, RESULT_CODE);
                }
                break;
            case R.id.tv_view_car_all:
                //查看详细页面内容
                if (!ButCommonUtils.isFastDoubleClick()) {
                    it = new Intent(mContext, VeConListActivity.class);
                    it.putExtra("sourcesId", modelDetailinfo.getCarModel().getSourcesId());
//                    it.putExtra("sourcesId", info.getId());
                    startActivity(it);
                }
                break;
            case R.id.ivTopRight:
            case R.id.ll_consult:
                //咨询
                if (!ButCommonUtils.isFastDoubleClick()) {
                    CalldialogShow();
                }
                break;
            case R.id.btn_purchase:
                //主人，快把我开回家
                if (!ButCommonUtils.isFastDoubleClick()) {
                    if (!TextUtils.isEmpty(myglobal.user.userId)) {
                        getVerifybankcard();
//                        Intent Intent = new Intent(mContext, ToExamineOneActivity.class);
//                        startActivity(Intent);
                    } else {
                        //登录
                        it = new Intent(mContext, LoginActivity.class);
                        startActivity(it);
                    }
                }
                break;
            case R.id.view_programme_close:
            case R.id.view_programme_none:
                if (!ButCommonUtils.isFastDoubleClick()) {
                    programmeLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.rl_btn:
                getModelDetail();
                refresh.setVisibility(View.GONE);
                break;
            case R.id.programme_layout:
                break;
        }
    }

    @Override
    public void OnBannerClick(int position) {
        if (TextUtils.isEmpty(modelDetailinfo.getImage().get(position).getImageUrl()))
            return;

        ArrayList<String> list = new ArrayList<>();
        int tmp = 0, curIdx = 0;
        for (int i = 0; i < modelDetailinfo.getImage().size(); i++) {
            BannerInfo car = modelDetailinfo.getImage().get(i);
            if (!TextUtils.isEmpty(car.getImageUrl())) {
                list.add(car.getImageUrl());
                if (i == position) curIdx = tmp;
                tmp++;
            }

        }
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.FILE_FLAG, false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, list);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, curIdx);
        startActivity(intent);
    }

    private void getVerifybankcard() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("sessionId", myglobal.user.userId);
                postMap(ServerUrl.verifyAppUserPage, fields, verifybankcardhandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
        }
    }

    private Handler verifybankcardhandler = new Handler() {

        public void handleMessage(Message msg) {
            Intent it;
            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("code") + "";
                        String id = result.get("result") + "";
                        if ("null".equals(id)) {
                            id = "";
                        }
                        String message = result.get("desc") + "";
                        orderInfo.setCarId(info.getId());
                        orderInfo.setCarType(info.getCarType());
                        if (TextUtils.isEmpty(orderInfo.getStoreId())) {
                            ToastUtil.showToast("当前车辆没有门店信息...");
                        } else if ("SYS0008".equals(status)) {
                            ToastUtil.showToast(message);
                            baselogout();
                            it = new Intent(mContext, LoginActivity.class);
                            startActivity(it);
                            hideProgress();
                            return;
                        } else if ("LOANPRE0008".equals(status)) {
                            it = new Intent(mContext, TransitionActivity.class);
                            it.putExtra("style", 3);
                            startActivity(it);
                        } else if ("LOANPRE0002".equals(status)) {
                            it = new Intent(mContext, ToExamineActivity.class);
                            it.putExtra("orderInfo", orderInfo);
                            startActivity(it);
                        } else if ("1".equals(status) || "LOANPRE0005".equals(status)) {
                            it = new Intent(mContext, ToExamineOneActivity.class);
                            it.putExtra("orderInfo", orderInfo);
                            startActivity(it);
                        } else if ("LOANPRE0006".equals(status)) {
                            if (!TextUtils.isEmpty(id)) {
                                orderInfo.setId(id);
                                it = new Intent(mContext, ToExamineOneActivity.class);
                                it.putExtra("orderInfo", orderInfo);
                                it.putExtra("style", 1);
                                startActivity(it);
                            } else {
                                ToastUtil.showToast("审核资料有误，请到我的订单处理");
//                                ToastUtil.showToast(message);
                            }
                        } else if ("2".equals(status)) {
                            ToastUtil.showToast("当前已有订单正在进行中,请前去处理");
                        } else {
                            ToastUtil.showToast(message);
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

    private void getInfo() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                String lat = PrefUtils.getString(mContext, "Latitude", "");
                String lon = PrefUtils.getString(mContext, "Longitude", "");
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("sourcesId", info.getSourcesId());//info.getId()"3128"
                fields.put("name", myglobal.locationCity.getName());
                fields.put("channelLat", lat);
                fields.put("channelLon", lon);
                postMap(ServerUrl.getModelDoorList, fields, shophandler);
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
            }
        } else {
            shortToast("网络连接不可用");
        }
    }

    private Handler shophandler = new Handler() {
        public void handleMessage(Message msg) {
            hideProgress();
            switch (msg.what) {
                case 0:
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("code") + "";
                        if ("0".equals(status)) {
                            Gson gson = new Gson();
                            JSONArray data = JSON.parseArray(gson.toJson(result.get("result")));
                            String strdata = data.toJSONString();
                            Type type = new TypeToken<List<ShopInfo>>() {
                            }.getType();
                            List<ShopInfo> shoplist = gson.fromJson(strdata, type);
                            if (shoplist.size() > 0) {
                                setCity(shoplist.get(0));
                            }
                        } else {
                            String message = result.get("message") + "";
                            shortToast(message);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        shortToast("抱歉数据异常");
                        finish();
                    }
                    break;
                default:
                    shortToast("网络不给力!");
                    finish();
                    break;
            }
            hideProgress();
        }
    };

    private void setCity(ShopInfo result) {
        tvCarStore.setText(result.getChannleName());
        orderInfo.setStoreId(result.getId());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (View.VISIBLE == programmeLayout.getVisibility()) {
                programmeLayout.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
