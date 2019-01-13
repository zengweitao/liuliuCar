package com.cheweibao.liuliu.used;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.adapter.ChooseCarAdapter;
import com.cheweibao.liuliu.adapter.ConstellationAdapter;
import com.cheweibao.liuliu.agent.InfoCarActivity;
import com.cheweibao.liuliu.agent.SearchActivity;
import com.cheweibao.liuliu.appraiser.RegisterCityActivity;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.AppCarSelfListInfo;
import com.cheweibao.liuliu.data.CarModelInfo;
import com.cheweibao.liuliu.data.Province;
import com.cheweibao.liuliu.db.LocalCityTable;
import com.cheweibao.liuliu.event.JingZhenGuEvent;
import com.cheweibao.liuliu.main.MainActivity;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.BaseGridView;
import com.cheweibao.liuliu.ui.BaseListView;
import com.cheweibao.liuliu.ui.ScrollViewExt;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgentUsedListActivity extends BaseActivity {

    Context context;
    @Bind(R.id.rv_choose_cars)
    BaseListView rvChooseCars;
    @Bind(R.id.sv_home)
    ScrollViewExt svHome;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.tv_default_list)
    TextView tvDefaultList;
    @Bind(R.id.tv_default_down_payments_h_l)
    TextView tvDefaultDownPaymentsHL;
    @Bind(R.id.tv_default_down_payments_l_h)
    TextView tvDefaultDownPaymentsLH;
    @Bind(R.id.tv_default_monthly_supply_h_l)
    TextView tvDefaultMonthlySupplyHL;
    @Bind(R.id.tv_default_monthly_supply_l_h)
    TextView tvDefaultMonthlySupplyLH;
    @Bind(R.id.tv_default_car_price_h_l)
    TextView tvDefaultCarPriceHL;
    @Bind(R.id.tv_default_car_price_l_h)
    TextView tvDefaultCarPriceLH;
    @Bind(R.id.view_default_none)
    View viewDefaultNone;
    @Bind(R.id.gv_down_payments)
    BaseGridView gvDownPayments;
    @Bind(R.id.view_down_payments)
    View viewDownPayments;
    @Bind(R.id.gv_monthly_supply)
    BaseGridView gvMonthlySupply;
    @Bind(R.id.view_monthly_supply)
    View viewMonthlySupply;
    @Bind(R.id.city_name)
    TextView cityName;
    @Bind(R.id.ll_city)
    LinearLayout llCity;
    @Bind(R.id.ll_search)
    LinearLayout llSearch;
    @Bind(R.id.call_customer_service)
    ImageView callCustomerService;
    @Bind(R.id.ll_top)
    LinearLayout llTop;
    @Bind(R.id.img_default)
    ImageView imgDefault;
    @Bind(R.id.img_down_payments)
    ImageView imgDownPayments;
    @Bind(R.id.img_monthly_supply)
    ImageView imgMonthlySupply;
    @Bind(R.id.rl_choose)
    RelativeLayout rlChoose;
    @Bind(R.id.img_to_top)
    ImageView imgToTop;
    @Bind(R.id.ll_layout_default)
    LinearLayout llDefault;
    @Bind(R.id.ll_layout_down_payments)
    LinearLayout llDownPayments;
    @Bind(R.id.ll_layout_monthly_supply)
    LinearLayout llMonthlySupply;
    @Bind(R.id.tv_default)
    TextView tvDefault;
    @Bind(R.id.tv_down_payments)
    TextView tvDownPayments;
    @Bind(R.id.tv_monthly_supply)
    TextView tvMonthlySupply;
    @Bind(R.id.tv_search)
    TextView tvSearch;
    @Bind(R.id.is_none)
    RelativeLayout isNone;
    @Bind(R.id.tv_top)
    TextView tvTop;
    @Bind(R.id.refresh)
    View refresh;
    @Bind(R.id.tv_refresh_content)
    TextView tvContent;

    private Integer strDownPayments[] = {R.string.tv_all, R.string.tv_down_payments_1w,
            R.string.tv_down_payments_1_2w, R.string.tv_down_payments_2_3w,
            R.string.tv_down_payments_3_4w, R.string.tv_down_payments_4w};
    private Integer strMonthlySupply[] = {R.string.tv_all, R.string.tv_monthly_supply_2k,
            R.string.tv_monthly_supply_2_3k, R.string.tv_monthly_supply_3_4k,
            R.string.tv_monthly_supply_4_5k, R.string.tv_monthly_supply_5k};
    private String downPayment[] = {",", "0,1", "1,2", "2,3", "3,4", "4,"};
    private String monthPay[] = {",", "0,2000", "2000,3000", "3000,4000", "4000,5000", "5000,"};
    private ConstellationAdapter downPaymentsAdapter;
    private ConstellationAdapter monthlySupplyAdapter;
    private ChooseUsedCarAdapter choosearadapter;
    private int pageNum = 1;
    private int pageSize = 20;
    private String keyOrder = "";//排序规则：car_price_htol、car_price_ltoh、down_payments_htol、down_payments_ltoh、month_pay_htol、month_pay_ltoh
    private String name = "";
    private String downPaymentmin = "";
    private String downPaymentmax = "";
    private String monthPaymin = "";
    private String monthPaymax = "";
    boolean isfirst = true;
    boolean islock = false;
    private List<CarModelInfo> list = new ArrayList<>();
    private MyBroadcastReceiver myReceiver;
    private String listNum = "0";
    private boolean iskeyOrder = false;

    /****/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_list);
        ButterKnife.bind(this);
        //注册订阅者
        EventBus.getDefault().register(this);
        context = this;
        initView();
        initBroadcast();
        setListener();
    }

    private void initBroadcast() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(MyConstants.SEARCH);
