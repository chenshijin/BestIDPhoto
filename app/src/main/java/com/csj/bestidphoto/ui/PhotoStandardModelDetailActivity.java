package com.csj.bestidphoto.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseActivity;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zhouwei.mzbanner.MZBannerView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoStandardModelDetailActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    QMUITopBar titleBar;
    @BindView(R.id.sizeModelTv)
    TextView sizeModelTv;
    @BindView(R.id.saveModelTv)
    TextView saveModelTv;
    @BindView(R.id.standardSizeTv)
    TextView standardSizeTv;
    @BindView(R.id.pxSizeTv)
    TextView pxSizeTv;
    @BindView(R.id.dpiTv)
    TextView dpiTv;
    @BindView(R.id.bgcolorTag)
    TextView bgcolorTag;
    @BindView(R.id.colorsLL)
    LinearLayout colorsLL;
    @BindView(R.id.fileSizeTv)
    TextView fileSizeTv;
    @BindView(R.id.otherTv)
    TextView otherTv;
    @BindView(R.id.banner_normal)
    MZBannerView bannerNormal;
    @BindView(R.id.albumUpTv)
    TextView albumUpTv;
    @BindView(R.id.camaraUpTv)
    TextView camaraUpTv;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_photomodel_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        titleBar.setTitle("规格详情");
        titleBar.addLeftImageButton(R.mipmap.back,R.id.goback);
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
