
package com.rengh.app.star.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AdBean implements Parcelable {
    public static class AdType {
        public static final int TYPE_PIC = 1;
        public static final int TYPE_VIDEO = 2;
        public static final int TYPE_ANIM = 3;
    }

    private int type;
    private int id;
    private long length;
    private String url;
    private String path;
    private String param;

    public int getType() {
        return type;
    }

    public AdBean setType(int type) {
        this.type = type;
        return this;
    }

    public int getId() {
        return id;
    }

    public AdBean setId(int id) {
        this.id = id;
        return this;
    }

    public long getLength() {
        return length;
    }

    public AdBean setLength(long length) {
        this.length = length;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AdBean setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getPath() {
        return path;
    }

    public AdBean setPath(String path) {
        this.path = path;
        return this;
    }

    public String getParam() {
        return param;
    }

    public AdBean setParam(String param) {
        this.param = param;
        return this;
    }

    @Override
    public String toString() {
        return "AdBean{" +
                "type=" + type +
                ", id=" + id +
                ", length=" + length +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", param='" + param + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeInt(id);
        dest.writeLong(length);
        dest.writeString(url);
        dest.writeString(path);
        dest.writeString(param);
    }

    public static final Creator<AdBean> CREATOR = new Creator<AdBean>() {

        @Override
        public AdBean[] newArray(int size) {
            return new AdBean[size];
        }

        @Override
        public AdBean createFromParcel(Parcel source) {
            AdBean adBean = new AdBean();
            adBean.type = source.readInt();
            adBean.id = source.readInt();
            adBean.length = source.readLong();
            adBean.url = source.readString();
            adBean.path = source.readString();
            adBean.param = source.readString();
            return adBean;
        }
    };

}
