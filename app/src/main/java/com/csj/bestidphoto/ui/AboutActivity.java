package com.csj.bestidphoto.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseActivity;
import com.csj.bestidphoto.view.HorizontalCommonMenu;
import com.maoti.lib.utils.Utils;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.logoIv)
    ImageView logoIv;
    @BindView(R.id.policyHcm)
    HorizontalCommonMenu policyHcm;
    @BindView(R.id.userProHcm)
    HorizontalCommonMenu userProHcm;
    @BindView(R.id.aboutHcm)
    HorizontalCommonMenu aboutHcm;
    @BindView(R.id.contactUsHcm)
    HorizontalCommonMenu contactUsHcm;
    @BindView(R.id.versionTv)
    TextView versionTv;
    @BindView(R.id.titleBar)
    QMUITopBar titleBar;

    public static void startAboutActivity(Context ctx) {
        Intent to = new Intent(ctx, AboutActivity.class);
        if (!(ctx instanceof Activity)) {
            to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (ctx instanceof BaseActivity) {
            ((BaseActivity) ctx).openPage(to);
        } else {
            ctx.startActivity(to);
        }
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_about;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        titleBar.setTitle(String.format("关于%s",getResources().getString(R.string.app_name)));
        titleBar.addLeftImageButton(R.mipmap.back, R.id.goback);
        findViewById(R.id.goback).setOnClickListener(mainOnClickListener);

        versionTv.setText(Utils.getBaseAppVersionName());
        policyHcm.setOnClickListener(mainOnClickListener);
        userProHcm.setOnClickListener(mainOnClickListener);
        aboutHcm.setOnClickListener(mainOnClickListener);
        contactUsHcm.setOnClickListener(mainOnClickListener);
    }

    View.OnClickListener mainOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.goback:
                    finish();
                    break;
                case R.id.policyHcm:
                    AboutContentActivity.startAboutActivity(getContext(),1);
                    break;
                case R.id.userProHcm:
                    AboutContentActivity.startAboutActivity(getContext(),2);
                    break;
                case R.id.aboutHcm:
                    AboutContentActivity.startAboutActivity(getContext(),3);
                    break;
                case R.id.contactUsHcm:
                    AboutContentActivity.startAboutActivity(getContext(),4);
                    break;
            }
        }
    };
}
