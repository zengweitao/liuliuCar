package com.cheweibao.liuliu.data;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by unknow on 2018/6/23.
 */

public class Districts implements Serializable{
    public String id;
    public String name;

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

    public Districts() {
    }

    public Districts(JSONObject jsonObject) {
        id = jsonObject.getString("id");
        name = jsonObject.getString("name");
    }

    @Override
    public String toString() {
        return "Districts{" +
                "code='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
