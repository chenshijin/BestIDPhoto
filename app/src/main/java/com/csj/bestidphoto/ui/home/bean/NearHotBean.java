package com.csj.bestidphoto.ui.home.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class NearHotBean implements MultiItemEntity,Parcelable {

    public final static int HOME_ITEM_TYPE_CONTENT = 1;
    public final static int HOME_ITEM_TYPE_TITLE = 2;

    private int itemType;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public NearHotBean() {
    }

    protected NearHotBean(Parcel in) {
    }

    public static final Creator<NearHotBean> CREATOR = new Creator<NearHotBean>() {
        @Override
        public NearHotBean createFromParcel(Parcel source) {
            return new NearHotBean(source);
        }

        @Override
        public NearHotBean[] newArray(int size) {
            return new NearHotBean[size];
        }
    };

    @Override
    public int getItemType() {
        return itemType;
    }
}
