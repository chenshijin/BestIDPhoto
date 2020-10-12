package com.csj.bestidphoto.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseActivity;
import com.csj.bestidphoto.ui.home.bean.NearHotBean;
import com.csj.bestidphoto.ui.mine.bean.MinePhotoBean;
import com.csj.bestidphoto.ui.presenter.EditPhotoCallBack;
import com.csj.bestidphoto.ui.presenter.EditPhotoPresenter;
import com.csj.bestidphoto.utils.glide.ImageLoaderHelper;
import com.csj.bestidphoto.view.PhotoBeautyBar;
import com.csj.bestidphoto.view.PhotoBgColorsBar;
import com.csj.bestidphoto.view.PhotoCutBar;
import com.lamfire.utils.StringUtils;
import com.maoti.lib.utils.LogUtil;
import com.maoti.lib.utils.Utils;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;

public class PhotoEditorActivity extends BaseActivity<EditPhotoPresenter> implements EditPhotoCallBack.View {
    @BindView(R.id.titleBar)
    QMUITopBar titleBar;
    @BindView(R.id.photoIv)
    ImageView photoIv;
    @BindView(R.id.photoBgColorsBar)
    PhotoBgColorsBar photoBgColorsBar;
    @BindView(R.id.photoBeautyBar)
    PhotoBeautyBar photoBeautyBar;
    @BindView(R.id.photoCutBar)
    PhotoCutBar photoCutBar;
    @BindView(R.id.cutCb)
    CheckBox cutCb;
    @BindView(R.id.beautyCb)
    CheckBox beautyCb;
    @BindView(R.id.bgCb)
    CheckBox bgCb;

    private String imgPath;
    private String imgPathCopy;//原图

    private final static String _KEY_IMG_PATH = "imgPath";
    private final static String _KEY_PHOTO_MODEL = "photo_model";

    public static void startPhotoEditorActivity(Context ctx, String imgPath, Parcelable dataBean) {
        Intent to = new Intent(ctx, PhotoEditorActivity.class);
        to.putExtra(_KEY_IMG_PATH, imgPath);
        to.putExtra(_KEY_PHOTO_MODEL,dataBean);
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
        return getIntent().getExtras().getString(_KEY_IMG_PATH);
    }

    private NearHotBean getBeanData(){
        return getIntent().getExtras().getParcelable(_KEY_PHOTO_MODEL);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        titleBar.setTitle("编辑");
        titleBar.addLeftImageButton(R.mipmap.back, R.id.goback);
        titleBar.addRightTextButton("保存", R.id.photo_edit_save);
        findViewById(R.id.goback).setOnClickListener(mainOnClickListener);
        findViewById(R.id.photo_edit_save).setOnClickListener(mainOnClickListener);

        ViewGroup.LayoutParams rlp = photoIv.getLayoutParams();
        rlp.width = Utils.getWindowWidth(PhotoEditorActivity.this) * 335/375;
        rlp.height = rlp.width * 453/335;
        photoIv.setLayoutParams(rlp);

        showPhoto(getImgPath());

        initListener();

        cutPhoto(getImgPath(),335, 453, "red");
    }

    private void showPhoto(String path){
        ImageLoaderHelper.loadImageSimpleTarget(path,-1,new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                LogUtil.i(TAG,"resource.getIntrinsicWidth() = " + resource.getWidth() + "  resource.getIntrinsicHeight()=" + resource.getHeight());
                ViewGroup.LayoutParams vlp = photoIv.getLayoutParams();
                vlp.width = Utils.getWindowWidth(PhotoEditorActivity.this) * 335/375;
                vlp.height = vlp.width * photoCutBar.getCutH() / photoCutBar.getCutW();

                photoIv.setLayoutParams(vlp);
                photoIv.setImageBitmap(resource);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                super.onLoadStarted(placeholder);
//                if(placeholder!=null){
//                    photoIv.setImageDrawable(placeholder);
//                }
            }
        });
    }

    private void initListener(){
        cutCb.setOnClickListener(mainOnClickListener);
        beautyCb.setOnClickListener(mainOnClickListener);
        bgCb.setOnClickListener(mainOnClickListener);

        photoCutBar.setmOnCutCheckListener(new PhotoCutBar.OnCutCheckListener() {
            @Override
            public void onCutCheck(int w, int h) {
                cutPhoto(getImgPath(),w, h, photoBgColorsBar.getBgColor());
            }
        });

        photoBeautyBar.setmOnBeautyCheckListener(new PhotoBeautyBar.OnBeautyCheckListener() {
            @Override
            public void doPhoto(String type, String value) {
                if(!StringUtils.isEmpty(type)){
                    beautyPhoto(imgPath,type,value);
                }else{
                    cutPhoto(getImgPath(),photoCutBar.getCutW(), photoCutBar.getCutH(), photoBgColorsBar.getBgColor());
                }
            }
        });

        photoBgColorsBar.setmOnBgColorCheckListener(new PhotoBgColorsBar.OnBgColorCheckListener() {
            @Override
            public void onBgColorCheck(String color) {
                cutPhoto(getImgPath(),photoCutBar.getCutW(), photoCutBar.getCutH(), color);
            }
        });
    }

    private void cutPhoto(String path,int w,int h,String colorBg){
        showProgress("");
        getPresenter().cutPhoto(path, w, h, colorBg);
    }

    private void beautyPhoto(String path,String type,String value){
        showProgress("");
        getPresenter().beautyPhoto(type, value, path);
    }

    View.OnClickListener mainOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.goback:
                    finish();
                    break;
                case R.id.photo_edit_save:
                    MinePhotoBean photoBean = new MinePhotoBean();
                    NearHotBean photoModel = getBeanData();
                    if(photoModel != null){
                        photoBean.setMmW(photoModel.getMmW());
                        photoBean.setMmH(photoModel.getMmH());

                    }
                    break;
                case R.id.cutCb:
                case R.id.beautyCb:
                case R.id.bgCb:
                    checkCb(v.getId());
                    break;
            }
        }
    };

    private void checkCb(int id){
        cutCb.setChecked(id == R.id.cutCb?true:false);
        beautyCb.setChecked(id == R.id.beautyCb?true:false);
        bgCb.setChecked(id == R.id.bgCb?true:false);

        photoBgColorsBar.setVisibility(id == R.id.bgCb?View.VISIBLE:View.GONE);
        photoBeautyBar.setVisibility(id == R.id.beautyCb?View.VISIBLE:View.GONE);
        photoCutBar.setVisibility(id == R.id.cutCb?View.VISIBLE:View.GONE);
    }

    @Override
    public void onEditPhotoSuccess(String imgPath) {
        hideProgress();
        this.imgPathCopy = imgPath;
        this.imgPath = imgPath;
        showPhoto(imgPath);
    }

    @Override
    public void onBeautyPhotoSuccess(String imgPath) {
        hideProgress();
        this.imgPath = imgPath;
        showPhoto(imgPath);
    }

    @Override
    public void onFaile(int code, String message, int type) {
        hideProgress();
        LogUtil.i(TAG, "裁剪失败 " + message);
    }
}
