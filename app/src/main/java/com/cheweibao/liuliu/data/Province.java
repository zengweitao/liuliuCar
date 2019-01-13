package com.cheweibao.liuliu.data;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Province implements Serializable {

    public String id;
    public String name;
    public List<Citys> citys;

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

    public List<Citys> getCitys() {
        return citys;
    }

    public void setCitys(List<Citys> citys) {
        this.citys = citys;
    }

    public Province() {
    }

    public Province(JSONObject jsonObject) {
        id = jsonObject.getString("id");
        name = jsonObject.getString("name");
        citys = new ArrayList<>();
        JSONArray jsonCitys = jsonObject.getJSONArray("citys");
        for (int i = 0; i < jsonCitys.size(); i++) {
            Citys city = new Citys(jsonCitys.getJSONObject(i));
            citys.add(city);
        }
    }

    @Override
    public String toString() {
        return "Province{" +
                "code='" + id + '\'' +
                ", name='" + name + '\'' +
                ", citys=" + citys +
                '}';
    }
}
