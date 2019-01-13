package com.cheweibao.liuliu.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cheweibao.liuliu.agent.AgentListActivity;
import com.cheweibao.liuliu.agent.AgentMainActivity;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.data.Loadinfo;
import com.cheweibao.liuliu.dialog.LoadDialog;
import com.cheweibao.liuliu.net.UpdateAppManager;
import com.cheweibao.liuliu.used.AgentUsedListActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.agent.AgentOrderActivity;
import com.cheweibao.liuliu.agent.AgentPjOrderDetailActivity;
import com.cheweibao.liuliu.agent.AgentWodeActivity;
import com.cheweibao.liuliu.appraiser.AppriserOrderActivity;
import com.cheweibao.liuliu.appraiser.AppriserWodeActivity;
import com.cheweibao.liuliu.appraiser.JingZhenGuActivity;
import com.cheweibao.liuliu.checker.CheckerOrderActivity;
import com.cheweibao.liuliu.checker.CheckerWodeActivity;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.common.PrefUtils;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.data.Province;
import com.cheweibao.liuliu.db.LocalCityTable;
import com.cheweibao.liuliu.event.MainLocationEvent;
import com.cheweibao.liuliu.event.RegisterCityLocationEvent;
import com.cheweibao.liuliu.home.HomeActivity;
import com.cheweibao.liuliu.net.ServerUrl;
import com.wangli.FinalHttp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2018/4/8.
 */

public class MainActivity extends TabActivity {
    private LocationClient mLocationClient;

    private static final int REQ_PERMISSION = 2001;
    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;
    private boolean isLocating = false;
    protected MyGlobal myglobal;
    TabHost tabHost;
    Context mContext;
    boolean isFinish = false;
    boolean bFinish = false;

    public static final String LOGIN_SUCCESS = "loginSuccess";

    protected FinalHttp finalHttp;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    public DisplayImageOptions optionsUser = null;

    MainActivity.MyBroadcastReceiver myReceiver;
    public static String name = "";
    private String eventS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        mContext = this;

        myglobal = (MyGlobal) getApplicationContext();
        myglobal.mActivity = this;
        finalHttp = FinalHttp.getInstance();
        //公司坐标 120.223509,30.253047
        PrefUtils.putString(MyGlobal.context, "locationCity", "杭州市");
        PrefUtils.putString(MyGlobal.context, "Latitude", "30.253047");
        PrefUtils.putString(MyGlobal.context, "Longitude", "120.223509");
        Utils.putIntPreferences(mContext, "CURRENT_TAB_ID", 0);
        if (myglobal.SCR_WIDTH == 0 || savedInstanceState != null) {
            Utils.setCachePath(mContext);
            Utils.setupUnits(mContext, this);
            myglobal.user = Utils.getUserInfo(mContext);
        }
        initView();
        initBroadcast();

        initLocationService();

