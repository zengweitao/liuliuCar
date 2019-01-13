package com.cheweibao.liuliu.checker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.adapter.MyAdapter;
import com.cheweibao.liuliu.common.BaseFragment;
import com.cheweibao.liuliu.common.MyBaseDialog;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.data.CheckCarData;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.net.ServerUrl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主界面-订单
 */
public class CheckFragment extends BaseFragment {


    String orderId;
    @Bind(R.id.rl_zuoqian)
    RelativeLayout rlZuoqian;
    @Bind(R.id.rl_zhengqian)
    RelativeLayout rlZhengqian;
    @Bind(R.id.rl_youqian)
    RelativeLayout rlYouqian;
    @Bind(R.id.rl_zuoce)
    RelativeLayout rlZuoce;
    @Bind(R.id.rl_chedingA)
    RelativeLayout rlChedingA;
    @Bind(R.id.rl_chedingB)
    RelativeLayout rlChedingB;
    @Bind(R.id.rl_youce)
    RelativeLayout rlYouce;
    @Bind(R.id.rl_zuohou)
    RelativeLayout rlZuohou;
    @Bind(R.id.rl_zhenghou)
    RelativeLayout rlZhenghou;
    @Bind(R.id.rl_youhou)
    RelativeLayout rlYouhou;
    @Bind(R.id.rl_lushi)
    RelativeLayout rlLushi;
    @Bind(R.id.rl_dipan)
    RelativeLayout rlDipan;


    public List<String> mList;
    public List<String> bList;
    public List<String> pList;
    public List<String> sList;
    public List<Integer> numberList;

    private MyAdapter mAdapter;
    MyBaseDialog dialog;

