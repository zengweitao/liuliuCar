package com.cheweibao.liuliu.data;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CheckCarData {


	public static String checkInitStr = "null&null&null&null&null&null&null&null&null&null&null&null&null&null&null&null&null&null&null&null&null&" +
			"21-00000&22-001020304050&23-001020304050&24-001020304050&25-001020304050&26-00000&27-00000&28-001020304050&29-001020304050&30-001020304050&31-00000&" +
			"null&33-00000&34-001020304050&35-001020304050&36-001020304050&37-001020304050&38-00000&null&null&41-00000&42-001020304050&43-001020304050&" +
			"44-001020304050&45-00000&46-001020304050&47-001020304050&48-00000&49-001020304050&50-001020304050&null&52-001020304050&53-001020304050&" +
			"54-00000&55-001020304050&56-001020304050&null&null&null&null&61-001020304050&62-001020304050&63-00000&64-00000&65-00000&66-0&67-0&68-0&" +
			"69-0&70-0&null&72-3&73-0&74-00000&75-0&76-0&77-001020304050&78-001020304050&79-0&null&null&82-0&83-0&84-0&85-0&86-0&87-0&88-0&89-0&90-0&" +
			"91-0&92-0&93-0&94-0&95-0&96-0&97-0&98-0&99-0&100-0&101-0&102-0&103-0&104-0&105-001020304050&106-001020304050&107-0&108-0&null&null&111-3&" +
			"112-3&113-3&114-3&115-3&116-3&117-3&118-3&119-3&120-3&121-3&122-3&null&";
	//基本信息
	public static String jcdId;
	public static String carId;
	public static String detailId;
	public static String orderNo;
	public static String carBrand;	//品牌
	public static String carYear;	//款式
	public static String proviceName;	//所在省
	public static String cityName;	//所在市
	public static String detailAddr;	//详细地址
	public static String carTypeName;	//车辆型号
	public static String carNumber;	//车牌号码
	public static String carDist;	//表显历程
	public static String regDate;	//登记日期
	public static String comIns;	//商业险(0-无、1-有)
	public static String traffIns;	//交强险(0-无、1-有)
	public static String carColor;	//颜色
	public static String carPl;	//排量
	public static String ctrlPress;	//涡轮增压(0-无、1-有)
	public static String isNew;	//是否已一手车(0-不， 1-是)
	public static String rcvDate;	//最近过户时间
	public static String phone;	//手机号码

	public static String checkAll;	//是否检测

	public static String basicInfoSave;	//是否基本信息已保存（0-未保存, 1-已保存）
	public static String basicConfSave;	//是否基本配置已保存（0-未保存, 1-已保存）
	public static String checkSave;	//是否检测结果已保存（0-未保存, 1-已保存）
	public static String photoSave;	//是否拍照已保存（0-未保存, 1-已保存）
	public static String afterSrvSave;	//是否售后已保存（0-未保存, 1-已保存）

	//基本配置
	public static String basicConf;
	//检测结果
	public static String checkRes;
	public static String checkResPic;
	public static String checkStatus;
	//拍照
	public static String photo;

	//售后
	public static String jcPrice;	//卖家报价
	public static String period;	//售后质保期限 (0-无/1-1周或500公里/2-1或1500公里）
	public static String srvMode;	//售后质保方式（0-无/1-包换/2-有条件包退/2-无条件包退）
	public static String remark;	//追加描述信息



	public static String[] scoreArray;
	public static String[] scoreArrayReal;
	public static String[] scorePicArray;
	public static String[] scorePicArrayReal;
	public static String[] chkStatusArray;

	public static List<String> photoList;

	public CheckCarData() {
		recycle();
	}

	public static void setJcOrderDetailInfo(JSONObject obj) {
		recycle();
		if(obj == null) return;
		
		try {
			if(obj.containsKey("jcdId"))
				jcdId = obj.getString("jcdId");
			if(obj.containsKey("carId"))
				carId = obj.getString("carId");
			if(obj.containsKey("id"))
				detailId = obj.getString("id");
			if(obj.containsKey("jcdNo"))
				orderNo = obj.getString("jcdNo");


			if(obj.containsKey("carBrand"))
				carBrand = obj.getString("carBrand");
			if(obj.containsKey("carYear"))
				carYear = obj.getString("carYear");
			if(obj.containsKey("proviceName"))
				proviceName = obj.getString("proviceName");
			if(obj.containsKey("cityName"))
				cityName = obj.getString("cityName");
			if(obj.containsKey("detailAddr"))
				detailAddr = obj.getString("detailAddr");
			if(obj.containsKey("carTypeName"))
				carTypeName = obj.getString("carTypeName");
			if(obj.containsKey("carNumber"))
				carNumber = obj.getString("carNumber");
			if(obj.containsKey("carDist"))
				carDist = obj.getString("carDist");
			if(obj.containsKey("regDate"))
				regDate = obj.getString("regDate");
			if(obj.containsKey("comIns"))
				comIns = obj.getString("comIns");
			if(obj.containsKey("traffIns"))
				traffIns = obj.getString("traffIns");
			if(obj.containsKey("carColor"))
				carColor = obj.getString("carColor");
			if(obj.containsKey("carPl"))
				carPl = obj.getString("carPl");
			if(obj.containsKey("ctrlPress"))
				ctrlPress = obj.getString("ctrlPress");
			if(obj.containsKey("isNew"))
				isNew = obj.getString("isNew");
			if(obj.containsKey("rcvDate"))
				rcvDate = obj.getString("rcvDate");
			if(obj.containsKey("phone"))
				phone = obj.getString("phone");

			if(obj.containsKey("basicInfoSave"))
				basicInfoSave = obj.getString("basicInfoSave");
			if(obj.containsKey("basicConfSave"))
				basicConfSave = obj.getString("basicConfSave");
			if(obj.containsKey("checkSave"))
				checkSave = obj.getString("checkSave");
			if(obj.containsKey("photoSave"))
				photoSave = obj.getString("photoSave");
			if(obj.containsKey("afterSrvSave"))
				afterSrvSave = obj.getString("afterSrvSave");

			if(obj.containsKey("basicConf"))
				basicConf = obj.getString("basicConf");
			if(obj.containsKey("checkResPic"))
				checkResPic = obj.getString("checkResPic");
			if(obj.containsKey("checkRes"))
				checkRes = obj.getString("checkRes");
			if(obj.containsKey("checkStatus"))
				checkStatus = obj.getString("checkStatus");
			if(obj.containsKey("photo"))
				photo = obj.getString("photo");

			if(obj.containsKey("jcPrice"))
				jcPrice = obj.getString("jcPrice");
			if(obj.containsKey("period"))
				period = obj.getString("period");
			if(obj.containsKey("srvMode"))
				srvMode = obj.getString("srvMode");
			if(obj.containsKey("remark"))
				remark = obj.getString("remark");

			if(obj.containsKey("checkAll"))
				checkAll = obj.getString("checkAll");

			if(!TextUtils.isEmpty(checkRes) && checkRes.split("&").length >= 124){
				String[] initArr = checkRes.split("&");
				for(int i =0; i < initArr.length; i++) {
					scoreArray[i] = initArr[i];
					scoreArrayReal[i] = initArr[i];
				}
			}

			if(!TextUtils.isEmpty(checkResPic)){
				JSONArray array = JSONArray.parseArray(checkResPic);
				for (int i = 0; i < array.size(); i++) {
					scorePicArray[i] = array.getJSONObject(i).getString("imgUrl");
					scorePicArrayReal[i] = array.getJSONObject(i).getString("imgUrl");
				}
			}
			if(!TextUtils.isEmpty(checkStatus) && checkStatus.length() >= 12){
				for(int i = 0; i < chkStatusArray.length; i++)
					chkStatusArray[i] = checkStatus.substring(i, i+1);
			}
			if(!TextUtils.isEmpty(photo)){
				JSONArray array = JSONArray.parseArray(photo);
				for (int i = 0; i < array.size(); i++) {
					photoList.set(i, array.getJSONObject(i).getString("imgUrl"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void recycle() {
		scoreArray = new String[124];
		scoreArrayReal = new String[124];
		scorePicArray = new String[124];
		scorePicArrayReal = new String[124];
		chkStatusArray = new String[12];

		String[] initArr = checkInitStr.split("&");
		for(int i = 0; i < initArr.length; i++) {
			scoreArray[i] = initArr[i];
			scoreArrayReal[i] = initArr[i];
			scorePicArray[i] = "";
			scorePicArrayReal[i] = "";
		}
		for(int i = 0; i < chkStatusArray.length; i++)
			chkStatusArray[i] = "0";

		photoList = new ArrayList<>();
		for(int i = 0; i < 16; i++)
			photoList.add("");
		jcdId = "";
		carId = "";
		carBrand = "";
		carYear = "";
		proviceName = "";
		cityName = "";
		detailAddr = "";
		carTypeName = "";
		carNumber = "";
		carDist = "";
		regDate = "";
		comIns = "0";
		traffIns = "0";
		carColor = "";
		carPl = "";
		ctrlPress = "";
		isNew = "0";
		rcvDate = "";
		phone = "";

		basicInfoSave = "";
		basicConfSave = "";
		checkSave = "";
		photoSave = "";
		afterSrvSave = "";

		basicConf = "";
		checkRes = "";
		checkResPic = "";

		photo = "";

		jcPrice = "";
		period = "0";
		srvMode = "0";
		remark = "";


		checkAll = "0";
	}
}