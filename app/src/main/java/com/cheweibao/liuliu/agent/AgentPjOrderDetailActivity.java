package com.cheweibao.liuliu.agent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.ImagePagerActivity;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.crop.ImageCropActivity;
import com.cheweibao.liuliu.data.BankInfo;
import com.cheweibao.liuliu.data.CarImgInfo;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.data.Province;
import com.cheweibao.liuliu.db.LocalCityTable;
import com.cheweibao.liuliu.event.AgentPjOrderDetailEvent;
import com.cheweibao.liuliu.event.SelectCarTypeEvent;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.popup.SelectAreaPopWindow;
import com.cheweibao.liuliu.ui.ClickEffectImageView;
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
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class AgentPjOrderDetailActivity extends BaseActivity implements SelectAreaPopWindow
        .OnSelectListener, View.OnTouchListener {

    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.tv_order_no)
    TextView tvOrderNo;
    @Bind(R.id.ll_order_no)
    LinearLayout llOrderNo;
    @Bind(R.id.et_mileage)
    EditText etMileage;
    @Bind(R.id.tv_car_type)
    TextView tvCarType;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.sp_pl)
    Spinner spPl;
    @Bind(R.id.tv_car_area)
    TextView tvCarArea;
    @Bind(R.id.sp_car_status)
    Spinner spCarStatus;
    @Bind(R.id.sp_bank)
    Spinner spBank;
    @Bind(R.id.gl_img)
    GridLayout glImg;
    @Bind(R.id.svMain)
    ScrollViewExt svMain;
    @Bind(R.id.ll_main)
    LinearLayout llMain;

    private static final int REQ_PERMISSION = 2001;

    PLListener mPLListener; //排量
    PLAdapter mPLAdapter;
    int selPL = 0;

    CKListener mCKListener; //车况
    CKAdapter mCKAdapter;
    int selCK = 0;

    BankListener mBankListener; //银行
    BankAdapter mBankAdapter;
    int selBank = 0;

    int nYear = 0, nMonth = 0;
    @Bind(R.id.ll_type)
    LinearLayout llType;
    @Bind(R.id.ll_new)
    LinearLayout llNew;
    @Bind(R.id.btn_rep_report)
    Button btnRepReport;
    @Bind(R.id.ll_modify)
    LinearLayout llModify;

    @Bind(R.id.btn_set_price)
    Button btnSetPrice;
    @Bind(R.id.btn_report)
    Button btnReport;
    @Bind(R.id.btn_save)
    Button btnSave;
    @Bind(R.id.ivTopBack)
    ClickEffectImageView ivTopBack;

    private boolean m_bPermissionGrant = false;
    private String m_strImgFile = "";
    private String m_TmpFile = "";
    int selImg = 0;

    String[] imgTitle = {"行驶证", "登记证", "左前45度", "发动机舱整体", "铭牌", "仪表盘", "左前门拍驾驶舱", "后座拍中控整体",
            "右后门拍后座",
            "右后45度", "正前整车", "发动机舱左", "发动机舱右", "右侧整车", "后尾箱整体", "自定义", "自定义", "自定义"};
    ArrayList<CarImgInfo> arrImgUrl;
    ArrayList<BankInfo> bankList;

    public String UPLOAD_OBJECT = "";
    private OSS oss;
    MyBroadcastReceiver myReceiver;
    private SelectAreaPopWindow selectAreaPopWindow;

    int reqType = 0;
    String numberDate = "", typeName = "", mileAge = "", lenderName = "", regionName = "",
            orderNo = "", orderType = "0", reportStatus = "0", bankId = "";

    String orderId = "", typeShort = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_publish);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        orderId = getIntent().getStringExtra("orderId");
        if (orderId != null) {
            ivTopBack.setVisibility(View.VISIBLE);
        } else {
            ivTopBack.setVisibility(View.GONE);
        }
        initView();
        initBroadCast();
    }

    @Override
    protected void onDestroy() {
        if (myReceiver != null) {
            if (myReceiver != null)
                LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        }
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    private void initView() {
        etName.setOnTouchListener(this);
        etMileage.setOnTouchListener(this);

        bankList = new ArrayList<>();

        setPLSpinnerAdapter();
        setCKSpinnerAdapter();
        setBankSpinnerAdapter();

        initImgGrid();
        if (TextUtils.isEmpty(orderId)) {
            tvTopTitle.setText("发布");
            llOrderNo.setVisibility(View.GONE);
            llNew.setVisibility(View.VISIBLE);
            llModify.setVisibility(View.GONE);
        } else {
            tvTopTitle.setText("订单详细");
            llNew.setVisibility(View.GONE);
            llModify.setVisibility(View.GONE);
            svMain.setVisibility(View.INVISIBLE);

        }

        getOrderInfo();
    }

    private void initBroadCast() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("upload_pic_success");
        myIntentFilter.addAction("upload_pic_fail");
        myReceiver = new MyBroadcastReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(myReceiver, myIntentFilter);
    }

    private void initImgGrid() {
        arrImgUrl = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            CarImgInfo info = new CarImgInfo();
            info.id = i;
            info.title = imgTitle[i];
            arrImgUrl.add(info);
        }
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        glImg.removeAllViews();

        int itemWidth = (myglobal.SCR_WIDTH - Utils.dp2Pixel(mContext, 40)) / 2;
        int itemHeight = itemWidth * 3 / 4;
        for (int i = 0; i < arrImgUrl.size(); i++) {
            final CarImgInfo info = arrImgUrl.get(i);
            View dayViewItem = inflater.inflate(R.layout.layout_car_image_item, null);

            ImgViewHolder viewHolder = new ImgViewHolder(dayViewItem);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.ivImg
                    .getLayoutParams();
            params.width = itemWidth;
            params.height = itemHeight;
            viewHolder.ivImg.setLayoutParams(params);
            arrImgUrl.get(i).ivImg = viewHolder.ivImg;
            viewHolder.tvTitle.setText(info.title);
            if (i < 9 || i == 10) {
                viewHolder.tv_title_left.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tv_title_left.setVisibility(View.INVISIBLE);
            }
            glImg.addView(dayViewItem);

            final int sel = i;
            viewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!"0".equals(reportStatus)) return;
                    Utils.showQuestionView(mContext, "温謦提示", "确定要删除吗？", "删除", "取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    arrImgUrl.get(sel).imgUrl = "";
                                    showImgWithGlid("", arrImgUrl.get(sel).ivImg);
                                }
                            },
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                }
            });

            viewHolder.ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if(!"0".equals(reportStatus)) return;
                    if (!TextUtils.isEmpty(orderId)) return;
                    selImg = sel;
                    requestPermission();
                    if (!m_bPermissionGrant)
                        return;
                    openCustomAlbum();
                }
            });
            viewHolder.tvDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(arrImgUrl.get(sel).imgUrl))
                        return;

                    ArrayList<String> list = new ArrayList<>();
                    int tmp = 0, curIdx = 0;
                    for (int i = 0; i < arrImgUrl.size(); i++) {
                        CarImgInfo car = arrImgUrl.get(i);
                        if (!TextUtils.isEmpty(car.imgUrl)) {
                            list.add(car.imgUrl);
                            if (i == sel) curIdx = tmp;
                            tmp++;
                        }

                    }
                    Intent intent = new Intent(mContext, ImagePagerActivity.class);
                    intent.putExtra(ImagePagerActivity.FILE_FLAG, false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, list);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, curIdx);
                    startActivity(intent);
                }
            });
        }
    }

    public void getOrderInfo() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("token", myglobal.user.token);
                if (TextUtils.isEmpty(orderId))
                    fields.put("pjdId", "0");
                else
                    fields.put("pjdId", orderId);

                postMap(ServerUrl.pjGetOrderInfo, fields, infohandler);
                showProgress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            shortToast("网络连接不可用");
        }
    }

    private Handler infohandler = new Handler() {
        public void handleMessage(Message msg) {
            hideProgress();
            setThread_flag(false);
            switch (msg.what) {
                case 0:
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            Gson gson = new Gson();
                            JSONObject data = JSON.parseObject(gson.toJson(result.get("data")));
                            if (!TextUtils.isEmpty(orderId)) {
                                JSONObject info = data.getJSONObject("info");
                                orderNo = info.getString("pjdNo");
                                numberDate = info.getString("numberDate");
                                lenderName = info.getString("lenderName");
                                typeName = info.getString("carTypeName");
                                typeShort = info.getString("carTypeShort");
                                mileAge = info.getString("carMileage");
                                regionName = info.getString("regionName");
                                orderType = info.getString("pjdType");
                                reportStatus = info.getString("pjdReportStatus");
                                bankId = info.getString("bankId");
                                for (int i = 0; i < arrImgUrl.size(); i++) {
                                    arrImgUrl.get(i).imgUrl = info.getString("carImg" + (i + 1));
                                }
                                selPL = info.getInteger("carPl");
                                selCK = info.getInteger("carStatus");
                            }

                            JSONArray jsonBank = data.getJSONArray("bankList");
                            for (int i = 0; i < jsonBank.size(); i++) {
                                BankInfo bankInfo = new BankInfo(jsonBank.getJSONObject(i));
                                bankList.add(bankInfo);
                                if (bankId.equals(bankInfo.bankId)) {
                                    selBank = i + 1;
                                }
                            }

                            setBankSpinnerAdapter();

                            if (!TextUtils.isEmpty(orderId)) {
                                showOrderInfo();
                            }

                        } else if ("401".equals(status)) {
                            String message = result.get("message") + "";
                            shortToast(message);
                            goLoginPage();
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
                    shortToast("网路不给力!");
                    break;
            }
        }

        ;
    };

    private void showOrderInfo() {
        tvOrderNo.setText(orderNo);
        etName.setText(lenderName);
        etMileage.setText(mileAge);
        tvCarType.setText(typeName);
        tvDate.setText(numberDate);
        tvCarArea.setText(regionName);
        selPL = selPL + 1;
        selCK = selCK + 1;
        spPl.setSelection(selPL);
        spCarStatus.setSelection(selCK);
        spBank.setSelection(selBank);
        for (int i = 0; i < arrImgUrl.size(); i++) {
            showImgWithGlid(arrImgUrl.get(i).imgUrl, arrImgUrl.get(i).ivImg);
        }
        //if("1".equals(orderType))
        //    btnRepReport.setVisibility(View.GONE);

//        if(!"0".equals(reportStatus)){
        if (!TextUtils.isEmpty(orderId)) {
            llModify.setVisibility(View.GONE);
            tvOrderNo.setEnabled(false);
            etName.setEnabled(false);
            etMileage.setEnabled(false);
            llType.setEnabled(false);
            tvDate.setEnabled(false);
            tvCarArea.setEnabled(false);
            spPl.setEnabled(false);
            spCarStatus.setEnabled(false);
            spBank.setEnabled(false);
        }

        svMain.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.ivTopBack, R.id.ll_type, R.id.tv_date, R.id.tv_car_area, R.id.btn_set_price, R
            .id.btn_report, R.id.btn_save, R.id.btn_rep_report})
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.ivTopBack:
                if (selectAreaPopWindow != null && selectAreaPopWindow.isShowing()) {
                    selectAreaPopWindow.dismiss();
                }
                finish();
                break;
            case R.id.ll_type:
                it = new Intent(mContext, BrandListReleaseActivity.class);
                startActivity(it);
                break;
            case R.id.tv_date:
                initPickerTime();
