package com.cheweibao.liuliu.checker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.adapter.CommonArrayAdapter;
import com.cheweibao.liuliu.agent.BrandListActivity;
import com.cheweibao.liuliu.common.BaseFragment;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.CheckCarData;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.data.Province;
import com.cheweibao.liuliu.db.LocalCityTable;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.popup.SelectAreaPopWindow;
import com.cheweibao.liuliu.ui.ScrollViewExt;

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
 * 主界面-订单
 */
public class BasicInfoFragment extends BaseFragment implements SelectAreaPopWindow.OnSelectListener {


    @Bind(R.id.tv_province)
    TextView tvProvince;
    @Bind(R.id.tv_city)
    TextView tvCity;
    @Bind(R.id.et_detail_addr)
    EditText etDetailAddr;
    @Bind(R.id.tv_car_type)
    TextView tvCarType;
    @Bind(R.id.tv_brand)
    TextView tvBrand;
    @Bind(R.id.tv_car_year)
    TextView tvCarYear;
    @Bind(R.id.et_car_number)
    EditText etCarNumber;
    @Bind(R.id.et_car_dist)
    EditText etCarDist;
    @Bind(R.id.tv_reg_date)
    TextView tvRegDate;
    @Bind(R.id.cb_com_ins)
    CheckBox cbComIns;
    @Bind(R.id.cb_traff_ins)
    CheckBox cbTraffIns;
    @Bind(R.id.sp_car_color)
    Spinner spCarColor;
    @Bind(R.id.et_car_pl)
    EditText etCarPl;
    @Bind(R.id.cb_ctrl_press)
    CheckBox cbCtrlPress;
    @Bind(R.id.cb_is_new)
    CheckBox cbIsNew;
    @Bind(R.id.tv_rcv_date)
    TextView tvRcvDate;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.svMain)
    ScrollViewExt svMain;
    @Bind(R.id.ll_main)
    LinearLayout llMain;
    @Bind(R.id.btn_save)
    Button btnSave;

    String orderId;

    Calendar calendar;

    private CustomerDatePickerDialog mCustomerDatePickerDialog;
    private DatePickerDialog mDatePickerDialog;
    private SelectAreaPopWindow selectAreaPopWindow;
    String[] items_color_sp;

    private HashMap<String, Integer> colorMap;

    public static BasicInfoFragment newInstance(String _orderId) {
        Bundle args = new Bundle();
        args.putString("orderId", _orderId);
        BasicInfoFragment fragment = new BasicInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        orderId = args.getString("orderId");
        View view = inflater.inflate(R.layout.fragment_checker_order_basic_info, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        calendar = Calendar.getInstance();
//        tvProvince.setText(CheckCarData.proviceName);
        tvCity.setText(CheckCarData.cityName);
        etDetailAddr.setText(CheckCarData.detailAddr);
        tvCarType.setText(CheckCarData.carTypeName);
        tvBrand.setText(CheckCarData.carBrand);
        tvCarYear.setText(CheckCarData.carYear);
        etCarNumber.setText(CheckCarData.carNumber);
        etCarDist.setText(CheckCarData.carDist);
        tvRegDate.setText(CheckCarData.regDate);
        etCarPl.setText(CheckCarData.carPl);
        tvRcvDate.setText(CheckCarData.rcvDate);
        etPhone.setText(CheckCarData.phone);

        if ("1".equals(CheckCarData.comIns))
            cbComIns.setChecked(true);
        else
            cbComIns.setChecked(false);
        if ("1".equals(CheckCarData.traffIns))
            cbTraffIns.setChecked(true);
        else
            cbTraffIns.setChecked(false);
        if ("1".equals(CheckCarData.isNew))
            cbIsNew.setChecked(true);
        else
            cbIsNew.setChecked(false);
        if ("1".equals(CheckCarData.ctrlPress))
            cbCtrlPress.setChecked(true);
        else
            cbCtrlPress.setChecked(false);

        if ("1".equals(CheckCarData.checkAll)) {
            disableAll();
        }
    }

    private void disableAll(){
        tvCity.setEnabled(false);
        etDetailAddr.setEnabled(false);
        tvCarType.setEnabled(false);
        etCarNumber.setEnabled(false);
        etCarDist.setEnabled(false);
        tvRegDate.setEnabled(false);
        etCarPl.setEnabled(false);
        tvRcvDate.setEnabled(false);
        etPhone.setEnabled(false);

        spCarColor.setEnabled(false);
        cbIsNew.setEnabled(false);
        cbCtrlPress.setEnabled(false);
        cbTraffIns.setEnabled(false);
        cbComIns.setEnabled(false);
        btnSave.setVisibility(View.GONE);
    }

    public void initListener() {
        tvRegDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                calendar.set(year, monthOfYear, dayOfMonth);
                                tvRegDate.setText(formatDay(calendar.getTime(), 0));
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.show();
            }
        });

        tvRcvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomerDatePickerDialog = new CustomerDatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        tvRcvDate.setText(formatDay(calendar.getTime(), 1));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                mCustomerDatePickerDialog.show();
            }
        });

        // 颜色适配器
        items_color_sp = mContext.getResources().getStringArray(
                R.array.color_sp);
        int selColor = 0;
        for (int i = 0; i < items_color_sp.length; i++) {
            if (CheckCarData.carColor.equals(items_color_sp[i])) {
                selColor = i;
            }
        }
        CommonArrayAdapter mCommonArrayAdapter = new CommonArrayAdapter(getActivity(), items_color_sp);
        spCarColor.setAdapter(mCommonArrayAdapter);
        spCarColor.setSelection(selColor);
        spCarColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        if (selectAreaPopWindow != null && selectAreaPopWindow.isShowing()) {
            selectAreaPopWindow.dismiss();
        }
    }

    @Override
    public void selectArea(int style, String province, String city,String districts) {
        if (province.equals(city))
            tvCity.setText(province);
        else
            tvCity.setText(province + city);
    }

    @OnClick({R.id.tv_city, R.id.tv_car_type, R.id.btn_save})
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.tv_city:
                if (!ButCommonUtils.isFastDoubleClick()) {
                    if (LocalCityTable.getInstance().getAllProvince().size() == 0) {
                        getCityInfo();
                    } else {
                        if (selectAreaPopWindow != null && selectAreaPopWindow.isShowing()) return;
                        selectAreaPopWindow = new SelectAreaPopWindow(mContext);
                        selectAreaPopWindow.setOnSelectListener(this);
                        selectAreaPopWindow.showAtLocation(llMain, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
                }
                break;
            case R.id.tv_car_type:
                it = new Intent(mContext, BrandListActivity.class);
                startActivity(it);
                break;
            case R.id.btn_save:
                saveData();
                break;
        }
    }

    private void saveData() {
        CheckCarData.cityName = tvCity.getText().toString().trim();
        CheckCarData.detailAddr = etDetailAddr.getText().toString().trim();
        CheckCarData.carTypeName = tvCarType.getText().toString().trim();
        CheckCarData.carBrand = tvBrand.getText().toString().trim();
        CheckCarData.carYear = tvCarYear.getText().toString().trim();
        CheckCarData.carNumber = etCarNumber.getText().toString().trim();
        CheckCarData.carDist = etCarDist.getText().toString().trim();
        CheckCarData.regDate = tvRegDate.getText().toString().trim();
        CheckCarData.comIns = "0";
        if (cbComIns.isChecked())
            CheckCarData.comIns = "1";
        CheckCarData.traffIns = "0";
        if (cbTraffIns.isChecked())
            CheckCarData.traffIns = "1";

        CheckCarData.carColor = items_color_sp[spCarColor.getSelectedItemPosition()];
        CheckCarData.carPl = etCarPl.getText().toString().trim();
        CheckCarData.isNew = "0";
        if (cbIsNew.isChecked())
            CheckCarData.isNew = "1";
        CheckCarData.ctrlPress = "0";
        if (cbTraffIns.isChecked())
            CheckCarData.ctrlPress = "1";

        CheckCarData.rcvDate = tvRcvDate.getText().toString().trim();

        CheckCarData.phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(CheckCarData.cityName)
                || TextUtils.isEmpty(CheckCarData.detailAddr)
                || TextUtils.isEmpty(CheckCarData.carTypeName)
                || TextUtils.isEmpty(CheckCarData.carNumber)
                || TextUtils.isEmpty(CheckCarData.carDist)
                || TextUtils.isEmpty(CheckCarData.regDate)
                || (spCarColor.getSelectedItemPosition() == 0)
                || TextUtils.isEmpty(CheckCarData.phone)) {
            longToast("请在提交前将基本信息补充完整");
            return;
        }
        if (!Utils.checkMobileNO(CheckCarData.phone)) {
            shortToast("手机号不正确～～");
            return;
        }

        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("token", myglobal.user.token);
                fields.put("carId", CheckCarData.carId);

                fields.put("cityName", CheckCarData.cityName);
                fields.put("detailAddr", CheckCarData.detailAddr);
                fields.put("carType", CheckCarData.carTypeName);
                fields.put("carBrand", CheckCarData.carBrand);
                fields.put("carYear", CheckCarData.carYear);
                fields.put("carNumber", CheckCarData.carNumber);
                fields.put("carDist", CheckCarData.carDist);
                fields.put("regDate", CheckCarData.regDate);
                fields.put("comIns", CheckCarData.comIns);
                fields.put("traffIns", CheckCarData.traffIns);
                fields.put("carColor", CheckCarData.carColor);
                fields.put("carPl", CheckCarData.carPl);
                fields.put("isNew", CheckCarData.isNew);
                fields.put("ctrlPress", CheckCarData.ctrlPress);
                fields.put("rcvDate", CheckCarData.rcvDate);
                fields.put("phone", CheckCarData.phone);
                postMap(ServerUrl.saveBasicInfo, fields, saveHandler);
            } catch (Exception e) {
                e.printStackTrace();
                setThread_flag(false);
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
            setThread_flag(false);
        }
    }

    private Handler saveHandler = new Handler() {

        public void handleMessage(Message msg) {
            setThread_flag(false);
            hideProgress();
            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            shortToast("已保存～～");
                            CheckCarData.basicInfoSave = "1";
                            MessageEvent event = new MessageEvent(MyConstants.SAVE_CHECK_INFO);
                            postEvent(event);
                        }else if("401".equals(status)){
                            String message = result.get("message") + "";
                            shortToast(message);
                            goLoginPage();
                        } else {
                            if (mContext != null) {
                                String message = result.get("message") + "";
                                shortToast(message);
                            }
                        }
                    } catch (Exception e) {
                        shortToast("抱歉数据异常");
                    }
                    break;
                default:
                    if (mContext != null)
                        shortToast("网络不给力!");
                    break;
            }
        }

        ;
    };


    private void getCityInfo() {
        if (LocalCityTable.getInstance().getAllProvince().size() == 0 || Utils.getIntPreferences(mContext, "newVersion") == 1) {
            if (ServerUrl.isNetworkConnected(mContext)) {
                try {
                    HashMap<String, String> fields = new HashMap<String, String>();
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


    class CustomerDatePickerDialog extends DatePickerDialog {

        public CustomerDatePickerDialog(Context context,
                                        OnDateSetListener callBack, int year, int monthOfYear,
                                        int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isMsgOf(MyConstants.SEL_CAR_NAME)) {
            String fullName = event.getStringExtra("fullName");
            String carName = event.getStringExtra("carName");
            String carYear = event.getStringExtra("carYear");
            tvBrand.setText(fullName);
            tvCarType.setText(carName);
            tvCarYear.setText(carYear + "款");
        }else if(event.isMsgOf(MyConstants.CHECK_ALL)){
            initView();
        }
    }

    public String formatDay(Date date, int format_method) {
        switch (format_method) {
            case 0:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(date);
            case 1:
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
                return sdf1.format(date);
        }
        return "";
    }


}
