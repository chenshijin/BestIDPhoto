package com.sowell.mvpbase.loader;


import com.sowell.mvpbase.presenter.BasePresenter;

/**
 * Created by：kenny
 * Created Time：2020/5/13
 */
public interface PresenterFactory<T extends BasePresenter> {
    T create();
}
