package com.cheweibao.liuliu.agent;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
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
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.crop.ImageCropActivity;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.main.LoginActivity;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.RoundedImageView;
import com.google.gson.Gson;

import com.cheweibao.liuliu.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class AgentWodeActivity extends BaseActivity {

    private static final int REQ_PERMISSION = 2001;
    @Bind(R.id.iv_user)
    RoundedImageView ivUser;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.ll_change_url)
    View changeUrl;
    @Bind(R.id.et_change_url)
    EditText etChangeUrl;
    private boolean m_bPermissionGrant = false;
    private String m_strImgFile = "";
    private String m_TmpFile = "";
    public String UPLOAD_OBJECT = "";
    private OSS oss;
    MyBroadcastReceiver myReceiver;

    String headUrl = "";
    private int isVisi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_wode);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initBroadCast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContext = this;
        isVisi = 0;
        initView();
    }

    private void initView() {
        if (!TextUtils.isEmpty(myglobal.user.userId) && !"".equals(myglobal.user.orgName)) {
            if (!TextUtils.isEmpty(myglobal.user.userName)) {
                tvName.setText(myglobal.user.userName);
            } else {
                String maskNumber = myglobal.user.userPhone.substring(0, 3) + "****" + myglobal.user.userPhone.substring(7, myglobal.user.userPhone.length());
                tvName.setText(maskNumber);
            }
            showImgWithGlid(this, myglobal.user.userAvatar, ivUser, R.drawable.logo);
        } else {
            tvName.setText("点我登录");
            showImgWithGlid(this, myglobal.user.userAvatar, ivUser, R.drawable.logo);
        }
        etChangeUrl.setText(ServerUrl.BASE_URL);
    }

    @Override
    protected void onDestroy() {
        if (myReceiver != null) {
            if (myReceiver != null)
                LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        }
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initBroadCast() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("upload_pic_success");
        myIntentFilter.addAction("upload_pic_fail");
        myReceiver = new MyBroadcastReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(myReceiver, myIntentFilter);
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

    @OnClick({R.id.iv_user, R.id.ll_my_order, R.id.ll_my_repayment, R.id.ll_tel, R.id.ll_intercalate, R.id.tv_name, R.id.change_url,
            R.id.tv_cancel, R.id.tv_confirm})
    public void onViewClicked(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.iv_user:
                isVisi = 0;
                if (!TextUtils.isEmpty(myglobal.user.userId) && !"".equals(myglobal.user.orgName)) {
//                        requestPermission();
//                        if (!m_bPermissionGrant) {
//                            return;
//                        }
//                        openCustomAlbum();
                } else {
                    if (!ButCommonUtils.isFastDoubleClick()) {
                        //登录
                        it = new Intent(this, LoginActivity.class);
                        startActivity(it);
                    }
                }
                break;
            case R.id.tv_name:
                isVisi = 0;
                if (!TextUtils.isEmpty(myglobal.user.userId) && !"".equals(myglobal.user.orgName)) {
                } else {
                    if (!ButCommonUtils.isFastDoubleClick()) {
                        //登录
                        it = new Intent(this, LoginActivity.class);
                        startActivity(it);
                    }
                }
                break;
            case R.id.ll_my_order:
                isVisi = 0;
                //我的订单
                if (!ButCommonUtils.isFastDoubleClick()) {
                    if (!TextUtils.isEmpty(myglobal.user.userId)) {
//                        WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
//                        builder.setAppId("wx54d2e8635a6e02d1")
//                                .setPartnerId("1505649501")
//                                .setPrepayId("wx23165555616352aca96368b90803371220")
//                                .setPackageValue("Sign=WXPay")
//                                .setNonceStr("WSE1TQlPHoVEvargSCY966QuNkRSpyNE")
//                                .setTimeStamp("2018-07-23 16:55:56")
//                                .setSign("13B824B114DDA4759EFCD62231C1460B")
//                                .build().toWXPayNotSign(AgentWodeActivity.this, "123");
                        getVerifybankcard(0);
                    } else {
                        //登录
//                        getAli("测试支付", "支付金额0.01，测试支付接口", "0.01");
                        it = new Intent(this, LoginActivity.class);
                        startActivity(it);
                    }
                }
                break;
            case R.id.ll_my_repayment:
                isVisi = 0;
                //还款
                if (!ButCommonUtils.isFastDoubleClick()) {
                    if (!TextUtils.isEmpty(myglobal.user.userId)) {
//                        getVerifybankcard(1);
                        getModelDetail();
//                        it = new Intent(mContext, RepaymentActivity.class);
//                        startActivity(it);
//                        it = new Intent(mContext, TransitionActivity.class);
//                        it.putExtra("style", 7);
//                        startActivity(it);
                    } else {
                        //登录
                        it = new Intent(this, LoginActivity.class);
                        startActivity(it);
                    }
                }
                break;
            case R.id.ll_tel:
                isVisi = 0;
                if (!ButCommonUtils.isFastDoubleClick()) {
                    CalldialogShow();
                }
                break;
            case R.id.ll_intercalate:
                isVisi = 0;
                //设置
                if (!ButCommonUtils.isFastDoubleClick()) {
                    it = new Intent(this, AgentIntercalateActivity.class);
                    startActivity(it);
                }
                break;
            case R.id.change_url:
                if (isVisi < 10) {
                    if (isVisi == 1) {
                        tvhandler.postDelayed(runnable, 10000);//每两秒执行一次runnable.
                    }
                    isVisi++;
                } else {
                    //显示修改地址
                    changeUrl.setVisibility(View.VISIBLE);
                    isVisi = 0;
                }
                break;
            case R.id.tv_cancel:
                //显示修改地址
                changeUrl.setVisibility(View.GONE);
                break;
            case R.id.tv_confirm:
                //显示修改地址
                ServerUrl.BASE_URL = etChangeUrl.getText().toString().trim();
                changeUrl.setVisibility(View.GONE);
                break;
        }
    }

    Handler tvhandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            isVisi = 0;
            tvhandler.removeCallbacks(runnable);
