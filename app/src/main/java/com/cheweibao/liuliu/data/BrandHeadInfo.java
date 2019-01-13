package com.cheweibao.liuliu.data;

/**
 * Created by user on 2018/4/9.
 */

public class BrandHeadInfo {
    int img;
    String text;
    public BrandHeadInfo(int img,String text) {
        this.img = img;
        this.text=text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
