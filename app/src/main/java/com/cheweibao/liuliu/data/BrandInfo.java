package com.cheweibao.liuliu.data;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class BrandInfo {


	public String id;
	public String name;
	public String logo;
	public String initial;
	//车系
	public String fullname;
	//车型
	public String yeartype;

	public BrandInfo() {
		recycle();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getYeartype() {
		return yeartype;
	}

	public void setYeartype(String yeartype) {
		this.yeartype = yeartype;
	}

	public BrandInfo(JSONObject obj) {
		if(obj == null) return;
		
		try {
			id = obj.getString("id");
			name = obj.getString("name");
			logo = obj.getString("logo");
			initial = obj.getString("initial");
			fullname = obj.getString("fullname");
			yeartype = obj.getString("yeartype");

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void recycle() {
		id = "";
		name = "";
		logo = "";
		initial = "";
		fullname = "";
		yeartype = "";
	}
}