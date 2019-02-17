package com.cheweibao.liuliu.agent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.adapter.CarShopAdapter;
import com.cheweibao.liuliu.adapter.ProgrammeAdapter;
import com.cheweibao.liuliu.appraiser.RegisterCityActivity;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.common.PrefUtils;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.BrandInfo;
import com.cheweibao.liuliu.data.IDPhotoInfo;
import com.cheweibao.liuliu.data.Province;
import com.cheweibao.liuliu.data.ShopInfo;
import com.cheweibao.liuliu.db.LocalCityTable;
import com.cheweibao.liuliu.event.JingZhenGuEvent;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.BaseListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cheweibao.liuliu.common.MyGlobal.context;

/**
 * 门店选择页面（订单确认页面里点击购买门店）
 */

public class ShopListActivity extends BaseActivity {
    final int RESULT_CODE = 0x101;
    List<ShopInfo> shoplist = new ArrayList<>();
    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    String parentid;
    String fullName;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.rv_list)
    ListView rvList;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private CarShopAdapter vcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_shop_list);
        ButterKnife.bind(this);
        //注册订阅者
        EventBus.getDefault().register(this);

        parentid = getIntent().getStringExtra("parentid");
//        Intent intent = new Intent();
//        intent.putExtra("shop", "");
//        setResult(RESULT_CODE, intent);
        initViews();
    }

    private void initViews() {
        tvTopTitle.setText("选择门店");
        tvLocation.setText(myglobal.locationCity.getName());
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getInfo();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
            }
        });
        //接口回调,listview的头的点击事件
        rvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!ButCommonUtils.isFastDoubleClick()) {
                    Intent intent = new Intent();
                    intent.putExtra("shop", shoplist.get(position));
                    intent.putExtra("position", position);
                    setResult(RESULT_CODE, intent);
                    finish();
                }
            }
        });

    }

    private void setList() {
        vcAdapter = new CarShopAdapter(mContext, shoplist);
        rvList.setAdapter(vcAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销注册
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getInfo();
    }


    private void getInfo() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                hideProgress();
                showProgress();
                shoplist = new ArrayList<>();
                String lat = PrefUtils.getString(mContext, "Latitude", "");
                String lon = PrefUtils.getString(mContext, "Longitude", "");
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("sourcesId", parentid);//parentid
                fields.put("name", myglobal.locationCity.getName());
                fields.put("channelLat", lat);
                fields.put("channelLon", lon);
                postMap(ServerUrl.getModelDoorList, fields, handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            shortToast("网络连接不可用");
        }
    }

    private Handler handler = new Handler() {
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
                            shoplist = gson.fromJson(strdata, type);
                            setList();
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
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
            hideProgress();
        }
    };

    @OnClick({R.id.ivTopBack, R.id.ll_location})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ivTopBack:
                finish();
                break;
            case R.id.ll_location:
                //选择城市
                if (!ButCommonUtils.isFastDoubleClick()) {
                    showProgress();
                    if (LocalCityTable.getInstance().getAllProvince().size() == 0) {
                        getCityInfo();
                    } else {
                        intent = new Intent(mContext, RegisterCityActivity.class);
                        startActivity(intent);
                    }
                }
                break;
        }
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
                }
            } else {
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
        }

    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEventBus(JingZhenGuEvent event) {
        tvLocation.setText(event.getPosition().getName());
    }
}