//        myIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//        myIntentFilter.addAction(Intent.ACTION_USER_PRESENT);
        myReceiver = new MyBroadcastReceiver();
//        LocalBroadcastManager.getInstance(mContext).registerReceiver(myReceiver, myIntentFilter);
        context.registerReceiver(myReceiver, myIntentFilter);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals(MyConstants.SEARCH)) {
                    if (!isfirst) {
                        name = intent.getStringExtra("name");
                        tvSearch.setText(name);
                        refreshLayout.setEnableLoadmore(true);
                        getCarList();
                    }
//                } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                    // 开屏
                } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                    // 锁屏
                    islock = true;
//                } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                    // 解锁
                }
            }
        }
    }

    private void setListener() {
        gvDownPayments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!ButCommonUtils.isFastDoubleClick()) {
                    downPaymentsAdapter.setCheckItem(position);
                    imgDownPayments.setImageResource(R.drawable.ic_down);
                    llDownPayments.setVisibility(View.GONE);
                    String downPay = downPayment[position];
                    //截取#之前的字符串
                    downPaymentmin = downPay.substring(0, downPay.indexOf(","));
                    //截取之后的字符
                    downPaymentmax = downPay.substring(downPay.indexOf(",") + 1);
                    tvDownPayments.setText(strDownPayments[position]);
                    getCarList();
                }
            }
        });
        gvMonthlySupply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!ButCommonUtils.isFastDoubleClick()) {
                    monthlySupplyAdapter.setCheckItem(position);
                    imgMonthlySupply.setImageResource(R.drawable.ic_down);
                    llMonthlySupply.setVisibility(View.GONE);
                    String month = monthPay[position];
                    //截取#之前的字符串
                    monthPaymin = month.substring(0, month.indexOf(","));
                    //截取之后的字符
                    monthPaymax = month.substring(month.indexOf(",") + 1);
                    tvMonthlySupply.setText(strMonthlySupply[position]);
                    getCarList();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getCarList();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum++;
                pageSize = 10;
                getAppCarSelfList(pageNum, pageSize, keyOrder, name, downPaymentmin, downPaymentmax, monthPaymin, monthPaymax);
            }
        });
