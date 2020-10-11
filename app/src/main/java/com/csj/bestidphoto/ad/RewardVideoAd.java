package com.csj.bestidphoto.ad;

import android.app.Activity;
import android.content.Context;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.maoti.lib.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Request;

/**
 * 激励视频广告
 */
public class RewardVideoAd {
    private static final String TAG = RewardVideoAd.class.getSimpleName();
    private Context ctx;
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private String codeId;//广告位id
    private int model = 1;//1-竖屏  2-横屏
    private boolean mHasShowDownloadActive = false;
    private String rewardName;//奖励的名称
    private int rewardAmount;//奖励的数量
    private String userID;//用户id,必传参数
    private int attach;//1-MainActivity  2-RemoveWatermarkActivity  3-QuShuiyinActivity  4-CutVideoActivity

    public RewardVideoAd(Context ctx, String codeId, String userID, String rewardName, int rewardAmount, int model) {//务必传入的context为activity，否则会影响应用下载广告的下载
        this.ctx = ctx;
        this.codeId = codeId;
        this.userID = userID;
        this.rewardName = rewardName;
        this.rewardAmount = rewardAmount;
        this.model = model;
        mTTAdNative = TTAdManagerHolder.get().createAdNative(ctx);
//        if(ctx instanceof MainActivity){
//            attach = 1;
//        }else if(ctx instanceof RemoveWatermarkActivity){
//            attach = 2;
//        }else if(ctx instanceof QuShuiyinActivity){
//            attach = 3;
//        }else if(ctx instanceof CutVideoActivity){
//            attach = 4;
//        }
    }

    public void loadAd() {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setRewardName(rewardName) //奖励的名称
                .setRewardAmount(rewardAmount)  //奖励的数量
                .setUserID(userID)//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(model == 2 ? TTAdConstant.HORIZONTAL : TTAdConstant.VERTICAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
//                ToastUtil.showShort( message);
                LogUtil.i(TAG, message);
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
//                ToastUtil.showShort( "rewardVideoAd video cached");
                LogUtil.i(TAG, "rewardVideoAd video cached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
//                ToastUtil.showShort(  "rewardVideoAd loaded 广告类型：" + getAdType(ad.getRewardVideoAdType()));
                LogUtil.i(TAG, "rewardVideoAd loaded 广告类型：" + getAdType(ad.getRewardVideoAdType()));
                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
//                        ToastUtil.showShort(  "rewardVideoAd show");
                        LogUtil.i(TAG, "rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
//                        ToastUtil.showShort(  "rewardVideoAd bar click");
                        LogUtil.i(TAG, "rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
//                        ToastUtil.showShort(  "rewardVideoAd close");
                        LogUtil.i(TAG, "rewardVideoAd close");
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
//                        ToastUtil.showShort(  "rewardVideoAd complete");
                        LogUtil.i(TAG, "rewardVideoAd complete");
//                        if(AdUtil.getRewardLeftTimes() > 0){
//                            giveTimes();
//                        }
                    }

                    @Override
                    public void onVideoError() {
//                        ToastUtil.showShort(  "rewardVideoAd error");
                        LogUtil.i(TAG, "rewardVideoAd error");
                    }

                    @Override
                    public void onRewardVerify(boolean b, int i, String s, int i1, String s1) {

                    }

//                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
//                    @Override
//                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
////                        ToastUtil.showShort( "verify:" + rewardVerify + " amount:" + rewardAmount + " name:" + rewardName);
//                        LogUtil.i(TAG, "verify:" + rewardVerify + " amount:" + rewardAmount + " name:" + rewardName);
//                    }

                    @Override
                    public void onSkippedVideo() {
//                        ToastUtil.showShort( "rewardVideoAd has onSkippedVideo");
                        LogUtil.i(TAG, "rewardVideoAd has onSkippedVideo");
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
//                            ToastUtil.showShort( "下载中，点击下载区域暂停");
                            LogUtil.i(TAG, "下载中，点击下载区域暂停");
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                        ToastUtil.showShort("下载暂停，点击下载区域继续");
                        LogUtil.i(TAG, "下载暂停，点击下载区域继续");
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                        ToastUtil.showShort("下载失败，点击下载区域重新下载");
                        LogUtil.i(TAG, "下载失败，点击下载区域重新下载");
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                        ToastUtil.showShort("下载完成，点击下载区域重新下载");
                        LogUtil.i(TAG, "下载完成，点击下载区域重新下载");
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
//                        ToastUtil.showShort( "安装完成，点击下载区域打开");
                        LogUtil.i(TAG, "安装完成，点击下载区域打开");
                    }
                });

                showRewardVideoAd();
            }
        });
    }

//    private void giveTimes() {
//        HttpManager.getInstance().giveTimes(new HttpListener() {
//            @Override
//            public void onSuccess(ResultData resultData, int tag) {
//                String data = resultData.getDataStr();
//                LogUtil.d(TAG, "onSuccess:" + data);
//                AdUtil.updateFeedTime();
//                EventBus.getDefault().post(new AdFinishEvent(attach));
//            }
//
//            @Override
//            public void onFailure(int responseCode, Request request, int tag) {
//                LogUtil.d(TAG, "onFailure:" + responseCode);
//            }
//        }, 0);
//    }

    /**
     * 展示广告
     */
    private void showRewardVideoAd() {
        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示
            //该方法直接展示广告
//                    mttRewardVideoAd.showRewardVideoAd(RewardVideoActivity.this);

            //展示广告，并传入广告展示的场景
            if (ctx instanceof Activity) {
                mttRewardVideoAd.showRewardVideoAd((Activity) ctx, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_sgl666");
            }
            mttRewardVideoAd = null;
        } else {
//            ToastUtil.showShort("请先加载广告");
            LogUtil.i(TAG, "请先加载广告");
        }
    }

    private String getAdType(int type) {
        switch (type) {
            case TTAdConstant.AD_TYPE_COMMON_VIDEO:
                return "普通激励视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE_VIDEO:
                return "Playable激励视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE:
                return "纯Playable，type=" + type;
        }

        return "未知类型+type=" + type;
    }

}
