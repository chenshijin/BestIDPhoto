package com.csj.bestidphoto.ui.presenter;

import com.csj.bestidphoto.comm.AdConfig;
import com.csj.bestidphoto.model.Retrofit;
import com.maoti.lib.net.ResponseResult;
import com.maoti.lib.net.interceptor.DefaultObserver;
import com.sowell.mvpbase.presenter.BasePresenter;

public class ConfigPresenter extends BasePresenter<GetConfigCallBack.View> {
    @Override
    public void onStart() {

    }

    public void getConfig() {

        Retrofit.getConfig(new DefaultObserver<AdConfig>() {
            @Override
            public void onSuccess(ResponseResult<AdConfig> result) {
                try {
                    if (result != null && result.isSuccess()) {
                        if (isViewActive()) {
                            getView().onGetConfigSuccess(result.getData());
                        }
                    } else {
                        if (isViewActive()) {
                            getView().onFaile(result.getRet(), result.getMsg(), 1);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onException(int code, String eMsg) {
                if (isViewActive()) {
                    getView().onFaile(code, eMsg, 1);
                }
            }
        });
    }
}