        getCityInfo();
        getVersion();
    }

    private void initBroadcast() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(MyConstants.LOGOUT);
        myIntentFilter.addAction(MyConstants.LOGIN_COMPLETE);
        myIntentFilter.addAction(MyConstants.SEARCH);
        myReceiver = new MainActivity.MyBroadcastReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(myReceiver, myIntentFilter);
    }


    private void initView() {


        tabHost = getTabHost();
//        if ("4".equals(myglobal.user.userType)) {//业务员
        addTab("溜溜", R.drawable.selector_tab1, AgentMainActivity.class);
        addTab("自选", R.drawable.selector_tab2, AgentListActivity.class);
        addTab("二手车", R.drawable.selector_tab2, AgentUsedListActivity.class);
//            addTab("评估", R.drawable.selector_tab_assessment, JingZhenGuActivity.class);
        addTab("我的", R.drawable.selector_tab3, AgentWodeActivity.class);
//        } else if ("5".equals(myglobal.user.userType)) {//评估员
//            addTab("订单", R.drawable.selector_tab1, AppriserOrderActivity.class);
//            addTab("车开通", R.drawable.selector_tab2, HomeActivity.class);
//            addTab("我的", R.drawable.selector_tab3, AppriserWodeActivity.class);
//
//
//        } else if ("6".equals(myglobal.user.userType)) {//检测员
//            addTab("订单", R.drawable.selector_tab1, CheckerOrderActivity.class);
//            addTab("车开通", R.drawable.selector_tab2, HomeActivity.class);
//            addTab("我的", R.drawable.selector_tab3, CheckerWodeActivity.class);
//
//        }
        //-------------------------------此处为首次进入显示平台的---------------------------------//
//		tabHost.setCurrentTab(2);
//		findViewById(android.R.id.tabs).setVisibility(View.GONE);
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int id = tabHost.getCurrentTab();
//                if(id == 2 ) {
//                    if(myglobal.user == null || !Utils.isValid(myglobal.user.id)) {
//                        setTab(Utils.getIntPreferences(mContext, "CURRENT_TAB_ID"));
//                        Intent it = new Intent(mContext, LoginActivity.class);
//                        startActivity(it);
//                        return;
//                    }
//                }
                if (id != 1) {
                    name = "";
                }
                Utils.putIntPreferences(mContext, "CURRENT_TAB_ID", id);
            }
        });

        if (optionsUser == null) {
            optionsUser = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.icon_user_def)
                    .showImageOnFail(R.drawable.icon_user_def)
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

    }

    public void initLocationService() {

//        locationService = myglobal.locationService;
        if (!isLocating) {
            getPersimmions();
        }
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
//                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }
            if (addPermission(permissions, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)) {
                permissions.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
//                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }
            if (addPermission(permissions, Manifest.permission.READ_CONTACTS)) {
                permissions.add(Manifest.permission.READ_CONTACTS);
//                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }
            if (addPermission(permissions, Manifest.permission.GET_ACCOUNTS)) {
                permissions.add(Manifest.permission.GET_ACCOUNTS);
//                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            } else {
                getLocation();
            }
        } else {
            getLocation();
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }
        } else {
            return true;
        }
    }


    protected void postMap(String url, HashMap<String, String> fields, Handler handler) {
        finalHttp.postMap(ServerUrl.BASE_URL + url, fields, handler);
    }

    private void getCityInfo() {
        if (LocalCityTable.getInstance().getAllProvince().size() == 0 || Utils.getIntPreferences(mContext, "newVersion") == 1) {
            if (ServerUrl.isNetworkConnected(this)) {
                try {
                    HashMap<String, String> fields = new HashMap<String, String>();
                    postMap(ServerUrl.getRegionList, fields, cityHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
        } else {
            new CityThread().start();
        }
    }

    public class CityThread extends Thread {
        //继承Thread类，并改写其run方法
        public void run() {
            myglobal.provinces = createMainDatas();
            if (myglobal.provinces.size() > 0) {
                myglobal.citys = createSubDatas();
                myglobal.districts = createDistrictsDatas();
            }
        }
    }

    /**
     * 省份数据
     *
     * @return
     */
    private List<String> createMainDatas() {
        List<Province> provinceList = LocalCityTable.getInstance().getAllProvince();
        List<String> list = new ArrayList<>();
        for (Province province : provinceList) {
            list.add(province.getName());
        }
        return list;
    }

    /**
     * 城市数据
     *
     * @return
     */
    private HashMap<String, List<String>> createSubDatas() {
        return LocalCityTable.getInstance().getAllProvinceAndCity();
    }

    /**
     * 区域数据
     *
     * @return
     */
    private HashMap<String, List<String>> createDistrictsDatas() {
        return LocalCityTable.getInstance().getAllCityAndDistrict();
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
                            new CityThread().start();
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

    private void addTab(String tagId, int drawableId, Class<?> classId) {
        Intent intent = new Intent(this, classId);
        TabHost.TabSpec spec = tabHost.newTabSpec(tagId);
        LayoutInflater vi = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
        View tabIndicator = vi.inflate(R.layout.tab_indicator, null);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.ivTabIcon);
        icon.setImageResource(drawableId);
        TextView title = (TextView) tabIndicator.findViewById(R.id.ivTabTitle);
        title.setText(tagId);
        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }

    private void setTab(int tabId) {
        tabHost.setCurrentTab(tabId);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        myglobal.mActivity = null;
        if (myReceiver != null) {
            if (myReceiver != null)
                LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        }
        super.onDestroy();
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals(MyConstants.LOGOUT)) {
                    Utils.setUserInfo(mContext, "token", "");
                    Utils.setUserInfo(mContext, "userId", "");
                    Utils.setUserInfo(mContext, "userPhone", "");
                    Utils.setLoginMobile(mContext, "");
                    Utils.setLoginPwd(mContext, "");
                    myglobal.user.userId = "";
                    Intent it = new Intent(mContext, LoginActivity.class);
                    startActivity(it);
                } else if (intent.getAction().equals(MyConstants.SEARCH)) {
                    name = intent.getStringExtra("name");
                    int isUsed = intent.getIntExtra("isUsed", 0);
                    if (isUsed != 0) {
                        setTab(2);
                    } else {
                        setTab(1);
                    }
                } else if (intent.getAction().equals(MyConstants.LOGIN_COMPLETE)) {
                    setTab(0);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MessageEvent event = new MessageEvent(MyConstants.PERMISSION_GRANTED);
                EventBus.getDefault().post(event);
            }
        } else if (requestCode == SDK_PERMISSION_REQUEST) {
            getLocation();
        }
    }

    private void getLocation() {
        location(MainActivity.this);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
//            appSession.setBdLocation(location);
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\noperationers:");
                sb.append(location.getOperators());
            }
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            // Log.i("province",location.getProvince()+"");
            if (location.getCity() != null) {
                PrefUtils.putString(MyGlobal.context, "locationCity", location.getCity());
                PrefUtils.putString(MyGlobal.context, "Latitude", lat + "");
                PrefUtils.putString(MyGlobal.context, "Longitude", lon + "");
                if ("重新".equals(eventS)) {
                    EventBus.getDefault().post(new RegisterCityLocationEvent());
                }
            }
            //Log.i("lat", "" + lat);
            // Log.i("lon",""+lon);
            //Log.i("BaiduLocationApiDem", sb.toString());
        }
    }

    /**
     * 用这个方法的时候要注意ct是 调用这个函数类的context，方法的参数应该是this.getApplication传到这个方法里面去，而不是这个类的context，一定要注意。
     *
     * @param ct
     */
    public void location(Context ct) {
        mLocationClient = new LocationClient(ct);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);// 中文地址
        option.setCoorType("bd09ll");// gcj02 国测局经纬度坐标系 ；bd09 百度墨卡托坐标系；bd09ll
        // 百度经纬度坐标系
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);// 设置定位模式
        option.setScanSpan(1000);//检查周期 小于1秒的按1秒
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new MyLocationListener());
        mLocationClient.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(MainLocationEvent event) {
        eventS = event.getS();
        location(MainActivity.this);
    }


    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
