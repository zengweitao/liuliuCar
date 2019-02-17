package com.cheweibao.liuliu.examine;

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
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.adapter.UploadDataAdapter;
import com.cheweibao.liuliu.agent.TransitionActivity;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.CacheData;
import com.cheweibao.liuliu.common.ContactUtil;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.AuditInformationInfo;
import com.cheweibao.liuliu.data.IDPhotoInfo;
import com.cheweibao.liuliu.data.OrderInfo;
import com.cheweibao.liuliu.data.Province;
import com.cheweibao.liuliu.db.LocalCityTable;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.popup.SelectAreaPopWindow;
import com.cheweibao.liuliu.popup.SelectOnePopWindow;
import com.cheweibao.liuliu.ui.BaseGridView;
import com.cheweibao.liuliu.ui.MyEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 贷前审核第二步（个人婚姻、最高学历、单位名称、单位地址、单位电话等信息的填写）
 */
public class ToExamineOneActivity extends BaseActivity implements SelectAreaPopWindow.OnSelectListener {
    private final int SDK_PERMISSION_REQUEST = 127;
    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    @Bind(R.id.tvTopRight)
    TextView tvTopRight;
    @Bind(R.id.tv_marital_status)
    TextView tvMaritalStatus;
    @Bind(R.id.tv_highest_education)
    TextView tvHighestEducation;
    @Bind(R.id.et_unit_name)
    MyEditText etUnitName;
    @Bind(R.id.clear_unit_name)
    ImageView clearUnitName;
    @Bind(R.id.et_work_telephone)
    EditText etWorkTelephone;
    @Bind(R.id.clear_work_telephone)
    ImageView clearWorkTelephone;
    @Bind(R.id.et_work_address)
    MyEditText etWorkAddress;
    @Bind(R.id.clear_work_address)
    ImageView clearWorkAddress;
    @Bind(R.id.tv_living_conditions)
    TextView tvLivingConditions;
    @Bind(R.id.et_home_address)
    MyEditText etHomeAddress;
    @Bind(R.id.clear_home_address)
    ImageView clearHomeAddress;
    @Bind(R.id.et_contacts)
    MyEditText etContacts;
    @Bind(R.id.clear_contacts)
    ImageView clearContacts;
    @Bind(R.id.et_contact_phone)
    EditText etContactPhone;
    @Bind(R.id.clear_contact_phone)
    ImageView clearContactPhone;
    @Bind(R.id.tv_relation)
    TextView tvRelation;
    @Bind(R.id.gv_upload_data)
    BaseGridView gvUploadData;
    String MaritalStatus, HighestEducation, UnitName, WorkTelephone, WorkArea, WorkAddress, LivingConditions, HomeArea, HomeAddress, Contacts, ContactPhone, Relation;
    @Bind(R.id.ll_work_address)
    LinearLayout llWorkAddress;
    @Bind(R.id.ll_home_address)
    LinearLayout llHomeAddress;
    @Bind(R.id.tv_work_address)
    TextView tvWorkAddress;
    @Bind(R.id.tv_home_address)
    TextView tvHomeAddress;
    @Bind(R.id.ll_marital_status)
    LinearLayout llMaritalStatus;
    @Bind(R.id.ll_highest_education)
    LinearLayout llHighestEducation;
    @Bind(R.id.ll_living_conditions)
    LinearLayout llLivingConditions;
    @Bind(R.id.ll_relation)
    LinearLayout llRelation;
    private UploadDataAdapter uploaddapter;
    private List<IDPhotoInfo> arrImgUrl;
    int selImg = 0;
    private static final int REQ_PERMISSION = 2001;
    private boolean m_bPermissionGrant = false;
    private String m_strImgFile = "";
    private String m_TmpFile = "";
    public String UPLOAD_OBJECT = "";
    private String[] strMaritalStatus = new String[]{
            "单身", "已婚", "离异", "丧偶"
    };
    private String[] strHighestEducation = new String[]{
            "大专以下", "大专", "本科", "研究生", "研究生以上"
    };
    private String[] strLivingConditions = new String[]{
            "租用", "全款", "按揭"
    };
    private String[] strRelation = new String[]{
            "父母", "兄弟姐妹", "子女"
    };
    private OSS oss;
    private MyBroadcastReceiver myReceiver;
    private SelectAreaPopWindow selectAreaPopWindow;
    SelectOnePopWindow selectMaritalStatus;
    SelectOnePopWindow selectHighestEducation;
    SelectOnePopWindow selectLivingConditions;
    SelectOnePopWindow selectRelation;
    private CacheData cachedata;
    private AuditInformationInfo info;
    private int style;
    private OrderInfo orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_examine_one);
        ButterKnife.bind(this);
        cachedata = new CacheData(mContext);
        info = cachedata.getRecentSubList(myglobal.user.userPhone);
        style = getIntent().getIntExtra("style", 0);
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");

        getSteps();
        initView();
        setListens();
        initBroadCast();
    }

    private void setListens() {
        gvUploadData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selImg = position;
                requestPermission();
                if (!m_bPermissionGrant)
                    return;
                openCustomAlbum();
            }
        });
    }

    private void initBroadCast() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("upload_pic_success");
        myIntentFilter.addAction("upload_pic_fail");
        myReceiver = new MyBroadcastReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(myReceiver, myIntentFilter);
    }

    private void requestPermission() {
        try {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ToExamineOneActivity.this, new
                                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQ_PERMISSION);
                m_bPermissionGrant = false;
                return;
            }
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ToExamineOneActivity.this, new
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
            case 1:
                boolean permission = ContactUtil.getPermission(getApplicationContext(), data);
                if (permission) {
                    HashMap<String, String> params = ContactUtil.getContactInfo(getApplicationContext(), data);
                    if (params != null) {
                        if (params.containsKey("name")) {
                            etContacts.setText(params.get("name"));
                        }
                        if (params.containsKey("mobile")) {
                            etContactPhone.setText(params.get("mobile"));
                        }
                        setTvTopRight();
                    } else {
                        ToastUtil.showToast("获取联系人信息失败");
                    }
                } else {
                    showPermissionDialog();
                }
                break;
        }
    }

    /***
     * 跳往权限设置界面
     */
    private void showPermissionDialog() {
        Utils.showQuestionView(mContext, "", "请允许溜溜好车访问\n" +
                        "你的通讯录", "确定", "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
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

    ////////////////////////////  date picker end  ///////////////////////////////
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals("upload_pic_success")) {
                    arrImgUrl.get(selImg).value = MyConstants.OssPicUrl + UPLOAD_OBJECT;
                    uploaddapter.notifyDataSetChanged();
                    setTvTopRight();
                    hideProgress();
                } else if (intent.getAction().equals("upload_pic_fail")) {
                    hideProgress();
                    shortToast("图片上传失败～～");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        tvTopTitle.setText("贷前审核");
        tvTopRight.setText("下一步");
        tvTopRight.setTextColor(getResources().getColor(R.color.color_grey_999999));
        tvTopRight.setVisibility(View.VISIBLE);
        tvTopRight.setEnabled(false);
        selectMaritalStatus = new SelectOnePopWindow(mContext, strMaritalStatus);
        selectHighestEducation = new SelectOnePopWindow(mContext, strHighestEducation);
        selectLivingConditions = new SelectOnePopWindow(mContext, strLivingConditions);
        selectRelation = new SelectOnePopWindow(mContext, strRelation);
    }

    public void setTvTopRight() {
        UnitName = etUnitName.getText().toString().trim();
        WorkTelephone = etWorkTelephone.getText().toString().trim();
        WorkArea = tvWorkAddress.getText().toString();
        WorkAddress = etWorkAddress.getText().toString().trim();
        HomeArea = tvHomeAddress.getText().toString();
        HomeAddress = etHomeAddress.getText().toString().trim();
        Contacts = etContacts.getText().toString().trim();
        ContactPhone = etContactPhone.getText().toString().trim();
        saveInfo();
        cachedata.saveRecentSubList(myglobal.user.userPhone, info);
        if (TextUtils.isEmpty(MaritalStatus)) {
            return;
        }
        if (TextUtils.isEmpty(HighestEducation)) {
            return;
        }
        if (TextUtils.isEmpty(UnitName)) {
            return;
        }
        if (TextUtils.isEmpty(WorkTelephone)) {
            return;
        }
        if (TextUtils.isEmpty(WorkArea)) {
            return;
        }
        if (TextUtils.isEmpty(WorkAddress)) {
            return;
        }
        if (TextUtils.isEmpty(LivingConditions)) {
            return;
        }
        if (TextUtils.isEmpty(HomeArea)) {
            return;
        }
        if (TextUtils.isEmpty(HomeAddress)) {
            return;
        }
        if (TextUtils.isEmpty(Contacts)) {
            return;
        }
        if (TextUtils.isEmpty(ContactPhone)) {
            return;
        }
        if (TextUtils.isEmpty(Relation)) {
            return;
        }
        for (int i = 0; i < arrImgUrl.size(); i++) {
            if (arrImgUrl.get(i).isType() && TextUtils.isEmpty(arrImgUrl.get(i).value)) {
                return;
            }
        }
        tvTopRight.setTextColor(getResources().getColor(R.drawable.top_right_selectable));
        tvTopRight.setEnabled(true);
    }

    @OnClick({R.id.ivTopBack, R.id.tvTopRight, R.id.clear_unit_name, R.id.clear_work_telephone, R.id.clear_work_address,
            R.id.clear_home_address, R.id.clear_contacts, R.id.clear_contact_phone, R.id.ll_marital_status,
            R.id.ll_highest_education, R.id.ll_living_conditions, R.id.ll_contacts, R.id.ll_relation, R.id.ll_work_address, R.id.ll_home_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivTopBack:
                if (selectAreaPopWindow != null && selectAreaPopWindow.isShowing()) {
                    selectAreaPopWindow.dismiss();
                }
                if (selectMaritalStatus != null && selectMaritalStatus.isShowing()) {
                    selectMaritalStatus.dismiss();
                }
                if (selectHighestEducation != null && selectHighestEducation.isShowing()) {
                    selectHighestEducation.dismiss();
                }
                if (selectLivingConditions != null && selectLivingConditions.isShowing()) {
                    selectLivingConditions.dismiss();
                }
                if (selectRelation != null && selectRelation.isShowing()) {
                    selectRelation.dismiss();
                }
                finish();
                break;
            case R.id.tvTopRight:
                //下一步
                if (selectAreaPopWindow != null && selectAreaPopWindow.isShowing()) {
                    selectAreaPopWindow.dismiss();
                }
                if (selectMaritalStatus != null && selectMaritalStatus.isShowing()) {
                    selectMaritalStatus.dismiss();
                }
                if (selectHighestEducation != null && selectHighestEducation.isShowing()) {
                    selectHighestEducation.dismiss();
                }
                if (selectLivingConditions != null && selectLivingConditions.isShowing()) {
                    selectLivingConditions.dismiss();
                }
                if (selectRelation != null && selectRelation.isShowing()) {
                    selectRelation.dismiss();
                }
                hideKeybrond();
                if (!ButCommonUtils.isFastDoubleClick3s()) {
                    setValidate();
                }
                break;
            case R.id.clear_unit_name:
                etUnitName.setText("");
                break;
            case R.id.clear_work_telephone:
                etWorkTelephone.setText("");
                break;
            case R.id.clear_work_address:
                etWorkAddress.setText("");
                break;
            case R.id.clear_home_address:
                etHomeAddress.setText("");
                break;
            case R.id.clear_contacts:
                etContacts.setText("");
                break;
            case R.id.clear_contact_phone:
                etContactPhone.setText("");
                break;
            case R.id.ll_marital_status:
                //婚姻状况
                hideKeybrond();
                if (!ButCommonUtils.isFastDoubleClick()) {
                    if (selectMaritalStatus != null && selectMaritalStatus.isShowing()) return;
                    selectMaritalStatus.setOnSelectOneListener(new SelectOnePopWindow.OnSelectListener() {
                        @Override
                        public void selectArea(String str, int position) {
                            tvMaritalStatus.setText(str);
                            MaritalStatus = position + "";
                            setTvTopRight();
                        }
                    });
                    selectMaritalStatus.showAtLocation(llMaritalStatus, Gravity.BOTTOM | Gravity
                            .CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.ll_highest_education:
                //最高学历
                hideKeybrond();
                if (!ButCommonUtils.isFastDoubleClick()) {
                    if (selectHighestEducation != null && selectHighestEducation.isShowing())
                        return;
                    selectHighestEducation.setOnSelectOneListener(new SelectOnePopWindow.OnSelectListener() {
                        @Override
                        public void selectArea(String str, int position) {
                            tvHighestEducation.setText(str);
                            HighestEducation = position + "";
                            setTvTopRight();
                        }
                    });
                    selectHighestEducation.showAtLocation(llHighestEducation, Gravity.BOTTOM | Gravity
                            .CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.ll_living_conditions:
                //居住情况
                hideKeybrond();
                if (!ButCommonUtils.isFastDoubleClick()) {
                    if (selectLivingConditions != null && selectLivingConditions.isShowing())
                        return;
                    selectLivingConditions.setOnSelectOneListener(new SelectOnePopWindow.OnSelectListener() {
                        @Override
                        public void selectArea(String str, int position) {
                            tvLivingConditions.setText(str);
                            LivingConditions = position + "";
                            setTvTopRight();
                        }
                    });
                    selectLivingConditions.showAtLocation(llLivingConditions, Gravity.BOTTOM | Gravity
                            .CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.ll_contacts:
                //联系人
                hideKeybrond();
                if (!ButCommonUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.ll_relation:
                //关系
                hideKeybrond();
                if (!ButCommonUtils.isFastDoubleClick()) {
                    if (selectRelation != null && selectRelation.isShowing())
                        return;
                    selectRelation.setOnSelectOneListener(new SelectOnePopWindow.OnSelectListener() {
                        @Override
                        public void selectArea(String str, int position) {
                            tvRelation.setText(str);
                            Relation = position + "";
                            setTvTopRight();
                        }
                    });
                    selectRelation.showAtLocation(llRelation, Gravity.BOTTOM | Gravity
                            .CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.ll_work_address:
                //工作城市
                hideKeybrond();
                if (!ButCommonUtils.isFastDoubleClick()) {
                    if (selectAreaPopWindow != null && selectAreaPopWindow.isShowing()) return;
                    selectAreaPopWindow = new SelectAreaPopWindow(mContext, 1);
                    selectAreaPopWindow.setOnSelectListener(this);
                    selectAreaPopWindow.showAtLocation(llWorkAddress, Gravity.BOTTOM | Gravity
                            .CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.ll_home_address:
                //家庭所在城市
                hideKeybrond();
                if (!ButCommonUtils.isFastDoubleClick()) {
                    if (selectAreaPopWindow != null && selectAreaPopWindow.isShowing()) return;
                    selectAreaPopWindow = new SelectAreaPopWindow(mContext, 2);
                    selectAreaPopWindow.setOnSelectListener(this);
                    selectAreaPopWindow.showAtLocation(llHomeAddress, Gravity.BOTTOM | Gravity
                            .CENTER_HORIZONTAL, 0, 0);
                }
                break;
        }
    }

    @Override
    public void selectArea(int style, String province, String city, String districts) {
        String address;
        if (province.equals(city)) {
            address = city + districts;
        } else {
            address = province + city + districts;
        }
        if (style == 1) {
            tvWorkAddress.setText(address);
        } else if (style == 2) {
            tvHomeAddress.setText(address);
        }
        setTvTopRight();
    }

    private void setValidate() {
        UnitName = etUnitName.getText().toString().trim();
        WorkTelephone = etWorkTelephone.getText().toString().trim();
        WorkArea = tvWorkAddress.getText().toString();
        WorkAddress = etWorkAddress.getText().toString().trim();
        HomeArea = tvHomeAddress.getText().toString();
        HomeAddress = etHomeAddress.getText().toString().trim();
        Contacts = etContacts.getText().toString().trim();
        ContactPhone = etContactPhone.getText().toString().trim();

        if (TextUtils.isEmpty(MaritalStatus)) {
            ToastUtil.showLongToast("请选择婚姻状况");
            return;
        }
        if (TextUtils.isEmpty(HighestEducation)) {
            ToastUtil.showLongToast("请选择最高学历");
            return;
        }
        if (TextUtils.isEmpty(UnitName)) {
            ToastUtil.showLongToast("请输入单位名称");
            return;
        }
        if (TextUtils.isEmpty(WorkTelephone)) {
            ToastUtil.showLongToast("请输入单位电话");
            return;
        }
        if (!Utils.isPhone(WorkTelephone) && !Utils.checkMobileNO(ContactPhone)) {
            ToastUtil.showLongToast("单位电话格式不正确");
            return;
        }
        if (TextUtils.isEmpty(WorkArea)) {
            ToastUtil.showLongToast("请选择单位地址省市区");
            return;
        }
        if (TextUtils.isEmpty(WorkAddress)) {
            ToastUtil.showLongToast("请输入单位地址");
            return;
        }
        if (TextUtils.isEmpty(LivingConditions)) {
            ToastUtil.showLongToast("请选择居住情况");
            return;
        }
        if (TextUtils.isEmpty(HomeArea)) {
            ToastUtil.showLongToast("请选择家庭地址省市区");
            return;
        }
        if (TextUtils.isEmpty(HomeAddress)) {
            ToastUtil.showLongToast("请输入家庭地址");
            return;
        }
        if (TextUtils.isEmpty(Contacts)) {
            ToastUtil.showLongToast("请选择联系人");
            return;
        }
        if (TextUtils.isEmpty(ContactPhone)) {
            ToastUtil.showLongToast("请输入联系人电话");
            return;
        }
        if (!Utils.checkMobileNO(ContactPhone)) {
            ToastUtil.showLongToast("联系人电话格式不正确");
            return;
        }
        if (TextUtils.isEmpty(Relation)) {
            ToastUtil.showLongToast("请选择您与联系人的关系");
            return;
        }
        for (int i = 0; i < arrImgUrl.size(); i++) {
            if (arrImgUrl.get(i).isType() && TextUtils.isEmpty(arrImgUrl.get(i).value)) {
                ToastUtil.showLongToast("请上传" + arrImgUrl.get(i).getName());
                return;
            }
        }
        secondSteps();
    }

    private void saveInfo() {
        info.setSessionId(myglobal.user.userId);
        info.setMaritalStatus(MaritalStatus);
        info.setEducation(HighestEducation);
        info.setOrganization(UnitName);
        info.setWorkNumber(WorkTelephone);
        info.setWorkArea(WorkArea);
        info.setWorkAddress(WorkAddress);
        info.setHousingSituation(LivingConditions);
        info.setHomeArea(HomeArea);
        info.setHomeAddress(HomeAddress);
        info.setContactName(Contacts);
        info.setContactPhone(ContactPhone);
        info.setContactRelation(Relation);
        info.setUserFile(arrImgUrl);
        info.setOrderInfo(orderInfo);
    }

    private void setViews() {
        MaritalStatus = info.getMaritalStatus();
        if (!TextUtils.isEmpty(MaritalStatus)) {
            tvMaritalStatus.setText(strMaritalStatus[Integer.parseInt(MaritalStatus) - 1]);
        }
        HighestEducation = info.getEducation();
        if (!TextUtils.isEmpty(HighestEducation)) {
            tvHighestEducation.setText(strHighestEducation[Integer.parseInt(HighestEducation) - 1]);
        }
        UnitName = info.getOrganization();
        etUnitName.setText(UnitName);
        WorkTelephone = info.getWorkNumber();
        etWorkTelephone.setText(WorkTelephone);
        WorkArea = info.getWorkArea();
        tvWorkAddress.setText(WorkArea);
        WorkAddress = info.getWorkAddress();
        etWorkAddress.setText(WorkAddress);
        LivingConditions = info.getHousingSituation();
        if (!TextUtils.isEmpty(LivingConditions)) {
            tvLivingConditions.setText(strLivingConditions[Integer.parseInt(LivingConditions) - 1]);
        }
        HomeArea = info.getHomeArea();
        tvHomeAddress.setText(HomeArea);
        HomeAddress = info.getHomeAddress();
        etHomeAddress.setText(HomeAddress);
        Contacts = info.getContactName();
        etContacts.setText(Contacts);
        ContactPhone = info.getContactPhone();
        etContactPhone.setText(ContactPhone);
        Relation = info.getContactRelation();
        if (!TextUtils.isEmpty(Relation)) {
            tvRelation.setText(strRelation[Integer.parseInt(Relation) - 1]);
        }
        if (info.getUserFile() != null) {
            for (int i = 0; i < arrImgUrl.size(); i++) {
                IDPhotoInfo img = arrImgUrl.get(i);
                for (int j = 0; j < info.getUserFile().size(); j++) {
                    IDPhotoInfo jmg = info.getUserFile().get(j);
                    if (img.getName().equals(jmg.getName())) {
                        arrImgUrl.get(i).setValue(jmg.getValue());
                        arrImgUrl.get(i).setId(jmg.getId());
                        arrImgUrl.get(i).setUid(jmg.getUid());
                        break;
                    }
                }
            }
        }
        uploaddapter = new UploadDataAdapter(this, arrImgUrl);
        gvUploadData.setAdapter(uploaddapter);
        Utils.setGridViewHeight(gvUploadData);
//        uploaddapter.notifyDataSetChanged();
        etUnitName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    clearUnitName.setVisibility(View.VISIBLE);
                } else {
                    clearUnitName.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                setTvTopRight();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                setTvTopRight();
            }
        });
        etWorkTelephone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    clearWorkTelephone.setVisibility(View.VISIBLE);
                } else {
                    clearWorkTelephone.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                setTvTopRight();
            }
        });
        etWorkAddress.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    clearWorkAddress.setVisibility(View.VISIBLE);
                } else {
                    clearWorkAddress.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                setTvTopRight();
            }
        });
        etHomeAddress.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    clearHomeAddress.setVisibility(View.VISIBLE);
                } else {
                    clearHomeAddress.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                setTvTopRight();
            }
        });
        etContacts.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    clearContacts.setVisibility(View.VISIBLE);
                } else {
                    clearContacts.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                setTvTopRight();
            }
        });
        etContactPhone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    clearContactPhone.setVisibility(View.VISIBLE);
                } else {
                    clearContactPhone.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                setTvTopRight();
            }
        });
        setTvTopRight();
    }

    private void getSteps() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("sessionId", myglobal.user.userId);
                postMap(ServerUrl.config, fields, handler);
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
                        if (mContext != null) {
                            if ("0".equals(status)) {
                                Gson gson = new Gson();
                                JSONArray data = JSON.parseArray(gson.toJson(result.get("result")));
                                String strdata = data.toJSONString();
                                Type type = new TypeToken<List<IDPhotoInfo>>() {
                                }.getType();
                                arrImgUrl = gson.fromJson(strdata, type);
                                switch (style) {
                                    case 0:
                                        setViews();
                                        break;
                                    case 1:
//                                        if (TextUtils.isEmpty(info.getId())) {
                                        getInfo();
//                                        } else {
//                                            setViews();
//                                        }
                                        break;
                                }
                            } else {
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

    private void getInfo() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("sessionId", myglobal.user.userId);
                postMap(ServerUrl.secondStepsReject, fields, infohandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
        }
    }

    private Handler infohandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("code") + "";
                        if (mContext != null) {
                            if ("0".equals(status)) {
                                Gson gson = new Gson();
                                JSONObject data = JSON.parseObject(gson.toJson(result.get("result")));
                                String strdata = data.toJSONString();
                                info = gson.fromJson(strdata, AuditInformationInfo.class);
                            } else {
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
            setViews();
            hideProgress();
        }
    };

    private void secondSteps() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                Gson gson = new Gson();
                HashMap<String, String> fields = new HashMap<String, String>();
                info.setSessionId(myglobal.user.userId);
                String strinfo = gson.toJson(info, AuditInformationInfo.class);
                fields.put("data", strinfo);

                if (!TextUtils.isEmpty(info.getId())) {
                    postMap(ServerUrl.secondStepsUpdate, fields, secondhandler);
                } else {
                    postMap(ServerUrl.secondSteps, fields, secondhandler);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
        }
    }

    private Handler secondhandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("code") + "";
                        if (mContext != null) {
                            String message = result.get("desc") + "";
                            ToastUtil.showLongToast(message);
                        }
                        if ("SYS0008".equals(status)) {
                            baselogout();
                        } else if ("0".equals(status)) {
                            Intent Intent = new Intent(mContext, TransitionActivity.class);
                            Intent.putExtra("style", 1);
                            startActivity(Intent);
                            finish();
                            cachedata.saveRecentSubList(myglobal.user.userPhone, new AuditInformationInfo());
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

    @Override
    protected void onRestart() {
        super.onRestart();
        info = cachedata.getRecentSubList(myglobal.user.userPhone);
        setViews();
    }
}
