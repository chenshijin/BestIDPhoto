package com.csj.bestidphoto.ui.presenter;

import com.csj.bestidphoto.model.Retrofit;
import com.csj.bestidphoto.model.response.EditPhotoResp;
import com.maoti.lib.net.ResponseResult;
import com.maoti.lib.net.interceptor.DefaultObserver;
import com.sowell.mvpbase.presenter.BasePresenter;

public class EditPhotoPresenter extends BasePresenter<EditPhotoCallBack.View> {
    @Override
    public void onStart() {

    }

    public void cutPhoto(String path, int width, int height, String color) {

        Retrofit.cutPhoto(path, width, height, color, new DefaultObserver<EditPhotoResp>() {
            @Override
            public void onSuccess(ResponseResult<EditPhotoResp> result) {
                try {
                    if (result != null && result.isSuccess()) {
                        if (isViewActive()) {
                            getView().onEditPhotoSuccess(result.getData().getImageURL());
                        }
                    } else {
                        if (isViewActive()) {
                            getView().onFaile(result.getRet(), result.getMsg(),1);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onException(int code, String eMsg) {
                if (isViewActive()) {
                    getView().onFaile(code,eMsg,2);
                }
            }
        });
    }

    public void beautyPhoto(String type, String value, String imgPath) {

        Retrofit.beautyPhoto(type, value, imgPath, new DefaultObserver<EditPhotoResp>() {
            @Override
            public void onSuccess(ResponseResult<EditPhotoResp> result) {
                try {
                    if (result != null && result.isSuccess()) {
                        if (isViewActive()) {
                            getView().onBeautyPhotoSuccess(result.getData().getImageURL());
                        }
                    } else {
                        if (isViewActive()) {
                            getView().onFaile(result.getRet(), result.getMsg(),3);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onException(int code, String eMsg) {
                if (isViewActive()) {
                    getView().onFaile(code,eMsg,4);
                }
            }
        });
    }
}
