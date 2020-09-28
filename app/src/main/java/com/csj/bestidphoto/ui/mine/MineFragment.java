package com.csj.bestidphoto.ui.mine;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseFragment;
import com.csj.bestidphoto.ui.home.adapter.NearHotListAdapter;
import com.csj.bestidphoto.ui.home.bean.NearHotBean;
import com.csj.bestidphoto.ui.mine.adapter.MineListAdapter;
import com.csj.bestidphoto.ui.mine.bean.MinePhotoBean;
import com.csj.bestidphoto.view.XRecycleView;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
        test();
        return null;
    }

    private void test(){
        List<MultiItemEntity> list = new ArrayList<>();
        MinePhotoBean bean;
        bean = new MinePhotoBean();
        bean.setItemType(MinePhotoBean.MINE_ITEM_TYPE_CONTENT);
        list.add(bean);
        for(int i = 0; i < 20; i++){
            bean = new MinePhotoBean();
            bean.setItemType(1);
            list.add(bean);
        }

        adapter = new MineListAdapter(list);
        minePhotosRv.setAdapter(adapter);
    }
}
