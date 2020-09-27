package com.csj.bestidphoto.utils.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.csj.bestidphoto.MApp;
import com.csj.bestidphoto.R;
import com.sunfusheng.progress.GlideApp;

public class ImageLoaderHelper {
	
	/**
	 * 
	 * 2018年5月18日
	 * 作者：csj
	 * 方法描述：加载图片，不需要缓存
	 * @param iv
	 * @param path
	 * @param defaultImage_resId
	 */
	public static void loadImageByGlideNotCache(ImageView iv, String path, int defaultImage_resId, RequestListener listener){
		GlideApp
	    .with(iv.getContext().getApplicationContext())
	    .load(path)
//	    .centerCrop()
	    .diskCacheStrategy(DiskCacheStrategy.NONE )//禁用磁盘缓存
	    .skipMemoryCache( true )//跳过内存缓存
	    .placeholder(defaultImage_resId!=-1?defaultImage_resId: R.drawable.ic_default_image)
		.listener(listener)
	    .into(iv);
	}

	public static void loadImageByGlideNotCacheAsBitmap(ImageView iv, String path, int defaultImage_resId, RequestListener listener){
		GlideApp
				.with(iv.getContext().getApplicationContext())
				.asBitmap()
				.load(path)
//	    .centerCrop()
				.diskCacheStrategy(DiskCacheStrategy.NONE )//禁用磁盘缓存
				.skipMemoryCache( true )//跳过内存缓存
				.placeholder(defaultImage_resId!=-1?defaultImage_resId: R.drawable.ic_default_image)
//				.crossFade()
				.listener(listener)
				.into(iv);
	}
	
	
	public static void loadImageByGlide(ImageView iv, String path, int defaultImage_resId, boolean getsmall){

		if(iv==null){
			return;
		}

		GlideApp
	    .with(iv.getContext().getApplicationContext())
	    .load(path)
//	    .centerCrop()
	    .placeholder(defaultImage_resId!=-1?defaultImage_resId: R.drawable.ic_default_image)
//				.listener(new RequestListener<String, GlideDrawable>() {
//					@Override
//					public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
//						return false;
//					}
//
//					@Override
//					public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
//						return false;
//					}
//				})
	    .into(iv);
	}

	public static void loadImageByGlide(ImageView iv, String path, int defaultImage_resId, RequestListener listener){

		if(iv==null){
			return;
		}

		GlideApp
				.with(iv.getContext().getApplicationContext())
				.asDrawable()
				.load(path)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
//	    .centerCrop()
				.placeholder(defaultImage_resId!=-1?defaultImage_resId:R.drawable.ic_default_image)
				.error(defaultImage_resId!=-1?defaultImage_resId:R.drawable.ic_default_image)
				.listener(listener)
//				.listener(new RequestListener<String, GlideDrawable>() {
//					@Override
//					public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
//						return false;
//					}
//
//					@Override
//					public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
//						return false;
//					}
//				})
				.into(iv);
	}

	public static void loadImageByGlide(ImageView iv, String path, int defaultImage_resId, RequestOptions options, RequestListener listener){
		if(iv==null){
			return;
		}
		GlideApp
				.with(iv.getContext().getApplicationContext())
				.load(path)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
//				.placeholder(defaultImage_resId!=-1?defaultImage_resId:R.mipmap.wode_img_touxiang_weidenglu)
//				.error(defaultImage_resId!=-1?defaultImage_resId:R.mipmap.wode_img_touxiang_weidenglu)
				.listener(listener)
				.apply(options)
				.into(iv);
	}

	public static void loadImageByGlideAsBitmap(ImageView iv, String path, int defaultImage_resId, RequestListener listener){

		if(iv==null){
			return;
		}

		GlideApp
				.with(iv.getContext().getApplicationContext())
				.asBitmap()//在Glide 3中的语法是先load()再asBitmap()的，而在Glide 4中是先asBitmap()再load()
				.load(path)

//	    .centerCrop()
				.placeholder(defaultImage_resId!=-1?defaultImage_resId:R.drawable.ic_default_image)
				.error(R.drawable.ic_default_image)
				.listener(listener)
//				.listener(new RequestListener<String, GlideDrawable>() {
//					@Override
//					public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
//						return false;
//					}
//
//					@Override
//					public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
//						return false;
//					}
//				})
				.into(iv);
	}

	public static void loadImageByGlideAsBitmap(ImageView iv, String path, int defaultImage_resId, RequestOptions options, RequestListener listener){

		if(iv==null){
			return;
		}

		GlideApp
				.with(iv.getContext().getApplicationContext())
				.asBitmap()//在Glide 3中的语法是先load()再asBitmap()的，而在Glide 4中是先asBitmap()再load()
				.load(path)
				.placeholder(defaultImage_resId!=-1?defaultImage_resId:R.drawable.ic_default_image)
				.error(R.drawable.ic_default_image)
				.listener(listener)
				.apply(options)
				.into(iv);
	}

	/**
	 * 仅加载图片，不做显示
	 * @param path
	 * @param target
	 */
	public static void loadImageSimpleTargetOnly(String path, SimpleTarget target) {
		GlideApp
				.with(MApp.getInstance()) // could be an issue!
				.asBitmap()   //制转换Bitmap
				.load(path)
				.into(target);
	}

	public static void loadImageSimpleTarget(String path, int defaultRes, SimpleTarget target) {
		GlideApp
				.with(MApp.getInstance()) // could be an issue!
				.asBitmap()   //制转换Bitmap
				.load(path)
				.placeholder(defaultRes == -1?R.drawable.ic_default_image:defaultRes)
				.fallback(defaultRes == -1?R.drawable.ic_default_image:defaultRes)
				.error(defaultRes == -1?R.drawable.ic_default_image:defaultRes)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(target);
	}

}
