package com.csj.bestidphoto.ui.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MinePhotoBean implements MultiItemEntity,Parcelable {

    public final static int MINE_ITEM_TYPE_CONTENT = 1;
    public final static int MINE_ITEM_TYPE_TITLE = 2;

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

    public MinePhotoBean() {
    }

    protected MinePhotoBean(Parcel in) {
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

    @Override
    public int getItemType() {
        return itemType;
    }
}
