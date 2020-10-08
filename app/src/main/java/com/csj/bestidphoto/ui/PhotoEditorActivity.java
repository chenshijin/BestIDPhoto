package com.csj.bestidphoto.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseActivity;
import com.csj.bestidphoto.ui.presenter.EditPhotoCallBack;
import com.csj.bestidphoto.ui.presenter.EditPhotoPresenter;
import com.csj.bestidphoto.utils.glide.ImageLoaderHelper;
import com.maoti.lib.utils.LogUtil;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;

public class PhotoEditorActivity extends BaseActivity<EditPhotoPresenter> implements EditPhotoCallBack.View {
    @BindView(R.id.titleBar)
    QMUITopBar titleBar;
    @BindView(R.id.photoIv)
    ImageView photoIv;

    public static void startPhotoEditorActivity(Context ctx, String imgPath) {
        Intent to = new Intent(ctx, PhotoEditorActivity.class);
        to.putExtra("imgPath", imgPath);
        if (!(ctx instanceof Activity)) {
            to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ctx.startActivity(to);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_photo_editor;
    }

    private String getImgPath() {
        return getIntent().getExtras().getString("imgPath");
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        titleBar.setTitle("编辑");
        titleBar.addLeftImageButton(R.mipmap.back, R.id.goback);
        titleBar.addRightTextButton("保存", R.id.photo_edit_save);
        findViewById(R.id.goback).setOnClickListener(mainOnClickListener);
        findViewById(R.id.photo_edit_save).setOnClickListener(mainOnClickListener);
        ImageLoaderHelper.loadImageByGlide(photoIv, getImgPath(), -1, null);

        getPresenter().cutPhoto(getImgPath(), 335, 453, "red");
    }

    View.OnClickListener mainOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.goback:
                    finish();
                    break;
                case R.id.photo_edit_save:
                    break;
            }
        }
    };

    @Override
    public void onEditPhotoSuccess(String imgPath) {
        ImageLoaderHelper.loadImageByGlide(photoIv, imgPath, -1, null);
    }

    @Override
    public void onFaile(int code, String message, int type) {
        LogUtil.i(TAG, "裁剪失败 " + message);
    }
}
