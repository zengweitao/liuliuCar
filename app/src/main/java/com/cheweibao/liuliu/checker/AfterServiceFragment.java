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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseFragment;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.data.CheckCarData;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.net.ServerUrl;

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
public class AfterServiceFragment extends BaseFragment {


    String orderId;
    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.zhibaoqi_wu_rd)
    RadioButton zhibaoqiWuRd;
    @Bind(R.id.zhibaoqi_1_rd)
    RadioButton zhibaoqi1Rd;
    @Bind(R.id.zhibaoqi_2_rd)
    RadioButton zhibaoqi2Rd;
    @Bind(R.id.zhiBaoQi_rg)
    RadioGroup zhiBaoQiRg;
    @Bind(R.id.zhibaofangshi_baohuan_rd)
    RadioButton zhibaofangshiBaohuanRd;
    @Bind(R.id.zhibaofangshi_youtiaojian_rd)
    RadioButton zhibaofangshiYoutiaojianRd;
    @Bind(R.id.zhibaofangshi_wutiaojian_rd)
    RadioButton zhibaofangshiWutiaojianRd;
    @Bind(R.id.zhiBaoFangShi_rg)
    RadioGroup zhiBaoFangShiRg;
    @Bind(R.id.et_remark)
    EditText etRemark;
    @Bind(R.id.btn_save)
    Button btnSave;
    @Bind(R.id.ll_after_srv)
    LinearLayout llAfterSrv;

    public static AfterServiceFragment newInstance(String _orderId) {
        Bundle args = new Bundle();
        args.putString("orderId", _orderId);
        AfterServiceFragment fragment = new AfterServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        orderId = args.getString("orderId");
        View view = inflater.inflate(R.layout.fragment_checker_order_after_srv, container, false);
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
        etPrice.setText(CheckCarData.jcPrice);
        etRemark.setText(CheckCarData.remark);
        zhibaofangshiWutiaojianRd.setChecked(true);

        switch (Integer.parseInt(CheckCarData.period)) {
            case 0:
                zhiBaoQiRg.check(R.id.zhibaoqi_wu_rd);
                break;
            case 1:
                zhiBaoQiRg.check(R.id.zhibaoqi_1_rd);
                break;
            case 2:
                zhiBaoQiRg.check(R.id.zhibaoqi_2_rd);
                break;
        }

        if ("1".equals(CheckCarData.period) || "2".equals(CheckCarData.period)) {

            llAfterSrv.setVisibility(View.VISIBLE);

            switch (Integer.parseInt(CheckCarData.srvMode)) {
                case 1:
                    zhiBaoFangShiRg.check(R.id.zhibaofangshi_baohuan_rd);
                    break;
                case 2:
                    zhiBaoFangShiRg.check(R.id.zhibaofangshi_youtiaojian_rd);
                    break;
                case 3:
                    zhiBaoFangShiRg.check(R.id.zhibaofangshi_wutiaojian_rd);
                    break;
            }
        }


        zhiBaoQiRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != R.id.zhibaoqi_1_rd && checkedId != R.id.zhibaoqi_2_rd) {
                    llAfterSrv.setVisibility(View.INVISIBLE);
                } else {
                    llAfterSrv.setVisibility(View.VISIBLE);
                }
            }
        });

        if ("1".equals(CheckCarData.checkAll)) {
            disableAll();
        }
    }

    private void disableAll(){
        etPrice.setEnabled(false);
        etRemark.setEnabled(false);
        zhibaoqiWuRd.setEnabled(false);
        zhibaoqi1Rd.setEnabled(false);
        zhibaoqi2Rd.setEnabled(false);
        zhibaofangshiBaohuanRd.setEnabled(false);
        zhibaofangshiYoutiaojianRd.setEnabled(false);
        zhibaofangshiWutiaojianRd.setEnabled(false);


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
        CheckCarData.remark = etRemark.getText().toString().trim();
        CheckCarData.jcPrice = etPrice.getText().toString().trim();
        if (TextUtils.isEmpty(CheckCarData.jcPrice)) {
            shortToast("请输入卖家报价");
            return;
        }
        switch (zhiBaoQiRg.getCheckedRadioButtonId()) {
            case R.id.zhibaoqi_wu_rd:
                CheckCarData.period = "0";
                break;
            case R.id.zhibaoqi_1_rd:
                CheckCarData.period = "1";
                break;
            case R.id.zhibaoqi_2_rd:
                CheckCarData.period = "2";
                break;
        }

        switch (zhiBaoFangShiRg.getCheckedRadioButtonId()) {
            case R.id.zhibaofangshi_baohuan_rd:
                CheckCarData.srvMode = "1";
                break;
            case R.id.zhibaofangshi_youtiaojian_rd:
                CheckCarData.srvMode = "2";
                break;
            case R.id.zhibaofangshi_wutiaojian_rd:
                CheckCarData.srvMode = "3";
                break;
        }
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("token", myglobal.user.token);
                fields.put("orderId", CheckCarData.jcdId);
                fields.put("detailId", CheckCarData.detailId);
                fields.put("carId", CheckCarData.carId);

                fields.put("jcPrice", CheckCarData.jcPrice);
                fields.put("period", CheckCarData.period);
                fields.put("srvMode", CheckCarData.srvMode);
                fields.put("remark", CheckCarData.remark);
                postMap(ServerUrl.saveAfterService, fields, saveHandler);
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
                            CheckCarData.afterSrvSave = "1";
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
                    shortToast("网络不给力!");
                    break;
            }
        }

        ;
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isMsgOf(MyConstants.CHECK_ALL)) {
            disableAll();
        }
    }

}
