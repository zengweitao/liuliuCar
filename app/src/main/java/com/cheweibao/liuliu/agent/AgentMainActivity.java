package com.cheweibao.liuliu.agent;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.adapter.GridViewAdapter;
import com.cheweibao.liuliu.adapter.GridViewCarAdapter;
import com.cheweibao.liuliu.appraiser.RegisterCityActivity;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.PrefUtils;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.BannerInfo;
import com.cheweibao.liuliu.data.BrandInfo;
import com.cheweibao.liuliu.data.CarModelInfo;
import com.cheweibao.liuliu.data.ModelListInfo;
import com.cheweibao.liuliu.data.ModelListUsedInfo;
import com.cheweibao.liuliu.data.Province;
import com.cheweibao.liuliu.db.LocalCityTable;
import com.cheweibao.liuliu.event.JingZhenGuEvent;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.BaseGridView;
import com.cheweibao.liuliu.ui.GlideImageLoader;
import com.cheweibao.liuliu.ui.ScrollViewExt;
import com.cheweibao.liuliu.used.InfoUsedCarActivity;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgentMainActivity extends BaseActivity implements OnBannerListener {

    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.city_name)
    TextView cityName;
    Context context;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.gv_search)
    BaseGridView gvSearch;
    @Bind(R.id.gv_cars)
    BaseGridView gvCars;
    @Bind(R.id.gv_used_cars)
    BaseGridView gvUsedCars;
    @Bind(R.id.sv_home)
    ScrollViewExt svHome;
    @Bind(R.id.ll_city)
    LinearLayout llCity;
    @Bind(R.id.ll_search)
    LinearLayout llSearch;
    @Bind(R.id.call_customer_service)
    ImageView callCustomerService;
    @Bind(R.id.img_to_top)
    ImageView imgToTop;
    @Bind(R.id.gv_choose_cars)
    BaseGridView gvChooseCars;

    ModelListInfo info;
    List<CarModelInfo> recommendCar = new ArrayList();
    List<CarModelInfo> selfSelectionCar = new ArrayList();
    List<CarModelInfo> usedCarList = new ArrayList();
    @Bind(R.id.ll_top_search)
    LinearLayout llTopSearch;
    @Bind(R.id.img_city)
    ImageView imgCity;
    @Bind(R.id.img_search)
    ImageView imgSearch;
    @Bind(R.id.tv_search)
    TextView tvSearch;
    @Bind(R.id.ll_recommend_car)
    LinearLayout llRecommendCar;
    @Bind(R.id.ll_used_car)
    LinearLayout llusedCarList;
    @Bind(R.id.ll_choose_car)
    LinearLayout llChooseCar;
    @Bind(R.id.refresh)
    View refresh;
    @Bind(R.id.tv_refresh_content)
    TextView tvContent;
    /**
     * 数据资源：标题 ＋ 图片
     */
    private String[] arrText = new String[]{
            "大众", "宝马", "奔驰",
            "奥迪", "丰田", "本田", "福特", "别克",
    };
    private int[] arrImages = new int[]{
            R.drawable.dazhong, R.drawable.baoma, R.drawable.benchi,
            R.drawable.aodi, R.drawable.fengtian, R.drawable.bentian, R.drawable.fute, R.drawable.bieke,
    };
    private List<BrandInfo> pictures = new ArrayList<>();

    /****/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_order_main);
        ButterKnife.bind(this);
        //注册订阅者
        EventBus.getDefault().register(this);
        context = this;
        initView();
        setListener();
    }

    private void setListener() {
        gvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!ButCommonUtils.isFastDoubleClick()) {
                    backtv(pictures.get(position).getName(),0);
                }
