package com.csj.bestidphoto.ui.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MinePhotoBean implements MultiItemEntity,Parcelable {

    public final static int MINE_ITEM_TYPE_CONTENT = 1;
    public final static int MINE_ITEM_TYPE_TITLE = 2;

    private int itemType = MINE_ITEM_TYPE_CONTENT;
    private String photoModelName;
    private int pxW;
    private int pxH;
    private int mmW;
    private int mmH;
    private String photoUrl;
    private int model = 2;//1-照片编辑器  2-照片模型

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

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

    public int getMmW() {
        return mmW;
    }

    public void setMmW(int mmW) {
        this.mmW = mmW;
    }

    public int getMmH() {
        return mmH;
    }

    public void setMmH(int mmH) {
        this.mmH = mmH;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public MinePhotoBean() {
    }

    @Override
    public int getItemType() {
        if(itemType != MINE_ITEM_TYPE_CONTENT){
            itemType = MINE_ITEM_TYPE_CONTENT;
        }
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
        dest.writeInt(this.mmW);
        dest.writeInt(this.mmH);
        dest.writeString(this.photoUrl);
        dest.writeInt(this.model);
    }

    protected MinePhotoBean(Parcel in) {
        this.itemType = in.readInt();
        this.photoModelName = in.readString();
        this.pxW = in.readInt();
        this.pxH = in.readInt();
        this.mmW = in.readInt();
        this.mmH = in.readInt();
        this.photoUrl = in.readString();
        this.model = in.readInt();
    }

    public static final Creator<MinePhotoBean> CREATOR = new Creator<MinePhotoBean>() {
        @Override
        public MinePhotoBean createFromParcel(Parcel source) {
            return new MinePhotoBean(source);
        }

        @Override
        public MinePhotoBean[] newArray(int size) {
            return new MinePhotoBean[size];
        }
    };
}
