package com.cheweibao.liuliu.agent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.OSS;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.Loadinfo;
import com.cheweibao.liuliu.db.DBHelper;
import com.cheweibao.liuliu.main.LoginActivity;
import com.cheweibao.liuliu.main.ResetPwdActivity;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.tools.ClearData;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgentIntercalateActivity extends BaseActivity {

    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;

    @Bind(R.id.tv_version)
    TextView tvVersion;
    private static final int REQ_PERMISSION = 2001;
    @Bind(R.id.ll_logout)
    RelativeLayout llLogout;
    @Bind(R.id.ll_change_pwd)
    LinearLayout llChangePwd;
    private boolean m_bPermissionGrant = false;
    private String m_strImgFile = "";
    private String m_TmpFile = "";
    public String UPLOAD_OBJECT = "";
    private OSS oss;

    String headUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_intercalate);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvVersion.setText(getVerName(AgentIntercalateActivity.this));
        tvTopTitle.setText(getResources().getString(R.string.intercalate));
        if (TextUtils.isEmpty(myglobal.user.userId)) {
            llLogout.setVisibility(View.GONE);
        } else {
            llLogout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @OnClick({R.id.ll_change_pwd, R.id.ll_version, R.id.ll_logout, R.id.ivTopBack, R.id.ll_clean_cache})
    public void onViewClicked(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.ll_change_pwd:
                if (!ButCommonUtils.isFastDoubleClick()) {
                    if (!TextUtils.isEmpty(myglobal.user.userId)) {
                        it = new Intent(mContext, ResetPwdActivity.class);
                        it.putExtra("type", 1);
                        startActivity(it);
                    } else {
                        //登录
                        it = new Intent(mContext, LoginActivity.class);
                        startActivity(it);
                    }
                }
                break;
            case R.id.ll_version:
                //版本更新检测
                if (!ButCommonUtils.isFastDoubleClick()) {
                    getVersion();
                }
                break;
            case R.id.ll_logout:
                if (!TextUtils.isEmpty(myglobal.user.userId)) {
                    dialoglogout();
                } else {
                    it = new Intent(MyConstants.LOGOUT);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(it);
                    finish();
                }
                break;
            case R.id.ivTopBack:
                finish();
                break;
            case R.id.ll_clean_cache:
                deleteData();
                ToastUtil.showToast("清除成功");
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
                fields.put("sessionId", myglobal.user.userId);
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
                            finish();
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

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = "V" + context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    private void getVersion() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("type", "1");
                postMap(ServerUrl.version_path, fields, versionHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
        }
    }

    //新版本号和描述语言
    private String update_versionName;
    private String update_describe = "";
    private Handler versionHandler = new Handler() {
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
                            Loadinfo info = gson.fromJson(data.getString("versionInfo"), Loadinfo.class);
                            update_versionName = info.getVersion();
                            update_describe = info.getContent();
                            if (Utils.compareVersion(update_versionName, Utils.GetVersion(mContext)) > 0) {
                                Log.e(TAG, "提示更新！");
                                //是否强制更新
                                mshowNoticeDialog(true, info);
                            } else {
                                Log.e(TAG, "已是最新版本！");
                                shortToast("已是最新版本！");
                            }
                            return;

                        } else {
                            if (mContext != null) {
                                String message = result.get("message") + "";
                                shortToast(message);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
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

    /**
     * 清空数据
     */
    private void deleteData() {
        DBHelper helper = DBHelper.getInstance(MyGlobal.getInstance());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from searchHistory");
        db.close();

        ClearData cleardata = new ClearData(mContext);
        cleardata.clearAllCache();
    }
}
