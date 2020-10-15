package com.csj.bestidphoto.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseActivity;
import com.csj.bestidphoto.comm.Config;
import com.csj.bestidphoto.ui.home.adapter.NearHotListAdapter;
import com.csj.bestidphoto.ui.home.bean.NearHotBean;
import com.csj.bestidphoto.utils.PictureUtils;
import com.csj.bestidphoto.view.XRecycleView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AllPhotoModelListActivity extends BaseActivity {
    @BindView(R.id.commonXR)
    XRecycleView commonXR;
    @BindView(R.id.titleBar)
    QMUITopBar titleBar;

    private NearHotListAdapter adapter;
    private NearHotBean currentBean;


    public static void startAllPhotoModelListActivity(Context ctx) {
        Intent to = new Intent(ctx, AllPhotoModelListActivity.class);
        if (!(ctx instanceof Activity)) {
            to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ctx.startActivity(to);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_allphoto_model;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        titleBar.setTitle("尺寸列表");
        titleBar.addLeftImageButton(R.mipmap.back, R.id.goback);
        findViewById(R.id.goback).setOnClickListener(mainOnClickListener);

        test();
    }

    View.OnClickListener mainOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.goback:
                    finish();
                    break;
            }
        }
    };

    private void test(){
        String[] names = new String[]{"一寸","二寸","小一寸","小二寸","英语四六级考试","大一寸","学籍照片"};
        int[] pxW = new int[]{295,413,260,413,144,390,307};
        int[] pxH = new int[]{413,579,378,513,192,567,378};
        int[] mmW = new int[]{25,35,22,35,12,33,26};
        int[] mmH = new int[]{35,49,32,45,16,48,32};
        List<int[]> colors = new ArrayList<>();
        colors.add(new int[]{R.drawable.photo_bg_red,R.drawable.photo_bg_blue,R.drawable.photo_bg_white});
        colors.add(new int[]{R.drawable.photo_bg_red,R.drawable.photo_bg_blue,R.drawable.photo_bg_white});
        colors.add(new int[]{R.drawable.photo_bg_red,R.drawable.photo_bg_blue,R.drawable.photo_bg_white});
        colors.add(new int[]{R.drawable.photo_bg_red,R.drawable.photo_bg_blue,R.drawable.photo_bg_white});
        colors.add(new int[]{R.drawable.photo_bg_red,R.drawable.photo_bg_blue,R.drawable.photo_bg_white});
        colors.add(new int[]{R.drawable.photo_bg_red,R.drawable.photo_bg_blue,R.drawable.photo_bg_white});
        colors.add(new int[]{R.drawable.photo_bg_red,R.drawable.photo_bg_blue,R.drawable.photo_bg_white});
        String[] sizeLimit = new String[]{"无要求","无要求","无要求","无要求","10KB以下","无要求","60KB以下"};
        String[] otherLimit = new String[]{
                "免冠, 照片可看见两耳轮廊和相当于男式喉结处的地方",
                "免冠, 照片可看见两耳轮廊和相当于男式喉结处的地方",
                "免冠, 照片可看见两耳轮廊和相当于男式喉结处的地方",
                "免冠, 照片可看见两耳轮廊和相当于男式喉结处的地方",
                "免冠, 成像区上下要求头上部空1/10, 头部占7/10, 肩部占1/5, 左右各空1/10.",
                "免冠, 照片可看见两耳轮廊和相当于男式喉结处的地方",
                "人像在相片矩形框内水平居中, 头部占照片尺寸的2/3, 常戴眼镜的学生应配戴眼镜, 人像清晰, 层次丰富, 神态自然, 无明显畸变."};
        List<MultiItemEntity> list = new ArrayList<>();
        NearHotBean bean;
        for(int i = 0; i < names.length; i++){
            bean = new NearHotBean();
            bean.setPhotoModelName(names[i]);
            bean.setPxW(pxW[i]);
            bean.setPxH(pxH[i]);
            bean.setMmW(mmW[i]);
            bean.setMmH(mmH[i]);
            bean.setSizeLimit(sizeLimit[i]);
            bean.setOtherLimit(otherLimit[i]);
            bean.setColors(colors.get(i));
            bean.setItemType(NearHotBean.HOME_ITEM_TYPE_CONTENT);
            list.add(bean);
        }

        adapter = new NearHotListAdapter(list);
        adapter.setRvChildClickListener(rvChildClickListener);
        commonXR.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                MultiItemEntity bean = (MultiItemEntity)adapter.getData().get(position);
                if(bean.getItemType() == NearHotBean.HOME_ITEM_TYPE_CONTENT){
                    PhotoStandardModelDetailActivity.startPhotoStandardModelDetailActivity(AllPhotoModelListActivity.this,(NearHotBean)bean);
                }
            }
        });
    }

    View.OnClickListener rvChildClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            currentBean = (NearHotBean)v.getTag();
            PictureUtils.openImagePicker((Activity) getContext(), true, true, currentBean.getPxW(),currentBean.getPxH(),1);
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
                    PhotoEditorActivity.startPhotoEditorActivity(this,imgs.get(0).path,currentBean);
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
