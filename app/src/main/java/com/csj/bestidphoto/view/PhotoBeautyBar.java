package com.csj.bestidphoto.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.csj.bestidphoto.MApp;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseRecyclerAdapter;
import com.csj.bestidphoto.base.RecyclerViewHolder;
import com.lzy.imagepicker.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoBeautyBar extends FrameLayout {
    @BindView(R.id.commonXR)
    XRecycleView commonXR;

    private final String[] beautyName = new String[]{"原图","磨皮","美白","瘦脸","眼睛"};
    private final String[] beautyType = new String[]{"","Smooth_White","Smooth_White","ShapeType2","ShapeType8"};
    private final int[] checkDrawables = new int[]{R.mipmap.bres_check,R.mipmap.bsmooth_check,R.mipmap.bwhite_check,R.mipmap.bthinface_check,R.mipmap.beye_check};
    private final int[] unCheckDrawables = new int[]{R.mipmap.bres,R.mipmap.bsmooth,R.mipmap.bwhite,R.mipmap.bthinface,R.mipmap.beye};
    private int dp16 = Utils.dp2px(MApp.getInstance(),16F);
    private BeautyListAdapter adapter;

    private BeautyBean selectBean;

    public BeautyBean getSelectBean() {
        return selectBean;
    }

    public void setSelectBean(BeautyBean selectBean) {
        this.selectBean = selectBean;
    }

    public PhotoBeautyBar(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public PhotoBeautyBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PhotoBeautyBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PhotoBeautyBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View inflate = inflate(context, R.layout.v_photo_beauty_bar, this);
        ButterKnife.bind(inflate);
        commonXR.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        initData();
    }

    private void initData(){
        List<BeautyBean> datas = new ArrayList<>();
        BeautyBean bean;
        for(int i = 0; i < beautyName.length; i++){
            bean = new BeautyBean();
            bean.setBeautyName(beautyName[i]);
            bean.setCheckLogo(checkDrawables[i]);
            bean.setUnCheckLogo(unCheckDrawables[i]);
            bean.setBeautyType(beautyType[i]);
            if(i == 0){
                bean.setCheck(true);
            }
            datas.add(bean);
        }
        adapter = new BeautyListAdapter(getContext());
        commonXR.setAdapter(adapter);
        adapter.setData(datas);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position, Object data) {
                setSelectBean((BeautyBean)data);
                refreshMenus();
            }
        });
    }

    private void refreshMenus(){
        if(getSelectBean() != null){
            for(BeautyBean bean : adapter.getData()){
                if(bean.beautyType.equals(getSelectBean().beautyType)){
                    bean.setCheck(true);
                }else{
                    bean.setCheck(false);
                }
            }

            adapter.notifyDataSetChanged();
        }
    }

    class BeautyBean {
        private String beautyName;
        private String beautyType;
        private int checkLogo;
        private int unCheckLogo;
        private boolean isCheck = false;

        public String getBeautyType() {
            return beautyType;
        }

        public void setBeautyType(String beautyType) {
            this.beautyType = beautyType;
        }

        public int getCheckLogo() {
            return checkLogo;
        }

        public void setCheckLogo(int checkLogo) {
            this.checkLogo = checkLogo;
        }

        public int getUnCheckLogo() {
            return unCheckLogo;
        }

        public void setUnCheckLogo(int unCheckLogo) {
            this.unCheckLogo = unCheckLogo;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getBeautyName() {
            return beautyName;
        }

        public void setBeautyName(String beautyName) {
            this.beautyName = beautyName;
        }
    }

    class BeautyListAdapter extends BaseRecyclerAdapter<BeautyBean> {

        private Context context;

        public BeautyListAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public void bindView(RecyclerViewHolder holder, int position) {
            BeautyBean bean = getData().get(position);
            ImageView logoIv = holder.getView(R.id.logoIv);
            ImageView checkIv = holder.getView(R.id.checkIv);

            TextView beautyNameTv = holder.getView(R.id.beautyNameTv);
            checkIv.setBackgroundResource(bean.isCheck()?R.drawable.beauty_check_bg:R.drawable.beauty_uncheck_bg);
            logoIv.setImageResource(bean.isCheck()?bean.getCheckLogo():bean.getUnCheckLogo());
            beautyNameTv.setText(bean.getBeautyName());
        }

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        };

        @Override
        public int getLayoutId(int viewType) {
            return R.layout.item_beauty;
        }

    }
}
