package com.cheweibao.liuliu.appraiser;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.agent.BrandListActivity;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.SoftkeyboardUtil;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.data.Province;
import com.cheweibao.liuliu.db.LocalCityTable;
import com.cheweibao.liuliu.event.JingZhenGuEvent;
import com.cheweibao.liuliu.event.ResultBackEvent;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.popup.SelectAreaPopWindow;
import com.cheweibao.liuliu.ui.ClickEffectImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 123456 on 2018/3/7.
 */

public class JingZhenGuActivity extends BaseActivity implements SelectAreaPopWindow.OnSelectListener, TextView.OnEditorActionListener {
    private long mExitTime;
    private DrawerLayout drawerLayout;
    String typeShort = "";
    int nYear = 0, nMonth = 0;

    String carId;
    @Bind(R.id.ivTopBack)
    ClickEffectImageView ivTopBack;
    @Bind(R.id.tv_pingg_models)
    TextView tvPinggModels;
    @Bind(R.id.rela_pingg_models)
    RelativeLayout relaPinggModels;
    @Bind(R.id.tv_pingg_time)
    TextView tvPinggTime;
    @Bind(R.id.tv_pingg_city)
    TextView tvPinggCity;
    @Bind(R.id.et_invest_dialog)
    EditText etInvestDialog;
    @Bind(R.id.rela_pingg_time)
    RelativeLayout relaPinggTime;
    @Bind(R.id.rela_pingg_city)
    RelativeLayout relaPinggCity;
    @Bind(R.id.rela_pingg_mileage)
    RelativeLayout relaPinggMileage;
    Intent intent;
    @Bind(R.id.btn_pingg)
    Button btnPingg;
    @Bind(R.id.ll_main)
    RelativeLayout llMain;
    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    private InputMethodManager imm;
    private boolean dralayoutB = true;

