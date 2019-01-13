package com.cheweibao.liuliu.viewholder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cheweibao.liuliu.R;

/**
 * 
 * @author raphael
 * 描述录音项
 *
 */
public class ViewHolder4{
	
	public View mView;
	public Activity mActivity;
	public LayoutInflater inflater;
	public TextView tv_name;
	public EditText miaoshu_et;
	public ImageButton record_btn;
	public ImageButton play_btn;
	
	public ViewHolder4(View view, Activity activity){
		mView = view;
		mActivity = activity;
		inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
	}
	
	public View setData(){
		mView = inflater.inflate(R.layout.miaoshu, null);
		tv_name = (TextView) mView.findViewById(R.id.textView1);
		record_btn = (ImageButton) mView.findViewById(R.id.record_btn);
		record_btn.setBackgroundResource(R.drawable.ic_btn_speak);
		play_btn = (ImageButton) mView.findViewById(R.id.play_btn);
		play_btn.setBackgroundResource(R.drawable.ic_media_play);
		play_btn.setVisibility(View.INVISIBLE);
		return mView;
	}
}