//            handler.postDelayed(this, 2000);
        }
    };

    ///////////////////////
    private void requestPermission() {
        try {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(myglobal.mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQ_PERMISSION);
                m_bPermissionGrant = false;
                return;
            }
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(myglobal.mActivity, new String[]{Manifest.permission.CAMERA}, REQ_PERMISSION);
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

    private void startCropImage() {
        //Toast.makeText(ShangpinXingxiActivity.this, m_strImgFile, Toast.LENGTH_SHORT).show();
        Intent it = new Intent(this, ImageCropActivity.class);
        it.putExtra(Utils.IT_KEY_1, m_strImgFile);
        it.putExtra(Utils.IT_KEY_2, 1);
        it.putExtra(Utils.IT_KEY_3, 1);
        startActivityForResult(it, REQUEST_CODE_CROP_IMAGE);
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
                        startCropImage();
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

    private void processImg() {
        if (m_strImgFile.length() > 0) {
            uploadPhoto(m_strImgFile);
        }
    }

    private void uploadPhoto(String path) {
        initOSS();
        if (oss != null) {
            showProgress();
            asyncPutObjectFromLocalFile(path);
        } else {
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
                // LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("upload_pic_success"));
                Log.e("PutObject", "UploadSuccess");
                Log.e("ETag", result.getETag());
                Log.e("RequestId", result.getRequestId());

                updateUserInfo();
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

    private void updateUserInfo() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("token", myglobal.user.userId);
                fields.put("userAvatar", MyConstants.OssPicUrl + UPLOAD_OBJECT);
                postMap(ServerUrl.updateUserInfo, fields, updatehandler);
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
            }
        } else {
            hideProgress();
            shortToast("网络连接不可用");
        }
    }

    private Handler updatehandler = new Handler() {
        public void handleMessage(Message msg) {
            hideProgress();
            setThread_flag(false);
            switch (msg.what) {
                case 0:
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            showImgWithGlid(MyConstants.OssPicUrl + UPLOAD_OBJECT, ivUser);
                            myglobal.user.userAvatar = MyConstants.OssPicUrl + UPLOAD_OBJECT;
                            Utils.setUserInfo(mContext, "userAvatar", MyConstants.OssPicUrl + UPLOAD_OBJECT);
                            shortToast("设置成功！");
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
                    shortToast("网络不给力!");
                    break;
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isMsgOf(MyConstants.PERMISSION_GRANTED)) {
            requestPermission();
            if (!m_bPermissionGrant)
                return;
            openCustomAlbum();
        }
    }

    ///////////////////////////  date picker end  ///////////////////////////////
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals("upload_pic_success")) {
//                    headUrl = MyConstants.OssPicUrl + UPLOAD_OBJECT;
                } else if (intent.getAction().equals("upload_pic_fail")) {
                    hideProgress();
                    shortToast("图片上传失败～～");
                }
            }
        }
    }

    private void getVerifybankcard(int code) {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("sessionId", myglobal.user.userId);
                switch (code) {
                    case 0:
                        postMap(ServerUrl.verifyAppUserPage, fields, verifybankcardhandler);
                        break;
                    case 1:
                        postMap(ServerUrl.verifyAppUserPage, fields, verifybankcardhandler);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
        }
    }

    private Handler verifybankcardhandler = new Handler() {

        public void handleMessage(Message msg) {
            Intent it;
            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("code") + "";
                        String id = result.get("result") + "";
                        String message = result.get("desc") + "";
                        if ("SYS0008".equals(status)) {
                            ToastUtil.showToast(message);
                            baselogout();
                            it = new Intent(mContext, LoginActivity.class);
                            startActivity(it);
                            hideProgress();
                            return;
                        } else if ("LOANPRE0008".equals(status)) {
                            it = new Intent(mContext, TransitionActivity.class);
                            it.putExtra("style", 3);
                            startActivity(it);
                        } else if ("LOANPRE00010".equals(status)) {
                            it = new Intent(mContext, WXPayEntryActivity.class);
                            it.putExtra("style", 0);
                            startActivity(it);
                        } else if ("LOANPRE0006".equals(status)) {
                            it = new Intent(mContext, TransitionActivity.class);
                            it.putExtra("style", 4);
                            it.putExtra("desc", message);
                            it.putExtra("id", id);
                            startActivity(it);
                        } else if ("2".equals(status)) {
                            it = new Intent(mContext, WXPayEntryActivity.class);
                            it.putExtra("style", 1);
                            startActivity(it);
                        } else {
                            ToastUtil.showToast(message);
                            it = new Intent(mContext, TransitionActivity.class);
                            it.putExtra("style", 7);
                            startActivity(it);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    it = new Intent(mContext, TransitionActivity.class);
                    it.putExtra("style", 0);
                    startActivity(it);
                    break;
            }
            hideProgress();
        }

    };

    private void getModelDetail() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("sessionId", myglobal.user.userId);
                postMap(ServerUrl.getMyRepay, fields, handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
        }
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("code") + "";
                        if ("0".equals(status)) {
                            Gson gson = new Gson();
                            JSONObject data = JSON.parseObject(gson.toJson(result.get("result")));
                            if (data != null && !TextUtils.isEmpty(data + "")) {
                                Intent it = new Intent(mContext, RepaymentActivity.class);
                                startActivity(it);
                            } else {
                                Intent it = new Intent(mContext, TransitionActivity.class);
                                it.putExtra("style", 7);
                                startActivity(it);
                            }
                        } else if ("LOANPRE00033".equals(status)) {
                            Intent it = new Intent(mContext, TransitionActivity.class);
                            it.putExtra("style", 7);
                            startActivity(it);
                        } else {
                            if (mContext != null) {
                                String message = result.get("desc") + "";
                                ToastUtil.showToast(message);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            hideProgress();
        }

    };
}
