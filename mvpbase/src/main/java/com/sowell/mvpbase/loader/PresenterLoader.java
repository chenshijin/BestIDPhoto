package com.sowell.mvpbase.loader;

import android.content.Context;
import androidx.loader.content.Loader;

import com.sowell.mvpbase.presenter.BasePresenter;

/**
 * Created by：kenny
 * Created Time：2020/5/13
 */
public class PresenterLoader<T extends BasePresenter> extends Loader<T> {

    private final PresenterFactory<T> mFactory;
    private T mPresenter;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     * @param factory
     */
    public PresenterLoader(Context context, PresenterFactory<T> factory) {
        super(context);
        this.mFactory = factory;
    }

    // 省略构造方法

    @Override
    protected void onStartLoading() {

        // 如果已经有Presenter实例那就直接返回
        if (mPresenter != null) {
            deliverResult(mPresenter);
            return;
        }

        // 如果没有
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        // 通过工厂来实例化Presenter
        mPresenter = mFactory.create();

        // 返回Presenter
        deliverResult(mPresenter);
    }

    @Override
    protected void onReset() {
        if(mPresenter != null) {
            mPresenter.onDestroy();
        }
        mPresenter = null;
    }
}