//                Intent intent = new Intent(mContext, SearchActivity.class);
//                intent.putExtra("keyOrder", "");
//                intent.putExtra("downPaymentArry", "");
//                intent.putExtra("monthPayArray", "");
//                intent.putExtra("name", pictures.get(position).getName());
//                startActivity(intent);
            }
        });
        gvCars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!ButCommonUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(context, InfoCarActivity.class);
                    intent.putExtra("info", recommendCar.get(position));
                    startActivity(intent);
                }
            }
        });
        gvUsedCars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!ButCommonUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(context, InfoUsedCarActivity.class);
                    intent.putExtra("info", usedCarList.get(position));
                    startActivity(intent);
                }
            }
        });
        gvChooseCars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!ButCommonUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(context, InfoCarActivity.class);
                    intent.putExtra("info", selfSelectionCar.get(position));
                    startActivity(intent);
                }
            }
        });
    }

    private void initView() {
//        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getModelList();
            }
        });
        myglobal.locationCity.setName(PrefUtils.getString(context, "locationCity", ""));
        myglobal.locationCity.setId("");
        cityName.setText(myglobal.locationCity.getName());
        for (int i = 0; i < arrText.length; i++) {
            BrandInfo pt = new BrandInfo();
            pt.setName(arrText[i]);
            pt.setLogo(arrImages[i] + "");
            pictures.add(pt);
        }
        gvSearch.setAdapter(new GridViewAdapter(context, pictures));
        Utils.setGridViewHeight(gvSearch);
        svHome.setOnScrollChanged(new ScrollViewExt.OnScrollChanged() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                if (t >= banner.getHeight()) {
                    llTopSearch.setBackgroundResource(R.color.white);
                    cityName.setTextColor(getResources().getColor(R.color.tv_gray));
                    tvSearch.setTextColor(getResources().getColor(R.color.tv_gray));
                    imgSearch.setImageResource(R.drawable.ic_navbar_search_gray);
                    imgCity.setImageResource(R.drawable.ic_navbar_pop_up_gray);
                    callCustomerService.setImageResource(R.drawable.ic_navbar);
                } else {
                    llTopSearch.setBackgroundResource(R.color.transparent);
                    cityName.setTextColor(getResources().getColor(R.color.white));
                    tvSearch.setTextColor(getResources().getColor(R.color.white));
                    imgSearch.setImageResource(R.drawable.ic_navbar_search_white);
                    imgCity.setImageResource(R.drawable.ic_navbar_pop_up_white);
                    callCustomerService.setImageResource(R.drawable.ic_navbar_white);
                }
                if (t >= 2 * getHeight()) {
                    imgToTop.setVisibility(View.VISIBLE);
                } else {
                    imgToTop.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setBanner(List<BannerInfo> bannerlist) {
        //设置图片集合
        List<String> imgs = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < bannerlist.size(); i++) {
            imgs.add(bannerlist.get(i).getImageUrl());
            titles.add("");
        }
        this.banner.setImages(imgs).setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setDelayTime(4000)
                .setBannerAnimation(Transformer.Tablet).setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this).start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        refresh.setVisibility(View.GONE);
        getModelList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEventBus(JingZhenGuEvent event) {
        myglobal.locationCity = event.getPosition();
        cityName.setText(myglobal.locationCity.getName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @OnClick({R.id.ll_city, R.id.call_customer_service, R.id.ll_search, R.id.img_to_top, R.id.rl_btn})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_city:
                if (!ButCommonUtils.isFastDoubleClick1s()) {
                    showProgress();
                    if (LocalCityTable.getInstance().getAllProvince().size() == 0) {
                        getCityInfo();
                    } else {
                        hideProgress();
                        intent = new Intent(context, RegisterCityActivity.class);
                        startActivity(intent);
                    }

                }
                break;
            case R.id.ll_search:
                //搜索车型
                intent = new Intent(context, SearchActivity.class);
                intent.putExtra("keyOrder", "");
                intent.putExtra("downPaymentArry", "");
                intent.putExtra("monthPayArray", "");
                intent.putExtra("name", "");
                startActivity(intent);
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
            case R.id.rl_btn:
                getModelList();
                refresh.setVisibility(View.GONE);
                break;
        }
    }

    private void getCityInfo() {
        HashMap<String, String> fields = new HashMap<String, String>();
        if (LocalCityTable.getInstance().getAllProvince().size() == 0 || Utils.getIntPreferences
                (mContext, "newVersion") == 1) {
            if (ServerUrl.isNetworkConnected(this)) {
                try {
                    showProgress();
                    postMap(ServerUrl.getRegionList, fields, cityHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgress();
                }
            }
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

                            Intent intent = new Intent(context, RegisterCityActivity.class);
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

    private void getModelList() {
        HashMap<String, String> fields = new HashMap<String, String>();
        if (ServerUrl.isNetworkConnected(this)) {
            try {
                if (!(progress != null && progress.isShowing())) {
                    showProgress();
                }
                postMap(ServerUrl.getModelList, fields, carHandler);
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
            }
        } else {
            shortToast("网络连接不可用");
        }
        getUsedModelList();
    }

    private Handler carHandler = new Handler() {

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
                            info = gson.fromJson(data.toJSONString(), ModelListInfo.class);
                            setView(info);
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

    private void getUsedModelList() {
        HashMap<String, String> fields = new HashMap<String, String>();
        if (ServerUrl.isNetworkConnected(this)) {
            try {
                if (!(progress != null && progress.isShowing())) {
                    showProgress();
                }
                postMap(ServerUrl.getUsedModelList, fields, usedcarHandler);
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
            }
        } else {
            shortToast("网络连接不可用");
        }
    }

    private Handler usedcarHandler = new Handler() {

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
                            ModelListUsedInfo info = gson.fromJson(data.toJSONString(), ModelListUsedInfo.class);
                            usedCarList = info.getUsedCarList();
                            setUsed();
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

    private void setUsed() {
        if (usedCarList.size() > 0) {
            llusedCarList.setVisibility(View.VISIBLE);
            GridViewCarAdapter adaptermid = new GridViewCarAdapter(context, gvUsedCars, usedCarList);
            gvUsedCars.setAdapter(adaptermid);
            Utils.setGridViewHeight(gvUsedCars);
        } else {
            llusedCarList.setVisibility(View.GONE);
        }
    }

    private void setView(ModelListInfo info) {
        recommendCar = info.getRecommendCar();
        selfSelectionCar = info.getSelfSelectionCar();
        List<BannerInfo> bannerlist = info.getImage();
        if (recommendCar.size() > 0) {
            llRecommendCar.setVisibility(View.VISIBLE);
            GridViewCarAdapter adaptermid = new GridViewCarAdapter(context, gvCars, recommendCar);
            gvCars.setAdapter(adaptermid);
            Utils.setGridViewHeight(gvCars);
        } else {
            llRecommendCar.setVisibility(View.GONE);
        }
        if (selfSelectionCar.size() > 0) {
            llChooseCar.setVisibility(View.VISIBLE);
            GridViewCarAdapter adapterbottom = new GridViewCarAdapter(context, gvChooseCars, selfSelectionCar);
            gvChooseCars.setAdapter(adapterbottom);
            Utils.setGridViewHeight(gvChooseCars);
        } else {
            llChooseCar.setVisibility(View.GONE);
        }
        if (bannerlist != null && bannerlist.size() > 0) {
            setBanner(bannerlist);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
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
    }

    @Override
    public void OnBannerClick(int position) {
        //跳转webH5页面
    }
}
