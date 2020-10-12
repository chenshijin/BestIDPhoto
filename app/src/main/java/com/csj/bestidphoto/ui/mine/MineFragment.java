package com.csj.bestidphoto.ui.mine;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseFragment;
import com.csj.bestidphoto.comm.SPKey;
import com.csj.bestidphoto.ui.mine.adapter.MineListAdapter;
import com.csj.bestidphoto.ui.mine.bean.MinePhotoBean;
import com.csj.bestidphoto.utils.PrefManager;
import com.csj.bestidphoto.view.XRecycleView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lamfire.utils.StringUtils;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MineFragment extends BaseFragment {

    @BindView(R.id.titleBar)
    QMUITopBar titleBar;
    @BindView(R.id.minePhotosRv)
    XRecycleView minePhotosRv;
    private MineViewModel mineViewModel;

    private MineListAdapter adapter;

    @Override
    public int getContentViewResId() {
        return R.layout.fragment_mine;
    }

    @Override
    public View initView(View contentView, @Nullable Bundle savedInstanceState) {
        mineViewModel = ViewModelProviders.of(this).get(MineViewModel.class);

        titleBar.setTitle("我的相册");
        minePhotosRv.setPullListener(new XRecycleView.PullListener() {
            @Override
            public void onRefresh(QMUIPullLayout.PullAction pullAction) {
                loadData();
            }

            @Override
            public void onLoadMore(QMUIPullLayout.PullAction pullAction) {

            }
        });

        minePhotosRv.doRefresh();
        return null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            minePhotosRv.doRefresh();
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            //TODO now it's visible to user
            minePhotosRv.doRefresh();
        } else {
            //TODO now it's invisible to user
        }
    }

    private void loadData() {
//        showProgress("");
        Observable.create(new ObservableOnSubscribe<List<MinePhotoBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MinePhotoBean>> emitter) {
                String cacheData = PrefManager.getPrefString(SPKey._PHOTOS_RECORD, null);
                List<MinePhotoBean> datas = new ArrayList<>();
                if (!StringUtils.isEmpty(cacheData)) {
                    datas.addAll(new Gson().fromJson(cacheData, new TypeToken<List<MinePhotoBean>>() {
                    }.getType()));
                }
                emitter.onNext(datas);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MinePhotoBean>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<MinePhotoBean> result) {
//                        hideProgress();
                        minePhotosRv.finishAllActionsRun();
                        List<MultiItemEntity> datas = new ArrayList<>();
                        for (MinePhotoBean bean : result) {
                            datas.add(bean);
                        }
                        if(adapter == null){
                            adapter = new MineListAdapter(datas);
                            minePhotosRv.setAdapter(adapter);
                        }else{
                            adapter.setNewData(datas);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
