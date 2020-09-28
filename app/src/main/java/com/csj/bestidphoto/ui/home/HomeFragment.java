package com.csj.bestidphoto.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseFragment;
import com.csj.bestidphoto.ui.home.adapter.NearHotListAdapter;
import com.csj.bestidphoto.ui.home.bean.NearHotBean;
import com.csj.bestidphoto.view.XRecycleView;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.titleBar)
    QMUITopBar titleBar;
    @BindView(R.id.homeRv)
    XRecycleView homeRv;
    private HomeViewModel homeViewModel;

    private NearHotListAdapter adapter;
    private HomelHeaderView homelHeaderView;

    @Override
    public int getContentViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    public View initView(View contentView, @Nullable Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homelHeaderView = new HomelHeaderView(requireActivity());

        titleBar.setTitle("主页");
        test();
        return null;
    }

    private void test(){
        List<MultiItemEntity> list = new ArrayList<>();
        NearHotBean bean;
        bean = new NearHotBean();
        bean.setItemType(NearHotBean.HOME_ITEM_TYPE_TITLE);
        list.add(bean);
        for(int i = 0; i < 20; i++){
            bean = new NearHotBean();
            bean.setItemType(NearHotBean.HOME_ITEM_TYPE_CONTENT);
            list.add(bean);
        }

        adapter = new NearHotListAdapter(list);
        adapter.addHeaderView(homelHeaderView);
        homeRv.setAdapter(adapter);
    }
}
