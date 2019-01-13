package com.cheweibao.liuliu.viewholder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cheweibao.liuliu.R;


/**
 * 
 * @author raphael
 * 单个打钩项
 *
 */
public class ViewHolder1 {
	public View mView;
	public Activity mActivity;
	public LayoutInflater inflater;
	public TextView tv_name;
	public CheckBox cb;
	
	
	public ViewHolder1(View view, Activity activity){
		mView = view;
		mActivity = activity;
		inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
	}

	public View setData(){
		mView = inflater.inflate(R.layout.common_select, null);
		tv_name = (TextView) mView.findViewById(R.id.textView1);
		cb = (CheckBox) mView.findViewById(R.id.common_checkbox);
		return mView;
	}
}
