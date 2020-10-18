package com.csj.bestidphoto.ui.presenter;

import com.csj.bestidphoto.comm.AdConfig;
import com.sowell.mvpbase.view.BaseView;

public interface GetConfigCallBack {
    interface View extends BaseView {
        void onGetConfigSuccess(AdConfig config);//成功

        void onFaile(int code, String message, int type); //失败
    }
}
