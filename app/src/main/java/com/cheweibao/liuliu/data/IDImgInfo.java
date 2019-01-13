package com.cheweibao.liuliu.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by unknow on 2018/6/25.
 */

public class IDImgInfo implements Serializable {
    public String code;
    public String desc;
    public List<IDPhotoInfo> result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<IDPhotoInfo> getResult() {
        return result;
    }

    public void setResult(List<IDPhotoInfo> result) {
        this.result = result;
    }
}
