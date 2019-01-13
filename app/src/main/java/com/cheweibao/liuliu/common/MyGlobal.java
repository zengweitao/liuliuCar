package com.cheweibao.liuliu.common;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.data.Citys;
import com.cheweibao.liuliu.data.UserInfo;
import com.cheweibao.liuliu.main.MainActivity;
import com.cheweibao.liuliu.tools.CrashHandler;
import com.cheweibao.liuliu.tools.LogToFile;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.cheweibao.doctorcar.service.LocationService;

public class MyGlobal extends MultiDexApplication {

    //百度地图
//	public LocationService locationService;
    public Vibrator mVibrator;

    public static Context context;
    private static MyGlobal mInstance = null;
    /**
     * 这是sp储存的地方
     */
    public static String share_user_xml = "LiuLiuHaoChe";
    public static boolean wifiConnected = true;
    public static boolean picOnlyOnWifi = false;

    public MainActivity mActivity = null;

    public UserInfo user = null;

    public static String cache_path = "";
    public static String temp_path = "";

    public int SCR_WIDTH = 0;
    public int SCR_HEIGHT = 0;
    public float SCR_DENSITY = 0.0f;
    public static List<String> provinces = new ArrayList<>();
    public static HashMap<String, List<String>> citys = new HashMap<>();
    public static HashMap<String, List<String>> districts = new HashMap<>();
    public Citys locationCity = new Citys();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = getApplicationContext();
        initVariable();
        initImageLoader(getApplicationContext());

        try {
/*baidu map */
//			locationService = new LocationService(getApplicationContext());
            mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

//			SDKInitializer.initialize(getApplicationContext());

        } catch (Exception e) {
            e.printStackTrace();
        }
//        PushAgent mPushAgent = PushAgent.getInstance(this);
//        //注册推送服务，每次调用register方法都会回调该接口
//        mPushAgent.register(new IUmengRegisterCallback() {
//
//            @Override
//            public void onSuccess(String deviceToken) {
//                //注册成功会返回device token
//                Log.i("success", deviceToken);
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//                Log.i("failure", "deviceToken onFailure " + s + " " + s1);
//            }
//        });
//        Log.i("------", "deviceToken " + mPushAgent.getRegistrationId());
//        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
//        LogToFile.init(getApplicationContext());
        // 异常处理，不需要处理时注释掉这两句即可！
//        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
//        crashHandler.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void initVariable() {
        user = new UserInfo();
    }

    public static MyGlobal getInstance() {
        return mInstance;
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800)          // default = device screen dimensions
                //.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
                .threadPoolSize(3)                          // default
                .threadPriority(Thread.NORM_PRIORITY - 1)   // default
                .tasksProcessingOrder(QueueProcessingType.LIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)              // default
                .discCacheSize(50 * 1024 * 1024)        // 缓冲大小
                //.discCacheFileCount(100)                // 缓冲文件数目
                .discCacheFileNameGenerator(new Md5FileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .imageDecoder(new BaseImageDecoder(false)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                //.writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.activity_background, R.color.tv_gray);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
