package com.csj.bestidphoto;

import android.app.Activity;
import android.os.Bundle;

import androidx.multidex.MultiDexApplication;

import com.bumptech.glide.request.target.ViewTarget;
import com.csj.bestidphoto.ad.TTAdManagerHolder;
import com.csj.bestidphoto.utils.glide.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.maoti.lib.BaseApplication;
import com.maoti.lib.db.SPFKey;
import com.maoti.lib.db.SPfUtil;
import com.maoti.lib.utils.LogUtil;
import com.maoti.lib.utils.OsUtils;

public class MApp extends MultiDexApplication {

    private static final String TAG = MApp.class.getSimpleName();
    private static MApp mInstance;
    private ActivityLifecycleCallbacks activityLifecycleCallback;
    public final String packageName = "com.csj.bestidphoto";//
    //    private LoginBean loginBean = null;
    private int activityStartCount = 0;
    private boolean isBackGround;//是否在后台运行
    public String APP_NAME = "便民证件照";

    @Override
    public void onCreate() {
        super.onCreate();

        //LeakCanary.install(this);

        String processName = OsUtils.getProcessName(getApplicationContext(), android.os.Process.myPid());
        LogUtil.i(TAG, "进程名称" + processName);
        if (processName != null) {
            boolean defaultProcess = processName.equals(packageName);//
            if (defaultProcess) {//主进程 才初始化  防止远端服务（多进程）多次调用执行
                mInstance = this;
                initBaseLib();
//                initBugly();
                initImagePicker();//初始化图片选择器
                ViewTarget.setTagId(R.id.glide_tag);//避免在listview中的ImageView调用了setTag().而用Glide加载图片崩溃问题
//                initLogger();
//                initPushApp();
                registerActivityLifecycle();
//                initSkin();
//                initDb();
//                initMobSdk();
//                initSharSdkListener();
//                SoundPlayer.init(this);

                TTAdManagerHolder.init(this);//初始化穿山甲
            }
        }
    }

    public boolean isBackGround() {
        return isBackGround;
    }

    public void setBackGround(boolean backGround) {
        isBackGround = backGround;
    }

    private void initBaseLib() {
        BaseApplication.initBaseApplication(mInstance);
    }

//    public void initMobSdk(){
//        //设置Mobsdk用户协议
//        MobSDK.submitPolicyGrantResult(true, null);
//    }
//    private void initBugly(){
//        if(BuildConfig.DEBUG){
//            CrashReport.initCrashReport(getApplicationContext(), "31f9eeed90", false);
//        }else{
//            CrashReport.initCrashReport(getApplicationContext(), "abbd536240", false);
//        }
//
//    }

//    public LoginBean getLoginBean() {
//        if (loginBean == null) {
//            loginBean = new LoginBean();
//            String str = SPfUtil.getInstance().getString(SPFKey.LoginBean);
//            if (!StringUtils.isEmpty(str)) {
//                Gson gson = new Gson();
//                loginBean = gson.fromJson(str, LoginBean.class);
//            }
//        }
//        return loginBean;
//    }
//
//    public void setLoginBean(LoginBean loginBean) {
//        this.loginBean = loginBean;
//    }

//    /**
//     * 初始化greendao 数据库
//     */
//    private void initDb() {
//        PersonUtil.getInstance(this).init();
//    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false);                  //是否按矩形区域保存
        imagePicker.setSelectLimit(9);                        //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                       //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                          //保存文件的高度。单位像素
    }

//    /**
//     * 日志框架初始化
//     */
//    private void initLogger() {
//        Logger.addLogAdapter(new AndroidLogAdapter() {
//            @Override
//            public boolean isLoggable(int priority, String tag) {
//                // 关闭log false， 打开log true
//                return BuildConfig.DEBUG;
//            }
//        });
//    }

//    /**
//     * 通过分享成功后，监听网页打开的回传
//     */
//    public void initSharSdkListener(){
//        ShareSDK.prepareLoopShare(new LoopShareResultListener() {
//            @Override
//            public void onResult(Object o) {
//                // startActivity
//                Logger.i("onResult"+ o.toString());
//                Map<String,String > map = (Map<String, String>) o;
////                detail(文章),person(个人主页),post1(圈子帖子),postItem(话题帖子)
//                String type = map.get("type");
//                int id = Integer.valueOf(map.get("id"));
//                Logger.i("onResult: type"+type +" id:"+id);
//                if(type.equals("detail")){
//                    NewsDetailActivity.startNewsDetailToCommentItem(getApplicationContext(),id);
//                }else  if(type.equals("person")){
//                    PersonCenterActivity.startToPersonCenterActivity(getApplicationContext(),id);
//                }else  if(type.equals("post1")){
//                    CirNewsDetailActivity.startNewsDetailActivity(getApplicationContext(),id,0,0);
//                }else  if(type.equals("postItem")){
//                    CirNewsDetailActivity.startNewsDetailActivity(getApplicationContext(),id,1,0);
//                }
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                Logger.i("onResult"+ throwable);
//            }
//        });
//    }


//    /**
//     * 初始化推送
//     */
//    private void initPushApp() {
//        PushApplication.getInstance().init(this);
//    }

//    /**
//     * 初始化换肤 skin框架
//     */
//    private void initSkin() {
//        SkinCompatManager.withoutActivity(this)
//                .addInflater(new SkinAppCompatViewInflater())   // 基础控件换肤
//                .addInflater(new SkinConstraintViewInflater())  // ConstraintLayout
////                .setSkinStatusBarColorEnable(true)              // 关闭状态栏换肤
//                .loadSkin();
//    }


    private void registerActivityLifecycle() {
        activityLifecycleCallback = new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityManager.getInstance().pushActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                activityStartCount++;
                //数值从0变到1说明是从后台切到前台
                if (activityStartCount == 1) {
                    //从后台切到前台
                    setBackGround(false);
                }

            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                activityStartCount--;
                //数值从1到0说明是从前台切到后台
                if (activityStartCount == 0) {
                    //从前台切到后台
                    setBackGround(true);
//                    changeLogo();
                }

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityManager.getInstance().popActivity(activity);
            }
        };
        registerActivityLifecycleCallbacks(activityLifecycleCallback);
    }

//    public void changeLogo() {
//        String logo = SPfUtil.getInstance().getString(SPFKey.ICO_NAME);
//        LogUtil.i(TAG, "logo name= " + logo);
//        LogoUtil.enableComponent(logo);//切换logo
//    }


    public static MApp getInstance() {
        return mInstance;
    }

    public static String getPackName() {
        return mInstance.getPackageName();
    }

    public void unregisterActivityLifecycle() {
        ActivityManager.getInstance().finishAllActivity();
        if (activityLifecycleCallback != null) {
            unregisterActivityLifecycleCallbacks(activityLifecycleCallback);
        }
    }

    /**
     * 退出app
     */
    public static void exitApp() {
//        for (Activity activity : list) {
//            if (null != activity) {
//                activity.finish();
//            }
//        }
        ActivityManager.getInstance().finishAllActivity();
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
