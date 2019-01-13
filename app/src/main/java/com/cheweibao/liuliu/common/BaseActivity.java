package com.cheweibao.liuliu.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.data.Loadinfo;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.data.Province;
import com.cheweibao.liuliu.db.DBHelper;
import com.cheweibao.liuliu.db.LocalCityTable;
import com.cheweibao.liuliu.dialog.LoadDialog;
import com.cheweibao.liuliu.main.LoginActivity;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.net.UpdateAppManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.wangli.FinalHttp;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private final String mPageName = "AnalyticsHome";
    public static final int RESULT_REPAY = 0x999;
    public static final int REQUEST_CODE_SELECT_AREA = 0x20;
    public static final int REQUEST_CODE_GALLERY = 0x21;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x22;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x23;
    public static final int REQUEST_VIEW_PHOT0 = 0x24;
    public static final int RELOGIN = 0x25;

    protected MyGlobal myglobal;
    protected static Context mContext;
    public static MyBaseDialog progress = null;
    public static final String TAG = "Update_log";
    private LoadDialog dialog;
    // 下载应用的进度条
    private ProgressDialog progressDialog;
    protected static FinalHttp finalHttp;

    // The top level content view.
    private ViewGroup m_contentView = null;

    private static ProgressDialog prog = null;

    public ImageLoader imageLoader = ImageLoader.getInstance();
    public DisplayImageOptions options = null;
    public DisplayImageOptions optionsEmpty = null;
    public DisplayImageOptions optionsUser = null;
    public DBHelper helper;
    public int hismax = 3;

    private boolean thread_flag = false;
    private final Object lock_thread_flag = new Object();

    public void setThread_flag(boolean value) {
        synchronized (lock_thread_flag) {
            thread_flag = value;
        }
    }

    public boolean getThread_flag() {
        synchronized (lock_thread_flag) {
            return thread_flag;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myglobal = (MyGlobal) getApplicationContext();
//        mContext = getApplicationContext();
        mContext = BaseActivity.this;
        addStatusBarView();
        if (myglobal.SCR_WIDTH == 0 || savedInstanceState != null) {
            Utils.setCachePath(mContext);
            Utils.setupUnits(mContext, this);
            myglobal.user = Utils.getUserInfo(mContext);
        }
        MyGlobal.wifiConnected = Utils.canConnect(mContext, true);
        MyGlobal.picOnlyOnWifi = Utils.getBooleanPreferences(mContext, "picMode3G");
        initImageOption();

        finalHttp = FinalHttp.getInstance();
    }

    private void addStatusBarView() {
//        View view = new View(this);
//        view.setBackgroundColor(getResources().getColor(R.color.transparent));
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                Utils.getStatusBarHeight(this));
//        ViewGroup decorView = (ViewGroup) findViewById(android.R.id.content);
//        decorView.addView(view, params);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContext = this;
        if (myglobal == null) myglobal = (MyGlobal) getApplicationContext();
        if (myglobal.SCR_WIDTH == 0) {
            Utils.setCachePath(mContext);
            Utils.setupUnits(mContext, this);
            myglobal.user = Utils.getUserInfo(mContext);
        }
        MyGlobal.wifiConnected = Utils.canConnect(mContext, true);
        MyGlobal.picOnlyOnWifi = Utils.getBooleanPreferences(mContext, "picMode3G");
        initImageOption();


    }

    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }

    @Override
    public void setContentView(int layoutResID) {
        View mainView = (View) LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(mainView);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        m_contentView = (ViewGroup) view;
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        m_contentView = (ViewGroup) view;
    }

    @Override
    protected void onDestroy() {
        try {
            Utils.nullViewDrawablesRecursive(m_contentView);
            m_contentView = null;
//            mContext = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        super.onDestroy();
        System.gc();
    }

    @Override
    public void finish() {
        try {
            Utils.hideKeyboard(mContext);
        } catch (Exception e) {
            // TODO: handle exception
        }

        super.finish();
    }

    @Override
    public void onClick(View v) {

    }

//    protected void showWaitingView(){
//        if(prog!=null)	prog.dismiss();
//        prog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
//        prog.setTitle("");
//        prog.setMessage("请稍等。。。");
//        prog.setCancelable(true);
//        prog.show();
//    }
//
//    protected void hideWaitingView(){
//        if(prog!=null){
//            prog.dismiss();
//            prog = null;
//        }
//    }


    private void initImageOption() {
        if (options == null) {
            if (options == null) {
                options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.default_icon)
                        .showImageForEmptyUri(R.drawable.default_icon)
                        .showImageOnFail(R.drawable.default_icon)
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .considerExifParams(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();
            }
        }

        if (optionsUser == null) {
            optionsUser = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.icon_user_def)
                    .showImageForEmptyUri(R.drawable.icon_user_def)
                    .showImageOnFail(R.drawable.icon_user_def)
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        if (optionsEmpty == null) {
            optionsEmpty = new DisplayImageOptions.Builder()
//                    .showImageOnFail(R.drawable.default_noimage)
//                    .showImageOnLoading(R.drawable.default_noimage)
//                    .showImageForEmptyUri(R.drawable.default_noimage)
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }


    }

    public void showImageByLoader(String imageUrl, ImageView imageview, DisplayImageOptions options, int default_type) {


        if (MyGlobal.wifiConnected || !MyGlobal.picOnlyOnWifi)
            imageLoader.displayImage(imageUrl, imageview, options);
        else {
            File file = imageLoader.getDiscCache().get(imageUrl);
            if (file != null && file.exists() && file.isFile()) {
                imageLoader.displayImage(imageUrl, imageview, options);
            } else {
                if (default_type == 1)
                    imageview.setImageResource(R.drawable.default_noimage);
                else if (default_type == 2)
                    imageview.setImageResource(R.drawable.default_icon);
                else if (default_type == 3)
                    imageview.setImageResource(R.drawable.icon_user_def);
                else
                    imageview.setBackgroundColor(Color.WHITE);
            }
        }
    }

    public void showLocalImageByLoader(String imageUrl, ImageView imageview, DisplayImageOptions options, int default_type) {

        File file = imageLoader.getDiscCache().get(imageUrl);
        if (file != null && file.exists() && file.isFile()) {
            imageLoader.displayImage(imageUrl, imageview, options);
        } else {
            if (default_type == 1)
                imageview.setImageResource(R.drawable.default_noimage);
            else if (default_type == 2)
                imageview.setImageResource(R.drawable.default_icon);
            else if (default_type == 3)
                imageview.setImageResource(R.drawable.icon_user_def);
            else
                imageview.setBackgroundColor(Color.WHITE);
        }
    }

    public void showImageByLoader(String imageUrl, ImageView imageview, DisplayImageOptions options, int default_type, SimpleImageLoadingListener listener) {
        if (MyGlobal.wifiConnected || !MyGlobal.picOnlyOnWifi)
            imageLoader.displayImage(imageUrl, imageview, options, listener);
        else {
            File file = imageLoader.getDiscCache().get(imageUrl);
            if (file != null && file.exists() && file.isFile()) {
                imageLoader.displayImage(imageUrl, imageview, options, listener);
            } else {
                if (default_type == 1)
                    imageview.setImageResource(R.drawable.default_noimage);
                else
                    imageview.setImageResource(R.drawable.default_icon);
            }
        }
    }

    public static void shortToast(String msg) {
        if (mContext == null) return;
        ToastUtil.showToast(msg);
//        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void longToast(String msg) {
        if (mContext == null) return;
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    public boolean isLogin() {
        if (MyGlobal.getInstance().user == null) return false;
        return Utils.isValid(MyGlobal.getInstance().user.userId);
    }

    boolean isfirst = true;

    public void goLoginPage1() {
        //if(isLogin()) return;
        Intent it = new Intent(mContext, LoginActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
    }

    public void goLoginPage() {
        //if(isfirst) {
        Intent it = new Intent(mContext, LoginActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.putExtra("type", 1);
        startActivity(it);
        isfirst = false;
//        }else {
//
//        }
    }

    protected void showImgWithGlid(String url, ImageView imageView, int defRes) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .placeholder(defRes)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    protected void showImgWithGlid(Context context, String url, ImageView imageView, int defRes) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(defRes)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    protected void showImgWithGlid(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    protected void postEvent(MessageEvent event) {
        EventBus.getDefault().post(event);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    protected static void postMap(String url, HashMap<String, String> fields, Handler handler) {
        Log.i(TAG, "url:" + url);
        finalHttp.postMap(ServerUrl.BASE_URL + url, fields, handler);
    }

    protected void getMap(String url, Handler handler) {
        finalHttp.getMap(url, handler);
    }

    protected void uploadMap(String url, HashMap<String, String> fields, HashMap<String, File> files, Handler handler, int timeout) {
        finalHttp.uploadMap(url, fields, files, handler, timeout);
    }

    public static void showProgress() {
        if (mContext == null) return;

        try {
            progress = new MyBaseDialog(mContext, R.style.LoadDialog, "dlgProgress", "请稍等!");
            progress.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void showProgress(String name) {
        if (mContext == null) return;
        if (TextUtils.isEmpty(name)) {
            name = "请稍等!";
        }
        try {
            progress = new MyBaseDialog(mContext, R.style.LoadDialog, "dlgProgress", name);
            progress.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void hideProgress() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 获取当前版本号
     */
    public int getCurrentVersion() {
        try {

            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);

            Log.e(TAG, "当前版本名和版本号" + info.versionName + "--" + info.versionCode);

            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();

            Log.e(TAG, "获取当前版本号出错");
            return 0;
        }
    }

    /**
     * 显示强制提示更新对话框
     */
    public void mshowNoticeDialog(boolean iscompel, final Loadinfo loadinfo) {
        dialog = new LoadDialog(mContext, iscompel);
        dialog.setMessage("最新版本：V" + loadinfo.getVersion() + "\n" +
                "新版本大小：" + loadinfo.getSize() + "M\n" +
                "新版本特性：\n" + Utils.replaceWithBlank(loadinfo.getContent()));
        dialog.setYesOnclickListener("", new LoadDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                dialog.dismiss();
                showDownloadDialog(loadinfo.getUrl());
            }
        });
        dialog.setNoOnclickListener(new LoadDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                Utils.setIsLoad(mContext, true);
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
    public void showDownloadDialog(String apk_path) {
        new UpdateAppManager(mContext);
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        new UpdateAppManager.downloadAsyncTask(progressDialog, apk_path).execute();
    }

    public void CalldialogShow(String storeName, final String storePhone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTheme2);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_call, null);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView content = (TextView) v.findViewById(R.id.message);
        Button btn_sure = (Button) v.findViewById(R.id.yes);
        Button btn_cancel = (Button) v.findViewById(R.id.no);
        title.setText(storeName + "电话");
        content.setText(storePhone);
        btn_sure.setText("呼叫");
        btn_cancel.setText("取消");
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + storePhone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException a) {
                    a.getMessage();
                    TextUtils.isEmpty("调用呼叫功能异常");
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    public void CalldialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTheme2);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_call, null);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView content = (TextView) v.findViewById(R.id.message);
        Button btn_sure = (Button) v.findViewById(R.id.yes);
        Button btn_cancel = (Button) v.findViewById(R.id.no);
        title.setText("客服电话");
        content.setText("400-966-0081");
        btn_sure.setText("呼叫");
        btn_cancel.setText("取消");
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-966-0081"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException a) {
                    a.getMessage();
                    TextUtils.isEmpty("调用呼叫功能异常");
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    public int getHeight() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int heigth = dm.heightPixels;
        return heigth;
    }

    public int getWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        return width;
    }

    /**
     * 省份数据
     *
     * @return
     */
    public List<String> createMainDatas() {
        List<Province> provinceList = LocalCityTable.getInstance().getAllProvince();
        List<String> list = new ArrayList<>();
        for (Province province : provinceList) {
            list.add(province.getName());
        }
        return list;
    }

    /**
     * 城市数据
     *
     * @return
     */
    public HashMap<String, List<String>> createSubDatas() {
        return LocalCityTable.getInstance().getAllProvinceAndCity();
    }

    /**
     * 区域数据
     *
     * @return
     */
    public HashMap<String, List<String>> createDistrictsDatas() {
        return LocalCityTable.getInstance().getAllCityAndDistrict();
    }

    public void baselogout() {
        Intent it = new Intent(MyConstants.LOGOUT);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(it);
        it = new Intent(mContext, LoginActivity.class);
        startActivity(it);
    }

    public void hideKeybrond() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showToast(String status, String message) {
        if ("SYS0008".equals(status)) {
            baselogout();
            ToastUtil.showToast(message);
        }
    }

    public void backtv(String tv, int isUsed) {
        if (!TextUtils.isEmpty(tv)) {
            helper = DBHelper.getInstance(myglobal.getInstance());
            if (!hasData(tv)) {
                insertData(tv);
                //通知handler更新UI
            }
        }
        Intent it = new Intent(MyConstants.SEARCH);
        it.putExtra("name", tv);
        it.putExtra("isUsed", isUsed);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(it);
    }

    /**
     * 检查数据库中是否已经有该条记录
     */
    public boolean hasData(String tempName) {
        //判断是否有下一个
//        helper = DBHelper.getInstance(myglobal.getInstance());
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from searchHistory where name =?", new String[]{tempName});
        //判断是否有下一个
        if (cursor.moveToNext()) {
            SQLiteDatabase db = helper.getReadableDatabase();
            db.delete("searchHistory", "name =?", new String[]{tempName});
        }
        return false;
//            return cursor.moveToNext();
    }

    /**
     * 插入数据
     */
    public void insertData(String tempName) {
//        helper = DBHelper.getInstance(myglobal.getInstance());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put("name", tempName);
            db.insert("searchHistory", null, cv);
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }

    public void logout() {
        Utils.setUserInfo(mContext, "token", "");
        Utils.setUserInfo(mContext, "userId", "");
        Utils.setUserInfo(mContext, "userPhone", "");
        Utils.setLoginMobile(mContext, "");
        Utils.setLoginPwd(mContext, "");
        myglobal.user.userId = "";
    }

    public InputFilter inputFilter = new InputFilter() {

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]");

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Matcher matcher = pattern.matcher(charSequence);
            if (!matcher.find()) {
                return null;
            } else {
                ToastUtil.showToast("只能输入汉字,英文，数字");
                return "";
            }

        }
    };
}
