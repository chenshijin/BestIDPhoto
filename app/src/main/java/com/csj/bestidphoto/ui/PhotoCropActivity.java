package com.csj.bestidphoto.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.csj.bestidphoto.utils.glide.ImageLoaderHelper;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.R;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageBaseActivity;
import com.lzy.imagepicker.util.BitmapUtil;
import com.lzy.imagepicker.view.CropImageView;
import com.maoti.lib.utils.LogUtil;
import com.maoti.lib.utils.Utils;

import java.io.File;
import java.util.ArrayList;

public class PhotoCropActivity extends ImageBaseActivity implements View.OnClickListener, CropImageView.OnBitmapSaveCompleteListener {

    private CropImageView mCropImageView;
//    private Bitmap mBitmap;
    private boolean mIsSaveRectangle;
    private int mOutputX;
    private int mOutputY;
    private ImagePicker imagePicker;

    public final static String _KEY_IMG_W = "IMG_W";
    public final static String _KEY_IMG_H = "IMG_H";
    public final static String _KEY_IMG_RES_W = "_key_img_res_w";
    public final static String _KEY_IMG_RES_H = "_key_img_res_h";
    private final static String _KEY_IMG_PATH = "img_path";
    public final static String EXTRA_RESULT_IMG = "crop_result_ing";


    public static void startPhotoCropActivity(Activity act, String path, int resW_dp,int resH_dp,int cropW_dp,int cropH_dp) {//
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setFocusWidth(Utils.dipToPx(act,cropW_dp));
        imagePicker.setFocusHeight(Utils.dipToPx(act,cropH_dp));
        Intent to = new Intent(act, PhotoCropActivity.class);
        to.putExtra(_KEY_IMG_W, cropW_dp);
        to.putExtra(_KEY_IMG_H, cropH_dp);
        to.putExtra(_KEY_IMG_RES_W, resW_dp);
        to.putExtra(_KEY_IMG_RES_H, resH_dp);
        to.putExtra(_KEY_IMG_PATH, path);
        act.startActivityForResult(to, ImagePicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
    }

    private int getIMG_W(){
        return getIntent().getExtras().getInt(_KEY_IMG_W);
    }

    private int getIMG_H(){
        return getIntent().getExtras().getInt(_KEY_IMG_H);
    }

    private int getIMG_RES_W(){
        return getIntent().getExtras().getInt(_KEY_IMG_RES_W);
    }

    private int getIMG_RES_H(){
        return getIntent().getExtras().getInt(_KEY_IMG_RES_H);
    }

    private String getImgPath(){
        return getIntent().getExtras().getString(_KEY_IMG_PATH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);

        imagePicker = ImagePicker.getInstance();

        //初始化View
        findViewById(R.id.btn_back).setOnClickListener(this);
        Button btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setText(getString(R.string.ip_complete));
        btn_ok.setOnClickListener(this);
        TextView tv_des = (TextView) findViewById(R.id.tv_des);
        tv_des.setText(getString(R.string.ip_photo_crop));
        mCropImageView = (CropImageView) findViewById(R.id.cv_crop_image);
        mCropImageView.setOnBitmapSaveCompleteListener(this);

        //获取需要的参数
        mOutputX = imagePicker.getOutPutX();
        mOutputY = imagePicker.getOutPutY();
        mIsSaveRectangle = imagePicker.isSaveRectangle();
//        mImageItems = imagePicker.getSelectedImages();
        String imagePath = getImgPath();//mImageItems.get(0).path;

        mCropImageView.setFocusStyle(imagePicker.getStyle());
        mCropImageView.setFocusWidth(imagePicker.getFocusWidth());//
        mCropImageView.setFocusHeight(imagePicker.getFocusHeight());//

//        initCropImageViewSize();

//        //缩放图片
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(imagePath, options);
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        options.inSampleSize = calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels);
//        options.inJustDecodeBounds = false;
//        mBitmap = BitmapFactory.decodeFile(imagePath, options);
////        mCropImageView.setImageBitmap(mBitmap);
//        //设置默认旋转角度
////        mCropImageView.setImageBitmap(mCropImageView.rotate(mBitmap, BitmapUtil.getBitmapDegree(imagePath)));
        showPhoto(imagePath);

//        mCropImageView.setImageURI(Uri.fromFile(new File(imagePath)));
    }

    private void showPhoto(String path) {
//        ImageLoaderHelper.loadImageByGlide(mCropImageView,path,-1,null);
        ImageLoaderHelper.loadImageSimpleTarget(path, -1, new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                LogUtil.i("TAG", "resource.getIntrinsicWidth() = " + resource.getWidth() + "  resource.getIntrinsicHeight()=" + resource.getHeight());
//                ViewGroup.LayoutParams vlp = mCropImageView.getLayoutParams();
//                vlp.width = Utils.getWindowWidth(PhotoCropActivity.this) * 335 / 375;
//                vlp.height = vlp.width * getBeanData().getPxH() / getBeanData().getPxW();

                mCropImageView.setImageBitmap(resource);
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

    private void initCropImageViewSize(){
        ViewGroup.LayoutParams vlp = mCropImageView.getLayoutParams();
        vlp.width = Utils.getWindowWidth(this);
        vlp.height = vlp.width * getIMG_RES_H() / getIMG_RES_W();
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = width / reqWidth;
            } else {
                inSampleSize = height / reqHeight;
            }
        }
        return inSampleSize;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.btn_ok) {
            mCropImageView.saveBitmapToFile(imagePicker.getCropCacheFolder(this), mOutputX, mOutputY, mIsSaveRectangle);
        }
    }

    @Override
    public void onBitmapSaveSuccess(File file) {
//        Toast.makeText(ImageCropActivity.this, "裁剪成功:" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        //裁剪后替换掉返回数据的内容，但是不要改变全局中的选中数据
//        mImageItems.remove(0);
//        ImageItem imageItem = new ImageItem();
//        imageItem.path = file.getAbsolutePath();
//        mImageItems.add(imageItem);

        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT_IMG, file.getAbsolutePath());
        intent.putExtra(_KEY_IMG_W, getIMG_W());
        intent.putExtra(_KEY_IMG_H, getIMG_H());
        setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
        finish();
    }

    @Override
    public void onBitmapSaveError(File file) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCropImageView.setOnBitmapSaveCompleteListener(null);
//        if (null != mBitmap && !mBitmap.isRecycled()) {
//            mBitmap.recycle();
//            mBitmap = null;
//        }
    }
}
