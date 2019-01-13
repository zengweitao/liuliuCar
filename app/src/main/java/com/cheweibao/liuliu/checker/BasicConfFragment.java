package com.cheweibao.liuliu.checker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseFragment;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.data.CheckCarData;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.ScrollViewExt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主界面-订单
 */
public class BasicConfFragment extends BaseFragment {


    String orderId;
    @Bind(R.id.hub_cb)
    CheckBox hubCb;
    @Bind(R.id.smart_key_cb)
    CheckBox smartKeyCb;
    @Bind(R.id.power_steering_cb)
    CheckBox powerSteeringCb;
    @Bind(R.id.gps_cb)
    CheckBox gpsCb;
    @Bind(R.id.radar_cb)
    CheckBox radarCb;
    @Bind(R.id.video_cb)
    CheckBox videoCb;
    @Bind(R.id.auto_park_cb)
    CheckBox autoParkCb;
    @Bind(R.id.cruise_cb)
    CheckBox cruiseCb;
    @Bind(R.id.abs_cb)
    CheckBox absCb;
    @Bind(R.id.esp_cb)
    CheckBox espCb;
    @Bind(R.id.leather_seats_cb)
    CheckBox leatherSeatsCb;
    @Bind(R.id.seat_heating_cb)
    CheckBox seatHeatingCb;
    @Bind(R.id.roof_cb)
    CheckBox roofCb;
    @Bind(R.id.airbag_cb)
    CheckBox airbagCb;
    @Bind(R.id.aircon_manual_rb)
    RadioButton airconManualRb;
    @Bind(R.id.aircon_auto_rb)
    RadioButton airconAutoRb;
    @Bind(R.id.aircon_null_rb)
    RadioButton airconNullRb;
    @Bind(R.id.aircon_rg)
    RadioGroup airconRg;
    @Bind(R.id.cd_rb)
    RadioButton cdRb;
    @Bind(R.id.dvd_rb)
    RadioButton dvdRb;
    @Bind(R.id.tape_rb)
    RadioButton tapeRb;
    @Bind(R.id.sound_null_rb)
    RadioButton soundNullRb;
    @Bind(R.id.sound_rg)
    RadioGroup soundRg;
    @Bind(R.id.gearbox_manual_rb)
    RadioButton gearboxManualRb;
    @Bind(R.id.gearbox_auto_rb)
    RadioButton gearboxAutoRb;
    @Bind(R.id.gearbox_rg)
    RadioGroup gearboxRg;
    @Bind(R.id.seat_adjuster_manual_rb)
    RadioButton seatAdjusterManualRb;
    @Bind(R.id.seat_adjuster_auto_rb)
    RadioButton seatAdjusterAutoRb;
    @Bind(R.id.seat_adjuster_rg)
    RadioGroup seatAdjusterRg;
    @Bind(R.id.svMain)
    ScrollViewExt svMain;
    @Bind(R.id.ll_main)
    LinearLayout llMain;
    @Bind(R.id.btn_save)
    Button btnSave;


