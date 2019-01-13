package com.cheweibao.liuliu.checker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import com.google.gson.Gson;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.BaseFragment;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.CheckCarData;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.net.ServerUrl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class CheckerOrderDetailActivity extends BaseActivity {


    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    @Bind(R.id.tvTopRight)
    TextView tvTopRight;
    @Bind(R.id.tv_basic_info)
    TextView tvBasicInfo;
    @Bind(R.id.v_basic_info)
    View vBasicInfo;
    @Bind(R.id.tv_basic_conf)
    TextView tvBasicConf;
    @Bind(R.id.v_basic_conf)
    View vBasicConf;
    @Bind(R.id.tv_check)
    TextView tvBasicCheck;
    @Bind(R.id.v_check)
    View vBasicCheck;
    @Bind(R.id.tv_photo)
    TextView tvPhoto;
    @Bind(R.id.v_photo)
    View vPhoto;
    @Bind(R.id.tv_after_srv)
    TextView tvAfterSrv;
    @Bind(R.id.v_after_srv)
    View vAfterSrv;
    @Bind(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @Bind(R.id.ll_main)
    RelativeLayout llMain;
    @Bind(R.id.iv_basic_info_wc)
    ImageView ivBasicInfoWc;
    @Bind(R.id.iv_basic_conf_wc)
    ImageView ivBasicConfWc;
    @Bind(R.id.iv_check_wc)
    ImageView ivCheckWc;
    @Bind(R.id.iv_photo_wc)
    ImageView ivPhotoWc;
    @Bind(R.id.iv_after_srv_wc)
    ImageView ivAfterSrvWc;

    String orderId = "";

    private FragmentTransaction ft;
    BaseFragment fragmentBasicInfo = null;
    BaseFragment fragmentBasicConf = null;
    BaseFragment fragmentCheck = null;
    BaseFragment fragmentPhoto = null;
    BaseFragment fragmentAfterSrv = null;

    private static final int REQ_PERMISSION = 2001;
    private boolean m_bPermissionGrant = false;
    private String m_strImgFile = "";
    private String m_TmpFile = "";
    public String UPLOAD_OBJECT = "";
    private OSS oss;
    MyBroadcastReceiver myReceiver;

    int fromPhoto, picIndex, reqCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker_order_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        orderId = getIntent().getStringExtra("orderId");

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

        tvTopTitle.setText("订单详细");
        tvTopRight.setText("全部完成");
        unselectAll();
        tvBasicInfo.setSelected(true);
        vBasicInfo.setVisibility(View.VISIBLE);

        ivBasicInfoWc.setVisibility(View.GONE);
        ivBasicConfWc.setVisibility(View.GONE);
        ivCheckWc.setVisibility(View.GONE);
        ivPhotoWc.setVisibility(View.GONE);
        ivAfterSrvWc.setVisibility(View.GONE);

        CheckCarData.recycle();
        getOrderInfo();
    }

    private void initBroadCast() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("upload_pic_success");
        myIntentFilter.addAction("upload_pic_fail");
        myReceiver = new MyBroadcastReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(myReceiver, myIntentFilter);
    }


    public void getOrderInfo() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("orderId", orderId);
                fields.put("token", myglobal.user.userId);
                postMap(ServerUrl.jcGetOrderInfo, fields, infohandler);
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
                            JSONObject info = data.getJSONObject("info");
                            CheckCarData.setJcOrderDetailInfo(info);
                            setSelect(0);

                            showInfo();
                        }else if("401".equals(status)){
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
                    shortToast("网络不给力!");
                    break;
            }

        }

    };

    private void showInfo() {
        if ("1".equals(CheckCarData.basicInfoSave)) {
            ivBasicInfoWc.setVisibility(View.VISIBLE);
        } else {
            ivBasicInfoWc.setVisibility(View.GONE);
        }
        if ("1".equals(CheckCarData.basicConfSave)) {
            ivBasicConfWc.setVisibility(View.VISIBLE);
        } else {
            ivBasicConfWc.setVisibility(View.GONE);
        }
        if ("1".equals(CheckCarData.checkSave)) {
            ivCheckWc.setVisibility(View.VISIBLE);
        } else {
            ivCheckWc.setVisibility(View.GONE);
        }
        if ("1".equals(CheckCarData.photoSave)) {
            ivPhotoWc.setVisibility(View.VISIBLE);
        } else {
            ivPhotoWc.setVisibility(View.GONE);
        }
        if ("1".equals(CheckCarData.afterSrvSave)) {
            ivAfterSrvWc.setVisibility(View.VISIBLE);
        } else {
            ivAfterSrvWc.setVisibility(View.GONE);
        }

        if ("1".equals(CheckCarData.basicInfoSave) & "1".equals(CheckCarData.basicConfSave)
                && "1".equals(CheckCarData.checkSave) && "1".equals(CheckCarData.photoSave)
                && "1".equals(CheckCarData.afterSrvSave) && "0".equals(CheckCarData.checkAll)){

                    tvTopRight.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.ivTopBack, R.id.rl_basic_info, R.id.rl_basic_conf, R.id.rl_check, R.id.rl_photo, R.id.rl_after_srv, R.id.tvTopRight})
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.ivTopBack:
                finish();
                break;
            case R.id.tvTopRight:
                finishCheck();
                break;
            case R.id.rl_basic_info:
                unselectAll();
                tvBasicInfo.setSelected(true);
                vBasicInfo.setVisibility(View.VISIBLE);
                setSelect(0);
                break;
            case R.id.rl_basic_conf:
                unselectAll();
                tvBasicConf.setSelected(true);
                vBasicConf.setVisibility(View.VISIBLE);
                setSelect(1);
                break;
            case R.id.rl_check:
                unselectAll();
                tvBasicCheck.setSelected(true);
                vBasicCheck.setVisibility(View.VISIBLE);
                setSelect(2);
                break;
            case R.id.rl_photo:
                unselectAll();
                tvPhoto.setSelected(true);
                vPhoto.setVisibility(View.VISIBLE);
                setSelect(3);
                break;
            case R.id.rl_after_srv:
                unselectAll();
                tvAfterSrv.setSelected(true);
                vAfterSrv.setVisibility(View.VISIBLE);
                setSelect(4);
                break;
        }
    }

    public void finishCheck() {
        showProgress();
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("token", myglobal.user.userId);
                fields.put("orderId", orderId);
                postMap(ServerUrl.finishCheck, fields, handler);

            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
            }
        } else {
            hideProgress();
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
                            CheckCarData.checkAll = "1";
                            tvTopRight.setVisibility(View.GONE);
                            MessageEvent event = new MessageEvent(MyConstants.CHECK_ALL);
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
                        e.printStackTrace();
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

    @SuppressLint("CommitTransaction")
    public void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        hideFragments();
        switch (i) {
            case 0:
                if (fragmentBasicInfo == null) {
                    fragmentBasicInfo = BasicInfoFragment.newInstance(orderId);
                    ft.add(R.id.fragment_container, fragmentBasicInfo);
                }
                ft.show(fragmentBasicInfo);
                break;
            case 1:
                if (fragmentBasicConf == null) {
                    fragmentBasicConf = BasicConfFragment.newInstance(orderId);
                    ft.add(R.id.fragment_container, fragmentBasicConf);
                }
                ft.show(fragmentBasicConf);
                break;
            case 2:
                if (fragmentCheck == null) {
                    fragmentCheck = CheckFragment.newInstance(orderId);
                    ft.add(R.id.fragment_container, fragmentCheck);
                }
                ft.show(fragmentCheck);
                break;
            case 3:
                if (fragmentPhoto == null) {
                    fragmentPhoto = PhotoFragment.newInstance(orderId);
                    ft.add(R.id.fragment_container, fragmentPhoto);
                }
                ft.show(fragmentPhoto);
                break;
            case 4:
                if (fragmentAfterSrv == null) {
                    fragmentAfterSrv = AfterServiceFragment.newInstance(orderId);
                    ft.add(R.id.fragment_container, fragmentAfterSrv);
                }
                ft.show(fragmentAfterSrv);
                break;
        }
        ft.commit();
    }

    private void hideFragments() {
        if (fragmentBasicInfo != null) {
            ft.hide(fragmentBasicInfo);
        }
        if (fragmentBasicConf != null) {
            ft.hide(fragmentBasicConf);
        }
        if (fragmentCheck != null) {
            ft.hide(fragmentCheck);
        }
        if (fragmentPhoto != null) {
            ft.hide(fragmentPhoto);
        }
        if (fragmentAfterSrv != null) {
            ft.hide(fragmentAfterSrv);
        }

    }

    private void unselectAll() {
        tvBasicInfo.setSelected(false);
        tvBasicConf.setSelected(false);
        tvBasicCheck.setSelected(false);
        tvPhoto.setSelected(false);
        tvAfterSrv.setSelected(false);

        vBasicInfo.setVisibility(View.INVISIBLE);
        vBasicConf.setVisibility(View.INVISIBLE);
        vBasicCheck.setVisibility(View.INVISIBLE);
        vPhoto.setVisibility(View.INVISIBLE);
        vAfterSrv.setVisibility(View.INVISIBLE);
    }

    /////////////////
    private void requestPermission() {
        try {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CheckerOrderDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQ_PERMISSION);
                m_bPermissionGrant = false;
                return;
            }
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CheckerOrderDetailActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_PERMISSION);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        // 默认选择
        if (!m_strImgFile.equals("")) {
            ArrayList<String> arrSelImg = new ArrayList<String>();
            arrSelImg.add(m_strImgFile);
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, arrSelImg);
        }
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                try {
                    ArrayList<String> arrSelImg = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (arrSelImg == null) arrSelImg = new ArrayList<String>();
                    if (arrSelImg.size() > 0) {
                        m_strImgFile = arrSelImg.get(0);
                        compressImage();
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
        uploadPhoto(m_TmpFile);
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
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(MyConstants.OssAccessKeyId, MyConstants.OssAccessKeySecret);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(getApplicationContext(), MyConstants.OssEndpoint, credentialProvider, conf);
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

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                //hideProgress();
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("upload_pic_success"));
                Log.e("PutObject", "UploadSuccess");
                Log.e("ETag", result.getETag());
                Log.e("RequestId", result.getRequestId());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                hideProgress();

                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("upload_pic_fail"));
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

    ////////////////////
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals("upload_pic_success")) {

                    if (fromPhoto == 0) {
                        CheckCarData.scorePicArray[picIndex] = MyConstants.OssPicUrl + UPLOAD_OBJECT;
                        MessageEvent event = new MessageEvent(MyConstants.UPDATE_PIC);
                        event.putExtra("reqCode", reqCode);
                        postEvent(event);
                    } else {
                        CheckCarData.photoList.set(picIndex, MyConstants.OssPicUrl + UPLOAD_OBJECT);
                        MessageEvent event = new MessageEvent(MyConstants.UPDATE_PHOTO);
                        event.putExtra("photoId", picIndex);
                        postEvent(event);
                    }
                    hideProgress();
                } else if (intent.getAction().equals("upload_pic_fail")) {
                    hideProgress();
                    shortToast("图片上传失败～～");
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isMsgOf(MyConstants.SAVE_CHECK_INFO)) {
            showInfo();
        } else if (event.isMsgOf(MyConstants.REQUEST_TAKE_PIC)) {
            fromPhoto = event.getIntExtra("fromPhoto", 0);

            picIndex = event.getIntExtra("position", 0);
            reqCode = event.getIntExtra("reqCode", 0);

            requestPermission();
            if (!m_bPermissionGrant)
                return;
            openCustomAlbum();
        }
    }
}
