package com.cheweibao.liuliu.data;

import java.io.Serializable;

/**
 * Created by hasee on 2018/12/19.
 */

public class UsedImageInfo implements Serializable {
    public String imageName;
    public String imageUrl;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