//        locationService.unregisterListener(mListener); //注销掉监听
//        locationService.stop(); //停止定位服务
        super.onStop();
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
//    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            // TODO Auto-generated method stub
//            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
//                StringBuffer sb = new StringBuffer(256);
//                sb.append("time : ");
//                /**
//                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
//                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
//                 */
//                sb.append(location.getTime());
//                sb.append("\nlocType : ");// 定位类型
//                sb.append(location.getLocType());
//                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
//                sb.append(location.getLocTypeDescription());
//                sb.append("\nlatitude : ");// 纬度
//                sb.append(location.getLatitude());
//                sb.append("\nlontitude : ");// 经度
//                sb.append(location.getLongitude());
//                sb.append("\nradius : ");// 半径
//                sb.append(location.getRadius());
//                sb.append("\nCountryCode : ");// 国家码
//                sb.append(location.getCountryCode());
//                sb.append("\nCountry : ");// 国家名称
//                sb.append(location.getCountry());
//                sb.append("\ncitycode : ");// 城市编码
//                sb.append(location.getCityCode());
//                sb.append("\ncity : ");// 城市
//                sb.append(location.getCity());
//                sb.append("\nDistrict : ");// 区
//                sb.append(location.getDistrict());
//                sb.append("\nStreet : ");// 街道
//                sb.append(location.getStreet());
//                sb.append("\naddr : ");// 地址信息
//                sb.append(location.getAddrStr());
//
//                updateLocation(location.getLongitude(), location.getLatitude(), location.getCity() + location.getDistrict() + location.getStreet());
//            }
//        }
//    };
    private void updateLocation(double lng, double lat, String addr) {
        if (!"6".equals(myglobal.user.userType)) return;
        if ("1".equals(myglobal.user.isOnline)) {
            if (ServerUrl.isNetworkConnected(this)) {
                try {
                    HashMap<String, String> fields = new HashMap<String, String>();
                    fields.put("token", myglobal.user.token);
                    fields.put("lng", lng + "");
                    fields.put("lat", lat + "");
                    fields.put("location", addr);
                    postMap(ServerUrl.updateLocation, fields, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
        }
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {

                        } else if ("401".equals(status)) {
                            String message = result.get("message") + "";
                            ToastUtil.showToast(message);
                            Intent it = new Intent(mContext, LoginActivity.class);
                            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            it.putExtra("type", 1);
                            startActivity(it);
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
    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                ToastUtil.showToast("再按一次退出程序");
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getVersion() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("type", "1");
                postMap(ServerUrl.version_path, fields, versionHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //新版本号和描述语言
    private String size;
    private String update_versionName;
    private String update_describe;
    private String appurl;
    private boolean is_update = false;
    public LoadDialog dialog;
    private Handler versionHandler = new Handler() {
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
                            String newestForce = data.getString("newestForce");
                            Loadinfo info = gson.fromJson(data.getString("versionInfo"), Loadinfo.class);
                            size = info.getSize();
                            update_versionName = info.getVersion();
                            update_describe = info.getContent();
                            appurl = info.getUrl();
                            //判断是否强制更新
                            if (!TextUtils.isEmpty(newestForce) && Utils.compareVersion(newestForce, Utils.GetVersion(mContext)) > 0) {
                                is_update = false;
                            } else {
                                is_update = true;
                            }

                            if (Utils.compareVersion(update_versionName, Utils.GetVersion(mContext)) > 0) {

                                //是否强制更新
                                if (is_update) {
                                    if (Utils.IsLoad(mContext)) {
                                        return;
                                    }
                                }
                                showNoticeDialog(is_update, info);
                            }
                            return;
                        } else {
                            if (mContext != null) {
                                String message = result.get("message") + "";
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 显示强制提示更新对话框
     */
    public void showNoticeDialog(boolean iscompel, final Loadinfo loadinfo) {
        dialog = new LoadDialog(mContext, iscompel);
        dialog.setMessage("最新版本：V" + loadinfo.getVersion() + "\n" +
                "新版本大小：" + loadinfo.getSize() + "M\n" +
                "新版本特性：\n" + loadinfo.getContent());
        dialog.setYesOnclickListener("", new LoadDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                dialog.dismiss();
                showDownloadDialog(loadinfo.getUrl(), MainActivity.this);
            }
        });
        dialog.setNoOnclickListener(new LoadDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                Utils.setIsLoad(mContext, true);
                dialog.dismiss();
            }
        });
        dialog.setClosenclickListener(new LoadDialog.onCloseOnclickListener() {
            @Override
            public void onCloseClick() {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 显示下载进度对话框
     */
    public void showDownloadDialog(String apk_path, MainActivity activity) {
        new UpdateAppManager(mContext);
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        new UpdateAppManager.downloadAsyncTask(progressDialog, apk_path, activity).execute();
    }
}
