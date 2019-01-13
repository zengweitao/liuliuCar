package com.cheweibao.liuliu.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.GetJsonDataUtil;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.data.JsonBean;
import com.cheweibao.liuliu.data.Province;
import com.cheweibao.liuliu.db.LocalCityTable;
import com.google.gson.Gson;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cheweibao.liuliu.common.MyGlobal.context;

/**
 * Created by sx on 2017/1/9.
 */
public class SelectAreaPopWindow extends PopupWindow {


    @Bind(R.id.tv_cancel)
    TextView tvCancel;
    @Bind(R.id.tv_confirm)
    TextView tvConfirm;
    @Bind(R.id.wv_province)
    WheelView wvProvince;
    @Bind(R.id.wv_city)
    WheelView wvCity;
    @Bind(R.id.wv_districts)
    WheelView wvDistricts;
    private Context mContext;
    private int style = -1;
    List<String> provinces = new ArrayList<>();
    HashMap<String, List<String>> citys = new HashMap<>();
    HashMap<String, List<String>> districts = new HashMap<>();

    public SelectAreaPopWindow(final Context context) {
        super(context);
        mContext = context;
        //pop_bottom为自定义布局
        View view = LayoutInflater.from(context).inflate(R.layout.pop_select_area, null);
        //设置PopupWindow的View
        setContentView(view);
        ButterKnife.bind(this, view);

        initPopWindow();
        initWheelViewStyle();
        initWheelViewDate();
    }

    public SelectAreaPopWindow(final Context context, int style) {
        super(context);
        mContext = context;
        this.style = style;
        //pop_bottom为自定义布局
        View view = LayoutInflater.from(context).inflate(R.layout.pop_select_area, null);
        //设置PopupWindow的View
        setContentView(view);
        ButterKnife.bind(this, view);

        initPopWindow();
        initWheelViewStyle();
        initWheelViewDate();
    }


    /**
     * 设置popwindow
     */
    private void initPopWindow() {

        //设置PopupWindow弹出窗体的宽
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        setFocusable(true);
        //设置PopupWindow弹出窗体动画效果
        setAnimationStyle(R.style.popupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        getBackground().setAlpha(0);
        final Activity activity = (Activity) mContext;
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 1;
        activity.getWindow().setAttributes(params);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        setOutsideTouchable(true);

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
    }

    /**
     * 初始化wheelview样式
     */
    private void initWheelViewStyle() {
        //样式配置
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.textColor = Color.BLACK;
        style.selectedTextSize = 18;////单位dp
        style.holoBorderColor = Color.parseColor("#f4f4f4");
        style.textColor = Color.parseColor("#999999");
        style.selectedTextColor = Color.BLACK;

        //设置样式
        wvProvince.setWheelAdapter(new ArrayWheelAdapter(mContext)); // 文本数据源
        wvProvince.setSkin(WheelView.Skin.Holo); // holo皮肤
        wvProvince.setStyle(style);

        wvCity.setWheelAdapter(new ArrayWheelAdapter(mContext)); // 文本数据源
        wvCity.setSkin(WheelView.Skin.Holo);
        wvCity.setStyle(style);

        wvDistricts.setWheelAdapter(new ArrayWheelAdapter(mContext)); // 文本数据源
        wvDistricts.setSkin(WheelView.Skin.Holo);
        wvDistricts.setStyle(style);
    }

    /**
     * 设置wheelview数据
     */
    private void initWheelViewDate() {
        MyGlobal myglobal = (MyGlobal) mContext.getApplicationContext();
//        List<String> provinces = createMainDatas();
//        HashMap<String, List<String>> citys = createSubDatas();
//        HashMap<String, List<String>> districts = createDistrictsDatas();
        provinces = myglobal.provinces;
        citys = myglobal.citys;
        districts = myglobal.districts;

        if (provinces.size() < 1 || citys.size() < 1 || districts.size() < 1) {
//            dismiss();
            initJsonData();
            return;
        }


        wvProvince.setWheelData(provinces);
        //联动城市wheelview
        wvCity.setWheelData(citys.get(provinces.get(wvProvince.getSelection())));
        wvDistricts.setWheelData(districts.get(provinces.get(wvCity.getSelection())));

        wvCity.join(wvDistricts);
        wvCity.joinDatas(districts);
        wvProvince.join(wvCity);
        wvProvince.joinDatas(citys);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_confirm})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                if (onSelectListener != null) {
                    onSelectListener.selectArea(style, wvProvince.getSelectionItem().toString(), wvCity
                            .getSelectionItem().toString(), wvDistricts.getSelectionItem().toString());
                    dismiss();
                }
                break;
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

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    private OnSelectListener onSelectListener;

    public interface OnSelectListener {
        void selectArea(int style, String province, String city, String districts);
    }

    private void initJsonData() {//解析数据
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

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            provinces.add(jsonBean.get(i).getName());
            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
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
                districts.put(CityName, City_AreaList);
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            citys.put(jsonBean.get(i).getName(), CityList);
        }
        if (provinces.size() < 1 || citys.size() < 1 || districts.size() < 1) {
            dismiss();
            return;
        }


        wvProvince.setWheelData(provinces);
        //联动城市wheelview
        wvCity.setWheelData(citys.get(provinces.get(wvProvince.getSelection())));
        wvDistricts.setWheelData(districts.get(provinces.get(wvCity.getSelection())));

        wvCity.join(wvDistricts);
        wvCity.joinDatas(districts);
        wvProvince.join(wvCity);
        wvProvince.joinDatas(citys);
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
}
