package com.cheweibao.liuliu.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RadioGroup;


import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.PicPicker;
import com.cheweibao.liuliu.data.CheckCarData;
import com.cheweibao.liuliu.viewholder.ViewHolder1;
import com.cheweibao.liuliu.viewholder.ViewHolder2;
import com.cheweibao.liuliu.viewholder.ViewHolder3;
import com.cheweibao.liuliu.viewholder.ViewHolder4;
import com.cheweibao.liuliu.viewholder.ViewHolder5;
import com.cheweibao.liuliu.viewholder.ViewHolder6;
import com.cheweibao.liuliu.viewholder.ViewHolder7;
import com.cheweibao.liuliu.viewholder.ViewHolder8;
import com.cheweibao.liuliu.viewholder.ViewHolder9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyAdapter extends BaseAdapter{

	private final static String TAG = "MyAdapter";

	final int TYPE_1 = 0;	// 打钩项
	final int TYPE_2 = 1;	// 五种缺陷描述，直接判定为事故车
	final int TYPE_3 = 2;	// 六种缺陷描述情况，根据程度相应扣
	final int TYPE_4 = 3;	// 只描述缺陷
	final int TYPE_5 = 4;	// 3选1	正常，轻微，严重
	final int TYPE_6 = 5;	// 拍照
	final int TYPE_7 = 6;	// 4选1 	正常，轻微，严重， 未查
	final int TYPE_8 = 7;	// 3选1    正常，轻微，严重(照)
	final int TYPE_9 = 8;	// 4选1    正常，轻微（照），严重（照），未查

	public ViewHolder1 holder1 = null;
	public ViewHolder2 holder2 = null;
	public ViewHolder3 holder3 = null;
	public ViewHolder4 holder4 = null;
	public ViewHolder5 holder5 = null;
	public ViewHolder6 holder6 = null;
	public ViewHolder7 holder7 = null;
	public ViewHolder8 holder8 = null;
	public ViewHolder9 holder9 = null;

	LayoutInflater inflater;
	Activity mActivity;

	List<String> mList;			// 评估问题项
	List<String> bList;			// 用哪种view来组装
	List<String> pList;			// 如不需要拍照则填*
	List<String> sList;			// 扣分项
	List<Integer> numberList;	// 序号

	List<ImageButton> play_btn_list;
	List<ImageButton> record_btn_list;

	List<ImageButton> sd_camera_btn_List;
	List<ImageButton> fd_camera_btn_List;
	List<ImageButton> pic_camera_btn_List;
	List<ImageButton> to_camera_btn_List;
	List<ImageButton> fo_camera_btn_List;

	List<ImageButton> verify_cam_btn_List;

	private PicPicker mPicker;

	boolean isRecording = true;

	int flag = 0;							//用来记录6中缺陷项的 btn是哪种状态
	List<Boolean> isShowCameraList;			// 用来记录同一个adapter中是否有高于等于2级的评估项——> show camera icon
	boolean isShowCamera = false;			// 标志是否需要显示 camera icon
	HashMap<Integer, List<Boolean>> map;	// 存放不同adapter的 isShowCameraList，用position做为key来区分

	public MyAdapter(Activity activity){
		mActivity = activity;

		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		record_btn_list = new ArrayList<ImageButton>();
		play_btn_list = new ArrayList<ImageButton>();

		sd_camera_btn_List = new ArrayList<ImageButton>();
		fd_camera_btn_List = new ArrayList<ImageButton>();
		pic_camera_btn_List = new ArrayList<ImageButton>();
		to_camera_btn_List = new ArrayList<ImageButton>();
		fo_camera_btn_List = new ArrayList<ImageButton>();

		verify_cam_btn_List = new ArrayList<ImageButton>();

		map = new HashMap<Integer, List<Boolean>>();
		mPicker = new PicPicker(mActivity);
	}

	public boolean isRecording() {
		return isRecording;
	}

	public List<ImageButton> getSd_camera_btn_List() {
		return sd_camera_btn_List;
	}

	public List<ImageButton> getFd_camera_btn_List() {
		return fd_camera_btn_List;
	}

	public List<ImageButton> getPic_camera_btn_List() {
		return pic_camera_btn_List;
	}
	public List<ImageButton> getTo_camera_btn_List() {
		return to_camera_btn_List;
	}

	public List<ImageButton> getFo_camera_btn_List() {
		return fo_camera_btn_List;
	}

	public List<ImageButton> getVerify_cam_btn_List() {
		return verify_cam_btn_List;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		int type = getItemViewType(position);

		if (view == null) {

			Log.d("TAG", "---if 的 position------------>" +position);

			switch(type){
			// 打钩项
			case TYPE_1:
				holder1 = new ViewHolder1(view,mActivity);
				view = holder1.setData();
				setViewHolder1Listener(position);
				view.setTag(holder1);
				break;
			// 五种缺陷描述，直接判定为事故车
			case TYPE_2:
				holder2 = new ViewHolder2(view,mActivity);
				view = holder2.setData();
				holder2.camera_btn.setTag(position);
				holder2.camera_btn.setId(R.drawable.ic_menu_camera);
				fd_camera_btn_List.add(holder2.camera_btn);

				setViewHolder2Listener(position);
				view.setTag(holder2);
				break;
			// 六种缺陷描述情况，根据程度相应扣
			case TYPE_3:
				holder3 = new ViewHolder3(view,mActivity);
				view = holder3.setData();
				holder3.camera_btn.setTag(position);
				holder3.camera_btn.setId(R.drawable.ic_menu_camera);
				sd_camera_btn_List.add(holder3.camera_btn);

				setViewHolder3Listener(position);
				view.setTag(holder3);
				break;
			// 只描述缺陷
			case TYPE_4:
				holder4 = new ViewHolder4(view,mActivity);
				view = holder4.setData();
				//添加进list  为了一个按钮按下录音时disable的btn
				record_btn_list.add(holder4.record_btn);
				holder4.play_btn.setId(numberList.get(position));
				play_btn_list.add(holder4.play_btn);
				setViewHolder4Listener(position);
				view.setTag(holder4);
				break;
			// 3选1打钩交
			case TYPE_5:
				holder5 = new ViewHolder5(view,mActivity);
				view = holder5.setData();
				setViewHolder5Listener(position);
				view.setTag(holder5);
				break;
			// 拍照
			case TYPE_6:
				holder6 = new ViewHolder6(view,mActivity);
				view = holder6.setData();
				holder6.pic_btn.setTag(position);
				holder6.pic_btn.setId(R.drawable.ic_menu_camera);
				pic_camera_btn_List.add(holder6.pic_btn);
				verify_cam_btn_List.add(holder6.pic_btn);
				setViewHolder6Listener(position);
				view.setTag(holder6);
				break;
			// 4选1
			case TYPE_7:
				holder7 = new ViewHolder7(view,mActivity);
				view = holder7.setData();
				setViewHolder7Listener(position);
				view.setTag(holder7);
				break;
			// 3选1（照）
			case TYPE_8:
				holder8 = new ViewHolder8(view,mActivity);
				view = holder8.setData();
				holder8.camera_btn.setTag(position);
				holder8.camera_btn.setId(R.drawable.ic_menu_camera);
				to_camera_btn_List.add(holder8.camera_btn);

				setViewHolder8Listener(position);
				view.setTag(holder8);
				break;
			// 4选1（照）
			case TYPE_9:
				holder9 = new ViewHolder9(view,mActivity);
				view = holder9.setData();
				holder9.camera_btn.setTag(position);
				holder9.camera_btn.setId(R.drawable.ic_menu_camera);
				fo_camera_btn_List.add(holder9.camera_btn);

				setViewHolder9Listener(position);
				view.setTag(holder9);
				break;
			}
		}else {

			Log.d("", "---else的position------------>" +position);

			switch(type){
			case TYPE_1:
				holder1 = (ViewHolder1) view.getTag();
				setViewHolder1Listener(position);
				break;
			// 五种缺陷描述，直接判定为事故车
			case TYPE_2:
//				holder2 = (ViewHolder2) view.getTag();
				holder2 = new ViewHolder2(view,mActivity);
				view = holder2.setData();
				holder2.camera_btn.setTag(position);
				holder2.camera_btn.setId(R.drawable.ic_menu_camera);

				for(int i=0;i<fd_camera_btn_List.size();i++){
					if(fd_camera_btn_List.get(i).getTag().equals(position)){
						fd_camera_btn_List.remove(i);
					}
				}

				fd_camera_btn_List.add(holder2.camera_btn);
				setViewHolder2Listener(position);

				break;
			// 六种缺陷描述情况，根据程度相应扣
			case TYPE_3:
				holder3 = (ViewHolder3) view.getTag();
//				holder3 = new ViewHolder3(view,mActivity);
//				view = holder3.setData();
//				holder3.camera_btn.setTag(position);
//				holder3.camera_btn.setId(R.drawable.ic_menu_camera);
//				
//				for(int i=0;i<sd_camera_btn_List.size();i++){
//					if(sd_camera_btn_List.get(i).getTag().equals(position)){
//						sd_camera_btn_List.remove(i);
//					}
//				}
//				
//				sd_camera_btn_List.add(holder3.camera_btn);			
				setViewHolder3Listener(position);
				break;
			case TYPE_4:
				holder4 = (ViewHolder4) view.getTag();
				setViewHolder4Listener(position);
				break;
			case TYPE_5:
				holder5 = (ViewHolder5) view.getTag();
				setViewHolder5Listener(position);
				break;
			case TYPE_6:
				holder6 = (ViewHolder6) view.getTag();
				setViewHolder6Listener(position);
				break;
			case TYPE_7:
				holder7 = (ViewHolder7) view.getTag();
				setViewHolder7Listener(position);
				break;
			// 3选1（照）
			case TYPE_8:
				holder8 = new ViewHolder8(view,mActivity);
				view = holder8.setData();
				holder8.camera_btn.setTag(position);
				holder8.camera_btn.setId(R.drawable.ic_menu_camera);
				for(int i=0;i<to_camera_btn_List.size();i++){
					if(to_camera_btn_List.get(i).getTag().equals(position)){
						to_camera_btn_List.remove(i);
					}
				}
				to_camera_btn_List.add(holder8.camera_btn);
				setViewHolder8Listener(position);
				break;
			// 4选1（照）
			case TYPE_9:
				holder9 = new ViewHolder9(view,mActivity);
				view = holder9.setData();
				holder9.camera_btn.setTag(position);
				holder9.camera_btn.setId(R.drawable.ic_menu_camera);
				for(int i=0;i<fo_camera_btn_List.size();i++){
					if(fo_camera_btn_List.get(i).getTag().equals(position)){
						fo_camera_btn_List.remove(i);
					}
				}
				fo_camera_btn_List.add(holder9.camera_btn);
				setViewHolder9Listener(position);
				break;
			}
		}

		switch(type){
		case TYPE_1:
			holder1.tv_name.setText(mList.get(position));
			restoreHolder1(position);
			break;
		case TYPE_2:
			holder2.tv_name.setText(mList.get(position));
			restoreHolder2(position);
			camVerify(holder2.camera_btn,position);
			break;
		case TYPE_3:
			holder3.tv_name.setText(mList.get(position));
			restoreHolder3(position);
			camVerify(holder3.camera_btn,position);
			break;
		case TYPE_4:
			holder4.tv_name.setText(mList.get(position));
			break;
		case TYPE_5:
			holder5.tv_name.setText(mList.get(position));
			restoreHolder5(position);
			break;
		case TYPE_6:
			holder6.tv_name.setText(mList.get(position));
			restoreHolder6(position);
			break;
		case TYPE_7:
			holder7.tv_name.setText(mList.get(position));
			restoreHolder7(position);
			break;
		case TYPE_8:
			holder8.tv_name.setText(mList.get(position));
			restoreHolder8(position);
			camVerify(holder8.camera_btn,position);
			break;
		case TYPE_9:
			holder9.tv_name.setText(mList.get(position));
			restoreHolder9(position);
			camVerify(holder9.camera_btn,position);
			break;
		}

		if(position%2==0){
			view.setBackgroundResource(R.drawable.white);
		}else{
			view.setBackgroundResource(R.drawable.gray);
		}

		return view;
	}


	@Override
	public int getItemViewType(int position) {
		return Integer.valueOf(bList.get(position));
	}

	@Override
	public int getViewTypeCount() {
		return 9;
	}


	public void setList(List<String> mList,List<String> bList,List<String> pList,List<Integer> numberList) {
		this.numberList = numberList; 	// 序号
		this.mList = mList;				// 内容
		this.bList = bList;				// view样式
		this.pList = pList;				// 照片位置
	}

	public int getCount() {
		return mList.size();
	}

	public Object getItem(int position) {
		return mList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 *  单个打钩项数据restore
	 */
	private void restoreHolder1(int position){
		//载入之前的记录
		if(!TextUtils.isEmpty(CheckCarData.scoreArray[numberList.get(position)])){
			if(CheckCarData.scoreArray[numberList.get(position)].split("-")[1].equals("0")){
				holder1.cb.setChecked(false);
			}else{
				holder1.cb.setChecked(true);
			}
		}else{
			CheckCarData.scoreArray[numberList.get(position)]
			= numberList.get(position)+"-1";
		}
	}
	/**
	 *  单个打钩项
	 */
	private void setViewHolder1Listener(int position){

		//默认全部选中
		holder1.cb.setChecked(true);

		holder1.cb.setTag(position);
		holder1.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					CheckCarData.scoreArray[numberList.get((Integer)buttonView.getTag())]
							= numberList.get((Integer)buttonView.getTag())+"-1";
				}else{
					CheckCarData.scoreArray[numberList.get((Integer)buttonView.getTag())]
							= numberList.get((Integer)buttonView.getTag())+"-0";
				}
			}
		});
	}

	/**
	 *  5种缺陷打钩项 的监听数据restore
	 */
	private void restoreHolder2(int position){
		List<CheckBox> cbList = holder2.getVh2List();

		//载入之前的记录
		if(!TextUtils.isEmpty(CheckCarData.scoreArray[numberList.get(position)])){
			String boc = CheckCarData.scoreArray[numberList.get(position)].split("-")[1];
			for(int i=0;i<boc.length();i++){
				if(boc.substring(i, i+1).equals("0")){
					cbList.get(i).setChecked(false);
				}else{
					cbList.get(i).setChecked(true);
				}
			}
		}else{
			CheckCarData.scoreArray[numberList.get(position)] = numberList.get(position)+"-"+"00000";		//初始化str全是未选中的状态
		}
	}
	/**
	 *  5种缺陷打钩项 的监听
	 */
	private void setViewHolder2Listener(int position){

		List<CheckBox> cbList = holder2.getVh2List();

		holder2.camera_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mPicker.doTakePhoto(numberList.get((Integer)v.getTag()),(Integer)v.getTag()+100);
			}
		});

		for(int i=0;i<cbList.size();i++){
			cbList.get(i).setTag(position);
			cbList.get(i).setId(i);

			cbList.get(i).setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					String str = CheckCarData.scoreArray[numberList.get((Integer)buttonView.getTag())].split("-")[1];
					if(isChecked){
						try {
							CheckCarData.scoreArray[numberList.get((Integer)buttonView.getTag())] =
									numberList.get((Integer)buttonView.getTag())+"-"+replace(str, buttonView.getId()+1, "1");
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}else{
						try {
							CheckCarData.scoreArray[numberList.get((Integer)buttonView.getTag())] =
									numberList.get((Integer)buttonView.getTag())+"-"+replace(str, buttonView.getId()+1, "0");
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}

					if(CheckCarData.scoreArray[numberList.get((Integer)buttonView.getTag())].split("-")[1].contains("1")){
						for(int i=0;i<fd_camera_btn_List.size();i++){
							if(fd_camera_btn_List.get(i).getTag() == buttonView.getTag()){
								fd_camera_btn_List.get(i).setVisibility(View.VISIBLE);

								for(int j=0;j<verify_cam_btn_List.size();j++){
									if(verify_cam_btn_List.get(j).getTag().equals(buttonView.getTag())){
										verify_cam_btn_List.remove(j);
									}
								}
								verify_cam_btn_List.add(fd_camera_btn_List.get(i));
							}
						}
					}else{
						for(int i=0;i<fd_camera_btn_List.size();i++){
							if(fd_camera_btn_List.get(i).getTag() == buttonView.getTag()){
								fd_camera_btn_List.get(i).setVisibility(View.INVISIBLE);
								if(verify_cam_btn_List.contains(fd_camera_btn_List.get(i))){
									verify_cam_btn_List.remove(fd_camera_btn_List.get(i));
								}
							}
						}
					}
				}
			});
		}
	}

	private void camVerify(ImageButton camera_btn,int position){
        //"-------------照片是否打勾----------------->
        if(!TextUtils.isEmpty(CheckCarData.scorePicArray[numberList.get(position)])){
            camera_btn.setBackgroundResource(R.drawable.ic_menu_camera_save);
            camera_btn.setVisibility(View.VISIBLE);
            camera_btn.setId(R.drawable.ic_menu_camera_save);
        }
	}

	private void camShownCheck(int which,boolean isShown){
		isShowCameraList.remove(which);
		isShowCameraList.add(which,isShown);
	}

	/**
	 * 6种缺陷打钩项数据restore
	 */
	private void restoreHolder3(int position){
		//载入之前的记录
		if(!TextUtils.isEmpty(CheckCarData.scoreArray[numberList.get(position)])){
			String boc = CheckCarData.scoreArray[numberList.get(position)].split("-")[1];
			for(int i=0;i<6;i++){
				switch(i){
				case 0:
					switch(Integer.valueOf(boc.substring(1,2))){
					case 0:
						holder3.hh_ibtn.setBackgroundResource(R.drawable.hh);
						camShownCheck(0,false);
						break;
					case 1:
						holder3.hh_ibtn.setBackgroundResource(R.drawable.hh1);
						break;
					case 2:
						holder3.hh_ibtn.setBackgroundResource(R.drawable.hh2);
						camShownCheck(0,true);
						break;
					case 3:
						holder3.hh_ibtn.setBackgroundResource(R.drawable.hh3);
						break;
					}
					break;
				case 1:
					switch(Integer.valueOf(boc.substring(3,4))){
					case 0:
						holder3.bx_ibtn.setBackgroundResource(R.drawable.bx);
						camShownCheck(1,false);
						break;
					case 1:
						holder3.bx_ibtn.setBackgroundResource(R.drawable.bx1);
						break;
					case 2:
						holder3.bx_ibtn.setBackgroundResource(R.drawable.bx2);
						camShownCheck(1,true);
						break;
					case 3:
						holder3.bx_ibtn.setBackgroundResource(R.drawable.bx3);
						break;
					}
					break;
				case 2:
					switch(Integer.valueOf(boc.substring(5,6))){
					case 0:
						holder3.xs_ibtn.setBackgroundResource(R.drawable.xs);
						camShownCheck(2,false);
						break;
					case 1:
						holder3.xs_ibtn.setBackgroundResource(R.drawable.xs1);
						break;
					case 2:
						holder3.xs_ibtn.setBackgroundResource(R.drawable.xs2);
						camShownCheck(2,true);
						break;
					case 3:
						holder3.xs_ibtn.setBackgroundResource(R.drawable.xs3);
						break;
					}
					break;
				case 3:
					switch(Integer.valueOf(boc.substring(7,8))){
					case 0:
						holder3.lw_ibtn.setBackgroundResource(R.drawable.lw);
						camShownCheck(3,false);
						break;
					case 1:
						holder3.lw_ibtn.setBackgroundResource(R.drawable.lw1);
						break;
					case 2:
						holder3.lw_ibtn.setBackgroundResource(R.drawable.lw2);
						camShownCheck(3,true);
						break;
					case 3:
						holder3.lw_ibtn.setBackgroundResource(R.drawable.lw3);
						break;
					}
					break;
				case 4:
					switch(Integer.valueOf(boc.substring(9,10))){
					case 0:
						holder3.ax_ibtn.setBackgroundResource(R.drawable.ax);
						camShownCheck(4,false);
						break;
					case 1:
						holder3.ax_ibtn.setBackgroundResource(R.drawable.ax1);
						break;
					case 2:
						holder3.ax_ibtn.setBackgroundResource(R.drawable.ax2);
						camShownCheck(4,true);
						break;
					case 3:
						holder3.ax_ibtn.setBackgroundResource(R.drawable.ax3);
						break;
					}
					break;
				case 5:
					switch(Integer.valueOf(boc.substring(11, 12))){
					case 0:
						holder3.xf_ibtn.setBackgroundResource(R.drawable.xf);
						camShownCheck(5,false);
						break;
					case 1:
						holder3.xf_ibtn.setBackgroundResource(R.drawable.xf1);
						break;
					case 2:
						holder3.xf_ibtn.setBackgroundResource(R.drawable.xf2);
						camShownCheck(5,true);
						break;
					case 3:
						holder3.xf_ibtn.setBackgroundResource(R.drawable.xf3);
						break;
					}
					break;
				}
			}
		}else{
			CheckCarData.scoreArray[numberList.get(position)] = numberList.get(position)+"-"+"001020304050";		//初始化str全是未选中的状态
		}
	}
	/**
	 * 6种缺陷打钩项
	 */
	private void setViewHolder3Listener(int position){

		isShowCameraList = new  ArrayList<Boolean>();
		for(int i=0;i<6;i++){
			isShowCameraList.add(i, false);	//全部设置为不显示
		}
		map.put(position, isShowCameraList);

		holder3.camera_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mPicker.doTakePhoto(numberList.get((Integer)v.getTag()),
						(Integer)v.getTag()+1000);
			}
		});

		holder3.hh_ibtn.setTag(position);

		if(!TextUtils.isEmpty(CheckCarData.scoreArray[numberList.get(position)])){
			String boc = CheckCarData.scoreArray[numberList.get(position)].split("-")[1];
			holder3.hh_ibtn.setId(Integer.valueOf(boc.substring(1,2)) < 3?Integer.valueOf(boc.substring(1,2)) : -1);
		}else{
			holder3.hh_ibtn.setId(0);
		}

		holder3.hh_ibtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				isShowCameraList = map.get(v.getTag());

				String str = CheckCarData.scoreArray[numberList.get((Integer)v.getTag())].split("-")[1];

				flag = v.getId() + 1;

				v.setId(flag);
				switch(flag){
				case 0:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 1,2, "00");
					} catch (Throwable e) {
						e.printStackTrace();
					}

					v.setBackgroundResource(R.drawable.hh);
					camShownCheck(0,false);	//set false

					for(int i=0;i<isShowCameraList.size();i++){
						if(isShowCameraList.get(i)){
							isShowCamera = true; 	//	如果有一个是true就要显示
							return;
						}
					}
					isShowCamera = false; 	//	如果全是false就不显示
					if(!isShowCamera){
						for(int i=0;i<sd_camera_btn_List.size();i++){
							if(sd_camera_btn_List.get(i).getTag() == v.getTag()){
								sd_camera_btn_List.get(i).setVisibility(View.INVISIBLE);
								if(verify_cam_btn_List.contains(sd_camera_btn_List.get(i))){
									verify_cam_btn_List.remove(sd_camera_btn_List.get(i));
								}
							}
						}
					}
					break;
				case 1:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 1,2, "01");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.hh1);
					break;
				case 2:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 1,2, "02");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					for(int i=0;i<sd_camera_btn_List.size();i++){
						if(sd_camera_btn_List.get(i).getTag() == v.getTag()){
							sd_camera_btn_List.get(i).setVisibility(View.VISIBLE);

							for(int j=0;j<verify_cam_btn_List.size();j++){
								if(verify_cam_btn_List.get(j).getTag().equals(v.getTag())){
									verify_cam_btn_List.remove(j);
								}
							}
							verify_cam_btn_List.add(sd_camera_btn_List.get(i));

							camShownCheck(0,true);	//set true
						}
					}
					v.setBackgroundResource(R.drawable.hh2);
					break;
				case 3:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 1,2, "03");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.hh3);
					v.setId(-1);
					break;
				}
			}
		});


		holder3.bx_ibtn.setTag(position);

		if(!TextUtils.isEmpty(CheckCarData.scoreArray[numberList.get(position)])){
			String boc = CheckCarData.scoreArray[numberList.get(position)].split("-")[1];
			holder3.bx_ibtn.setId(Integer.valueOf(boc.substring(3,4)) < 3?Integer.valueOf(boc.substring(3,4)) : -1);
		}else{
			holder3.bx_ibtn.setId(0);
		}

		holder3.bx_ibtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				isShowCameraList = map.get(v.getTag());
				String str = CheckCarData.scoreArray[numberList.get((Integer)v.getTag())].split("-")[1];

				flag = v.getId() + 1;
				v.setId(flag);
				switch(flag){
				case 0:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 3,4 , "10");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.bx);
					camShownCheck(1,false);		//set false
					for(int i=0;i<isShowCameraList.size();i++){
						if(isShowCameraList.get(i)){
							isShowCamera = true; 	//	如果有一个是true就要显示
							return;
						}
					}
					isShowCamera = false; 	//	如果全是false就不显示
					if(!isShowCamera){
						for(int i=0;i<sd_camera_btn_List.size();i++){
							if(sd_camera_btn_List.get(i).getTag() == v.getTag()){
								sd_camera_btn_List.get(i).setVisibility(View.INVISIBLE);
								if(verify_cam_btn_List.contains(sd_camera_btn_List.get(i))){
									verify_cam_btn_List.remove(sd_camera_btn_List.get(i));
								}
							}
						}
					}
					break;
				case 1:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 3,4, "11");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.bx1);
					break;
				case 2:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 3,4, "12");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					for(int i=0;i<sd_camera_btn_List.size();i++){
						if(sd_camera_btn_List.get(i).getTag() == v.getTag()){
							sd_camera_btn_List.get(i).setVisibility(View.VISIBLE);
							for(int j=0;j<verify_cam_btn_List.size();j++){
								if(verify_cam_btn_List.get(j).getTag().equals(v.getTag())){
									verify_cam_btn_List.remove(j);
								}
							}
							verify_cam_btn_List.add(sd_camera_btn_List.get(i));
							camShownCheck(1,true);	//set true
						}
					}
					v.setBackgroundResource(R.drawable.bx2);
					break;
				case 3:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 3,4, "13");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.bx3);
					v.setId(-1);
					break;
				}
			}
		});

		holder3.xs_ibtn.setTag(position);

		if(!TextUtils.isEmpty(CheckCarData.scoreArray[numberList.get(position)])){
			String boc = CheckCarData.scoreArray[numberList.get(position)].split("-")[1];
			holder3.xs_ibtn.setId(Integer.valueOf(boc.substring(5,6)) < 3?Integer.valueOf(boc.substring(5,6)) : -1);
		}else{
			holder3.xs_ibtn.setId(0);
		}


		holder3.xs_ibtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				isShowCameraList = map.get(v.getTag());
				String str = CheckCarData.scoreArray[numberList.get((Integer)v.getTag())].split("-")[1];

				flag = v.getId() + 1;
				v.setId(flag);
				switch(flag){
				case 0:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 5,6, "20");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.xs);
					camShownCheck(2,false);		//set false
					for(int i=0;i<isShowCameraList.size();i++){
						if(isShowCameraList.get(i)){
							isShowCamera = true; 	//	如果有一个是true就要显示
							return;
						}
					}
					isShowCamera = false; 	//	如果全是false就不显示
					if(!isShowCamera){
						for(int i=0;i<sd_camera_btn_List.size();i++){
							if(sd_camera_btn_List.get(i).getTag() == v.getTag()){
								sd_camera_btn_List.get(i).setVisibility(View.INVISIBLE);
								if(verify_cam_btn_List.contains(sd_camera_btn_List.get(i))){
									verify_cam_btn_List.remove(sd_camera_btn_List.get(i));
								}
							}
						}
					}
					break;
				case 1:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 5,6, "21");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.xs1);
					break;
				case 2:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 5,6, "22");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					for(int i=0;i<sd_camera_btn_List.size();i++){
						if(sd_camera_btn_List.get(i).getTag() == v.getTag()){
							sd_camera_btn_List.get(i).setVisibility(View.VISIBLE);
							for(int j=0;j<verify_cam_btn_List.size();j++){
								if(verify_cam_btn_List.get(j).getTag().equals(v.getTag())){
									verify_cam_btn_List.remove(j);
								}
							}
							verify_cam_btn_List.add(sd_camera_btn_List.get(i));
							camShownCheck(2,true);		//set true
						}
					}
					v.setBackgroundResource(R.drawable.xs2);
					break;
				case 3:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 5,6, "23");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.xs3);
					v.setId(-1);
					break;
				}
			}
		});

		holder3.lw_ibtn.setTag(position);

		if(!TextUtils.isEmpty(CheckCarData.scoreArray[numberList.get(position)])){
			String boc = CheckCarData.scoreArray[numberList.get(position)].split("-")[1];
			holder3.lw_ibtn.setId(Integer.valueOf(boc.substring(7,8))  < 3?Integer.valueOf(boc.substring(7,8)) : -1);
		}else{
			holder3.lw_ibtn.setId(0);
		}

		holder3.lw_ibtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				isShowCameraList = map.get(v.getTag());
				String str = CheckCarData.scoreArray[numberList.get((Integer)v.getTag())].split("-")[1];

				flag = v.getId() + 1;
				v.setId(flag);
				switch(flag){
				case 0:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 7,8, "30");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.lw);
					camShownCheck(3,false);		//set false
					for(int i=0;i<isShowCameraList.size();i++){
						if(isShowCameraList.get(i)){
							isShowCamera = true; 	//	如果有一个是true就要显示
							return;
						}
					}
					isShowCamera = false; 	//	如果全是false就不显示
					if(!isShowCamera){
						for(int i=0;i<sd_camera_btn_List.size();i++){
							if(sd_camera_btn_List.get(i).getTag() == v.getTag()){
								sd_camera_btn_List.get(i).setVisibility(View.INVISIBLE);
								if(verify_cam_btn_List.contains(sd_camera_btn_List.get(i))){
									verify_cam_btn_List.remove(sd_camera_btn_List.get(i));
								}
							}
						}
					}
					break;
				case 1:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 7,8, "31");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.lw1);
					break;
				case 2:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 7,8, "32");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					for(int i=0;i<sd_camera_btn_List.size();i++){
						if(sd_camera_btn_List.get(i).getTag() == v.getTag()){
							sd_camera_btn_List.get(i).setVisibility(View.VISIBLE);
							for(int j=0;j<verify_cam_btn_List.size();j++){
								if(verify_cam_btn_List.get(j).getTag().equals(v.getTag())){
									verify_cam_btn_List.remove(j);
								}
							}
							verify_cam_btn_List.add(sd_camera_btn_List.get(i));
							camShownCheck(3,true);	//set true
						}
					}
					v.setBackgroundResource(R.drawable.lw2);
					break;
				case 3:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 7,8, "33");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.lw3);
					v.setId(-1);
					break;
				}
			}
		});

		holder3.ax_ibtn.setTag(position);

		if(!TextUtils.isEmpty(CheckCarData.scoreArray[numberList.get(position)])){
			String boc = CheckCarData.scoreArray[numberList.get(position)].split("-")[1];
			holder3.ax_ibtn.setId(Integer.valueOf(boc.substring(9,10)) < 3?Integer.valueOf(boc.substring(9,10)) : -1);
		}else{
			holder3.ax_ibtn.setId(0);
		}

		holder3.ax_ibtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				isShowCameraList = map.get(v.getTag());
				String str = CheckCarData.scoreArray[numberList.get((Integer)v.getTag())].split("-")[1];

				flag = v.getId() + 1;
				v.setId(flag);
				switch(flag){
				case 0:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 9,10, "40");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.ax);
					camShownCheck(4,false);			//set false
					for(int i=0;i<isShowCameraList.size();i++){
						if(isShowCameraList.get(i)){
							isShowCamera = true; 	//	如果有一个是true就要显示
							return;
						}
					}
					isShowCamera = false; 	//	如果全是false就不显示
					if(!isShowCamera){
						for(int i=0;i<sd_camera_btn_List.size();i++){
							if(sd_camera_btn_List.get(i).getTag() == v.getTag()){
								sd_camera_btn_List.get(i).setVisibility(View.INVISIBLE);
								if(verify_cam_btn_List.contains(sd_camera_btn_List.get(i))){
									verify_cam_btn_List.remove(sd_camera_btn_List.get(i));
								}
							}
						}
					}
					break;
				case 1:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 9,10, "41");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.ax1);
					break;
				case 2:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 9,10, "42");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					for(int i=0;i<sd_camera_btn_List.size();i++){
						if(sd_camera_btn_List.get(i).getTag() == v.getTag()){
							sd_camera_btn_List.get(i).setVisibility(View.VISIBLE);
							for(int j=0;j<verify_cam_btn_List.size();j++){
								if(verify_cam_btn_List.get(j).getTag().equals(v.getTag())){
									verify_cam_btn_List.remove(j);
								}
							}
							verify_cam_btn_List.add(sd_camera_btn_List.get(i));
							camShownCheck(4,true);	//set true
						}
					}
					v.setBackgroundResource(R.drawable.ax2);
					break;
				case 3:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 9,10, "43");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.ax3);
					v.setId(-1);
					break;
				}
			}
		});

		holder3.xf_ibtn.setTag(position);

		if(!TextUtils.isEmpty(CheckCarData.scoreArray[numberList.get(position)])){
			String boc = CheckCarData.scoreArray[numberList.get(position)].split("-")[1];
			holder3.xf_ibtn.setId(Integer.valueOf(boc.substring(11,12)) < 3?Integer.valueOf(boc.substring(11,12)) : -1);
		}else{
			holder3.xf_ibtn.setId(0);
		}

		holder3.xf_ibtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				isShowCameraList = map.get(v.getTag());
				String str =CheckCarData.scoreArray[numberList.get((Integer)v.getTag())].split("-")[1];

				flag = v.getId() + 1;
				v.setId(flag);
				switch(flag){
				case 0:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 9,10, "50");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.xf);
					camShownCheck(5,false);	//set false
					for(int i=0;i<isShowCameraList.size();i++){
						if(isShowCameraList.get(i)){
							isShowCamera = true; 	//	如果有一个是true就要显示
							return;
						}
					}
					isShowCamera = false; 	//	如果全是false就不显示
					if(!isShowCamera){
						for(int i=0;i<sd_camera_btn_List.size();i++){
							if(sd_camera_btn_List.get(i).getTag() == v.getTag()){
								sd_camera_btn_List.get(i).setVisibility(View.INVISIBLE);
								if(verify_cam_btn_List.contains(sd_camera_btn_List.get(i))){
									verify_cam_btn_List.remove(sd_camera_btn_List.get(i));
								}
							}
						}
					}
					break;
				case 1:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 11,12, "51");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.xf1);
					break;
				case 2:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 11,12, "52");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					for(int i=0;i<sd_camera_btn_List.size();i++){
						if(sd_camera_btn_List.get(i).getTag() == v.getTag()){
							sd_camera_btn_List.get(i).setVisibility(View.VISIBLE);
							for(int j=0;j<verify_cam_btn_List.size();j++){
								if(verify_cam_btn_List.get(j).getTag().equals(v.getTag())){
									verify_cam_btn_List.remove(j);
								}
							}
							verify_cam_btn_List.add(sd_camera_btn_List.get(i));
							camShownCheck(5,true);	//set true
						}
					}
					v.setBackgroundResource(R.drawable.xf2);
					break;
				case 3:
					try {
						CheckCarData.scoreArray[numberList.get((Integer)v.getTag())] =
								numberList.get((Integer)v.getTag())+"-"+replacePro(str, 11,12, "53");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					v.setBackgroundResource(R.drawable.xf3);
					v.setId(-1);
					break;
				}
			}
		});
	}

	/**
	 * 描述录音项
	 */
	/**
	 *
	 * 优化点：没有很好的跟recorderHelper分开，playbtn和recordbtn 是否 enable的逻辑判断有点混乱， 初始化RecorderHelper的传值不是那么漂亮。录音地址做缓存
	 *
	 */
	private void setViewHolder4Listener(int position){
		//meida_record
		holder4.record_btn.setTag(position);
		/*holder4.record_btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//全部设置不可用
	        	for(int i = 0; i< record_btn_list.size();i++){
	        		record_btn_list.get(i).setEnabled(false);     	
	        		play_btn_list.get(i).setEnabled(false);
	        		
	        	}
	        	//当前btn设为可用
	        	v.setEnabled(true);
				RecorderHelper rh = RecorderHelper.getRecorderHelper(mActivity, 
						String.valueOf(numberList.get((Integer)v.getTag())),record_btn_list,play_btn_list);
				rh.onRecord(isRecording);
		        if (isRecording) {	            	
		        	v.setBackgroundResource(R.drawable.ic_btn_speak_now);		        	
		        } else {
		        	v.setBackgroundResource(R.drawable.ic_btn_speak);		        	

		        	//录音完毕全部设为可用
		        	for(int i = 0; i< record_btn_list.size();i++){
		        		record_btn_list.get(i).setEnabled(true);
		        		play_btn_list.get(i).setEnabled(true);
		        	}
		        }
		        isRecording = !isRecording;	
			}
		});	*/

		//media_play
		holder4.play_btn.setTag(position);
		holder4.play_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//全部设置不可用
				for(int i = 0; i< record_btn_list.size();i++){
	        		record_btn_list.get(i).setEnabled(false);
	        		play_btn_list.get(i).setEnabled(false);
	        	}
				//当前btn设为可用
				v.setEnabled(true);
//				RecorderHelper rh = RecorderHelper.getRecorderHelper(mActivity,
//						String.valueOf(numberList.get((Integer)v.getTag())),record_btn_list,play_btn_list);
////				Log.d(TAG, "---------ConstantDictionary.IS_PLAYING = " + ConstantDictionary.IS_PLAYING);
//				if(ConstantDictionary.IS_PLAYING){
//					Log.d(TAG, "---------playing");
//					rh.startPlaying(v);
//				}else{
//					Log.d(TAG, "---------not playing");
//					rh.stopPlaying();
//				}
			}
		});
	}

	/**
	 * 三选一打钩项数据restore
	 */
	private void restoreHolder5(int position){
		//载入之前的记录
		if(!TextUtils.isEmpty(CheckCarData.scoreArray[numberList.get(position)])){
			String boc = CheckCarData.scoreArray[numberList.get(position)].split("-")[1];
			if(boc.equals("0")){
				holder5.fadongji_rg.check(R.id.ok_rd);
			}else if(boc.equals("1")){
				holder5.fadongji_rg.check(R.id.qingwei_rd);
			}else if(boc.equals("2")){
				holder5.fadongji_rg.check(R.id.yanzhong_rd);
			}
		}else{
			CheckCarData.scoreArray[numberList.get(position)] = numberList.get(position)+"-"+"0";
		}
	}
	/**
	 * 三选一打钩项
	 */
	private void setViewHolder5Listener(int position){

		holder5.fadongji_rg.setTag(position);
		holder5.fadongji_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.ok_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"0";
					break;
				case R.id.qingwei_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"1";
					break;
				case R.id.yanzhong_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"2";
					break;
				}
			}
		});
	}

	/**
	 * 四选一打钩项数据restore
	 */
	private void restoreHolder7(int position){
		//载入之前的记录
		if(!TextUtils.isEmpty(CheckCarData.scoreArray[numberList.get(position)])){
			String boc = CheckCarData.scoreArray[numberList.get(position)].split("-")[1];
			if(boc.equals("0")){
				holder7.fadongji_rg.check(R.id.ok_rd);
			}else if(boc.equals("1")){
				holder7.fadongji_rg.check(R.id.qingwei_rd);
			}else if(boc.equals("2")){
				holder7.fadongji_rg.check(R.id.yanzhong_rd);
			}else if(boc.equals("3")){
				holder7.fadongji_rg.check(R.id.uncheck_rd);
			}
		}else{
			CheckCarData.scoreArray[numberList.get(position)] = numberList.get(position)+"-"+"3";
		}
	}
	/**
	 * 四选一打钩项
	 */
	private void setViewHolder7Listener(int position){

		holder7.fadongji_rg.setTag(position);
		holder7.fadongji_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.ok_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"0";
					break;
				case R.id.qingwei_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"1";
					break;
				case R.id.yanzhong_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"2";
					break;
				case R.id.uncheck_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"3";
					break;
				}
			}
		});
	}

	/**
	 * 四选一打钩项（照）数据restore
	 */
	private void restoreHolder9(int position){

		//载入之前的记录
		if(!TextUtils.isEmpty(CheckCarData.scoreArray[numberList.get(position)])){
			String boc = CheckCarData.scoreArray[numberList.get(position)].split("-")[1];
			if(boc.equals("0")){
				holder9.fadongji_rg.check(R.id.ok_rd);
			}else if(boc.equals("1")){
				holder9.fadongji_rg.check(R.id.qingwei_rd);
			}else if(boc.equals("2")){
				holder9.fadongji_rg.check(R.id.yanzhong_rd);
			}else if(boc.equals("3")){
				holder9.fadongji_rg.check(R.id.uncheck_rd);
			}
		}else{
			CheckCarData.scoreArray[numberList.get(position)] = numberList.get(position)+"-"+"3";
		}
	}
	/**
	 * 四选一打钩项（照）
	 */
	private void setViewHolder9Listener(int position){

		holder9.camera_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mPicker.doTakePhoto(numberList.get((Integer)v.getTag()),(Integer)v.getTag()+1000000);
			}
		});

		holder9.fadongji_rg.setTag(position);
		holder9.fadongji_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.ok_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"0";
					for(int i=0;i<fo_camera_btn_List.size();i++){
						if(fo_camera_btn_List.get(i).getTag() == group.getTag()){
							fo_camera_btn_List.get(i).setVisibility(View.INVISIBLE);
							if(verify_cam_btn_List.contains(fo_camera_btn_List.get(i))){
								verify_cam_btn_List.remove(fo_camera_btn_List.get(i));
							}
						}
					}
					break;
				case R.id.qingwei_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"1";
					for(int i=0;i<fo_camera_btn_List.size();i++){
						if(fo_camera_btn_List.get(i).getTag() == group.getTag()){
							fo_camera_btn_List.get(i).setVisibility(View.VISIBLE);
							for(int j=0;j<verify_cam_btn_List.size();j++){
								if(verify_cam_btn_List.get(j).getTag().equals(group.getTag())){
									verify_cam_btn_List.remove(j);
								}
							}
							verify_cam_btn_List.add(fo_camera_btn_List.get(i));
						}
					}
					break;
				case R.id.yanzhong_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"2";
					for(int i=0;i<fo_camera_btn_List.size();i++){
						if(fo_camera_btn_List.get(i).getTag() == group.getTag()){
							fo_camera_btn_List.get(i).setVisibility(View.VISIBLE);
							for(int j=0;j<verify_cam_btn_List.size();j++){
								if(verify_cam_btn_List.get(j).getTag().equals(group.getTag())){
									verify_cam_btn_List.remove(j);
								}
							}
							verify_cam_btn_List.add(fo_camera_btn_List.get(i));
						}
					}
					break;
				case R.id.uncheck_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"3";
					for(int i=0;i<fo_camera_btn_List.size();i++){
						if(fo_camera_btn_List.get(i).getTag() == group.getTag()){
							fo_camera_btn_List.get(i).setVisibility(View.INVISIBLE);
							if(verify_cam_btn_List.contains(fo_camera_btn_List.get(i))){
								verify_cam_btn_List.remove(fo_camera_btn_List.get(i));
							}
						}
					}
					break;
				}
			}
		});
	}

	/**
	 * 三选一打钩项（照）数据restore
	 */
	private void restoreHolder8(int position){
		//载入之前的记录
		if(!TextUtils.isEmpty(CheckCarData.scoreArray[numberList.get(position)])){
			String boc = CheckCarData.scoreArray[numberList.get(position)].split("-")[1];
			if(boc.equals("0")){
				holder8.fadongji_rg.check(R.id.ok_rd);
			}else if(boc.equals("1")){
				holder8.fadongji_rg.check(R.id.qingwei_rd);
			}else if(boc.equals("2")){
				holder8.fadongji_rg.check(R.id.yanzhong_rd);
			}
		}else{
			CheckCarData.scoreArray[numberList.get(position)] = numberList.get(position)+"-"+"0";
		}
	}

	/**
	 * 三选一打钩项（照）
	 */
	private void setViewHolder8Listener(int position){

		holder8.camera_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mPicker.doTakePhoto(numberList.get((Integer)v.getTag()),(Integer)v.getTag()+100000);
			}
		});

		holder8.fadongji_rg.setTag(position);
		holder8.fadongji_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.ok_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"0";
					for(int i=0;i<to_camera_btn_List.size();i++){
						if(to_camera_btn_List.get(i).getTag() == group.getTag()){
							to_camera_btn_List.get(i).setVisibility(View.INVISIBLE);
							if(verify_cam_btn_List.contains(to_camera_btn_List.get(i))){
								verify_cam_btn_List.remove(to_camera_btn_List.get(i));
							}
						}
					}
					break;
				case R.id.qingwei_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"1";
					for(int i=0;i<to_camera_btn_List.size();i++){
						if(to_camera_btn_List.get(i).getTag() == group.getTag()){
							to_camera_btn_List.get(i).setVisibility(View.INVISIBLE);
							if(verify_cam_btn_List.contains(to_camera_btn_List.get(i))){
								verify_cam_btn_List.remove(to_camera_btn_List.get(i));
							}
						}
					}
					break;
				case R.id.yanzhong_rd:
					CheckCarData.scoreArray[numberList.get((Integer)group.getTag())] =
							numberList.get((Integer)group.getTag())+"-"+"2";
					for(int i=0;i<to_camera_btn_List.size();i++){
						if(to_camera_btn_List.get(i).getTag() == group.getTag()){
							to_camera_btn_List.get(i).setVisibility(View.VISIBLE);
							for(int j=0;j<verify_cam_btn_List.size();j++){
								if(verify_cam_btn_List.get(j).getTag().equals(group.getTag())){
									verify_cam_btn_List.remove(j);
								}
							}
							verify_cam_btn_List.add(to_camera_btn_List.get(i));
							break;
						}
					}
					break;
				}
			}
		});

	}

	/**
	 * 拍照view数据restore
	 */
	private void restoreHolder6(int position){

		if(!TextUtils.isEmpty(CheckCarData.scorePicArray[numberList.get(position)])){
			holder6.pic_btn.setBackgroundResource(R.drawable.ic_menu_camera_save);
			holder6.pic_btn.setId(R.drawable.ic_menu_camera_save);
			if(verify_cam_btn_List.contains(holder6.pic_btn)){
				verify_cam_btn_List.remove(holder6.pic_btn);
			}
		}
	}
	/**
	 * 拍照view监听
	 */
	private void setViewHolder6Listener(int position){
		holder6.pic_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				if(!pList.get((Integer)v.getTag()).equals("*")){
//					mPicker.doTakePhoto(pList.get((Integer)v.getTag()),(Integer)v.getTag()+10000);
//				}
				mPicker.doTakePhoto(numberList.get((Integer)v.getTag()),(Integer)v.getTag()+10000);
			}
		});
	}

	/**
	 * 处理字符串
	 */
	public static String replace(String str,int n,String newChar) throws Throwable{
		String s1="";
		String s2="";
		try {
			s1=str.substring(0,n-1);
			s2=str.substring(n,str.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s1+newChar+s2;
	}

	public static String replacePro(String str,int n,int m,String newChar) throws Throwable{
		String s1="";
		String s2="";
		try {
			s1=str.substring(0,n-1);
			s2=str.substring(m,str.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s1+newChar+s2;
	}
}
