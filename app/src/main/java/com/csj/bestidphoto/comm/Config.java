package com.csj.bestidphoto.comm;

import android.Manifest;
import android.os.Environment;

public final class Config {

    //打开图片选择
    public final static int IMAGE_PICKER = 10000;
    public final static int REQUEST_CODE_PREVIEW = 10001;
//    public static final String OSS_AES_PW = "sQhc_W-.b17*!ken";//勿改
//
//    public static final String PATH_EMOJJ = "emjj";//表情资源目录
//    public static final String APP_STORAGE_ROOT = "maoti";
//    public static final String imagePath = Environment.getExternalStorageDirectory().getPath() + "/"+ APP_STORAGE_ROOT;// APP 所有缓存地址
//    public static final String IMG_COMPRESS_CACHE = imagePath + "/" + "imgcache/";//图片压缩缓存路径（用完可删除）
//    public static final String IMG_FAN_DRAFIT_CACHE = imagePath + "/" + "fan_drafit/";//球迷圈草稿图片
//    public static final String maoTiAppLocalBasePath = "/"+ APP_STORAGE_ROOT +"/downloadApp/";// 下载APk缓存地址
//    public static final String DEVICE_ID_PATH = imagePath + "/device_id.txt";//缓存机器唯一标识


    // 所需的全部权限
    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    /**
     * 头条穿山甲广告
     */
    public static final String TOUTIAO_USER_ID = "47525";//应用的用户id
    public static final String TOUTIAO_APPID = "5114918";//应用appid
    public static final String TOUTIAO_SPLASH_ID = "887395618";//开屏广告位
    public static final String TOUTIAO_BANNER_ID = "945570431";//banner广告位
    public static final String TOUTIAO_FLOATSCREEN_ID = "945570433";//插屏广告位
    public static final String TOUTIAO_REWARD_ID = "945570440";//激励视频广告位
    public static final String TOUTIAO_REWARD_NAME = "免费制作证件照次数";//激励视频-奖励名称
    public static final int TOUTIAO_REWARD_AMOUNT = 1;//激励视频-奖励数量

    public static final String APP_DATA = Environment.getExternalStorageDirectory().getPath() + "/"+ "bestIdPhoto";// APP 所有缓存地址
    public static final String PHOTOS = APP_DATA + "/" + "photos/";//缓存制作好的照片(在相册可查看)


}
