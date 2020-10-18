package com.csj.bestidphoto.ad;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.csj.bestidphoto.utils.ToastUtil;
import com.shehuan.niv.Utils;

import java.util.List;

public class NativeInteractionAd {

    private final String TAG = NativeInteractionAd.class.getSimpleName();
    private TTAdNative mTTAdNative;
    private Activity act;
//    private TTAdDislike mTTAdDislike;
    private TTNativeExpressAd mTTAd;
    private long startTime = 0;
    private boolean mHasShowDownloadActive = false;

    public NativeInteractionAd(Activity act) {
        this.act = act;
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(act);
    }

    public void loadExpressAd(String codeId, int expressViewWidth, int expressViewHeight) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
//                ToastUtil.showShort("load error : " + code + ", " + message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
//                ToastUtil.showShort("load success !");
                showAd();
            }
        });
    }

    private void showAd() {
        if (mTTAd != null) {
            mTTAd.render();
        } else {
//            ToastUtil.showShort("请先加载广告");
        }
    }

    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
            @Override
            public void onAdDismiss() {
//                ToastUtil.showShort("广告关闭");
            }

            @Override
            public void onAdClicked(View view, int type) {
//                ToastUtil.showShort("广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
//                ToastUtil.showShort("广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
//                ToastUtil.showShort(msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp
//                ToastUtil.showShort("渲染成功");
                mTTAd.showInteractionExpressAd(act);
                view.bringToFront();
            }
        });
        bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
//                ToastUtil.showShort("点击开始下载");
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
//                    ToastUtil.showShort("下载中，点击暂停");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                ToastUtil.showShort("下载暂停，点击继续");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                ToastUtil.showShort("下载失败，点击重新下载");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
//                ToastUtil.showShort("安装完成，点击图片打开");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                ToastUtil.showShort("点击安装");
            }
        });
    }

    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
//        if (customStyle) {
//            //使用自定义样式
//            List<FilterWord> words = ad.getFilterWords();
//            if (words == null || words.isEmpty()) {
//                return;
//            }
//
//            final DislikeDialog dislikeDialog = new DislikeDialog(this, words);
//            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
//                @Override
//                public void onItemClick(FilterWord filterWord) {
//                    //屏蔽广告
//                    ToastUtil.showShort( "点击 " + filterWord.getName());
//                }
//            });
//            ad.setDislikeDialog(dislikeDialog);
//            return;
//        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(act, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                //TToast.show(mContext, "反馈了 " + value);
//                ToastUtil.showShort("\t\t\t\t\t\t\t感谢您的反馈!\t\t\t\t\t\t\n我们将为您带来更优质的广告体验");
            }

            @Override
            public void onCancel() {
//                ToastUtil.showShort("点击取消 ");
            }

            @Override
            public void onRefuse() {
//                ToastUtil.showShort("您已成功提交反馈，请勿重复提交哦！");
            }

        });
    }

    public void onDestroy() {
        if (mTTAd != null) {
            mTTAd.destroy();
        }
    }
}
