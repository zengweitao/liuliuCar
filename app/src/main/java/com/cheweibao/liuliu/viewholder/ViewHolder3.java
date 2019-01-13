package com.cheweibao.liuliu.viewholder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cheweibao.liuliu.R;


/**
 * 
 * @author raphael
 * 6种缺陷项
 *
 */
public class ViewHolder3 {

	public TextView tv_name;
	
//	public Spinner hh_sp;	//划痕
//	public Spinner bx_sp;	//变形
//	public Spinner xs_sp;	//锈蚀
//	public Spinner lw_sp;	//裂纹
//	public Spinner ax_sp;	//凹陷
//	public Spinner xf_sp;	//修复痕迹
	
	public ImageButton hh_ibtn;	//划痕
	public ImageButton bx_ibtn;	//变形
	public ImageButton xs_ibtn;	//锈蚀
	public ImageButton lw_ibtn;	//裂纹
	public ImageButton ax_ibtn;	//凹陷
	public ImageButton xf_ibtn;	//修复痕迹	
	
	public ImageButton camera_btn;
	
	public LayoutInflater inflater;
	public Activity mActivity;
	
	public View mView;
	
	public ViewHolder3(View view,Activity activity){
		mView = view;
		mActivity = activity;
		inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View setData(){
		mView = inflater.inflate(R.layout.six_defects_pro, null);
		tv_name 	= (TextView) mView.findViewById(R.id.textView1);
		hh_ibtn 	= (ImageButton) mView.findViewById(R.id.hh_ibtn);
		bx_ibtn 	= (ImageButton) mView.findViewById(R.id.bx_ibtn);
		xs_ibtn 	= (ImageButton) mView.findViewById(R.id.xs_ibtn);
		lw_ibtn 	= (ImageButton) mView.findViewById(R.id.lw_ibtn);
		ax_ibtn 	= (ImageButton) mView.findViewById(R.id.ax_ibtn);
		xf_ibtn 	= (ImageButton) mView.findViewById(R.id.xf_ibtn);
		camera_btn	= (ImageButton) mView.findViewById(R.id.camera_btn);
		
		hh_ibtn.setBackgroundResource(R.drawable.hh);
		bx_ibtn.setBackgroundResource(R.drawable.bx);
		xs_ibtn.setBackgroundResource(R.drawable.xs);
		lw_ibtn.setBackgroundResource(R.drawable.lw);
		ax_ibtn.setBackgroundResource(R.drawable.ax);
		xf_ibtn.setBackgroundResource(R.drawable.xf);	
		camera_btn.setVisibility(View.INVISIBLE);
		

        return mView;
	}
}
