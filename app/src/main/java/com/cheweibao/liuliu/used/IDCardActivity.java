package com.cheweibao.liuliu.used;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.Loadinfo;
import com.cheweibao.liuliu.data.UserOrderDetail;
import com.cheweibao.liuliu.dialog.LoadDialog;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.wxapi.WXPayEntryActivity;

import org.json.JSONObject;

import java.util.HashMap;

import cn.tsign.esign.tsignlivenesssdk.TESeal;
import cn.tsign.esign.tsignlivenesssdk.bean.AuthResult;
import cn.tsign.esign.tsignlivenesssdk.bean.TESealConfig;
import cn.tsign.esign.tsignlivenesssdk.bean.custom.CustomStyle;
import cn.tsign.esign.tsignlivenesssdk.callback.FaceAuthListener;
import cn.tsign.esign.tsignlivenesssdk.callback.IdCardCompareListener;
import cn.tsign.esign.tsignlivenesssdk.callback.InitListener;
import cn.tsign.esign.tsignlivenesssdk.enums.EnumLogo;
import cn.tsign.esign.tsignlivenesssdk.enums.EnumServer;
import cn.tsign.esign.tsignlivenesssdk.util.PageChangeAnim;
import cn.tsign.esign.tsignlivenesssdk.util.PermissionUtils;
import cn.tsign.esign.tsignlivenesssdk.util.StatusBarUtils;
import cn.tsign.esign.tsignlivenesssdk.view.Activity.IdCardAutoActivity;
import cn.tsign.network.enums.Sign.EnumAlgorithm;
import cn.tsign.network.util.androidCommonMaster.StringUtils;

/**
 * Created by hasee on 2018/12/18.
 */

public class IDCardActivity extends BaseActivity {
    /**
     * 项目Id
     */
    public static final String PROJECT_ID = "1111563517";   //正式环境projectId
    /**
     * 项目密钥    (hmacSha256加密时,使用这个做为密钥)
     */
    public static final String PROJECT_SECRET = "95439b0863c241c63a861b87d1e647b7";
    //服务器环境
    private EnumServer server = EnumServer.test;
//    private EnumServer server = EnumServer.official;
    /**
     * RSA加密私钥     (使用rsa签名时,使用这个做签名)
     */
    private static final String RSA_PRIVATE_KEY = "";

    private static String _idCard;
    private static String _name;
    private static Activity activity;
    public static UserOrderDetail info = new UserOrderDetail();

    //比较识别出的身份信息.
    private static IdCardCompareListener idCardCompareListener = new IdCardCompareListener() {
        @Override
        public boolean compare(String idCard, String name) {
            boolean flag = true;
            if (!StringUtils.isEmpty(idCard) && !StringUtils.isEmpty(_idCard) && !StringUtils.isEquals(_idCard, idCard)) {
                flag = false;
            }
            if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(_name) && !StringUtils.isEquals(_name, name)) {
                flag = false;
            }
            return flag;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
//        setContentView(R.layout.activity_main);

//        StatusBarUtils.setWindowStatusBarColor(this, R.color.tsign_red);
        init();
//        setTitle(getTitle() + " V" + getVersion());
    }

    public  void Start() {
        //开始实名认证
        TESeal teSeal = TESeal.getInstance();
        if (teSeal != null) {     //如果不先调用TESeal.init方法,那么会getInstance方法会返回null
            // 开始实名认证 有第一步方式
//            TESeal.getInstance().faceAuth(activity, idCardCompareListener, new MyFaceAuthListener());
            // 开始实名认证 没有第一步方式
            TESeal.getInstance().faceAuth(activity, idCardCompareListener, new MyFaceAuthListener(), mPermissionGrant);
            // 清理存储的身份证图片.
            TESeal.getInstance().clearImageCache();
        }
    }

