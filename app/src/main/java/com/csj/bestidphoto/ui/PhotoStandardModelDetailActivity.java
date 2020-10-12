package com.csj.bestidphoto.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseActivity;
import com.csj.bestidphoto.comm.Config;
import com.csj.bestidphoto.ui.home.HomelHeaderView;
import com.csj.bestidphoto.ui.home.bean.NearHotBean;
import com.csj.bestidphoto.ui.home.bean.TopBannerBean;
import com.csj.bestidphoto.utils.PictureUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.shehuan.niv.Utils;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoStandardModelDetailActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    QMUITopBar titleBar;
    @BindView(R.id.sizeModelTv)
    TextView sizeModelTv;
    @BindView(R.id.saveModelTv)
    TextView saveModelTv;
    @BindView(R.id.standardSizeTv)
    TextView standardSizeTv;
    @BindView(R.id.pxSizeTv)
    TextView pxSizeTv;
    @BindView(R.id.dpiTv)
    TextView dpiTv;
    @BindView(R.id.bgcolorTag)
    TextView bgcolorTag;
    @BindView(R.id.colorsLL)
    LinearLayout colorsLL;
    @BindView(R.id.fileSizeTv)
    TextView fileSizeTv;
    @BindView(R.id.otherTv)
    TextView otherTv;
    @BindView(R.id.banner_normal)
    MZBannerView bannerNormal;
    @BindView(R.id.albumUpTv)
    TextView albumUpTv;
    @BindView(R.id.camaraUpTv)
    TextView camaraUpTv;

    private final static String KEY_DATA = "photoModelBean";

    public static void startPhotoStandardModelDetailActivity(Context ctx, Parcelable dataBean){
        Intent to = new Intent(ctx, PhotoStandardModelDetailActivity.class);
        to.putExtra(KEY_DATA,dataBean);
        if (!(ctx instanceof Activity)) {
            to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ctx.startActivity(to);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_photomodel_detail;
    }

    private NearHotBean getBeanData(){
        return getIntent().getExtras().getParcelable(KEY_DATA);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        titleBar.setTitle("规格详情");
        titleBar.addLeftImageButton(R.mipmap.back,R.id.goback);
        findViewById(R.id.goback).setOnClickListener(mainOnClickListener);

        initView();
        albumUpTv.setOnClickListener(mainOnClickListener);
        camaraUpTv.setOnClickListener(mainOnClickListener);
    }

    private void initView(){
        NearHotBean bean = getBeanData();
        if(bean != null){
            sizeModelTv.setText(bean.getPhotoModelName());
            standardSizeTv.setText(String.format("%1$d × %2$d mm",bean.getMmW(),bean.getMmH()));
            pxSizeTv.setText(String.format("%1$d × %2$d px",bean.getPxW(),bean.getPxH()));
            dpiTv.setText(String.format("%d DPI",bean.getDpi()));
            colorsLL.removeAllViews();
            LinearLayout.LayoutParams llp;
            View v;
            int dp10 = Utils.dp2px(this,10F);
            int dp13 = Utils.dp2px(this,13F);
            for(int colorBg : bean.getColors()){
                llp = new LinearLayout.LayoutParams(dp13,dp13);
                llp.leftMargin = dp10;
                v = new View(this);
                v.setBackgroundResource(colorBg);
                colorsLL.addView(v,llp);
            }
            fileSizeTv.setText(bean.getSizeLimit());
            otherTv.setText(bean.getOtherLimit());

            List<TopBannerBean> list = new ArrayList<>();
            TopBannerBean tipBean;
            int[] banners = new int[]{R.mipmap.tip1, R.mipmap.tip2,R.mipmap.tip3, R.mipmap.tip4,R.mipmap.tip5};
            for (int res : banners) {
                tipBean = new TopBannerBean();
                tipBean.setImgRes(res);
                list.add(tipBean);
            }
            bannerNormal.setPages(list, (MZHolderCreator<HomelHeaderView.BannerPaddingViewHolder>) () -> new HomelHeaderView.BannerPaddingViewHolder());
            bannerNormal.start();
        }
    }

    View.OnClickListener mainOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.goback:
                    finish();
                    break;
                case R.id.albumUpTv:
                    PictureUtils.openImagePicker((Activity) getContext(), false, true, 1);
                    break;
                case R.id.camaraUpTv:
                    PictureUtils.openImagePicker((Activity) getContext(), true, true, 1);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == Config.IMAGE_PICKER) {
                List<ImageItem> imgs = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (imgs != null && imgs.size() > 0) {
                    imgs.get(0).setKey(String.valueOf(System.currentTimeMillis()));
                    PhotoEditorActivity.startPhotoEditorActivity(this,imgs.get(0).path,getBeanData());
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == Config.REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {

                }
            }
        }
    }

}
