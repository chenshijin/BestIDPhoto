package com.csj.bestidphoto.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.csj.bestidphoto.MApp;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.comm.Config;
import com.csj.bestidphoto.utils.glide.ImageLoaderHelper;
import com.csj.bestidphoto.utils.glide.RoundedCornersTransform;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.maoti.lib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PictureUtils {

    /**
     * takePicker 是否默认直接打开摄像机
     */
    public static void openImagePicker(Activity act, boolean takePicker, boolean isCrop, int selectLimit){
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setSelectLimit(selectLimit);
        imagePicker.setCrop(isCrop);
        Intent intent = new Intent(act, ImageGridActivity.class);
        if(takePicker){
            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true);
        }
        act.startActivityForResult(intent, Config.IMAGE_PICKER);
    }



    /**
     * 打开预览图片
     * @param act
     * @param index 从哪个开始预览
     * @param imgList
     */
    public static void openImgReview(Activity act, int index, List<ImageItem> imgList){
        //打开预览
        Intent intentPreview = new Intent(act, ImagePreviewDelActivity.class);
        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>)imgList);
        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, index);
        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
        act.startActivityForResult(intentPreview, Config.REQUEST_CODE_PREVIEW);
    }

    /**
     * 加载圆形图片（可自定义各个角度）
     * @param iv
     * @param path
     * @param defaultImage_resId
     * @param listener
     */
    public static void loadCircleImg(ImageView iv, String path, int defaultImage_resId, RequestListener listener){
        RequestOptions options = new RequestOptions().circleCrop();
        ImageLoaderHelper.loadImageByGlide(iv, path, defaultImage_resId, options, listener);
    }

    /**
     * 显示图片，不修边
     * @param act
     * @param url
     * @param imageView
     */
    public static void loadImg(Context act, String url, ImageView imageView){
        Glide.with(act).load(url)
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.mipmap.ic_launcher).into(imageView);
    }
    /**
     * 加载圆形图片（可自定义各个角度）
     * @param radius_dp
     * @param leftTop
     * @param rightTop
     * @param leftBottom
     * @param rightBottom
     * @param defaultImage_resId
     * @param error_resId
     */
    public static void loadCornersImg(ImageView iv, String path, float radius_dp, boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom, int defaultImage_resId, int error_resId, RequestListener listener){
        if(radius_dp <= 0F){
            radius_dp = 1F;
        }
        RoundedCornersTransform transform = new RoundedCornersTransform( radius_dp);
        transform.setNeedCorner(true, true, true, true);
        RequestOptions options = new RequestOptions()
                .placeholder(defaultImage_resId != -1?defaultImage_resId: R.drawable.ic_default_image)
                .error(error_resId != -1?error_resId: R.drawable.ic_default_image)
                .transform(transform);
        ImageLoaderHelper.loadImageByGlide(iv, path, defaultImage_resId, options, listener);
    }

    /**
     * 加载圆形图片（4个角度）
     * @param iv
     * @param path
     * @param radius_dp
     * @param defaultImage_resId
     * @param error_resId
     * @param listener
     */
    public static void loadCornersImg(ImageView iv, String path, float radius_dp, int defaultImage_resId, int error_resId, RequestListener listener){
        if(radius_dp <= 0F){
            radius_dp = 1F;
        }
        RoundedCorners roundedCorners = new RoundedCorners(Utils.dipToPx(MApp.getInstance(),radius_dp));
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        // RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        RequestOptions options = new RequestOptions()
                .placeholder(defaultImage_resId != -1?defaultImage_resId: R.drawable.ic_default_image)
                .error(error_resId != -1?error_resId: R.drawable.ic_default_image)
                .transform(roundedCorners);
        ImageLoaderHelper.loadImageByGlide(iv, path, defaultImage_resId, options, listener);
    }

}
