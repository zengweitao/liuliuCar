package com.cheweibao.liuliu.viewholder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cheweibao.liuliu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author raphael
 * 5种缺陷打钩项
 */
public class ViewHolder2{
	
	private static final String TAG = "ViewHolder2";
	
	public View mView;
	public Activity mActivity;
	public LayoutInflater inflater;
	public TextView tv_name;
	public CheckBox zz_cb;	//褶皱
	public CheckBox sh_cb;	//烧焊
	public CheckBox gh_cb;	//更换
	public CheckBox nq_cb;	//扭曲
	public CheckBox bx_cb;	//变形
	public ImageButton camera_btn;
	
	public List<CheckBox> vh2List;
	
	
	public ViewHolder2(View view, Activity activity){
		mView = view;
		mActivity = activity;		
		inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		vh2List = new ArrayList<CheckBox>();
	}

	public View setData(){
		mView = inflater.inflate(R.layout.five_defects, null);
		tv_name = (TextView) mView.findViewById(R.id.textView1);
		zz_cb = (CheckBox) mView.findViewById(R.id.zz_cb);
		zz_cb.setTag(R.id.zz_cb);
		sh_cb = (CheckBox) mView.findViewById(R.id.sh_cb);
		gh_cb = (CheckBox) mView.findViewById(R.id.gh_cb);
		nq_cb = (CheckBox) mView.findViewById(R.id.nq_cb);
		bx_cb = (CheckBox) mView.findViewById(R.id.bx_cb);
		camera_btn = (ImageButton) mView.findViewById(R.id.fd_camera_btn);
		camera_btn.setVisibility(View.INVISIBLE);
		
		vh2List.add(zz_cb);
		vh2List.add(sh_cb);
		vh2List.add(gh_cb);
		vh2List.add(nq_cb);
		vh2List.add(bx_cb);
	
		return mView;
	}

	public List<CheckBox> getVh2List() {
		return vh2List;
	}
}