//                setDate();
                break;
            case R.id.tv_car_area:
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
            case R.id.btn_set_price:
                confirmSetPrice();
                break;
            case R.id.btn_report:
                confirmReport();
                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.btn_rep_report:
                make2Report();
                break;

        }
    }

    private void confirmSetPrice() {
        getValues();
        if (TextUtils.isEmpty(mileAge)) {
            shortToast("请输入里程数～～");
            return;
        }
        if (TextUtils.isEmpty(typeName)) {
            shortToast("请选择车辆型号～～");
            return;
        }
        if (TextUtils.isEmpty(numberDate)) {
            shortToast("请选择上牌时间～～");
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (TextUtils.isEmpty(arrImgUrl.get(i).imgUrl)) {
                shortToast("请上传" + arrImgUrl.get(i).title + "照片～～");
                return;
            }
        }
        if (TextUtils.isEmpty(arrImgUrl.get(10).imgUrl)) {
            shortToast("请上传" + arrImgUrl.get(10).title + "照片～～");
            return;
        }
        Utils.showQuestionView(mContext, "温謦提示", "确定要预估价格吗？", "确定", "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPrice(0);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
    }

    private void confirmReport() {
        getValues();
        if (TextUtils.isEmpty(mileAge)) {
            shortToast("请输入里程数～～");
            return;
        }
        if (TextUtils.isEmpty(typeName)) {
            shortToast("请选择车辆型号～～");
            return;
        }
        if (TextUtils.isEmpty(numberDate)) {
            shortToast("请选择上牌时间～～");
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (TextUtils.isEmpty(arrImgUrl.get(i).imgUrl)) {
                shortToast("请上传" + arrImgUrl.get(i).title + "照片～～");
                return;
            }
        }
        if (TextUtils.isEmpty(arrImgUrl.get(10).imgUrl)) {
            shortToast("请上传" + arrImgUrl.get(10).title + "照片～～");
            return;
        }
        Utils.showQuestionView(mContext, "温謦提示", "确定要预估价格吗？", "确定", "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPrice(1);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
    }

    private void save() {
        getValues();
        if (TextUtils.isEmpty(mileAge)) {
            shortToast("请输入里程数～～");
            return;
        }
        if (TextUtils.isEmpty(typeName)) {
            shortToast("请选择车辆型号～～");
            return;
        }
        if (TextUtils.isEmpty(numberDate)) {
            shortToast("请选择上牌时间～～");
            return;
        }

        for (int i = 0; i < 9; i++) {
            if (TextUtils.isEmpty(arrImgUrl.get(i).imgUrl)) {
                shortToast("请上传" + arrImgUrl.get(i).title + "照片～～");
                return;
            }
        }
        if (TextUtils.isEmpty(arrImgUrl.get(10).imgUrl)) {
            shortToast("请上传" + arrImgUrl.get(10).title + "照片～～");
            return;
        }
        requestPrice(2);
    }

    private void make2Report() {
        getValues();
        if (TextUtils.isEmpty(mileAge)) {
            shortToast("请输入里程数～～");
            return;
        }
        if (TextUtils.isEmpty(typeName)) {
            shortToast("请选择车辆型号～～");
            return;
        }
        if (TextUtils.isEmpty(numberDate)) {
            shortToast("请选择上牌时间～～");
            return;
        }

        for (int i = 0; i < 9; i++) {
            if (TextUtils.isEmpty(arrImgUrl.get(i).imgUrl)) {
                shortToast("请上传" + arrImgUrl.get(i).title + "照片～～");
                return;
            }
        }
        if (TextUtils.isEmpty(arrImgUrl.get(10).imgUrl)) {
            shortToast("请上传" + arrImgUrl.get(10).title + "照片～～");
            return;
        }
        requestPrice(3);
    }

    private void getValues() {
        numberDate = tvDate.getText().toString().trim();
        typeName = tvCarType.getText().toString().trim();
        mileAge = etMileage.getText().toString().trim();
        lenderName = etName.getText().toString().trim();
        regionName = tvCarArea.getText().toString().trim();
        if (selPL == 0) selPL = -1;
        if (selCK == 0) selPL = -1;

    }

    private void requestPrice(int type) {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                reqType = type;
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("token", myglobal.user.token);
                fields.put("orderId", orderId);
                fields.put("numberDate", numberDate);
                fields.put("typeName", typeName);
                fields.put("typeShort", typeShort);
                fields.put("mileAge", mileAge);
                fields.put("lenderName", lenderName);
                fields.put("regionName", regionName);
                fields.put("carPl", (selPL - 1) + "");
                fields.put("carStatus", (selCK - 1) + "");
                if (selBank == 0)
                    fields.put("bankId", "0");
                else
                    fields.put("bankId", bankList.get(selBank - 1).bankId);

                for (int i = 0; i < arrImgUrl.size(); i++)
                    fields.put("carImg" + (i + 1), arrImgUrl.get(i).imgUrl);
                if (type == 0)
                    postMap(ServerUrl.submitOrder, fields, handler);
                else if (type == 1)
                    postMap(ServerUrl.requestReport, fields, handler);
                else if (type == 2)
                    postMap(ServerUrl.pjSaveOrderInfo, fields, handler);
                else if (type == 3)
                    postMap(ServerUrl.pjMake2ReportOrder, fields, handler);
                showProgress();
            } catch (Exception e) {
                e.printStackTrace();
                setThread_flag(false);
            }
        } else {
            setThread_flag(false);
            shortToast("网络连接不可用");
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            hideProgress();
            setThread_flag(false);
            switch (msg.what) {
                case 0:
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            MessageEvent event = new MessageEvent(MyConstants
                                    .AGENT_SUBMIT_ORDER_SUCCESS);
                            postEvent(event);

                            Intent it = new Intent(mContext, RequestSuccessActivity.class);
                            if (reqType == 2) {
                                it.putExtra("title", "保存");
                                it.putExtra("content", "保存成功");
                            } else {
                                it.putExtra("title", "发布");
                                it.putExtra("content", "发布成功");
                            }
                            startActivity(it);

                        } else if ("401".equals(status)) {
                            String message = result.get("message") + "";
                            shortToast(message);
                            goLoginPage();
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
                    shortToast("网路不给力!");
                    break;
            }
        }

        ;
    };

    /////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void selectArea(int style, String province, String city,String districts) {
        tvCarArea.setText(city);
    }

    private void setPLSpinnerAdapter() {
        if (mPLAdapter == null) {
            mPLAdapter = new PLAdapter();
            mPLListener = new PLListener();
            spPl.setAdapter(mPLAdapter);
        } else {
            mPLAdapter.notifyDataSetChanged();
        }
        spPl.setOnItemSelectedListener(mPLListener);
        spPl.setSelection(0);
    }

    private void setCKSpinnerAdapter() {
        if (mCKAdapter == null) {
            mCKAdapter = new CKAdapter();
            mCKListener = new CKListener();
            spCarStatus.setAdapter(mCKAdapter);
        } else {
            mCKAdapter.notifyDataSetChanged();
        }
        spCarStatus.setOnItemSelectedListener(mCKListener);
        spCarStatus.setSelection(0);
    }

    private void setBankSpinnerAdapter() {
        if (mBankAdapter == null) {
            mBankAdapter = new BankAdapter();
            mBankListener = new BankListener();
            spBank.setAdapter(mBankAdapter);
        } else {
            mBankAdapter.notifyDataSetChanged();
        }
        spBank.setOnItemSelectedListener(mBankListener);
        spBank.setSelection(0);
    }


    private void requestPermission() {
        try {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AgentPjOrderDetailActivity.this, new
                                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQ_PERMISSION);
                m_bPermissionGrant = false;
                return;
            }
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AgentPjOrderDetailActivity.this, new
                        String[]{Manifest.permission.CAMERA}, REQ_PERMISSION);
                m_bPermissionGrant = false;
                return;
            }
            m_bPermissionGrant = true;
        } catch (Exception e) {
            e.printStackTrace();
            m_bPermissionGrant = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestPermission();
                if (m_bPermissionGrant)
                    openCustomAlbum();
            }
        }
    }

    private void openCustomAlbum() {
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity
                .MODE_SINGLE);
        // 默认选择
        if (!m_strImgFile.equals("")) {
            ArrayList<String> arrSelImg = new ArrayList<String>();
            arrSelImg.add(m_strImgFile);
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, arrSelImg);
        }
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    private void startCropImage() {
        //Toast.makeText(ShangpinXingxiActivity.this, m_strImgFile, Toast.LENGTH_SHORT).show();
        Intent it = new Intent(mContext, ImageCropActivity.class);
        it.putExtra(Utils.IT_KEY_1, m_strImgFile);
        it.putExtra(Utils.IT_KEY_2, 4);
        it.putExtra(Utils.IT_KEY_3, 3);
        startActivityForResult(it, REQUEST_CODE_CROP_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                try {
                    ArrayList<String> arrSelImg = data.getStringArrayListExtra
                            (MultiImageSelectorActivity.EXTRA_RESULT);
                    if (arrSelImg == null) arrSelImg = new ArrayList<String>();
                    if (arrSelImg.size() > 0) {
                        m_strImgFile = arrSelImg.get(0);
                        compressImage();
                    }
                } catch (Exception e) {
                }
                break;
            case REQUEST_CODE_CROP_IMAGE:
                try {
                    try {
                        processImg();
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }
                break;
        }
    }

    private void compressImage() {

        m_TmpFile = myglobal.temp_path + Calendar.getInstance().getTimeInMillis();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                showProgress();
                if (!Utils.compressBitmapFile(m_strImgFile, m_TmpFile)) {
                    hideProgress();
                    shortToast("压缩失败");
                    return;
                }
                processImg();

            }
        });

    }

    private void processImg() {
        if (m_strImgFile.length() > 0) {
            uploadPhoto(m_strImgFile);
        } else {
            hideProgress();
        }
    }

    private void uploadPhoto(String path) {
        initOSS();
        if (oss != null) {
            asyncPutObjectFromLocalFile(path);
        } else {
            hideProgress();
            shortToast("图片上传失败～～");
        }
    }

    private void initOSS() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider
                (MyConstants.OssAccessKeyId, MyConstants.OssAccessKeySecret);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(getApplicationContext(), MyConstants.OssEndpoint, credentialProvider,
                conf);
    }

    // 从本地文件上传，使用非阻塞的异步接口
    public void asyncPutObjectFromLocalFile(String path) {

        long time = System.nanoTime();
        UPLOAD_OBJECT = MyConstants.ossUploadObject + time + myglobal.user.userPhone + ".jpg";
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(MyConstants.OssBucket, UPLOAD_OBJECT, path);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest,
                PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                //hideProgress();
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent
                        ("upload_pic_success"));
                Log.e("PutObject", "UploadSuccess");
                Log.e("ETag", result.getETag());
                Log.e("RequestId", result.getRequestId());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion,
                                  ServiceException serviceException) {
                hideProgress();

                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent
                        ("upload_pic_fail"));
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    ////////////////////////////////////////////////

    class PLAdapter extends BaseAdapter {

        LayoutInflater _LayoutInflater;

        public PLAdapter() {
            _LayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 下面是重要代码
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = _LayoutInflater.inflate(R.layout.layout_spinner_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            switch (position) {
                case 0:
                    holder.tvTitle.setText("排量");
                    break;
                case 1:
                    holder.tvTitle.setText(" 1.0以下 ");
                    break;
                case 2:
                    holder.tvTitle.setText(" 1.0-1.6L ");
                    break;
                case 3:
                    holder.tvTitle.setText(" 1.6-2.0L ");
                    break;
                case 4:
                    holder.tvTitle.setText(" 2.0-2.5L ");
                    break;
                default:
                    holder.tvTitle.setText(" 2.5-3.0L ");
                    break;
            }
            return convertView;
        }

    }

    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class PLListener implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            selPL = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    }

    class CKAdapter extends BaseAdapter {

        LayoutInflater _LayoutInflater;

        public CKAdapter() {
            _LayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 下面是重要代码
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = _LayoutInflater.inflate(R.layout.layout_spinner_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            switch (position) {
                case 0:
                    holder.tvTitle.setText("车况");
                    break;
                case 1:
                    holder.tvTitle.setText(" A-车况优秀，没有任何事故 ");
                    break;
                case 2:
                    holder.tvTitle.setText(" B-车况良好，少量剐蹭或钣 ");
                    break;
                case 3:
                    holder.tvTitle.setText(" C-车况一般，有过前后轻碰撞事故 ");
                    break;
                default:
                    holder.tvTitle.setText(" D-车况很差，有发生过较重事故 \n或伤及主体框架事故");
                    break;
            }
            return convertView;
        }

    }

    class CKListener implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            selCK = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    }

    class BankAdapter extends BaseAdapter {

        LayoutInflater _LayoutInflater;

        public BankAdapter() {
            _LayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            if (bankList == null) return 1;
            return bankList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 下面是重要代码
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = _LayoutInflater.inflate(R.layout.layout_spinner_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            if (position == 0)
                holder.tvTitle.setText("选择银行");
            else
                holder.tvTitle.setText(bankList.get(position - 1).bankName);

            return convertView;
        }

    }

    class BankListener implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            selBank = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    }


    class ImgViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_title_left)
        TextView tv_title_left;
        @Bind(R.id.iv_img)
        ImageView ivImg;
        @Bind(R.id.tv_detail)
        TextView tvDetail;
        @Bind(R.id.tv_del)
        TextView tvDel;
        @Bind(R.id.ll_item)
        LinearLayout llItem;

        ImgViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    ////////////////////////////date picker start  ///////////////////////////////

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
            ((ViewGroup) ((ViewGroup) dialog.getDatePicker().getChildAt(0)).getChildAt(0))
                    .getChildAt(2).setVisibility(View.GONE);
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
            tvDate.setText(m_strStartDate);
        }
    }

    ////////////////////////////  date picker end  ///////////////////////////////
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals("upload_pic_success")) {
                    arrImgUrl.get(selImg).imgUrl = MyConstants.OssPicUrl + UPLOAD_OBJECT;
                    showImgWithGlid(MyConstants.OssPicUrl + UPLOAD_OBJECT, arrImgUrl.get(selImg)
                            .ivImg);
                    hideProgress();
                } else if (intent.getAction().equals("upload_pic_fail")) {
                    hideProgress();
                    shortToast("图片上传失败～～");
                }
            }
        }
    }

    private void getCityInfo() {
        if (LocalCityTable.getInstance().getAllProvince().size() == 0 || Utils.getIntPreferences
                (mContext, "newVersion") == 1) {
            if (ServerUrl.isNetworkConnected(this)) {
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.et_mileage:
                    etMileage.setFocusable(true);
                    etMileage.setFocusableInTouchMode(true);
                    etMileage.requestFocus();

                    break;
                case R.id.et_name:
                    etName.setFocusable(true);
                    etName.setFocusableInTouchMode(true);
                    etName.requestFocus();

                    break;
            }
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SelectCarTypeEvent event) {
        if (event.isMsgOf(MyConstants.SEL_CAR_NAME)) {
            typeShort = event.getStringExtra("fullName");
            String carName = event.getStringExtra("carName");
            int index = carName.indexOf("款");
            if (index != -1) {
                carName = carName.replace("款 ", "款 " + typeShort + " ");
            } else {
                carName = typeShort + " " + carName;
            }
            tvCarType.setText(carName);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AgentPjOrderDetailEvent event) {
        tvOrderNo.setText("");
        etMileage.setText("");
        etName.setText("");
        etName.setFocusable(false);
        etName.setFocusableInTouchMode(false);

        etMileage.setFocusable(false);
        etMileage.setFocusableInTouchMode(false);

        tvCarType.setText("");
        tvDate.setText("");
        tvCarArea.setText("");
        setPLSpinnerAdapter();
        setCKSpinnerAdapter();
        setBankSpinnerAdapter();
        for (int i = 0; i < arrImgUrl.size(); i++) {
            showImgWithGlid("", arrImgUrl.get(i).ivImg);
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

                tvDate.setText(format);
                tvDate.setTextColor(Color.parseColor("#FF666666"));
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
}