//        String locationCity = PrefUtils.getString(context, "locationCity", "");
        cityName.setText(myglobal.locationCity.getName());

//        svHome.setOnScrollChangeListener(new ScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY >= 2 * getHeight()) {
//                    imgToTop.setVisibility(View.VISIBLE);
//                } else {
//                    imgToTop.setVisibility(View.GONE);
//                }
//            }
//        });
        svHome.setOnScrollChanged(new ScrollViewExt.OnScrollChanged() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                if (t >= 2 * getHeight()) {
                    imgToTop.setVisibility(View.VISIBLE);
                } else {
                    imgToTop.setVisibility(View.GONE);
                }
            }
        });
        downPaymentsAdapter = new ConstellationAdapter(this, Arrays.asList(strDownPayments));
        gvDownPayments.setAdapter(downPaymentsAdapter);
        Utils.setGridViewHeight(gvDownPayments);
        monthlySupplyAdapter = new ConstellationAdapter(this, Arrays.asList(strMonthlySupply));
        gvMonthlySupply.setAdapter(monthlySupplyAdapter);
        Utils.setGridViewHeight(gvMonthlySupply);
    }


    private void getAppCarSelfList(int pageNum, int pageSize, String keyOrder, String name, String downPaymentmin, String downPaymentmax, String monthPaymin, String monthPaymax) {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("pageNum", pageNum + "");
                fields.put("pageSize", pageSize + "");
                fields.put("keyOrder", keyOrder);
                fields.put("name", name);
                fields.put("downPayMin", downPaymentmin);
                fields.put("downPayMax", downPaymentmax);
                fields.put("monthPayMin", monthPaymin);
                fields.put("monthPayMax", monthPaymax);
                postMap(ServerUrl.getAppUsedCarSelfList, fields, handler);
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
//                            strdata = Utils.replaceBlank(strdata);
                            AppCarSelfListInfo info = gson.fromJson(strdata, AppCarSelfListInfo.class);
                            listNum = info.getTotal();
                            list.addAll(info.getList());
                            if (info.getList().size() < pageSize) {
                                refreshLayout.setEnableLoadmore(false);
                            }
                            setView();
                        } else {
                            if (mContext != null) {
                                String message = result.get("desc") + "";
                                ToastUtil.showToast(message);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    if (refresh != null) {
                        tvContent.setText(getResources().getString(R.string.tv_network_link_failure));
                        refresh.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            hideProgress();
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }

    };


    @Override
    protected void onResume() {
        super.onResume();
        if (!islock) {
            refresh.setVisibility(View.GONE);
            if (isfirst) {
                if (!TextUtils.isEmpty(MainActivity.name)) {
                    name = MainActivity.name;
//                MainActivity.name = "";
                } else {
                    name = "";
                }
                keyOrder = "";//排序规则：car_price_htol、car_price_ltoh、down_payments_htol、down_payments_ltoh、month_pay_htol、month_pay_ltoh
                tvDefault.setText("默认");
                tvSearch.setText(name);
                downPaymentmin = "";
                downPaymentmax = "";
                monthPaymin = "";
                monthPaymax = "";
                if (downPaymentsAdapter != null) {
                    downPaymentsAdapter.setCheckItem(0);
                    tvDownPayments.setText(getResources().getString(R.string.tv_down_payments));
                    imgDownPayments.setImageResource(R.drawable.ic_down);
                    llDownPayments.setVisibility(View.GONE);
                }
                if (monthlySupplyAdapter != null) {
                    monthlySupplyAdapter.setCheckItem(0);
                    tvMonthlySupply.setText(getResources().getString(R.string.tv_monthly_supply));
                    imgMonthlySupply.setImageResource(R.drawable.ic_down);
                    llMonthlySupply.setVisibility(View.GONE);
                }
                refreshLayout.setEnableLoadmore(true);
                getCarList();
            } else {
                isfirst = true;
            }
        } else {
            islock = false;
        }
    }

    private void setView() {
        if (list.size() > 0) {
            isNone.setVisibility(View.GONE);
        } else {
            isNone.setVisibility(View.VISIBLE);
        }
        if (pageNum == 1 && !iskeyOrder) {
            tvTop.setText("为您找到了" + listNum + "款二手车");
            tvTop.setVisibility(View.VISIBLE);
            tvhandler.postDelayed(runnable, 2000);//每两秒执行一次runnable.
        } else {
            tvTop.setVisibility(View.GONE);
            iskeyOrder = false;
        }
        if (pageNum == 1) {
            svHome.post(new Runnable() {
                @Override
                public void run() {
                    //To change body of implemented methods use File | Settings | File Templates.
                    svHome.fullScroll(ScrollView.FOCUS_UP);
                    //                svHome.scrollTo(0, 0);
                }
            });
        }
        if (choosearadapter != null) {
            choosearadapter.notifyDataSetChanged();
        } else {
            choosearadapter = new ChooseUsedCarAdapter(context, list);
            rvChooseCars.setAdapter(choosearadapter);
        }
        Utils.setListViewHeight(rvChooseCars);
        rvChooseCars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!ButCommonUtils.isFastDoubleClick()) {
                    isfirst = false;
                    Intent intent = new Intent(mContext, InfoUsedCarActivity.class);
                    intent.putExtra("info", choosearadapter.getItem(position));
                    mContext.startActivity(intent);
                }
            }
        });
    }

    Handler tvhandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            tvTop.setVisibility(View.GONE);
            tvhandler.removeCallbacks(runnable);
