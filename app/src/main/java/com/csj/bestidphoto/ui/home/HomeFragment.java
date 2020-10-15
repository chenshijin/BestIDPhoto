package com.csj.bestidphoto.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseFragment;
import com.csj.bestidphoto.ui.AllPhotoModelListActivity;
import com.csj.bestidphoto.ui.PhotoEditorActivity;
import com.csj.bestidphoto.ui.PhotoStandardModelDetailActivity;
import com.csj.bestidphoto.ui.home.adapter.NearHotListAdapter;
import com.csj.bestidphoto.ui.home.bean.NearHotBean;
import com.csj.bestidphoto.utils.PictureUtils;
import com.csj.bestidphoto.view.XRecycleView;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.titleBar)
    QMUITopBar titleBar;
    @BindView(R.id.homeRv)
    XRecycleView homeRv;
    private HomeViewModel homeViewModel;

    private NearHotListAdapter adapter;
    private HomelHeaderView homelHeaderView;

    @Override
    public int getContentViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    public View initView(View contentView, @Nullable Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(requireActivity()).get(HomeViewModel.class);//new ViewModelProvider(requireActivity()).get(BallGameViewModel.class);
        homelHeaderView = new HomelHeaderView(requireActivity());

        titleBar.setTitle("主页");
        test();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                MultiItemEntity bean = (MultiItemEntity)adapter.getData().get(position);
                if(bean.getItemType() == NearHotBean.HOME_ITEM_TYPE_CONTENT){
                    PhotoStandardModelDetailActivity.startPhotoStandardModelDetailActivity(requireActivity(),(NearHotBean)bean);
                }
            }
        });

        homelHeaderView.setPhotoEditorListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearHotBean photoModel = new NearHotBean(1);
                photoModel.setPhotoModelName("自定义-" + System.currentTimeMillis());
                photoModel.setPxW(335);
                photoModel.setPxH(453);
                photoModel.setDpi(300);
                homeViewModel.setPhotoModel(photoModel);
            }
        });
        return null;
    }

    private void test(){
        String[] names = new String[]{"一寸","二寸","小一寸","小二寸"};
        int[] pxW = new int[]{295,413,260,413};
        int[] pxH = new int[]{413,579,378,513};
        int[] mmW = new int[]{25,35,22,35};
        int[] mmH = new int[]{35,49,32,45};
        List<int[]> colors = new ArrayList<>();
        colors.add(new int[]{R.drawable.photo_bg_red,R.drawable.photo_bg_blue,R.drawable.photo_bg_white});
        colors.add(new int[]{R.drawable.photo_bg_red,R.drawable.photo_bg_blue,R.drawable.photo_bg_white});
        colors.add(new int[]{R.drawable.photo_bg_red,R.drawable.photo_bg_blue,R.drawable.photo_bg_white});
        colors.add(new int[]{R.drawable.photo_bg_red,R.drawable.photo_bg_blue,R.drawable.photo_bg_white});
        String[] sizeLimit = new String[]{"无要求","无要求","无要求","无要求"};
        String[] otherLimit = new String[]{
                "免冠, 照片可看见两耳轮廊和相当于男式喉结处的地方",
                "免冠, 照片可看见两耳轮廊和相当于男式喉结处的地方",
                "免冠, 照片可看见两耳轮廊和相当于男式喉结处的地方",
                "免冠, 照片可看见两耳轮廊和相当于男式喉结处的地方"};
        List<MultiItemEntity> list = new ArrayList<>();
        NearHotBean bean;
        bean = new NearHotBean();
        bean.setItemType(NearHotBean.HOME_ITEM_TYPE_TITLE);
        list.add(bean);
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
        adapter.addHeaderView(homelHeaderView);
        homeRv.setAdapter(adapter);
    }

    View.OnClickListener rvChildClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            NearHotBean bean = (NearHotBean)v.getTag();
            homeViewModel.setPhotoModel(bean);
            PictureUtils.openImagePicker((Activity) getContext(), true, true, bean.getPxW(),bean.getPxH(),1);
        }
    };
}
