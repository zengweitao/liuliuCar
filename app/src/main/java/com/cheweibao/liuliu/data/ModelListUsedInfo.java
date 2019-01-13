package com.cheweibao.liuliu.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by unknow on 2018/6/10.
 */

public class ModelListUsedInfo implements Serializable {
    public boolean success;
    public List<CarModelInfo> usedCarList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<CarModelInfo> getUsedCarList() {
        return usedCarList;
    }

    public void setUsedCarList(List<CarModelInfo> usedCarList) {
        this.usedCarList = usedCarList;
    }
}