    public static BasicConfFragment newInstance(String _orderId) {
        Bundle args = new Bundle();
        args.putString("orderId", _orderId);
        BasicConfFragment fragment = new BasicConfFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        orderId = args.getString("orderId");
        View view = inflater.inflate(R.layout.fragment_checker_order_base_conf, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        if (TextUtils.isEmpty(CheckCarData.basicConf)) return;
        String[] data = CheckCarData.basicConf.split("&");
        if (data.length != 18) return;

        roofCb.setChecked(booleanOPUtil(data[0]));        // 天窗
        powerSteeringCb.setChecked(booleanOPUtil(data[1]));        // 助力转向
        leatherSeatsCb.setChecked(booleanOPUtil(data[2]));        // 真皮座椅
        absCb.setChecked(booleanOPUtil(data[3]));        // ABS
        hubCb.setChecked(booleanOPUtil(data[4]));        // 铝车毂
        cruiseCb.setChecked(booleanOPUtil(data[5]));        // 巡航定速
        gpsCb.setChecked(booleanOPUtil(data[6]));        // GPS导航
        radarCb.setChecked(booleanOPUtil(data[7]));        // 倒车雷达
        seatHeatingCb.setChecked(booleanOPUtil(data[8]));        // 座椅加热
        espCb.setChecked(booleanOPUtil(data[9]));        // ESP
        autoParkCb.setChecked(booleanOPUtil(data[10]));        // 自动泊车
        videoCb.setChecked(booleanOPUtil(data[11]));        // 倒车影像
        smartKeyCb.setChecked(booleanOPUtil(data[12]));        // 遥控钥匙
        airbagCb.setChecked(booleanOPUtil(data[13]));        // 安全气囊

        if (!TextUtils.isEmpty(data[14])) {
            switch (Integer.valueOf(data[14])) {                                //空调	1 自动  0手动 2无
                case 0:
                    airconRg.check(R.id.aircon_manual_rb);
                    break;
                case 1:
                    airconRg.check(R.id.aircon_auto_rb);
                    break;
                case 2:
                    airconRg.check(R.id.aircon_null_rb);
                    break;
            }
        }

        if (!TextUtils.isEmpty(data[15])) {
            switch (Integer.valueOf(data[15])) {                                //音响    	0 cd   1 dvd	2 卡带	3 无
                case 0:
                    soundRg.check(R.id.cd_rb);
                    break;
                case 1:
                    soundRg.check(R.id.dvd_rb);
                    break;
                case 2:
                    soundRg.check(R.id.tape_rb);
                    break;
                case 3:
                    soundRg.check(R.id.sound_null_rb);
            }
        }

        if (!TextUtils.isEmpty(data[16])) {
            switch (Integer.valueOf(data[16])) {                                //座椅调节	1 自动  0手动
                case 0:
                    seatAdjusterRg.check(R.id.seat_adjuster_manual_rb);
                    break;
                case 1:
                    seatAdjusterRg.check(R.id.seat_adjuster_auto_rb);
                    break;
            }
        }

        if (!TextUtils.isEmpty(data[17])) {
            switch (Integer.valueOf(data[17])) {                                //变速箱      1 自动  0手动
                case 0:
                    gearboxRg.check(R.id.gearbox_manual_rb);
                    break;
                case 1:
                    gearboxRg.check(R.id.gearbox_auto_rb);
                    break;
            }
        }

        if ("1".equals(CheckCarData.checkAll)) {
            disableAll();
        }
    }

    private  void disableAll(){
        roofCb.setEnabled(false);
        powerSteeringCb.setEnabled(false);
        leatherSeatsCb.setEnabled(false);
        absCb.setEnabled(false);
        hubCb.setEnabled(false);
        cruiseCb.setEnabled(false);
        gpsCb.setEnabled(false);
        radarCb.setEnabled(false);
        seatHeatingCb.setEnabled(false);
        espCb.setEnabled(false);
        autoParkCb.setEnabled(false);
        videoCb.setEnabled(false);
        smartKeyCb.setEnabled(false);
        airbagCb.setEnabled(false);
        airconManualRb.setEnabled(false);
        airconAutoRb.setEnabled(false);
        airconNullRb.setEnabled(false);
        cdRb.setEnabled(false);
        dvdRb.setEnabled(false);
        tapeRb.setEnabled(false);
        soundNullRb.setEnabled(false);
        seatAdjusterManualRb.setEnabled(false);
        seatAdjusterAutoRb.setEnabled(false);
        gearboxManualRb.setEnabled(false);
        gearboxAutoRb.setEnabled(false);

        btnSave.setVisibility(View.GONE);
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

    }


    @OnClick({R.id.btn_save})
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.btn_save:
                saveData();
                break;
        }
    }

    private void saveData() {
        String airconStr = null;
        String soundStr = null;
        String seat_adjuster_Str = null;
        String gearboxStr = null;

        switch (airconRg.getCheckedRadioButtonId()) {
            case R.id.aircon_auto_rb:
                airconStr = "1";
                break;
            case R.id.aircon_manual_rb:
                airconStr = "0";
                break;
            case R.id.aircon_null_rb:
                airconStr = "2";
                break;
        }

        switch (soundRg.getCheckedRadioButtonId()) {
            case R.id.cd_rb:
                soundStr = "0";
                break;
            case R.id.dvd_rb:
                soundStr = "1";
                break;
            case R.id.tape_rb:
                soundStr = "2";
                break;
            case R.id.sound_null_rb:
                soundStr = "3";
                break;
        }

        switch (seatAdjusterRg.getCheckedRadioButtonId()) {
            case R.id.seat_adjuster_auto_rb:
                seat_adjuster_Str = "1";
                break;
            case R.id.seat_adjuster_manual_rb:
                seat_adjuster_Str = "0";
                break;
        }

        switch (gearboxRg.getCheckedRadioButtonId()) {
            case R.id.gearbox_auto_rb:
                gearboxStr = "1";
                break;
            case R.id.gearbox_manual_rb:
                gearboxStr = "0";
                break;
        }

        CheckCarData.basicConf =
                String.valueOf(booleanUtil(roofCb.isChecked())) + "&" +        //天窗
                        String.valueOf(booleanUtil(powerSteeringCb.isChecked())) + "&" +        //助力转向
                        String.valueOf(booleanUtil(leatherSeatsCb.isChecked())) + "&" +        //真皮座椅
                        String.valueOf(booleanUtil(absCb.isChecked())) + "&" +        //ABS
                        String.valueOf(booleanUtil(hubCb.isChecked())) + "&" +        //铝车毂
                        String.valueOf(booleanUtil(cruiseCb.isChecked())) + "&" +        //定速巡航
                        String.valueOf(booleanUtil(gpsCb.isChecked())) + "&" +        //GPS
                        String.valueOf(booleanUtil(radarCb.isChecked())) + "&" +        //倒车雷达
                        String.valueOf(booleanUtil(seatHeatingCb.isChecked())) + "&" +        //座椅加热
                        String.valueOf(booleanUtil(espCb.isChecked())) + "&" +        //ESP
                        String.valueOf(booleanUtil(autoParkCb.isChecked())) + "&" +        //自动泊车
                        String.valueOf(booleanUtil(videoCb.isChecked())) + "&" +        //倒车影像
                        String.valueOf(booleanUtil(smartKeyCb.isChecked())) + "&" +        //遥控钥匙
                        String.valueOf(booleanUtil(airbagCb.isChecked())) + "&" +        //安全气囊
                        airconStr + "&" +        //空调
                        soundStr + "&" +        //音响
                        seat_adjuster_Str + "&" +        //座椅调节
                        gearboxStr + "&";        //变速箱

        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("token", myglobal.user.token);
                fields.put("detailId", CheckCarData.detailId);
                fields.put("carId", CheckCarData.carId);

                fields.put("basicConf", CheckCarData.basicConf);
                postMap(ServerUrl.saveBasicConf, fields, saveHandler);
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
                            CheckCarData.basicConfSave = "1";
                            MessageEvent event = new MessageEvent(MyConstants.SAVE_CHECK_INFO);
                            postEvent(event);
                        }else if("401".equals(status)){
                            String message = result.get("message") + "";
                            shortToast(message);
                            goLoginPage();
                        } else {
                            String message = result.get("message") + "";
                            shortToast(message);
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

    private int booleanUtil(boolean key) {
        return key ? 1 : 0;
    }

    private boolean booleanOPUtil(String key) {
        return key.equals("0") ? false : true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isMsgOf(MyConstants.CHECK_ALL)) {
            disableAll();
        }
    }
}
