package com.csj.bestidphoto;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.csj.bestidphoto.ad.TTAdManagerHolder;
import com.csj.bestidphoto.base.BaseActivity;
import com.csj.bestidphoto.comm.Config;
import com.csj.bestidphoto.ui.PhotoEditorActivity;
import com.csj.bestidphoto.ui.home.HomeViewModel;
import com.csj.bestidphoto.ui.home.bean.NearHotBean;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.maoti.lib.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private HomeViewModel viewModel;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_mine).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);//全屏样式需屏蔽,不然会空指针
        NavigationUI.setupWithNavController(navView, navController);

        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.getPhotoModel().observe(this, new Observer<NearHotBean>() {
            @Override
            public void onChanged(@Nullable NearHotBean data) {
                //刷新UI
                LogUtil.i(TAG,"MainActivity HomeViewModel");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == Config.IMAGE_PICKER) {
                List<ImageItem> imgs = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (imgs != null && imgs.size() > 0) {
                    imgs.get(0).setKey(String.valueOf(System.currentTimeMillis()));
                    PhotoEditorActivity.startPhotoEditorActivity(this,imgs.get(0).path,viewModel.getPhotoModel().getValue());
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
