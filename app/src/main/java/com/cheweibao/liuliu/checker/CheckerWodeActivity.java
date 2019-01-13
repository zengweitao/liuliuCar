package com.cheweibao.liuliu.checker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.crop.ImageCropActivity;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.main.ResetPwdActivity;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.RoundedImageView;
import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class CheckerWodeActivity extends BaseActivity {

    @Bind(R.id.iv_user)
    RoundedImageView ivUser;
    @Bind(R.id.iv_mark)
    ImageView ivMark;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.ll_logout)
    RelativeLayout llLogout;
    @Bind(R.id.tv_total)
    TextView tvTotal;
    @Bind(R.id.sb_online)
    SwitchButton sbOnline;

    private static final int REQ_PERMISSION = 2001;
    private boolean m_bPermissionGrant = false;
    private String m_strImgFile = "";
    private String m_TmpFile = "";
    public String UPLOAD_OBJECT = "";
    private OSS oss;
    MyBroadcastReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker_wode);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initBroadCast();
    }

    private void initView() {
        tvTotal.setText("已检测" + myglobal.user.checkCount + "辆");
        tvName.setText("您好，" + myglobal.user.orgName + "用户" + myglobal.user.userName);
        showImgWithGlid(this, myglobal.user.userAvatar, ivUser, R.drawable.icon_user_def);

        showOnlineStatus();

        sbOnline.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                changOnlineStatus(isChecked);
            }
        });
    }

    private void showOnlineStatus() {
        if ("0".equals(myglobal.user.isOnline))
            sbOnline.setChecked(false);
        else
            sbOnline.setChecked(true);
    }

    private void changOnlineStatus(boolean isChecked) {
        if (getThread_flag()) {
            showOnlineStatus();
            return;
        }
        setThread_flag(true);
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("token", myglobal.user.token);
                fields.put("isOnline", (isChecked ? "1" : "0"));
                postMap(ServerUrl.setOnlineStatus, fields, setHander);
            } catch (Exception e) {
                showOnlineStatus();
                e.printStackTrace();
            }
        } else {
            showOnlineStatus();
            shortToast("网络连接不可用");
        }
    }

    private Handler setHander = new Handler() {
        public void handleMessage(Message msg) {
            setThread_flag(false);
            switch (msg.what) {
                case 0:
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            myglobal.user.isOnline = sbOnline.isChecked() ? "1" : "0";
                            Utils.setUserInfo(mContext, "isOnline", myglobal.user.isOnline);
                            shortToast("1".equals(myglobal.user.isOnline) ? "已在线～～" : "已离线～～");
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

    @OnClick({R.id.iv_user, R.id.ll_change_pwd, R.id.ll_version, R.id.ll_tel, R.id.ll_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_user:
                requestPermission();
                if (!m_bPermissionGrant)
                    return;
                openCustomAlbum();
                break;
            case R.id.ll_change_pwd:
                Intent it = new Intent(mContext, ResetPwdActivity.class);
                it.putExtra("type", 1);
                startActivity(it);
                break;
            case R.id.ll_version:
                break;
            case R.id.ll_tel:
                Utils.showQuestionView(mContext, "", "客服电话   4000-3030-300", "呼叫", "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4000-3030-300"));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                break;
            case R.id.ll_logout:
                dialoglogout();
                break;
        }
    }

    private void dialoglogout() {
        Utils.showQuestionView(mContext, "温謦提示", "确定要退出吗？", "确定", "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        _logout();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
    }

    private void _logout() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("token", myglobal.user.token);
                postMap(ServerUrl.logout, fields, handler);
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
                        if ("200".equals(status) || "401".equals(status)) {
                            Intent it = new Intent(MyConstants.LOGOUT);
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(it);
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
        Intent it = new Intent(mContext, ImageCropActivity.class);
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
                fields.put("token", myglobal.user.token);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isMsgOf(MyConstants.CHECK_ALL)) {
            myglobal.user.checkCount = (Integer.parseInt(myglobal.user.checkCount) + 1) + "";
            tvTotal.setText("已检测" + myglobal.user.checkCount + "辆");
        } else if (event.isMsgOf(MyConstants.PERMISSION_GRANTED)) {
            requestPermission();
            if (!m_bPermissionGrant)
                return;
            openCustomAlbum();
        }
    }
}
