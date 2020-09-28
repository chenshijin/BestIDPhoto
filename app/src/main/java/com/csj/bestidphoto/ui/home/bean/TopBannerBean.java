package com.csj.bestidphoto.ui.home.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class TopBannerBean implements Parcelable {

    private int imgRes;

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public TopBannerBean() {
    }

    protected TopBannerBean(Parcel in) {
    }

    public static final Creator<TopBannerBean> CREATOR = new Creator<TopBannerBean>() {
        @Override
        public TopBannerBean createFromParcel(Parcel source) {
            return new TopBannerBean(source);
        }

        @Override
        public TopBannerBean[] newArray(int size) {
            return new TopBannerBean[size];
        }
    };
}
