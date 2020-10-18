package com.csj.bestidphoto.comm;

import android.os.Parcel;
import android.os.Parcelable;

public class AdConfig implements Parcelable {
    private boolean advertising;//字段为false则app关闭广告，为true则打开广告
    private String userid;//应用的用户Id
    private String appid;//应用的appid
    private String splashid;//开屏广告位
    private String bannerid;//banner广告位
    private String floatscreenid;//插屏广告位
    private String rewardid;//激励视频广告位
    private String rewardname;//激励视频-奖励名称
    private int rewardcnt;//激励视频-奖励数量

    public boolean isAdvertising() {
        return advertising;
    }

    public void setAdvertising(boolean advertising) {
        this.advertising = advertising;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSplashid() {
        return splashid;
    }

    public void setSplashid(String splashid) {
        this.splashid = splashid;
    }

    public String getBannerid() {
        return bannerid;
    }

    public void setBannerid(String bannerid) {
        this.bannerid = bannerid;
    }

    public String getFloatscreenid() {
        return floatscreenid;
    }

    public void setFloatscreenid(String floatscreenid) {
        this.floatscreenid = floatscreenid;
    }

    public String getRewardid() {
        return rewardid;
    }

    public void setRewardid(String rewardid) {
        this.rewardid = rewardid;
    }

    public String getRewardname() {
        return rewardname;
    }

    public void setRewardname(String rewardname) {
        this.rewardname = rewardname;
    }

    public int getRewardcnt() {
        return rewardcnt;
    }

    public void setRewardcnt(int rewardcnt) {
        this.rewardcnt = rewardcnt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.advertising ? (byte) 1 : (byte) 0);
        dest.writeString(this.userid);
        dest.writeString(this.appid);
        dest.writeString(this.splashid);
        dest.writeString(this.bannerid);
        dest.writeString(this.floatscreenid);
        dest.writeString(this.rewardid);
        dest.writeString(this.rewardname);
        dest.writeInt(this.rewardcnt);
    }

    public AdConfig() {
    }

    protected AdConfig(Parcel in) {
        this.advertising = in.readByte() != 0;
        this.userid = in.readString();
        this.appid = in.readString();
        this.splashid = in.readString();
        this.bannerid = in.readString();
        this.floatscreenid = in.readString();
        this.rewardid = in.readString();
        this.rewardname = in.readString();
        this.rewardcnt = in.readInt();
    }

    public static final Creator<AdConfig> CREATOR = new Creator<AdConfig>() {
        @Override
        public AdConfig createFromParcel(Parcel source) {
            return new AdConfig(source);
        }

        @Override
        public AdConfig[] newArray(int size) {
            return new AdConfig[size];
        }
    };
}
