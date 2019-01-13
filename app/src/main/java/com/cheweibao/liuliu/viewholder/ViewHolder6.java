package com.cheweibao.liuliu.viewholder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cheweibao.liuliu.R;


/**
 * @author raphael
 * 拍照项
 * 
 */
public class ViewHolder6{
	
	public View mView;
	public Activity mActivity;
	public LayoutInflater inflater;
	public TextView tv_name;
	
	public ImageButton pic_btn;
	
	
	public ViewHolder6(View view, Activity activity){
		mView = view;
		mActivity = activity;		
		inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View setData(){
		mView = inflater.inflate(R.layout.piclayout, null);
		tv_name = (TextView) mView.findViewById(R.id.textView1);
		pic_btn = (ImageButton) mView.findViewById(R.id.takepicbtn);
		return mView;
	}
}
