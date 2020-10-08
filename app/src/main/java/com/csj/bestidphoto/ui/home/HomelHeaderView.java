package com.csj.bestidphoto.ui.home;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.csj.bestidphoto.R;
import com.csj.bestidphoto.ui.home.bean.TopBannerBean;
import com.csj.bestidphoto.utils.AntiShakeUtil;
import com.csj.bestidphoto.utils.PictureUtils;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author kenny
 * @description: 新闻详情页头部
 * @date 2020/5/19
 */

public class HomelHeaderView extends FrameLayout {

    private static final String TAG = HomelHeaderView.class.getSimpleName();
    @BindView(R.id.banner_normal)
    MZBannerView bannerNormal;
    @BindView(R.id.allSizeTv)
    TextView allSizeTv;
    @BindView(R.id.photoEditorTv)
    TextView photoEditorTv;

    private Context mContext;

    public HomelHeaderView(Context context) {
        this(context, null);
    }

    public HomelHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomelHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.homeheader, this);
        ButterKnife.bind(this, this);
//            mNormalBanner.setIndicatorRes(R.color.colorAccent, R.color.colorPrimary);
        List<TopBannerBean> list = new ArrayList<>();
        TopBannerBean bean;
        int[] banners = new int[]{R.mipmap.banner1, R.mipmap.banner2};
        for (int res : banners) {
            bean = new TopBannerBean();
            bean.setImgRes(res);
            list.add(bean);
        }
        bannerNormal.setPages(list, (MZHolderCreator<BannerPaddingViewHolder>) () -> new BannerPaddingViewHolder());
        bannerNormal.start();

        photoEditorTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureUtils.openImagePicker((Activity) getContext(), false, true, 1);
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        onDestory();
        super.onDetachedFromWindow();
    }

    public void onDestory() {

//        if (mWebView != null) {
//            mWebView.onDestory();
//        }
    }

    public static class BannerPaddingViewHolder implements MZViewHolder<TopBannerBean> {
        private ImageView mImageView;
        private TextView mDesTv;
        AntiShakeUtil util = new AntiShakeUtil();

        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item_padding, null);
            mImageView = view.findViewById(R.id.banner_image);
            mDesTv = view.findViewById(R.id.banner_des_tv);
            mDesTv.setVisibility(View.GONE);
            return view;
        }

        @Override
        public void onBind(Context context, int position, TopBannerBean data) {
            // 数据绑定
//            ImageLoaderHelper.loadImageByGlide(mImageView, data.getImage(), -1, null);
            mImageView.setImageResource(data.getImgRes());
            mImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (util.check(v.getId())) {
                        return;
                    }
                    //  url类型 0 文章 1 视频 2 直播 3外链 4广告
                }
            });
        }
    }

}
