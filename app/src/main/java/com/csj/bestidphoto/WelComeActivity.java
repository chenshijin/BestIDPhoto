package com.csj.bestidphoto;

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
import com.csj.bestidphoto.base.BaseActivity;
import com.csj.bestidphoto.comm.Config;
import com.csj.bestidphoto.comm.SPKey;
import com.csj.bestidphoto.utils.PrefManager;
import com.lzy.imagepicker.util.BitmapUtil;
import com.maoti.lib.utils.LogUtil;
import com.maoti.lib.utils.Utils;

import butterknife.BindView;

public class WelComeActivity extends BaseActivity {
    @BindView(R.id.adIv)
    ImageView adIv;
    @BindView(R.id.adSplashContainer)
    FrameLayout adSplashContainer;

    private static final int AD_TIME_OUT = 1;
    private TTAdNative mTTAdNative;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_welcome;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        showAd();
//        getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 3000L);
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
        int[] wh = Utils.getWidth_Height(this);
        adSlot = new AdSlot.Builder()
                .setCodeId(Config.TOUTIAO_SPLASH_ID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(wh[0], wh[1])
                .build();
//        }

        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                Log.d(TAG, String.valueOf(message));
                showToast(message);
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
                    if(v != null){
                        adSplashContainer.removeView(v);
                    }
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                    int[] WH = BitmapUtil.readBitMapWH(getApplicationContext(), R.mipmap.logo_app);
                    int[] SWH = Utils.getWidth_Height(WelComeActivity.this);
                    int bottomH = WH[1] + Utils.dipToPx(getApplicationContext(), 36);
                    FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SWH[1] - bottomH);
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
                                showToast("下载中...");
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            showToast("下载暂停...");
                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            showToast("下载失败...");
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

    public void doAdFinish() {
        boolean showGuide = PrefManager.getPrefBoolean(SPKey._SHOW_GUIDE, false);
        if (showGuide) {
            openPage(GuidePageActivity.class);
        } else {
            openPage(MainActivity.class);
        }
    }

}
