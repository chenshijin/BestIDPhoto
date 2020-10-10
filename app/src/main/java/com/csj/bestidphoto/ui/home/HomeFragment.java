package com.csj.bestidphoto.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseFragment;
import com.csj.bestidphoto.ui.PhotoEditorActivity;
import com.csj.bestidphoto.ui.PhotoStandardModelDetailActivity;
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
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(PhotoStandardModelDetailActivity.class);//PhotoEditorActivity
            }
        });
        return null;
    }

    private void test(){
        String[] names = new String[]{"一寸","二寸","小一寸","小二寸"};
        int[] pxW = new int[]{295,413,260,413};
        int[] pxH = new int[]{413,579,378,513};
        List<MultiItemEntity> list = new ArrayList<>();
        NearHotBean bean;
        bean = new NearHotBean();
        bean.setItemType(NearHotBean.HOME_ITEM_TYPE_TITLE);
        list.add(bean);
        for(int i = 0; i < names.length; i++){
            bean = new NearHotBean();
            bean.setPhotoModelName(names[i]);
            bean.setPxW(pxW[i]);
            bean.setPxH(pxH[i]);
            bean.setItemType(NearHotBean.HOME_ITEM_TYPE_CONTENT);
            list.add(bean);
        }

        adapter = new NearHotListAdapter(list);
        adapter.addHeaderView(homelHeaderView);
        homeRv.setAdapter(adapter);
    }
}
