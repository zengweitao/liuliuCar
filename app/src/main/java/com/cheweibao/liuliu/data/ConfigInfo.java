package com.cheweibao.liuliu.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by unknow on 2018/6/10.
 */

public class ConfigInfo implements Serializable {
    public String id;
    public String modelId;//数据源ID 等于 sourcesId主键
    public String name;//配置名称
    public String sort;//排序
    public String status;//状态：0-停用，1-启用
    public String createTime;//'新增时间
    public String updateTime;//修改时间
    public String deleted;//删除0
    public List<VehicleConfigurationInfo> param;//配置参数数据集合

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public List<VehicleConfigurationInfo> getParam() {
        return param;
    }

    public void setParam(List<VehicleConfigurationInfo> param) {
        this.param = param;
    }
}
