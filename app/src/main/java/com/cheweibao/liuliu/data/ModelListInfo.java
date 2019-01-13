package com.cheweibao.liuliu.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by unknow on 2018/6/10.
 */

public class ModelListInfo implements Serializable {
    public List<BannerInfo> image;//首页banner图片list
    public List<CarModelInfo> recommendCar;//推荐车型
    public List<CarModelInfo> selfSelectionCar;//自选车型

    public List<BannerInfo> getImage() {
        return image;
    }

    public void setImage(List<BannerInfo> image) {
        this.image = image;
    }

    public List<CarModelInfo> getRecommendCar() {
        return recommendCar;
    }

    public void setRecommendCar(List<CarModelInfo> recommendCar) {
        this.recommendCar = recommendCar;
    }

    public List<CarModelInfo> getSelfSelectionCar() {
        return selfSelectionCar;
    }

    public void setSelfSelectionCar(List<CarModelInfo> selfSelectionCar) {
        this.selfSelectionCar = selfSelectionCar;
    }
}