//            handler.postDelayed(this, 2000);
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEventBus(JingZhenGuEvent event) {
        myglobal.locationCity = event.getPosition();
        cityName.setText(myglobal.locationCity.getName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @OnClick({R.id.ll_city, R.id.call_customer_service, R.id.ll_search, R.id.img_to_top, R.id.ll_default, R.id.ll_down_payments, R.id.ll_monthly_supply,
            R.id.tv_default_list, R.id.tv_default_down_payments_h_l, R.id.tv_default_down_payments_l_h, R.id.tv_default_monthly_supply_h_l,
            R.id.tv_default_monthly_supply_l_h, R.id.tv_default_car_price_h_l, R.id.tv_default_car_price_l_h,
            R.id.view_default_none, R.id.view_down_payments, R.id.view_monthly_supply, R.id.rl_btn})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_city:
                if (!ButCommonUtils.isFastDoubleClick1s()) {
                    showProgress();
                    if (LocalCityTable.getInstance().getAllProvince().size() == 0) {
                        getCityInfo();
                    } else {
                        isfirst = false;
                        hideProgress();
                        intent = new Intent(mContext, RegisterCityActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.ll_search:
                //搜索车型
                if (!ButCommonUtils.isFastDoubleClick()) {
                    intent = new Intent(mContext, SearchActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("isUsed", 1);
                    startActivity(intent);
                }
                break;
            case R.id.call_customer_service:
                if (!ButCommonUtils.isFastDoubleClick()) {
                    CalldialogShow();
                }
                break;
            case R.id.img_to_top:
                //置顶
                svHome.post(new Runnable() {
                    @Override
                    public void run() {
                        //To change body of implemented methods use File | Settings | File Templates.
                        svHome.fullScroll(ScrollView.FOCUS_UP);
                        //                svHome.scrollTo(0, 0);
                    }
                });
                break;
            case R.id.ll_default:
                //默认选项
                if (View.VISIBLE == llDefault.getVisibility()) {
                    imgDefault.setImageResource(R.drawable.ic_down);
                    llDefault.setVisibility(View.GONE);
                } else {
                    imgDefault.setImageResource(R.drawable.ic_up);
                    imgDownPayments.setImageResource(R.drawable.ic_down);
                    imgMonthlySupply.setImageResource(R.drawable.ic_down);
                    llDefault.setVisibility(View.VISIBLE);
                    llDownPayments.setVisibility(View.GONE);
                    llMonthlySupply.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_down_payments:
                //首付选项
                if (View.VISIBLE == llDownPayments.getVisibility()) {
                    imgDownPayments.setImageResource(R.drawable.ic_down);
                    llDownPayments.setVisibility(View.GONE);
                } else {
                    imgDownPayments.setImageResource(R.drawable.ic_up);
                    imgDefault.setImageResource(R.drawable.ic_down);
                    imgMonthlySupply.setImageResource(R.drawable.ic_down);
                    llDownPayments.setVisibility(View.VISIBLE);
                    llDefault.setVisibility(View.GONE);
                    llMonthlySupply.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_monthly_supply:
                //月供选项
                if (View.VISIBLE == llMonthlySupply.getVisibility()) {
                    imgMonthlySupply.setImageResource(R.drawable.ic_down);
                    llMonthlySupply.setVisibility(View.GONE);
                } else {
                    imgMonthlySupply.setImageResource(R.drawable.ic_up);
                    imgDownPayments.setImageResource(R.drawable.ic_down);
                    imgDefault.setImageResource(R.drawable.ic_down);
                    llMonthlySupply.setVisibility(View.VISIBLE);
                    llDownPayments.setVisibility(View.GONE);
                    llDefault.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_default_list:
                //默认排序
                imgDefault.setImageResource(R.drawable.ic_down);
                llDefault.setVisibility(View.GONE);
                keyOrder = "";//排序规则：car_price_htol、car_price_ltoh、down_payments_htol、down_payments_ltoh、month_pay_htol、month_pay_ltoh
                tvDefault.setText("默认");
                iskeyOrder = true;
                getCarList();
                break;
            case R.id.tv_default_down_payments_h_l:
                //首付由高到低
                imgDefault.setImageResource(R.drawable.ic_down);
                llDefault.setVisibility(View.GONE);
                keyOrder = "down_payments_htol";//排序规则：car_price_htol、car_price_ltoh、down_payments_htol、down_payments_ltoh、month_pay_htol、month_pay_ltoh
                tvDefault.setText(getResources().getString(R.string.tv_default_down_payments_h_l));
                iskeyOrder = true;
                getCarList();
                break;
            case R.id.tv_default_down_payments_l_h:
                //首付由低到高
                imgDefault.setImageResource(R.drawable.ic_down);
                llDefault.setVisibility(View.GONE);
                keyOrder = "down_payments_ltoh";//排序规则：car_price_htol、car_price_ltoh、down_payments_htol、down_payments_ltoh、month_pay_htol、month_pay_ltoh
                tvDefault.setText(getResources().getString(R.string.tv_default_down_payments_l_h));
                iskeyOrder = true;
                getCarList();
                break;
            case R.id.tv_default_monthly_supply_h_l:
                //月供由高到低
                imgDefault.setImageResource(R.drawable.ic_down);
                llDefault.setVisibility(View.GONE);
                keyOrder = "month_pay_htol";//排序规则：car_price_htol、car_price_ltoh、down_payments_htol、down_payments_ltoh、month_pay_htol、month_pay_ltoh
                tvDefault.setText(getResources().getString(R.string.tv_default_monthly_supply_h_l));
                iskeyOrder = true;
                getCarList();
                break;
            case R.id.tv_default_monthly_supply_l_h:
                //月供由低到高
                imgDefault.setImageResource(R.drawable.ic_down);
                llDefault.setVisibility(View.GONE);
                keyOrder = "month_pay_ltoh";//排序规则：car_price_htol、car_price_ltoh、down_payments_htol、down_payments_ltoh、month_pay_htol、month_pay_ltoh
                tvDefault.setText(getResources().getString(R.string.tv_default_monthly_supply_l_h));
                iskeyOrder = true;
                getCarList();
                break;
            case R.id.tv_default_car_price_h_l:
                //车价由高到低
                imgDefault.setImageResource(R.drawable.ic_down);
                llDefault.setVisibility(View.GONE);
                keyOrder = "car_price_htol";//排序规则：car_price_htol、car_price_ltoh、down_payments_htol、down_payments_ltoh、month_pay_htol、month_pay_ltoh
                tvDefault.setText(getResources().getString(R.string.tv_default_car_price_h_l));
                iskeyOrder = true;
                getCarList();
                break;
            case R.id.tv_default_car_price_l_h:
                //车价由低到高
                imgDefault.setImageResource(R.drawable.ic_down);
                llDefault.setVisibility(View.GONE);
                keyOrder = "car_price_ltoh";//排序规则：car_price_htol、car_price_ltoh、down_payments_htol、down_payments_ltoh、month_pay_htol、month_pay_ltoh
                tvDefault.setText(getResources().getString(R.string.tv_default_car_price_l_h));
                iskeyOrder = true;
                getCarList();
                break;
            case R.id.view_default_none:
                imgDefault.setImageResource(R.drawable.ic_down);
                llDefault.setVisibility(View.GONE);
                break;
            case R.id.view_down_payments:
                imgDownPayments.setImageResource(R.drawable.ic_down);
                llDownPayments.setVisibility(View.GONE);
                break;
            case R.id.view_monthly_supply:
                imgMonthlySupply.setImageResource(R.drawable.ic_down);
                llMonthlySupply.setVisibility(View.GONE);
                break;
            case R.id.rl_btn:
                showProgress();
                getAppCarSelfList(pageNum, pageSize, keyOrder, name, downPaymentmin, downPaymentmax, monthPaymin, monthPaymax);
                refresh.setVisibility(View.GONE);
                break;
        }
    }

    private void getCarList() {
        if (list.size() > 0) {
            list.clear();
        }
        refreshLayout.setEnableLoadmore(true);
        pageNum = 1;
        pageSize = 20;
        showProgress();
        getAppCarSelfList(pageNum, pageSize, keyOrder, name, downPaymentmin, downPaymentmax, monthPaymin, monthPaymax);
    }

    private void getCityInfo() {
        HashMap<String, String> fields = new HashMap<String, String>();
        if (LocalCityTable.getInstance().getAllProvince().size() == 0 || Utils.getIntPreferences
                (mContext, "newVersion") == 1) {
            if (ServerUrl.isNetworkConnected(this)) {
                try {
                    postMap(ServerUrl.getRegionList, fields, cityHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgress();
                }
            } else {
                hideProgress();
            }
        } else {
            hideProgress();
        }
    }

    private Handler cityHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {

                            Gson gson = new Gson();
                            JSONObject data = JSON.parseObject(gson.toJson(result.get("data")));

                            JSONArray list = data.getJSONArray("areas");
                            ArrayList<Province> areas = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                areas.add(new Province((JSONObject) list.get(i)));
                            }
                            LocalCityTable.getInstance().insertCityList(areas);
                            isfirst = false;
                            Intent intent = new Intent(mContext, RegisterCityActivity.class);
                            startActivity(intent);
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            hideProgress();
        }

    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (View.VISIBLE == llDefault.getVisibility()) {
                    imgDefault.setImageResource(R.drawable.ic_down);
                    llDefault.setVisibility(View.GONE);
                    return true;
                }
                if (View.VISIBLE == llDownPayments.getVisibility()) {
                    imgDownPayments.setImageResource(R.drawable.ic_down);
                    llDownPayments.setVisibility(View.GONE);
                    return true;
                }
                if (View.VISIBLE == llMonthlySupply.getVisibility()) {
                    imgMonthlySupply.setImageResource(R.drawable.ic_down);
                    llMonthlySupply.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销注册
        EventBus.getDefault().unregister(this);
        if (myReceiver != null) {
            if (myReceiver != null)
                LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        }
    }
}
