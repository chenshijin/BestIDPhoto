package com.csj.bestidphoto.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseActivity;
import com.csj.bestidphoto.view.XRecycleView;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;

public class AllPhotoModelListActivity extends BaseActivity {
    @BindView(R.id.commonXR)
    XRecycleView commonXR;
    @BindView(R.id.titleBar)
    QMUITopBar titleBar;


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

}