    public static CheckFragment newInstance(String _orderId) {
        Bundle args = new Bundle();
        args.putString("orderId", _orderId);
        CheckFragment fragment = new CheckFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        orderId = args.getString("orderId");
        View view = inflater.inflate(R.layout.fragment_checker_order_check, container, false);
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
        numberList = new ArrayList<Integer>();
        mList = new ArrayList<String>();
        bList = new ArrayList<String>();
        pList = new ArrayList<String>();
        sList = new ArrayList<String>();

        initCheckStatus();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);

    }

    private void initCheckStatus() {
        if ("1".equals(CheckCarData.chkStatusArray[0]))
            rlZuoqian.setSelected(true);
        else
            rlZuoqian.setSelected(false);
        if ("1".equals(CheckCarData.chkStatusArray[1]))
            rlZhengqian.setSelected(true);
        else
            rlZhengqian.setSelected(false);
        if ("1".equals(CheckCarData.chkStatusArray[2]))
            rlYouqian.setSelected(true);
        else
            rlYouqian.setSelected(false);
        if ("1".equals(CheckCarData.chkStatusArray[3]))
            rlZuoce.setSelected(true);
        else
            rlZuoce.setSelected(false);
        if ("1".equals(CheckCarData.chkStatusArray[4]))
            rlChedingA.setSelected(true);
        else
            rlChedingA.setSelected(false);
        if ("1".equals(CheckCarData.chkStatusArray[5]))
            rlChedingB.setSelected(true);
        else
            rlChedingB.setSelected(false);
        if ("1".equals(CheckCarData.chkStatusArray[6]))
            rlYouce.setSelected(true);
        else
            rlYouce.setSelected(false);
        if ("1".equals(CheckCarData.chkStatusArray[7]))
            rlZuohou.setSelected(true);
        else
            rlZuohou.setSelected(false);
        if ("1".equals(CheckCarData.chkStatusArray[8]))
            rlZhenghou.setSelected(true);
        else
            rlZhenghou.setSelected(false);
        if ("1".equals(CheckCarData.chkStatusArray[9]))
            rlYouhou.setSelected(true);
        else
            rlYouhou.setSelected(false);
        if ("1".equals(CheckCarData.chkStatusArray[10]))
            rlLushi.setSelected(true);
        else
            rlLushi.setSelected(false);
        if ("1".equals(CheckCarData.chkStatusArray[11]))
            rlDipan.setSelected(true);
        else
            rlDipan.setSelected(false);
    }

    @OnClick({/*R.id.btn_save,*/ R.id.rl_zuoqian, R.id.rl_zhengqian, R.id.rl_youqian, R.id.rl_zuoce, R.id.rl_chedingA, R.id.rl_chedingB, R.id.rl_youce,
            R.id.rl_zuohou, R.id.rl_zhenghou, R.id.rl_youhou, R.id.rl_lushi, R.id.rl_dipan})
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.rl_zuoqian:
                showDialog(getResources().getStringArray(R.array.zuoqian), 0);
                break;
            case R.id.rl_zhengqian:
                showDialog(getResources().getStringArray(R.array.zhengqian), 1);
                break;
            case R.id.rl_youqian:
                showDialog(getResources().getStringArray(R.array.youqian), 2);
                break;
            case R.id.rl_zuoce:
                showDialog(getResources().getStringArray(R.array.zuoce), 3);
                break;
            case R.id.rl_chedingA:
                showDialog(getResources().getStringArray(R.array.chedingA), 4);
                break;
            case R.id.rl_chedingB:
                showDialog(getResources().getStringArray(R.array.chedingB), 5);
                break;
            case R.id.rl_youce:
                showDialog(getResources().getStringArray(R.array.youce), 6);
                break;
            case R.id.rl_zuohou:
                showDialog(getResources().getStringArray(R.array.zuohou), 7);
                break;
            case R.id.rl_zhenghou:
                showDialog(getResources().getStringArray(R.array.zhenghou), 8);
                break;
            case R.id.rl_youhou:
                showDialog(getResources().getStringArray(R.array.youhou), 9);

                break;
            case R.id.rl_lushi:
                showDialog(getResources().getStringArray(R.array.lushi), 10);
                break;
            case R.id.rl_dipan:
                showDialog(getResources().getStringArray(R.array.dipan), 11);
                break;
        }
    }

    private void saveData() {
        if (getThread_flag()) return;
        setThread_flag(true);

        String checkRes = "";
        JSONArray checkResPic = new JSONArray();
        String checkStatus = "";
        for (int i = 0; i < CheckCarData.scoreArray.length; i++) {
            checkRes += CheckCarData.scoreArray[i] + "&";
            JSONObject object = new JSONObject();
            object.put("imgUrl", CheckCarData.scorePicArray[i]);
            checkResPic.add(object);
        }
        for (int i = 0; i < CheckCarData.chkStatusArray.length; i++) {
            checkStatus += CheckCarData.chkStatusArray[i];
        }

        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("token", myglobal.user.token);
                fields.put("detailId", CheckCarData.detailId);
                fields.put("carId", CheckCarData.carId);

                fields.put("checkRes", checkRes);
                fields.put("checkResPic", checkResPic.toJSONString());
                fields.put("checkStatus", checkStatus);
                postMap(ServerUrl.saveCheckRes, fields, saveHandler);
            } catch (Exception e) {
                e.printStackTrace();
                setThread_flag(false);
            }
        } else {
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
                            Gson gson = new Gson();
                            JSONObject data = JSON.parseObject(gson.toJson(result.get("data")));
                            shortToast("已保存～～");
                            CheckCarData.checkSave = data.getString("checkSave");
                            MessageEvent event = new MessageEvent(MyConstants.SAVE_CHECK_INFO);
                            postEvent(event);

                            initCheckStatus();
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

    private int booleanUtil(boolean key) {
        return key ? 1 : 0;
    }

    private boolean booleanOPUtil(String key) {
        return key.equals("0") ? false : true;
    }


    public void showDialog(String[] res, final int partId) {
        zuzhuang(res);

        mAdapter = new MyAdapter(getActivity());
        mAdapter.setList(mList, bList, pList, numberList);
        dialog = new MyBaseDialog(mContext, R.style.Theme_Transparent, "chkDlg", mAdapter, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ifPicToTake()) {
//                    //"---------还有照片要拍--------"
                } else {
                    //"---------关闭--------"
                    for (int i = 0; i < numberList.size(); i++) {
                        CheckCarData.scoreArrayReal[numberList.get(i)] = CheckCarData.scoreArray[numberList.get(i)];
                        CheckCarData.scorePicArrayReal[numberList.get(i)] = CheckCarData.scorePicArray[numberList.get(i)];
                        CheckCarData.chkStatusArray[partId] = "1";

                    }
                    dialog.dismiss();
                    saveData();
                }
            }
        });
        dialog.show();
    }

    private void zuzhuang(String[] question) {
        clearList();
        //     <item>#0#1.是否达到国家强制报废标准#0#7#10</item>
        for (int i = 0; i < question.length; i++) {
            numberList.add(Integer.valueOf(question[i].split("#")[1])); //  序号
            mList.add(question[i].split("#")[2]); // 评估问题项
            bList.add(question[i].split("#")[3]); // 用哪种view来组装
            //    		sList.add(question[i].split("#")[5]);		// 扣分项
            if (question[i].split("#").length == 4) {
                pList.add("*"); // 标明12张标准照的序号，其余填*
            } else {
                pList.add(question[i].split("#")[4]);
            }
        }

    }

    private boolean ifPicToTake() {

        for (int i = 0; i < mAdapter.getVerify_cam_btn_List().size(); i++) {
            if (mAdapter.getVerify_cam_btn_List().get(i).getId() != R.drawable.ic_menu_camera_save) {
                shortToast("还有照片需要采集");
                return false;
            }
        }
        return true;
    }

    private void clearList() {
        numberList.clear();
        mList.clear();
        bList.clear();
        pList.clear();
        sList.clear();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isMsgOf(MyConstants.CHECK_PART)) {
            initCheckStatus();
        } else if (event.isMsgOf(MyConstants.UPDATE_PIC)) {
            int requestCode = event.getIntExtra("reqCode", 0);
            if (null != mAdapter) {
                if (requestCode < 1000 && requestCode > 15) { //5种缺陷
                    Log.e("--1path of image is-", "2");
                    for (int i = 0; i < mAdapter.getFd_camera_btn_List().size(); i++) {
                        Log.e("",
                                "-----------------------5种缺陷  出错---------------------->   ");
                        if ((Integer) mAdapter.getFd_camera_btn_List().get(i)
                                .getTag() == (requestCode - 100)) {
                            mAdapter.getFd_camera_btn_List()
                                    .get(i)
                                    .setBackgroundResource(
                                            R.drawable.ic_menu_camera_save);
                            mAdapter.getFd_camera_btn_List().get(i)
                                    .setId(R.drawable.ic_menu_camera_save);
                            if (mAdapter.getVerify_cam_btn_List().contains(
                                    mAdapter.getFd_camera_btn_List().get(i))) {
                                mAdapter.getVerify_cam_btn_List()
                                        .remove(mAdapter
                                                .getFd_camera_btn_List().get(i));
                            }
                        }
                    }
                } else if (requestCode >= 1000 && requestCode < 10000) { //6种缺陷
                    Log.e("--1path of image is-", "2");
                    for (int i = 0; i < mAdapter.getSd_camera_btn_List().size(); i++) {
                        Log.e("--1path of image is-", "2");
                        Log.e("",
                                "-----------------------6种缺陷  出错---------------------->   ");
                        if ((Integer) mAdapter.getSd_camera_btn_List().get(i)
                                .getTag() == (requestCode - 1000)) {
                            mAdapter.getSd_camera_btn_List()
                                    .get(i)
                                    .setBackgroundResource(
                                            R.drawable.ic_menu_camera_save);
                            mAdapter.getSd_camera_btn_List().get(i)
                                    .setId(R.drawable.ic_menu_camera_save);
                            if (mAdapter.getVerify_cam_btn_List().contains(
                                    mAdapter.getSd_camera_btn_List().get(i))) {
                                mAdapter.getVerify_cam_btn_List()
                                        .remove(mAdapter
                                                .getSd_camera_btn_List().get(i));
                            }
                        }
                    }
                } else if (requestCode >= 10000 && requestCode < 100000) { //拍照项
                    Log.e("--1path of image is-", "4");
                    for (int i = 0; i < mAdapter.getPic_camera_btn_List()
                            .size(); i++) {
                        if ((Integer) mAdapter.getPic_camera_btn_List().get(i)
                                .getTag() == (requestCode - 10000)) {
                            mAdapter.getPic_camera_btn_List()
                                    .get(i)
                                    .setBackgroundResource(
                                            R.drawable.ic_menu_camera_save);
                            mAdapter.getPic_camera_btn_List().get(i)
                                    .setId(R.drawable.ic_menu_camera_save);
                            if (mAdapter.getVerify_cam_btn_List().contains(
                                    mAdapter.getPic_camera_btn_List().get(i))) {
                                mAdapter.getVerify_cam_btn_List().remove(
                                        mAdapter.getPic_camera_btn_List()
                                                .get(i));
                            }
                        }
                    }
                } else if (requestCode >= 100000 && requestCode < 1000000) { //3选1拍照
                    Log.e("--1path of image is-", "5");
                    for (int i = 0; i < mAdapter.getTo_camera_btn_List().size(); i++) {
                        Log.e("",
                                "-----------------------3选1拍照  出错---------------------->   ");
                        if ((Integer) mAdapter.getTo_camera_btn_List().get(i)
                                .getTag() == (requestCode - 100000)) {
                            mAdapter.getTo_camera_btn_List()
                                    .get(i)
                                    .setBackgroundResource(
                                            R.drawable.ic_menu_camera_save);
                            mAdapter.getTo_camera_btn_List().get(i)
                                    .setId(R.drawable.ic_menu_camera_save);
                            if (mAdapter.getVerify_cam_btn_List().contains(
                                    mAdapter.getTo_camera_btn_List().get(i))) {
                                mAdapter.getVerify_cam_btn_List()
                                        .remove(mAdapter
                                                .getTo_camera_btn_List().get(i));
                            }
                        }
                    }
                } else if (requestCode >= 1000000) { //4选1拍照
                    Log.e("--1path of image is-", "6");
                    for (int i = 0; i < mAdapter.getFo_camera_btn_List().size(); i++) {
                        Log.e("",
                                "-----------------------4选1拍照  出错---------------------->   ");
                        if ((Integer) mAdapter.getFo_camera_btn_List().get(i)
                                .getTag() == (requestCode - 1000000)) {
                            mAdapter.getFo_camera_btn_List()
                                    .get(i)
                                    .setBackgroundResource(
                                            R.drawable.ic_menu_camera_save);
                            mAdapter.getFo_camera_btn_List().get(i)
                                    .setId(R.drawable.ic_menu_camera_save);
                            if (mAdapter.getVerify_cam_btn_List().contains(
                                    mAdapter.getFo_camera_btn_List().get(i))) {
                                mAdapter.getVerify_cam_btn_List()
                                        .remove(mAdapter
                                                .getFo_camera_btn_List().get(i));
                            }
                        }
                    }
                }
            }
        }
    }


}
