package com.sowell.mvpbase.presenter;

import android.content.Context;

import com.sowell.mvpbase.view.BaseView;

import java.lang.ref.WeakReference;

/**
 * Created by：kenny
 * Created Time：2020/5/13
 */
public abstract class BasePresenter<V extends BaseView>  {

    protected Context context;


    protected WeakReference<V> mView;

    protected boolean isViewActive() {
        return mView != null && mView.get() != null;
    }

    public void setVM(V v) {
        mView = new WeakReference<V>(v);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * onPresenterStart
     */
    public abstract void onStart();

    /**
     * onDestory
     */
    public void onDestroy() {
        if (mView != null) {
            mView.clear();
            mView = null;
        }

        context = null;
    }

    /**
     * 获取 View
     * @return
     */
    protected V getView(){
        return mView.get();
    }
}