    private void init() {
        TESealConfig config = new TESealConfig.Builder(getApplicationContext())
                //.setNeedIdCardBack(true) //是否需要校验身份证有效期（拍身份证背面）
                .setProject_id(PROJECT_ID)//设置项目projectId
                .setProject_secret(PROJECT_SECRET) //设置项目projectSecert
                .setServer(server) //设置sdk连接的服务器地址
                .build();
        TESeal.getInstance().init(config, new InitListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                //项目初始化成功的回调
//                mTvText.setText(jsonObject.toString());
            }

            @Override
            public void onFailure(JSONObject jsonObject) {
                //初始化失败的回调,  比如返回projectId被停用,或者projectId不存在等等提示.
//                mTvText.setText(jsonObject.toString());
            }
        });
        //设置加密方式     一种是hmacSha256   另一种是RSA
        TESeal.getInstance().setEncrypt(EnumAlgorithm.hmacSha256, PROJECT_SECRET);
        // TESeal.getInstance().setEncrypt(EnumAlgorithm.rsa, RSA_PRIVATE_KEY);

        configStyle();                //设置页面样式
    }

    /**
     * 设置页面样式
     */
    private void configStyle() {
        CustomStyle customStyle = CustomStyle.getDefault();
        //设置标题的背景颜色
        customStyle.setTitleBackgroundColor(R.color.tsign_black);
        //设置标题的文字颜色
        customStyle.setTitleTextColor(R.color.tsign_red);
        //设置按钮的背景资源文件 (.xml)
        customStyle.setButtonBackgroundResource(R.drawable.selector_button);
        //设置按钮的文字颜色
        customStyle.setButtonTextColor(R.color.yellow);
        //设置第一页的底部显示e签宝的Logo还是e签宝的文字,或者可以不显示任务公司信息
        customStyle.setLogoIconOrText(EnumLogo.icon);
        //设置拍身份证页面的四个角颜色
        customStyle.setCameraCornerColor(R.color.green);
        TESeal.getInstance().setCustomStyle(customStyle);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

    private static PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {

        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_MULTI_PERMISSION:
                    Intent intent = new Intent(activity, IdCardAutoActivity.class);
                    activity.startActivity(intent);
                    PageChangeAnim.FadeInFadeOut(activity);
                    break;
                default:
                    break;
            }
        }
    };

     class MyFaceAuthListener implements FaceAuthListener {
        @Override
        public void onSuccess(JSONObject json) {
            //实名认证成功的回调
            AuthResult authResult = new AuthResult(json);
            authResult.getIdNo();  //身份证号
            authResult.getName();  //姓名
            authResult.getServiceId();   //serviceId
            authResult.getEvidenceId();
            authResult.getFrontIdCard();// 身份证正面照
            authResult.getBackIdCard();//身份证反面照
            authResult.getValidity(); //身份证反面照上的有效期
//            mTvText.setText(json.toString());
            checkUserInfo(authResult);
        }

        @Override
        public void onError(JSONObject json) {
            //实名认证失败的回调
//            mTvText.setText(json.toString());
            ToastUtil.showLongToast("实名认证失败");
            activity.finish();
        }

        @Override
        public void onCancel(JSONObject json) {
            //用户按后退键时的回调
//            mTvText.setText(json.toString());
            mshowNoticeDialog();
        }
    }

    private LoadDialog dialog;

    /**
     * 显示申请审核对话框
     */
    public  void mshowNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTheme2);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_agreement, null);
        TextView content = (TextView) v.findViewById(R.id.message);
        Button btn_sure = (Button) v.findViewById(R.id.yes);
        Button btn_cancel = (Button) v.findViewById(R.id.no);
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        content.setText(mContext.getResources().getText(R.string.xieyi));
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Start();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                activity.finish();
            }
        });
    }

    private  void checkUserInfo(AuthResult authResult) {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                if (progress != null && progress.isShowing()) {
                    hideProgress();
                }
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("id", info.getId());
                fields.put("realName", authResult.getName());
                fields.put("idcard", authResult.getIdNo());
                postMap(ServerUrl.checkUserInfo, fields, checkUserhandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
        }
    }

    public  Handler checkUserhandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        int status = Integer.parseInt(result.get("code") + "");
                        switch (status) {
                            case 0:
                                //申请审核
                                WXPayEntryActivity wxPayEntryActivity=new WXPayEntryActivity();
                                wxPayEntryActivity.setCount(30 * 60 * 1000l);
//                                WXPayEntryActivity.setCount(30 * 60 * 1000l);
                                break;
                            case -1:
                                if (mContext != null) {
                                    String message = result.get("msg") + "";
                                    ToastUtil.showToast(message);
                                    mshowNoticeDialog();
//                                    activity.finish();
                                }
                                break;
                            default:
                                break;
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