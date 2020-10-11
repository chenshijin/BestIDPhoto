package com.csj.bestidphoto.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseActivity;
import com.csj.bestidphoto.ui.home.adapter.NearHotListAdapter;
import com.csj.bestidphoto.ui.home.bean.NearHotBean;
import com.csj.bestidphoto.view.XRecycleView;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AllPhotoModelListActivity extends BaseActivity {
    @BindView(R.id.commonXR)
    XRecycleView commonXR;
    @BindView(R.id.titleBar)
    QMUITopBar titleBar;

    private NearHotListAdapter adapter;


    public static void startAllPhotoModelListActivity(Context ctx) {
        Intent to = new Intent(ctx, AllPhotoModelListActivity.class);
        if (!(ctx instanceof Activity)) {
            to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ctx.startActivity(to);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_allphoto_model;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        titleBar.setTitle("尺寸列表");
        titleBar.addLeftImageButton(R.mipmap.back, R.id.goback);
        findViewById(R.id.goback).setOnClickListener(mainOnClickListener);

        test();
    }

    View.OnClickListener mainOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.goback:
                    finish();
                    break;
            }
        }
    };

    private void test(){
        String[] names = new String[]{"一寸","二寸","小一寸","小二寸","英语四六级考试","大一寸","学籍照片"};
        int[] pxW = new int[]{295,413,260,413,144,390,307};
        int[] pxH = new int[]{413,579,378,513,192,567,378};
        int[] mmW = new int[]{25,35,22,35,20,33,26};
        int[] mmH = new int[]{35,49,32,45,27,48,32};
        List<MultiItemEntity> list = new ArrayList<>();
        NearHotBean bean;
        for(int i = 0; i < names.length; i++){
            bean = new NearHotBean();
            bean.setPhotoModelName(names[i]);
            bean.setPxW(pxW[i]);
            bean.setPxH(pxH[i]);
            bean.setItemType(NearHotBean.HOME_ITEM_TYPE_CONTENT);
            list.add(bean);
        }

        adapter = new NearHotListAdapter(list);
        commonXR.setAdapter(adapter);
    }

}