    private String digits = ".";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jingzhengu);
        ButterKnife.bind(this);
        ivTopBack.setVisibility(View.GONE);
        EventBus.getDefault().register(this);
        initView();

    }

    private void initView() {

        //禁掉软键盘
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        initEdittext();
    }

    private void initEdittext() {
        //给Edittext设置输入限制
        etInvestDialog.setOnEditorActionListener(this);
        etInvestDialog.setSelection(etInvestDialog.getText().length());
        tvTopTitle.setText("选择品牌");
        etInvestDialog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (view.getId()) {
                        case R.id.et_invest_dialog:
                            etInvestDialog.setFocusable(true);
                            etInvestDialog.setFocusableInTouchMode(true);
                            etInvestDialog.requestFocus();
                            break;
                    }
                }
                return false;
            }
        });
        etInvestDialog.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private String old;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                old = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable)) {
                    String s = temp.toString();
                    if (s.contains(".")) {
                        String[] split = s.split("\\.");
                        if (split.length > 1) {
                            if ((split[1].length() > 2)) {
                                etInvestDialog.setText(old);
                                etInvestDialog.setSelection(s.length() - 1);//将光标移至文字末尾
                            }
                        }
                    } else {
                        if (s.length() > 3) {
                            etInvestDialog.setText(old);
                            etInvestDialog.setSelection(s.length() - 1);//将光标移至文字末尾
                        }

                    }
                }
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() == R.id.et_invest_dialog) {
            if (i == EditorInfo.IME_ACTION_DONE) {
                etInvestDialog.setFocusable(true);
                etInvestDialog.setFocusableInTouchMode(true);
                etInvestDialog.requestFocus();
                imm.showSoftInput(etInvestDialog, 0);
                submit();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }

/*
    @OnClick({R.id.ivTopBack, R.id.jz_car_area, R.id.jz_ll_type, R.id.jz_date, R.id.btn_ok, R.id
            .btn_skip})
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.ivTopBack:
                if (selectAreaPopWindow != null && selectAreaPopWindow.isShowing()) {
                    selectAreaPopWindow.dismiss();
                }
                finish();
                break;
            case R.id.jz_ll_type:  //车辆型号
                it = new Intent(mContext, BrandListActivity.class);
                startActivity(it);
                break;
            case R.id.jz_date:  //选择日期
                setDate();
                break;
            case R.id.jz_car_area:  //车牌归属地
                if (!ButCommonUtils.isFastDoubleClick()) {
                    if (LocalCityTable.getInstance().getAllProvince().size() == 0) {
                        getCityInfo();
                    } else {
                        if (selectAreaPopWindow != null && selectAreaPopWindow.isShowing()) return;
                        selectAreaPopWindow = new SelectAreaPopWindow(mContext);
                        selectAreaPopWindow.setOnSelectListener(this);
                        selectAreaPopWindow.showAtLocation(llMain, Gravity.BOTTOM | Gravity
                                .CENTER_HORIZONTAL, 0, 0);
                    }
                }
                break;
            case R.id.btn_ok:
                submit();
                break;
            case R.id.btn_skip:
                it = new Intent(mContext, AgentPjOrderDetailActivity.class);
                startActivity(it);
                break;
        }
    }*/


    //点击区域 软键盘消失
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View view = getCurrentFocus();
            if (SoftkeyboardUtil.isHideInput(view, ev)) {
                SoftkeyboardUtil.hideSoftInput(view, JingZhenGuActivity.this);
                //让editText 失去焦点
                etInvestDialog.setFocusable(false);
                etInvestDialog.setFocusableInTouchMode(false);
            }
        }
        return super.dispatchTouchEvent(ev);


    }

    private void submit() {
        String date = tvPinggTime.getText().toString().trim();
        String mileage = etInvestDialog.getText().toString().trim();
        String type = tvPinggModels.getText().toString().trim();
        String city = tvPinggCity.getText().toString().trim();
        if (type.equals("选择品牌") || date.equals("选择日期") || city.equals("选择城市") || mileage.length() == 0) {
            shortToast("完善信息后才可查询");
            return;
        }

        String cityId = LocalCityTable.getInstance().getCityIdByCityName(city);
        Bundle bundle = new Bundle();
        //TrimId:车型 Id BuyCarDate:上牌时间，格式 yyyy-MM-dd Mileage:行驶里程(公里)
        // ColorId:车辆颜色 Id CityId:城市 Id
        bundle.putString("trimId", carId);
        bundle.putString("buyCarDate", date);
        bundle.putString("mileage", mileage);
        bundle.putString("cityId", cityId);
        bundle.putString("city", city);
        bundle.putString("type", type);

        //put("colorId",String.valueOf(1));
        //Intent it = new Intent(mContext, JingZhenGuNativeResultActivity.class);
       /* HashMap<String,String> fields = new HashMap(){
            {
                //TrimId:车型 Id BuyCarDate:上牌时间，格式 yyyy-MM-dd Mileage:行驶里程(公里)
                // ColorId:车辆颜色 Id CityId:城市 Id
                put("trimId",carId);
                put("buyCarDate",date);
                put("mileage",mileage);
//                put("colorId",String.valueOf(1));
                String cityId = LocalCityTable.getInstance().getCityIdByCityName(city);
                put("cityId",cityId);
                put("city",city);
                put("type",type);
            }
        };*/
        Intent it = new Intent(mContext, JingZhenGuNativeResultActivity.class);
        // it.putExtra("carInfo",fields);
        it.putExtras(bundle);
        startActivity(it);

        //getMap(ServerUrl.appraisalCar,appraisalCallback);
        //postMap(ServerUrl.appraisalCar,fields,appraisalCallback);
    }


    @SuppressLint("NewApi")
    private void setDate() {
        if (nYear <= 0) {
            setToday();
        } else {
            MyDatePicker dialogFragment = new MyDatePicker(nYear, nMonth - 1);
            dialogFragment.show(getFragmentManager(), "date_picker");
        }
    }

    @SuppressLint("NewApi")
    private void setToday() {

        Calendar c = Calendar.getInstance();
        int startYear = c.get(Calendar.YEAR);
        int startMonth = c.get(Calendar.MONTH);

        MyDatePicker dialogFragment = new MyDatePicker(startYear, startMonth);
        dialogFragment.show(getFragmentManager(), "date_picker");
    }

    @OnClick({R.id.ivTopBack, R.id.rela_pingg_models, R.id.rela_pingg_time, R.id.rela_pingg_city,
            R.id.btn_pingg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivTopBack:
                break;
            case R.id.rela_pingg_models:  //车型
                intent = new Intent(mContext, BrandListActivity.class);
                startActivity(intent);
                break;
            case R.id.rela_pingg_time:  //日期
                initPickerTime();
                break;
            case R.id.rela_pingg_city:  //城市
                if (!ButCommonUtils.isFastDoubleClick()) {
                    if (LocalCityTable.getInstance().getAllProvince().size() == 0) {
                        getCityInfo();
                    } else {
                        intent = new Intent(mContext, RegisterCityActivity.class);
                        startActivity(intent);
                    }
                }


                break;
            case R.id.btn_pingg:
                submit();
                break;
        }
    }

    private void initPickerTime() {
        Calendar selectedDate = Calendar.getInstance();
        //年
        int year = selectedDate.get(Calendar.YEAR);
        //月
        int month = selectedDate.get(Calendar.MONTH);
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2000, 1, 0);
        endDate.set(year, month, 1);
        selectedDate.set(Calendar.YEAR, 2015);
        selectedDate.set(Calendar.MONTH, 5);
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView
                .OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                String format = simpleDateFormat.format(date);

                tvPinggTime.setText(format);
                tvPinggTime.setTextColor(Color.parseColor("#FF666666"));
            }
        })//
                .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(16)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setTitleText("选择日期")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#0DBDB2"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#0DBDB2"))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.TRANSPARENT)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvTime.show();

    }


    @SuppressLint({"NewApi", "ValidFragment"})
    public class MyDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private int year, month;

        @SuppressLint("ValidFragment")
        public MyDatePicker(int nYear, int nMonth) {
            this.year = nYear;
            this.month = nMonth;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            DatePickerDialog dialog = new DatePickerDialog(mContext, android.R.style
                    .Theme_Holo_Light_Dialog, this, year, month, 1);
            if (((ViewGroup) ((ViewGroup) dialog.getDatePicker().getChildAt(0)).getChildAt(0))
                    .getChildAt(2) != null) {
                ((ViewGroup) ((ViewGroup) dialog.getDatePicker().getChildAt(0)).getChildAt(0))
                        .getChildAt(2).setVisibility(View.GONE);
            }
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            nYear = year;
            nMonth = monthOfYear + 1;

            Calendar c = Calendar.getInstance();
            int startYear = c.get(Calendar.YEAR);
            int startMonth = c.get(Calendar.MONTH);
            if (nYear > startYear || (nYear == startYear && nMonth > startMonth) || (nYear ==
                    startYear && nMonth == startMonth)) {
                nYear = startYear;
                nMonth = startMonth + 1;
            }

            String m_strStartDate = String.format("%4d-%02d", nYear, nMonth);
            tvPinggTime.setText(m_strStartDate);
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


    private Handler appraisalCallback = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        Boolean success = Boolean.valueOf(result.get("success").toString());
                        if (success) {
                            Gson gson = new Gson();
                            JSONObject data = JSON.parseObject(gson.toJson(result.get("content")));
                            Intent it = new Intent(mContext, JingZhenGuResultActivity.class);
                            it.putExtra("carInfo", data.toJSONString());
                            startActivity(it);
                        } else {
                            shortToast("查询失败");
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

                            intent = new Intent(mContext, RegisterCityActivity.class);
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

    @Override
    public void selectArea(int style, String province, String city, String districts) {
        tvPinggCity.setText(city);
        tvPinggCity.setTextColor(Color.parseColor("#FF666666"));
    }

    class ViewHolder {

        @Bind(R.id.tv_pingg_models)
        TextView tvCarType;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isMsgOf(MyConstants.SEL_CAR_NAME)) {
            typeShort = event.getStringExtra("fullName");
            String carName = event.getStringExtra("carName");
            int index = carName.indexOf("款");
            carId = event.getStringExtra("carId");
            if (index != -1) {
                carName = carName.replace("款 ", "款 " + typeShort + " ");
            } else {
                carName = typeShort + " " + carName;
            }
            tvPinggModels.setText(carName);
            tvPinggModels.setTextColor(Color.parseColor("#FF666666"));

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setCurrentPager(JingZhenGuEvent event) {
        tvPinggCity.setText(event.getPosition().getName());
        tvPinggCity.setTextColor(Color.parseColor("#FF666666"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setCurrentPager(ResultBackEvent event) {
        tvPinggCity.setText("选择城市");
        tvPinggCity.setTextColor(getResources().getColor(R.color.pinggu_text_color));
        tvPinggTime.setText("选择日期");
        tvPinggTime.setTextColor(getResources().getColor(R.color.pinggu_text_color));
        tvPinggModels.setText("选择品牌");
        tvPinggModels.setTextColor(getResources().getColor(R.color.pinggu_text_color));
        etInvestDialog.setText("");
    }


}
