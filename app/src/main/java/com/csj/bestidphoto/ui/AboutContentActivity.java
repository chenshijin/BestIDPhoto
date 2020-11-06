package com.csj.bestidphoto.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseActivity;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;

public class AboutContentActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    QMUITopBar titleBar;
    @BindView(R.id.contentTv)
    TextView contentTv;
    private int model;//1-隐私政策  2-用户协议  3-关于我们  4-联系我们
    private static  final String _MODEL = "model";

    public static void startAboutActivity(Context ctx,int model) {
        Intent to = new Intent(ctx, AboutContentActivity.class);
        to.putExtra(_MODEL,model);
        if (!(ctx instanceof Activity)) {
            to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (ctx instanceof BaseActivity) {
            ((BaseActivity) ctx).openPage(to);
        } else {
            ctx.startActivity(to);
        }
    }

    private int getModel(){
        return getIntent().getExtras().getInt(_MODEL);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_aboutcontent;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        init();
        titleBar.addLeftImageButton(R.mipmap.back, R.id.goback);
        findViewById(R.id.goback).setOnClickListener(mainOnClickListener);

    }

    private void init(){
        String title = "";
        switch (getModel()){
            case 1:
                title = getString(R.string.app_name) + "\u3000应用隐私政策";
                contentTv.setText(getResources().getString(R.string.policy_content));
                break;
            case 2:
                title = "用户协议";
                contentTv.setText(getResources().getString(R.string.user_pro));
                break;
            case 3:
                title = "关于我们";
                contentTv.setText(getResources().getString(R.string.about_us));
                break;
            case 4:
                title = "联系我们";
                contentTv.setText(getResources().getString(R.string.contact_us));
                break;
        }
        titleBar.setTitle(title);
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
