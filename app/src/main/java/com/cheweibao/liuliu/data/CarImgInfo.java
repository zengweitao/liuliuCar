package com.cheweibao.liuliu.data;

import android.widget.ImageView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class CarImgInfo {

    public int id;
    public String imgUrl;
    public String title;

    public ImageView ivImg;

    public CarImgInfo() {
        recycle();
    }

    public CarImgInfo(JSONObject obj) {
        if (obj == null) return;

        try {
            imgUrl = obj.getString("imgUrl");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImageView getIvImg() {
        return ivImg;
    }

    public void setIvImg(ImageView ivImg) {
        this.ivImg = ivImg;
    }

    public void recycle() {
        id = 0;
        imgUrl = "";
        title = "";
    }
}