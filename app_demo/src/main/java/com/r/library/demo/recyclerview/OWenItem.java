package com.r.library.demo.recyclerview;

public class OWenItem {
    private String type;
    private String picUrl;
    private String title;
    private String params;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "OWenItem{" +
                "type='" + type + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", title='" + title + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}
