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
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.csj.bestidphoto.MApp;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseActivity;
import com.csj.bestidphoto.comm.Config;
import com.csj.bestidphoto.comm.SPKey;
import com.csj.bestidphoto.ui.home.bean.NearHotBean;
import com.csj.bestidphoto.ui.mine.bean.MinePhotoBean;
import com.csj.bestidphoto.ui.presenter.EditPhotoCallBack;
import com.csj.bestidphoto.ui.presenter.EditPhotoPresenter;
import com.csj.bestidphoto.utils.JavaUtil;
import com.csj.bestidphoto.utils.PrefManager;
import com.csj.bestidphoto.utils.ToastUtil;
import com.csj.bestidphoto.utils.glide.ImageLoaderHelper;
import com.csj.bestidphoto.view.PhotoBeautyBar;
import com.csj.bestidphoto.view.PhotoBgColorsBar;
import com.csj.bestidphoto.view.PhotoCutBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lamfire.utils.StringUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.maoti.lib.utils.LogUtil;
import com.maoti.lib.utils.Utils;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.cutFl)
    FrameLayout cutFl;

    private String imgPath;
    private String localPath;//本地原图
    private NearHotBean photoModelBean;

    private final static String _KEY_IMG_PATH = "imgPath";
    private final static String _KEY_PHOTO_MODEL = "photo_model";

    public static void startPhotoEditorActivity(Context ctx, String imgPath, Parcelable dataBean) {
        Intent to = new Intent(ctx, PhotoEditorActivity.class);
        to.putExtra(_KEY_IMG_PATH, imgPath);
        to.putExtra(_KEY_PHOTO_MODEL, dataBean);
        if (!(ctx instanceof Activity)) {
            to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ctx.startActivity(to);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_photo_editor;
    }

    private NearHotBean getBeanData() {
        return getIntent().getExtras().getParcelable(_KEY_PHOTO_MODEL);
    }

    private String getImgPath() {
        return getIntent().getExtras().getString(_KEY_IMG_PATH);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        titleBar.setTitle("编辑");
        titleBar.addLeftImageButton(R.mipmap.back, R.id.goback);
        titleBar.addRightTextButton("保存", R.id.photo_edit_save);
        findViewById(R.id.goback).setOnClickListener(mainOnClickListener);
        findViewById(R.id.photo_edit_save).setOnClickListener(mainOnClickListener);

        ViewGroup.LayoutParams rlp = photoIv.getLayoutParams();
        rlp.width = Utils.getWindowWidth(PhotoEditorActivity.this) * 335 / 375;
        rlp.height = rlp.width * getBeanData().getPxH() / getBeanData().getPxW();
        photoIv.setLayoutParams(rlp);

        localPath = getImgPath();
        photoModelBean = getBeanData();
        showPhoto(localPath);

        initListener();

        if (getBeanData().getModel() == 2) {
            cutFl.setVisibility(View.GONE);
            photoCutBar.setVisibility(View.GONE);
            checkCb(R.id.beautyCb);
        }else{
            checkCb(R.id.cutCb);
        }

        cutPhoto(localPath, getBeanData().getPxW(), getBeanData().getPxH(), "red");
    }

    private void showPhoto(String path) {
        ImageLoaderHelper.loadImageSimpleTarget(path, -1, new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                LogUtil.i(TAG, "resource.getIntrinsicWidth() = " + resource.getWidth() + "  resource.getIntrinsicHeight()=" + resource.getHeight());
                ViewGroup.LayoutParams vlp = photoIv.getLayoutParams();
                vlp.width = Utils.pxToDip(MApp.getInstance(), photoModelBean.getPxW()) * 6;//* 335 / 375;
                vlp.height = vlp.width * photoModelBean.getPxH() / photoModelBean.getPxW();

//                photoIv.setLayoutParams(vlp);
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

    private void initListener() {
        cutCb.setOnClickListener(mainOnClickListener);
        beautyCb.setOnClickListener(mainOnClickListener);
        bgCb.setOnClickListener(mainOnClickListener);

        photoCutBar.setmOnCutCheckListener(new PhotoCutBar.OnCutCheckListener() {
            @Override
            public void onCutCheck(int w, int h) {
//                cutPhoto(getImgPath(), w, h, photoBgColorsBar.getBgColor());
                PhotoCropActivity.startPhotoCropActivity(PhotoEditorActivity.this,getImgPath(),getBeanData().getPxW(),getBeanData().getPxH(),w,h);
            }
        });

        photoBeautyBar.setmOnBeautyCheckListener(new PhotoBeautyBar.OnBeautyCheckListener() {
            @Override
            public void doPhoto(String type, String value) {
                if (!StringUtils.isEmpty(type)) {
                    beautyPhoto(imgPath, type, value);
                } else {
                    cutPhoto(localPath, photoModelBean.getPxW(), photoModelBean.getPxH(), photoBgColorsBar.getBgColor());
                }
            }
        });

        photoBgColorsBar.setmOnBgColorCheckListener(new PhotoBgColorsBar.OnBgColorCheckListener() {
            @Override
            public void onBgColorCheck(String color) {
                cutPhoto(localPath, photoModelBean.getPxW(), photoModelBean.getPxH(), color);
            }
        });
    }

    private void cutPhoto(String path, int w, int h, String colorBg) {
        showProgress("");
        getPresenter().cutPhoto(path, w, h, colorBg);
    }

    private void beautyPhoto(String path, String type, String value) {
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
                    String cacheData = PrefManager.getPrefString(SPKey._PHOTOS_RECORD, null);
                    List<MinePhotoBean> datas = new ArrayList<>();
                    if (!StringUtils.isEmpty(cacheData)) {
                        datas.addAll(new Gson().fromJson(cacheData, new TypeToken<List<MinePhotoBean>>() {
                        }.getType()));
                    }
                    if (photoModelBean != null) {
                        MinePhotoBean photoBean = JavaUtil.modelAconvertoB(photoModelBean, MinePhotoBean.class);
                        photoBean.setPhotoUrl(imgPath);
                        datas.add(photoBean);
                        PrefManager.setPrefString(SPKey._PHOTOS_RECORD, new Gson().toJson(datas));
                        ToastUtil.showShort("已保存!");
                        finish();
                    } else {
                        ToastUtil.showShort("保存失败!");
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

    private void checkCb(int id) {
        cutCb.setChecked(id == R.id.cutCb ? true : false);
        beautyCb.setChecked(id == R.id.beautyCb ? true : false);
        bgCb.setChecked(id == R.id.bgCb ? true : false);

        photoBgColorsBar.setVisibility(id == R.id.bgCb ? View.VISIBLE : View.GONE);
        photoBeautyBar.setVisibility(id == R.id.beautyCb ? View.VISIBLE : View.GONE);
        photoCutBar.setVisibility(id == R.id.cutCb ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onEditPhotoSuccess(String imgPath) {
        hideProgress();
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
        ToastUtil.showShort("操作失败!可能是系统检测不到图像中的人脸!");
        LogUtil.i(TAG, "操作失败 " + message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == ImagePicker.REQUEST_CODE_CROP) {
                localPath = data.getExtras().getString(PhotoCropActivity.EXTRA_RESULT_IMG);
                int w = data.getExtras().getInt(PhotoCropActivity._KEY_IMG_W);
                int h = data.getExtras().getInt(PhotoCropActivity._KEY_IMG_H);
                photoModelBean.setPxW(w);
                photoModelBean.setPxH(h);
                cutPhoto(localPath, w, h, photoBgColorsBar.getBgColor());
            }
        }
    }
}
