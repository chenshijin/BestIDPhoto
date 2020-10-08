package com.csj.bestidphoto.ui.presenter;

import com.sowell.mvpbase.view.BaseView;

public interface EditPhotoCallBack {
    interface View extends BaseView {
        void onEditPhotoSuccess(String imgPath);//成功
        void onFaile(int code, String message,int type); //失败
    }
}
