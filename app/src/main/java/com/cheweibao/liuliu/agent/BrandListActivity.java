package com.cheweibao.liuliu.agent;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.google.gson.Gson;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.adapter.MyBrandHeadAdapter;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.HanziToPinyin;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.common.PrefUtils;
import com.cheweibao.liuliu.data.BrandHeadInfo;
import com.cheweibao.liuliu.data.BrandInfo;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.dialog.CarDialog;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.DensityUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cheweibao.liuliu.R.id.car_type_listview;

public class BrandListActivity extends BaseActivity {
    RecyclerView brandRecyclerview;

    @Bind(R.id.listview)
    ListView listview;

    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.rela_city_type_list)
    RelativeLayout relaCityTypeList;
    @Bind(car_type_listview)
    ListView carTypeListview;
    @Bind(R.id.ll_car_type_empty)
    LinearLayout llCarTypeEmpty;
    @Bind(R.id.side_bar)
    WaveSideBar sideBar;
    @Bind(R.id.img_car_type_biaoti)
    ImageView imgCarTypeBiaoti;
    @Bind(R.id.tv_car_type_biaoti)
    TextView tvCarTypeBiaoti;
    @Bind(R.id.lin_car_type_biaoti)
    LinearLayout linCarTypeBiaoti;
    @Bind(R.id.view)
    View view;

    private MyAdapter mAdapter;
    private View headView;

    ArrayList<BrandInfo> brandList;
    ArrayList<BrandHeadInfo> brandHeadList;
    ArrayList<BrandInfo> brandCarTypeList = new ArrayList<>();
    int baoma = R.drawable.baoma;
    int baoshijie = R.drawable.baoshijie;
    int benchi = R.drawable.benchi;
    int bentian = R.drawable.bentian;
    int aerfaluomiou = R.drawable.aerfaluomiou;
    int asidunmading = R.drawable.asidunmading;
    int aodi = R.drawable.aodi;
    int baojun = R.drawable.baojun;

    private int currentK;
    private boolean first = true;
    private MyCarTypeAdapter mCarTypeAdapter;
    private int statusBarHeight;
    private CarDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initViews();
    }

    private void initViews() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        //让侧边栏阴影消失
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        tvTopTitle.setText("选择品牌");
        headView = LayoutInflater.from(this).inflate(R.layout.brand_head_view, null);
        brandRecyclerview = (RecyclerView) headView.findViewById(R.id.brand_recyclerview);

        sideBar.setTextColor(Color.parseColor("#FF666666"));
        sideBar.setMaxOffset(100);
        sideBar.setLazyRespond(true);
        sideBar.setIndexItems("热门", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P",
                "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                if (index.equals("热门")) {
                    listview.setSelection(0);
                    return;
                }
                first = true;
                if (brandList.size() > 0) {
                    for (int k = 0; k < brandList.size(); k++) {
                        if (index.equals(brandList.get(k).initial)) {
                            if (first) {
                                currentK = k;
                                listview.setSelection(currentK + 1);
                            }
                            first = false;
                        }
                    }
                } else {
                    return;
                }

            }
        });

    }


    private void getHeadInfo() {
        brandHeadList.add(new BrandHeadInfo(baoma, "宝马"));
        brandHeadList.add(new BrandHeadInfo(baoshijie, "保时捷"));
        brandHeadList.add(new BrandHeadInfo(benchi, "奔驰"));
        brandHeadList.add(new BrandHeadInfo(bentian, "本田"));
        brandHeadList.add(new BrandHeadInfo(aerfaluomiou, "阿尔法·罗密欧"));
        brandHeadList.add(new BrandHeadInfo(asidunmading, "阿斯顿·马丁"));
        brandHeadList.add(new BrandHeadInfo(aodi, "奥迪"));
        brandHeadList.add(new BrandHeadInfo(baojun, "宝骏"));

        MyBrandHeadAdapter myBrandHeadAdapter = new MyBrandHeadAdapter(brandHeadList);
        brandRecyclerview.setAdapter(myBrandHeadAdapter);
        //接口回调,listview的头的点击事件
        myBrandHeadAdapter.setOnItemClickListener(new MyBrandHeadAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (brandList.size() > 0) {
                    for (int m = 0; m < brandList.size(); m++) {
                        if (brandHeadList.get(position).getText().equals(brandList.get(m).name)) {
                            tvCarTypeBiaoti.setText(brandList.get(m).name);
                            getCarTypeInfo(brandList.get(m).id);
                            drawerLayout.openDrawer(relaCityTypeList);

                            break;
                        }
                    }
                }
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        brandRecyclerview.setLayoutManager(layoutManager);
    }

    private void getInfo() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                postMap(ServerUrl.getBrandList, fields, handler);
                showProgress();
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
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            Gson gson = new Gson();
                            JSONObject data = JSON.parseObject(gson.toJson(result.get("data")));
                            JSONArray list = data.getJSONArray("list");

                            if (list != null) {
                                for (int i = 0; i < list.size(); i++) {
                                    brandList.add(new BrandInfo((JSONObject) list.get(i)));
                                }
                            }

                            setAdapter();
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
        }


    };

    /**
     * 获取首页数据
     */
    private void setAdapter() {
        mAdapter = new MyAdapter();
        listview.setAdapter(mAdapter);
        listview.addHeaderView(headView);

    }

    private Handler mHandler = new Handler();


    @OnClick({R.id.ivTopBack, R.id.img_car_type_biaoti})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTopBack:
                if (dialog != null) {
                    dialog.dismiss();
                }
                finish();
                break;
            case R.id.img_car_type_biaoti:
                view.setVisibility(View.GONE);
                linCarTypeBiaoti.setVisibility(View.GONE);
                carTypeListview.setVisibility(View.GONE);
                drawerLayout.closeDrawer(relaCityTypeList);
                break;
        }
    }


    /**
     * 首页选择品牌的adapter
     */
    private class MyAdapter extends ArrayAdapter<BrandInfo> {
        public MyAdapter() {
            super(mContext, R.layout.layout_index_list_item, brandList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.layout_index_list_item, null);
            }
            holder = getHolder(convertView);
            final BrandInfo item = getItem(position);
            holder.nameTextView.setText(item.name);
            Glide.with(mContext)
                    .load(item.logo)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.ivLogo);
            String currIndexWord = item.initial;

            if (position > 0) {
                String lastIndexWord = getItem(position - 1).initial;
                if (lastIndexWord.equals(currIndexWord)) {
                    holder.indexTextView.setVisibility(View.GONE);
                } else {
                    holder.indexTextView.setVisibility(View.VISIBLE);
                    holder.indexTextView.setText(currIndexWord);
                }
            } else {
                holder.indexTextView.setVisibility(View.VISIBLE);
                holder.indexTextView.setText(currIndexWord);
            }

            holder.ivLogo.setVisibility(View.VISIBLE);

            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  Intent it = new Intent(mContext, CarTypeListActivity.class);
                    it.putExtra("parentid", item.id);
                    startActivity(it);*/
                    getCarTypeInfo(item.id);
                    tvCarTypeBiaoti.setText(item.name);
                    drawerLayout.openDrawer(relaCityTypeList);
                }
            });
            return convertView;
        }

        private ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.llItem = (LinearLayout) convertView.findViewById(R.id.ll_item);
                viewHolder.indexTextView = (TextView) convertView.findViewById(R.id.index);
                viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.name);
                viewHolder.ivLogo = (ImageView) convertView.findViewById(R.id.iv_logo);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }

        private ViewHolder holder;

        class ViewHolder {
            LinearLayout llItem;
            TextView indexTextView;
            TextView nameTextView;
            ImageView ivLogo;
        }
    }


    private void getCarTypeInfo(String id) {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("parentid", id);
                postMap(ServerUrl.getCarTypeList, fields, carTypeHandler);
                showProgress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            shortToast("网络连接不可用");
        }
    }

    private Handler carTypeHandler = new Handler() {
        public void handleMessage(Message msg) {
            hideProgress();
            switch (msg.what) {
                case 0:
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            Gson gson = new Gson();
                            JSONObject data = JSON.parseObject(gson.toJson(result.get("data")));
                            JSONArray list = data.getJSONArray("list");

                            if (list != null) {
                                brandCarTypeList.clear();
                                for (int i = 0; i < list.size(); i++) {
                                    brandCarTypeList.add(new BrandInfo((JSONObject) list.get(i)));
                                }
                            }
                            setCarTypeAdapter();
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
        }
    };

    private void setCarTypeAdapter() {
        if (brandList.size() < 1) {
            llCarTypeEmpty.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
            carTypeListview.setVisibility(View.VISIBLE);
            linCarTypeBiaoti.setVisibility(View.VISIBLE);
            llCarTypeEmpty.setVisibility(View.GONE);
            mCarTypeAdapter = new MyCarTypeAdapter();
            carTypeListview.setAdapter(mCarTypeAdapter);
        }


    }

    /**
     * 侧滑出来的第一个列表的adapter
     */
    private class MyCarTypeAdapter extends ArrayAdapter<BrandInfo> {

        public MyCarTypeAdapter() {
            super(mContext, R.layout.layout_car_type_item, brandCarTypeList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.layout_car_type_item, null);
            }
            ViewHolder holder = getHolder(convertView);
            final BrandInfo item = getItem(position);
            holder.nameTextView.setText(item.fullname);

            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  startActivity(new Intent(BrandListActivity.this,CarListActivity.class)
                  .putExtra("parentid",brandCarTypeList.get(position).id
                    ).putExtra("fullname",brandCarTypeList.get(position).fullname));*/
                    dialog = new CarDialog(BrandListActivity.this,
                            R.style.dialog, brandCarTypeList.get(position).id, brandCarTypeList.get(position).fullname);
                    dialog.show();//显示对话框
                    Window window = dialog.getWindow();
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                    lp.height = display.getHeight() - DensityUtils.dip2px(MyGlobal.context, 50) -
                            statusBarHeight;
                    lp.width = display.getWidth();
                    window.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                    dialog.getWindow().setAttributes(lp);
                   /* Window window = dialog.getWindow();
                    WindowManager.LayoutParams lp = window.getAttributes();
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height =display.getHeight()-50 ;
                    window.setGravity(Gravity.RIGHT);
                    window.setAttributes(lp);*/
                    PrefUtils.putString(MyGlobal.context, "logo", brandCarTypeList.get(position)
                            .logo);

                }
            });
            return convertView;
        }


        private ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.nameTextView = (TextView) convertView.findViewById(R.id
                        .tv_car_type_item);
                viewHolder.llItem = (LinearLayout) convertView.findViewById(R.id.lin_car_type_item);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }

        private ViewHolder holder;

        class ViewHolder {
            TextView nameTextView;
            LinearLayout llItem;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isMsgOf(MyConstants.SEL_CAR_NAME)) {

            finish();
        }

    }

    protected void onResume() {
        super.onResume();

        if (brandList == null || brandList.size() < 1) {
            brandList = new ArrayList<>();
            getInfo();
        }
        if (brandHeadList == null || brandHeadList.size() < 1) {
            brandHeadList = new ArrayList<>();
            getHeadInfo();
        }
    }


    /**
     * 根据字符串获取当前首字母
     *
     * @param name
     * @return
     */
    private String getLetter(String name) {
        String DefaultLetter = "#";
        if (TextUtils.isEmpty(name)) {
            return DefaultLetter;
        }
        char char0 = name.toLowerCase().charAt(0);
        if (Character.isDigit(char0)) {
            return DefaultLetter;
        }
        ArrayList<HanziToPinyin.Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
        if (l != null && l.size() > 0 && l.get(0).target.length() > 0) {
            HanziToPinyin.Token token = l.get(0);
            // toLowerCase()返回小写， toUpperCase()返回大写
            String letter = token.target.substring(0, 1).toUpperCase();
            char c = letter.charAt(0);
            // 这里的 'a' 和 'z' 要和letter的大小写保持一直。
            if (c < 'A' || c > 'Z') {
                return DefaultLetter;
            }
            return letter;
        }
        return DefaultLetter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
