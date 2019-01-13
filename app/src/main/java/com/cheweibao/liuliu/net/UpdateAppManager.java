package com.cheweibao.liuliu.net;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.cheweibao.liuliu.BuildConfig;
import com.cheweibao.liuliu.agent.AgentWodeActivity;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.dialog.LoadDialog;
import com.cheweibao.liuliu.main.MainActivity;
import com.cheweibao.liuliu.main.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by unknow on 2018/5/2.
 */

public class UpdateAppManager {
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/" + "cheweibao" + "/" + "download" + "/";
    // 下载应用存放全路径
    private static final String FILE_NAME = FILE_PATH + "liuliu.apk";
    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 1;
    //Log日志打印标签
    private static final String TAG = "Update_log";

    public static Context context;
    public static SplashActivity Scontext;
    private AgentWodeActivity Acontext;
    //获取版本数据的地址
    private String version_path = ServerUrl.version_path;
    // 下载应用的进度条
    private ProgressDialog progressDialog;

    //新版本号和描述语言
    private int update_versionCode;
    private String update_describe;
    private boolean is_update = false;
    private LoadDialog dialog;

    public UpdateAppManager(SplashActivity context) {
        this.context = context;
        this.Scontext = context;
    }

    public UpdateAppManager(AgentWodeActivity context) {
        this.context = context;
        this.Acontext = context;
    }

    public UpdateAppManager(Context context) {
        this.context = context;
    }

    /**
     * 获取当前版本号
     */
    private int getCurrentVersion() {
        try {

            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);

            Log.e(TAG, "当前版本名和版本号" + info.versionName + "--" + info.versionCode);

            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();

            Log.e(TAG, "获取当前版本号出错");
            Scontext.autoLogin();
            return 0;
        }
    }

    /**
     * 从服务器获得更新信息
     */
    public void getUpdateMsg() {
        class mAsyncTask extends AsyncTask<String, Integer, String> {
            @Override
            protected String doInBackground(String... params) {

                HttpURLConnection connection = null;
                try {
                    URL url_version = new URL(params[0]);
                    connection = (HttpURLConnection) url_version.openConnection();
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream in = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "GBK"));

                    Log.e(TAG, "bufferReader读到的数据--" + reader);

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {             //回到主线程，更新UI

                Log.e(TAG, "异步消息处理反馈--" + s);
                try {
                    JSONObject object = new JSONObject(s);

                    update_versionCode = object.getInt("version");
                    update_describe = object.getString("describe");
                    if ("1".equals(object.getString("is_update"))) {
                        is_update = true;
                    }

                    Log.e(TAG, "新版本号--" + update_versionCode);
                    Log.e(TAG, "新版本描述--\n" + update_describe);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (update_versionCode > getCurrentVersion()) {

                    Log.e(TAG, "提示更新！");
                    //是否强制更新
                    mshowNoticeDialog(is_update);
                } else {
                    Log.e(TAG, "已是最新版本！");
                    Scontext.autoLogin();
                }
            }
        }

        new mAsyncTask().execute(version_path);
    }


    /**
     * 显示提示更新对话框
     */
    private void showNoticeDialog() {
        new AlertDialog.Builder(context)
                .setTitle("检测到新版本！")
                .setMessage(update_describe)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDownloadDialog();
                    }
                }).setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 显示强制提示更新对话框
     */
    public void mshowNoticeDialog(boolean iscompel) {
        dialog = new LoadDialog(context, iscompel);
        dialog.setMessage("最新版本：V" + "\n" +
                "新版本大小：" + "\n" +
                "新版本特性：\n" +
                "1.新增发布订单功能\n" +
                "2.编辑功能优化\n" +
                "3.BUG的修复，原有老用户发布订单出现闪退的修复");
        dialog.setYesOnclickListener("", new LoadDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        dialog.setNoOnclickListener(new LoadDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                dialog.dismiss();
            }
        });
        dialog.setClosenclickListener(new LoadDialog.onCloseOnclickListener() {
            @Override
            public void onCloseClick() {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 显示下载进度对话框
     */
    public void showDownloadDialog() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        new downloadAsyncTask(progressDialog, "").execute();
    }

    /**
     * 下载新版本应用
     */
    public static class downloadAsyncTask extends AsyncTask<Void, Integer, Integer> {
        private ProgressDialog progressDialog;
        private String apk_path = "";
        private MainActivity activity;
        private boolean iswrong = false;

        public downloadAsyncTask(ProgressDialog progressDialog, String apk_path) {
            this.progressDialog = progressDialog;
            this.apk_path = apk_path;
        }

        public downloadAsyncTask(ProgressDialog progressDialog, String apk_path, MainActivity activity) {
            this.progressDialog = progressDialog;
            this.apk_path = apk_path;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            Log.e(TAG, "执行至--onPreExecute");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            Log.e(TAG, "执行至--doInBackground");

            URL url;
            HttpURLConnection connection = null;
            InputStream in = null;
            FileOutputStream out = null;
            try {
                url = new URL(apk_path);
                connection = (HttpURLConnection) url.openConnection();

                in = connection.getInputStream();
                long fileLength = connection.getContentLength();
                File file_path = new File(FILE_PATH);
                if (!file_path.exists()) {
                    file_path.mkdir();
                }

                out = new FileOutputStream(new File(FILE_NAME));//为指定的文件路径创建文件输出流
                byte[] buffer = new byte[1024 * 1024];
                int len = 0;
                long readLength = 0;

                Log.e(TAG, "执行至--readLength = 0");

                while ((len = in.read(buffer)) != -1) {

                    out.write(buffer, 0, len);//从buffer的第0位开始读取len长度的字节到输出流
                    readLength += len;

                    int curProgress = (int) (((float) readLength / fileLength) * 100);

                    Log.e(TAG, "当前下载进度：" + curProgress);

                    publishProgress(curProgress);

                    if (readLength >= fileLength) {

                        Log.e(TAG, "执行至--readLength >= fileLength");
                        break;
                    }
                }

                out.flush();
                return INSTALL_TOKEN;

            } catch (Exception e) {
                e.printStackTrace();
                iswrong = true;
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            Log.e(TAG, "异步更新进度接收到的值：" + values[0]);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {

            progressDialog.dismiss();//关闭进度条
            if (iswrong) {
                if (activity != null) {
                }
                ToastUtil.showToast("更新地址出错！");
                return;
            }
            //安装应用
            installApp();
        }
    }

    /**
     * 安装新版本应用
     */
    public static void installApp() {
        if (context == null || TextUtils.isEmpty(FILE_NAME)) {
            return;
        }
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri =
                    FileProvider.getUriForFile(context, "com.cheweibao.liuliu", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
//        File saveFile = new File(FILE_NAME);
//        if (!saveFile.exists()) {
//            return;
//        }
//        Intent install = new Intent(Intent.ACTION_VIEW);
//        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
//            Uri apkUri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", saveFile);//在AndroidManifest中的android:authorities值
//            install = new Intent(Intent.ACTION_VIEW);
//            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
//        } else {
//            install.setDataAndType(Uri.fromFile(saveFile), "application/vnd.android.package-archive");
//            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//        context.startActivity(install);
//        //安装应用的逻辑
//        File appFile = new File(FILE_NAME);
//        if (!appFile.exists()) {
//            return;
//        }
//        // 跳转到新版本应用安装页面
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
//        context.startActivity(intent);
    }
}
