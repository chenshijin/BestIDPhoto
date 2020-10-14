package com.csj.bestidphoto;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.csj.bestidphoto.ad.TTAdManagerHolder;
import com.csj.bestidphoto.comm.Config;
import com.csj.bestidphoto.comm.SPKey;
import com.csj.bestidphoto.permission.PermissionsActivity;
import com.csj.bestidphoto.permission.PermissionsChecker;
import com.csj.bestidphoto.utils.PrefManager;
import com.csj.bestidphoto.utils.StatusBarUtil;
import com.csj.bestidphoto.utils.StatusCompat;
import com.csj.bestidphoto.utils.ToastUtil;
import com.lzy.imagepicker.util.BitmapUtil;
import com.maoti.lib.utils.LogUtil;
import com.maoti.lib.utils.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class WelComeActivity extends Activity {

    private final static String TAG = WelComeActivity.class.getSimpleName();
    @BindView(R.id.adIv)
    ImageView adIv;
    @BindView(R.id.adSplashContainer)
    FrameLayout adSplashContainer;

    private static final int AD_TIME_OUT = 3000;
    private TTAdNative mTTAdNative;
    private int dp36 = Utils.dipToPx(MApp.getInstance(), 22F);
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusCompat.setStatusBarColor(this,  Color.TRANSPARENT);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initData(savedInstanceState);
    }

    public void initData(@Nullable Bundle savedInstanceState) {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
//        setStatusBar();
        checkPermissions();

//        getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 3000L);
    }

    protected void setStatusBar() {
        //此处为设置系统标题栏，
        if (Build.VERSION.SDK_INT > 20) {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.tt_white), 1);
        }
    }

    private void showAd() {
        View v = adSplashContainer.findViewById(R.id.ad_byte);
        if (v != null) {
            adSplashContainer.removeView(v);
        }
        adSplashContainer.setVisibility(View.VISIBLE);

        loadSplashAd();
    }

    /**
     * 加载开屏广告
     */
    public void loadSplashAd() {
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = null;
        int[] WHL_LOGO = BitmapUtil.readBitMapWH(getApplicationContext(), R.mipmap.logo_app);
        int[] SWH = Utils.getWidth_Height(WelComeActivity.this);
        int bottomH = WHL_LOGO[1] + dp36;

        int AD_W = SWH[0];
        int AD_H = SWH[1] - bottomH ;//- Utils.getStatusBarHeight(this);
//        if (mIsExpress) {
        //个性化模板广告需要传入期望广告view的宽、高，单位dp，请传入实际需要的大小，
        //比如：广告下方拼接logo、适配刘海屏等，需要考虑实际广告大小
//        float expressViewWidth = UIUtils.getScreenWidthDp(this);
//        float expressViewHeight = UIUtils.getHeight(this);
//        adSlot = new AdSlot.Builder()
//                .setCodeId(Config.TOUTIAO_SPLASH_ID)
//                .setSupportDeepLink(true)
//                .setImageAcceptedSize(1080, 1920)
//                //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
//                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight)
//                .build();
//        } else {
        adSlot = new AdSlot.Builder()
                .setCodeId(Config.TOUTIAO_SPLASH_ID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build();
//        }

        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                Log.d(TAG, String.valueOf(message));
                ToastUtil.showShort(message);
                doAdFinish();
            }

            @Override
            @MainThread
            public void onTimeout() {
                LogUtil.i(TAG, "开屏广告加载超时");
                doAdFinish();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                LogUtil.i(TAG, "开屏广告请求成功");
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                if (view != null && adSplashContainer != null && !isFinishing()) {
                    view.setId(R.id.ad_byte);
                    View v = adSplashContainer.findViewById(R.id.ad_byte);
                    if (v != null) {
                        adSplashContainer.removeView(v);
                    }
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                    FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(AD_W, AD_H);
//                    FrameLayout.LayoutParams logoFlp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    logoFlp.bottomMargin = Utils.dipToPx(getApplicationContext(), 22);
//                    logoFlp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
                    adSplashContainer.addView(view, flp);
//                    adSplashContainer.addView(adIv,logoFlp);
                    adSplashContainer.setVisibility(View.VISIBLE);
                    adIv.bringToFront();
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    //ad.setNotAllowSdkCountdown();
                } else {
                    doAdFinish();
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        LogUtil.i(TAG, "开屏广告点击");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        LogUtil.i(TAG, "开屏广告展示");
                    }

                    @Override
                    public void onAdSkip() {
                        LogUtil.i(TAG, "开屏广告跳过");
                        adSplashContainer.setVisibility(View.GONE);
                        doAdFinish();
                    }

                    @Override
                    public void onAdTimeOver() {
                        LogUtil.i(TAG, "开屏广告倒计时结束");
                        adSplashContainer.setVisibility(View.GONE);
                        doAdFinish();
                    }
                });
                if (ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {
                        boolean hasShow = false;

                        @Override
                        public void onIdle() {

                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            if (!hasShow) {
                                ToastUtil.showShort("下载中...");
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            ToastUtil.showShort("下载暂停...");
                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            ToastUtil.showShort("下载失败...");
                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {

                        }
                    });
                }
            }
        }, AD_TIME_OUT);
    }

    private void startPermissionsActivity() {
        try {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE, Config.PERMISSIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        //请求权限全部结果
        rxPermission.request(
                //添加需要的权限
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ).subscribe(new Observer<Boolean>() {//订阅
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if(!aBoolean){
                    mPermissionsChecker = new PermissionsChecker(WelComeActivity.this);
                    LogUtil.i(TAG, "=权限=>" + mPermissionsChecker.lacksPermissions(Config.PERMISSIONS));
                    if (mPermissionsChecker.lacksPermissions(Config.PERMISSIONS)) {
                        startPermissionsActivity();
                    } else {
                        //TODO 权限已打开
                        showAd();
                    }
                }else{
                    TTAdManagerHolder.get().requestPermissionIfNecessary(getApplicationContext());
                    showAd();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void doAdFinish() {
        boolean showGuide = PrefManager.getPrefBoolean(SPKey._SHOW_GUIDE, true);
        if (showGuide) {
            startActivity(new Intent(this, GuidePageActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        adSplashContainer.removeAllViews();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            this.finish();
        } else if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
            //TODO 打开权限后执行
            LogUtil.i(TAG, "=打开权限后执行2=>");
            showAd();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
