package com.cheweibao.liuliu.data;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Citys implements Serializable {

    public String id;
    public String name;
    public List<Districts> districts;

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

    public List<Districts> getDistricts() {
        return districts;
    }

    public void setDistricts(List<Districts> districts) {
        this.districts = districts;
    }

    public Citys() {
    }

    public Citys(JSONObject jsonObject) {
        id = jsonObject.getString("id");
        name = jsonObject.getString("name");
        districts = new ArrayList<>();
        JSONArray jsonCitys = jsonObject.getJSONArray("districts");
        for (int i = 0; i < jsonCitys.size(); i++) {
            Districts city = new Districts(jsonCitys.getJSONObject(i));
            districts.add(city);
        }
    }

    @Override
    public String toString() {
        return "Citys{" +
                "code='" + id + '\'' +
                ", name='" + name + '\'' +
                ", districts=" + districts +
                '}';
    }
}

