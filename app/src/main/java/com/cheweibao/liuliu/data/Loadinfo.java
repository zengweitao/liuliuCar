package com.cheweibao.liuliu.data;

public class Loadinfo {

    public String version;//版本名称
    public String content;//更新内容
    public String force_update;//强制更新状态(0:不强制更新,1：强制更新)
    public String url;//文件地址
    public String size;//文件大小

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getForce_update() {
        return force_update;
    }

    public void setForce_update(String force_update) {
        this.force_update = force_update;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}