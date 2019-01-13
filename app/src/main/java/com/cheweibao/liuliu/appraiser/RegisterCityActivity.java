package com.cheweibao.liuliu.appraiser;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.adapter.MyRegisterCityAdapter;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.GetJsonDataUtil;
import com.cheweibao.liuliu.common.HanziToPinyin;
import com.cheweibao.liuliu.common.PrefUtils;
import com.cheweibao.liuliu.data.Citys;
import com.cheweibao.liuliu.data.JsonBean;
import com.cheweibao.liuliu.db.LocalCityTable;
import com.cheweibao.liuliu.event.JingZhenGuEvent;
import com.cheweibao.liuliu.event.MainLocationEvent;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cheweibao.liuliu.common.MyGlobal.context;

/**
 * Created by user on 2018/4/9.
 */
public class RegisterCityActivity extends BaseActivity {
    private static ArrayList<JsonBean> options1Items = new ArrayList<>();
    private static ArrayList<ArrayList<Citys>> options2Items = new ArrayList<>();

    @Bind(R.id.tv_dingwei_des)
    TextView tvDingweiDes;
    @Bind(R.id.side_bar)
    WaveSideBar sideBar;
    @Bind(R.id.img_dingwei)
    ImageView imgDingwei;
    private List<Citys> registerCityList = new ArrayList<>();
    private MyRegisterCityAdapter mAdapter;
    @Bind(R.id.register_city_listview)
    ListView registerCityListview;

    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    private boolean first = true;
    private int currentK;
    private Citys locationCity = new Citys();
    private LinearInterpolator interpolator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_city);
        ButterKnife.bind(this);
        registerCityList = LocalCityTable.getInstance().getAllCity();
        initViews();
        //设置匀速旋转，在xml文件中设置会出现卡顿
        interpolator = new LinearInterpolator();
    }

    private void initViews() {
        locationCity = myglobal.locationCity;
        tvDingweiDes.setText(locationCity.getName());

        tvTopTitle.setText("选择城市");
        if (registerCityList.size() == 0) {
            initJsonData();
            for (int l = 0; l < options2Items.size(); l++) {
                for (int m = 0; m < options2Items.get(l).size(); m++) {
                    registerCityList.add(options2Items.get(l).get(m));
                }
            }
        }
        HashSet h = new HashSet(registerCityList);
        registerCityList.clear();
        registerCityList.addAll(h);
//        Comparator cmp = Collator.getInstance(Locale.CHINA);
//        Collections.sort(registerCityList, cmp);
        sort();
        setAdapter(registerCityList);


        sideBar.setTextColor(Color.parseColor("#FF666666"));
        sideBar.setMaxOffset(100);
        sideBar.setLazyRespond(true);
        sideBar.setIndexItems("定位", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P",
                "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                if (index.equals("定位")) {
                    registerCityListview.setSelection(0);
                    return;
                }
                first = true;
                if (registerCityList.size() > 0) {
                    for (int k = 0; k < registerCityList.size(); k++) {
                        if (index.equals(getLetter(registerCityList.get(k).getName()))) {

                            if (first) {
                                currentK = k;
                                registerCityListview.setSelection(currentK);
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

    public void sort() {
        // 排序
//        Collections.sort(registerCityList, new Comparator<String>() {
//            @Override
//            public int compare(String lhs, String rhs) {
//                if (getLetter(lhs).equals(getLetter(rhs))) {
//                    return lhs.compareTo(rhs);
//                } else {
//                    if ("#".equals(getLetter(lhs))) {
//                        return 1;
//                    } else if ("#".equals(getLetter(rhs))) {
//                        return -1;
//                    }
//                    return getLetter(lhs).compareTo(getLetter(rhs));
//                }
//            }
//        });
        Collections.sort(registerCityList, new Comparator<Citys>() {
            @Override
            public int compare(Citys lhs, Citys rhs) {
                if (getLetter(lhs.getName()).equals(getLetter(rhs.getName()))) {
                    return lhs.getName().compareTo(rhs.getName());
                } else {
                    if ("#".equals(getLetter(lhs.getName()))) {
                        return 1;
                    } else if ("#".equals(getLetter(rhs.getName()))) {
                        return -1;
                    }
                    return getLetter(lhs.getName()).compareTo(getLetter(rhs.getName()));
                }
            }
        });
    }

    @OnClick({R.id.tv_dingwei_des, R.id.lin_repositioning, R.id.ivTopBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_dingwei_des:  //定位的描述
                EventBus.getDefault().post(new JingZhenGuEvent(locationCity));
                finish();
                break;
            case R.id.lin_repositioning:  //重新定位
                EventBus.getDefault().post(new MainLocationEvent("重新"));
                Animation circle_anim = AnimationUtils.loadAnimation(context, R.anim.anim_dingwei_rote);
                circle_anim.setInterpolator(interpolator);
                if (circle_anim != null) {
                    imgDingwei.startAnimation(circle_anim);  //开始动画
                }
                String cityname = PrefUtils.getString(context, "locationCity", "");
                locationCity.setName(cityname);
                locationCity.setId("");
                tvDingweiDes.setText(locationCity.getName());
                EventBus.getDefault().post(new JingZhenGuEvent(locationCity));
                break;
            case R.id.ivTopBack:
                finish();
                break;
        }
    }


    private void setAdapter(List<Citys> registerCityList) {
        mAdapter = new MyRegisterCityAdapter(registerCityList);
        mAdapter.setCallback(new MyRegisterCityAdapter.RegisterCityCallback() {
            @Override
            public void onRegister(Citys s) {
                EventBus.getDefault().post(new JingZhenGuEvent(s));
                finish();
            }
        });
        registerCityListview.setAdapter(mAdapter);
    }


    private static void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(context, "province.json");
        //获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<Citys> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                Citys CityName = new Citys();
                CityName.setName(jsonBean.get(i).getCityList().get(c).getName());
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size();
                         d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

        }


    }

    public static ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
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
            String letter;
            if ("重".equals(l.get(0).source)) {
                letter = "C";
            } else {
                HanziToPinyin.Token token = l.get(0);
                // toLowerCase()返回小写， toUpperCase()返回大写
                letter = token.target.substring(0, 1).toUpperCase();
            }
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
    }
}
