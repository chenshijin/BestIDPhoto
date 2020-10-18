package com.csj.bestidphoto.model.response;

import android.os.Parcel;
import android.os.Parcelable;

public class EditPhotoResp implements Parcelable {
    private String ImageURL;

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public EditPhotoResp() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ImageURL);
    }

    protected EditPhotoResp(Parcel in) {
        this.ImageURL = in.readString();
    }

    public static final Creator<EditPhotoResp> CREATOR = new Creator<EditPhotoResp>() {
        @Override
        public EditPhotoResp createFromParcel(Parcel source) {
            return new EditPhotoResp(source);
        }

        @Override
        public EditPhotoResp[] newArray(int size) {
            return new EditPhotoResp[size];
        }
    };
}
