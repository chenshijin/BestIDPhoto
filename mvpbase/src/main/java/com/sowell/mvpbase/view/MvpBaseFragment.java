package com.sowell.mvpbase.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.sowell.mvpbase.mvputils.TUtil;
import com.sowell.mvpbase.presenter.BasePresenter;
import com.trello.rxlifecycle3.components.support.RxFragment;


/**
 * Created by：kenny
 * Created Time：2020/5/13
 */
public abstract class MvpBaseFragment<P extends BasePresenter> extends RxFragment implements BaseView {
    private static final String TAG = "MvpBaseFragment";
    private static int LOADER_ID = 102;
    //p 层实例
    private BasePresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter == null) {
            mPresenter = getPresenterInstance();
        }

        if (mPresenter != null) {
            mPresenter.setVM(this);
        }

        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }


    //获取 Presenter 实例
    private P getPresenterInstance() {
        return TUtil.getT(MvpBaseFragment.this, 0); //泛型首个参数
    }


    @SuppressWarnings("unchecked")
    protected P getPresenter() {
        if (mPresenter == null) return null;
        return (P) mPresenter;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
