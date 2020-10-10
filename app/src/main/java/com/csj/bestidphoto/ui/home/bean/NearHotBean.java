package com.csj.bestidphoto.ui.home.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class NearHotBean implements MultiItemEntity,Parcelable {

    public final static int HOME_ITEM_TYPE_CONTENT = 1;
    public final static int HOME_ITEM_TYPE_TITLE = 2;

    private int itemType;
    private String photoModelName;
    private int pxW;
    private int pxH;

    public String getPhotoModelName() {
        return photoModelName;
    }

    public void setPhotoModelName(String photoModelName) {
        this.photoModelName = photoModelName;
    }

    public int getPxW() {
        return pxW;
    }

    public void setPxW(int pxW) {
        this.pxW = pxW;
    }

    public int getPxH() {
        return pxH;
    }

    public void setPxH(int pxH) {
        this.pxH = pxH;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public NearHotBean() {
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.itemType);
        dest.writeString(this.photoModelName);
        dest.writeInt(this.pxW);
        dest.writeInt(this.pxH);
    }

    protected NearHotBean(Parcel in) {
        this.itemType = in.readInt();
        this.photoModelName = in.readString();
        this.pxW = in.readInt();
        this.pxH = in.readInt();
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
}
