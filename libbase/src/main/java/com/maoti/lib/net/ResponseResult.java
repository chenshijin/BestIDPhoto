package com.maoti.lib.net;

import android.os.Parcel;
import android.os.Parcelable;

public class ResponseResult<T> implements Parcelable {
    private int ret;
    private String msg;
    private T data;

    protected ResponseResult(Parcel in) {
        ret = in.readInt();
        msg = in.readString();
    }

    public static final Creator<ResponseResult> CREATOR = new Creator<ResponseResult>() {
        @Override
        public ResponseResult createFromParcel(Parcel in) {
            return new ResponseResult(in);
        }

        @Override
        public ResponseResult[] newArray(int size) {
            return new ResponseResult[size];
        }
    };

    public boolean isSuccess(){
        return ret == 0;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int code) {
        this.ret = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ret);
        dest.writeString(msg);
    }
}
