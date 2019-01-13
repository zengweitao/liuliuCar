package com.cheweibao.liuliu.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.cheweibao.liuliu.data.MessageEvent;
import com.wangli.FinalHttp;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.main.LoginActivity;
import com.cheweibao.liuliu.net.ServerUrl;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

public class BaseFragment extends Fragment implements OnClickListener
{
	public MyGlobal  myglobal;
	public Context mContext;

	protected FinalHttp finalHttp;
	
	public ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions options = null;
	public DisplayImageOptions optionsUser = null;
	public DisplayImageOptions optionsEmpty = null;
	public DisplayImageOptions optionsBook = null;
	public DisplayImageOptions optionsLogo = null;
	public DisplayImageOptions optionsVideo = null;
	public DisplayImageOptions optionsNews = null;

	private MyBaseDialog progress = null;

	private static ProgressDialog prog = null;
	
	private boolean thread_flag = false;
	private final Object lock_thread_flag = new Object();
	public void setThread_flag(boolean value){
		synchronized (lock_thread_flag) {
			thread_flag = value;
		}
	}
	public boolean getThread_flag(){
		synchronized (lock_thread_flag) {
			return thread_flag;
		}
	}	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		myglobal = (MyGlobal)mContext.getApplicationContext();
		
		if(myglobal.SCR_WIDTH== 0) {
			Utils.setCachePath(mContext);
			Utils.setupUnits(mContext);
			myglobal.user = Utils.getUserInfo(mContext);
		}
		MyGlobal.wifiConnected = Utils.canConnect(mContext, true);
		MyGlobal.picOnlyOnWifi = Utils.getBooleanPreferences(mContext, "picMode3G");
		initImageOption();

		finalHttp = FinalHttp.getInstance();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		System.gc();
		if(mContext == null) mContext = getActivity();
		if(myglobal == null) myglobal = (MyGlobal)mContext.getApplicationContext();
		if(myglobal.SCR_WIDTH== 0) {
			Utils.setCachePath(mContext);
			Utils.setupUnits(mContext);
			myglobal.user = Utils.getUserInfo(mContext);
		}
		MyGlobal.wifiConnected = Utils.canConnect(mContext, true);
		MyGlobal.picOnlyOnWifi = Utils.getBooleanPreferences(mContext, "picMode3G");
		initImageOption();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Utils.nullViewDrawablesRecursive(getView());
		System.gc();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }

	public void showProgress() {
		if(mContext == null) return;

		try {
			progress = new MyBaseDialog(mContext, R.style.Theme_Transparent, "dlgProgress", "请稍等!");
			progress.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void hideProgress(){
		try {
			if(progress != null){
				progress.dismiss();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void initImageOption(){
		if(options == null){
			if(options == null){
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
		
		if(optionsUser == null) {
			optionsUser = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.icon_user_def)
				.showImageOnFail(R.drawable.icon_user_def)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		}

		if(optionsEmpty == null) {
			optionsEmpty = new DisplayImageOptions.Builder()
					.showImageOnFail(R.drawable.default_icon)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
		}

		if(optionsLogo == null) {
			optionsLogo = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.applogo)
					.showImageOnFail(R.drawable.applogo)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
		}

		if(optionsVideo == null) {
			optionsVideo = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.def_video)
					.showImageOnFail(R.drawable.def_video)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
		}

		if(optionsNews == null) {
			optionsNews = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.default_icon)
					.showImageOnFail(R.drawable.default_icon)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
		}
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void showImageByLoader(String imageUrl, ImageView imageview, DisplayImageOptions options, int default_type){

//		int dipPx = Utils.dp2Pixel(mContext, 1);
//		if(default_type == 3)
//			imageUrl = String.format("%s?url=%s&w=%d&h=%d", ServerUrl.getCropImage, imageUrl, 100, 100);
//		else if(default_type == 4)
//			imageUrl = String.format("%s?url=%s&w=%d&h=%d", ServerUrl.getCropImage, imageUrl, 48*dipPx, 64*dipPx);
//		else if(default_type == 5)
//			imageUrl = String.format("%s?url=%s&w=%d&h=%d", ServerUrl.getCropImage, imageUrl, 62*dipPx, 34*dipPx);
//		else if(default_type == 6)
//			imageUrl = String.format("%s?url=%s&w=%d&h=%d", ServerUrl.getCropImage, imageUrl, 38*dipPx, 29*dipPx);

		if(MyGlobal.wifiConnected || !MyGlobal.picOnlyOnWifi) 
			imageLoader.displayImage(imageUrl, imageview, options);
		else{
			File file = imageLoader.getDiscCache().get(imageUrl);
			if(file != null && file.exists() && file.isFile()){
				imageLoader.displayImage(imageUrl, imageview, options);
			}
			else{
				if(default_type == 1)
					imageview.setImageResource(R.drawable.default_noimage);
				else if(default_type == 2)
					imageview.setImageResource(R.drawable.default_icon);
				else if(default_type == 3)
					imageview.setImageResource(R.drawable.icon_user_def);
			}
		}
	}
	
	public void showImageByLoader(String imageUrl, ImageView imageview, DisplayImageOptions options, int default_type, SimpleImageLoadingListener listener){
		if(MyGlobal.wifiConnected || !MyGlobal.picOnlyOnWifi) 
			imageLoader.displayImage(imageUrl, imageview, options, listener);
		else{
			File file = imageLoader.getDiscCache().get(imageUrl);
			if(file != null && file.exists() && file.isFile()){
				imageLoader.displayImage(imageUrl, imageview, options, listener);
			}
			else{
				if(default_type == 1)
					imageview.setImageResource(R.drawable.default_noimage);
				else
					imageview.setImageResource(R.drawable.default_icon);
			}
		}
	}

	protected  void showImgWithGlid(String url, ImageView imageView, int defRes){
		Glide.with(mContext)
				.load(url)
				.asBitmap()
				.placeholder(defRes)
				.diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.into(imageView);
	}
	protected  void showImgWithGlid(String url, ImageView imageView){
		Glide.with(mContext)
				.load(url)
				.asBitmap()
				.diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.into(imageView);
	}

	
	public void shortToast(String msg){
		if(mContext == null) return;
		Toast.makeText(mContext,	msg, Toast.LENGTH_SHORT).show();
	}
	
	public void longToast(String msg){
		if(mContext == null) return;
		Toast.makeText(mContext,	msg, Toast.LENGTH_LONG).show();
	}

	public boolean isLogin(){
		if (MyGlobal.getInstance().user == null) return false;
		return Utils.isValid(MyGlobal.getInstance().user.userId);
	}
	boolean isfirst = true;
	public void goLoginPage() {
//		if(isfirst) {
			Intent it = new Intent(mContext, LoginActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.putExtra("type", 1);
			startActivity(it);
//			isfirst = false;
//		}else {
//
//		}
	}

	protected void postEvent(MessageEvent event){
		EventBus.getDefault().post(event);
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void postMap(String url, HashMap<String, String> fields, Handler handler) {
		fields.put("timestamp", new Date().getTime()+""); //时间戳
		String preSignStr = ServerUrl.getCodeStr(fields, "&") + "ZFKJ_MLWH_TEST_APP_MD5_SIGN&KEY*()_+"; //获取待加密串
		fields.put("sign", ServerUrl.MD5(preSignStr));
		finalHttp.postMap(url, fields, handler);
	}
}