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

public class PhotoBgColorsBar extends FrameLayout {
    @BindView(R.id.commonXR)
    XRecycleView commonXR;

    private final String[] colorsName = new String[]{"红色","蓝色","白色"};
    private final String[] colorsValue = new String[]{"red","blue","white"};
    private final int[] bgDrawables = new int[]{R.drawable.bgcolor_red,R.drawable.bgcolor_blue,R.drawable.bgcolor_white};
    private int dp16 = Utils.dp2px(MApp.getInstance(),16F);

    public PhotoBgColorsBar(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public PhotoBgColorsBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PhotoBgColorsBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PhotoBgColorsBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View inflate = inflate(context, R.layout.v_photo_bg_bar, this);
        ButterKnife.bind(inflate);
        commonXR.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        initData();
    }

    private void initData(){
        List<BgColorBean> datas = new ArrayList<>();
        BgColorBean bean;
        for(int i = 0;i < colorsName.length;i++){
            bean = new BgColorBean();
            bean.setBgDrawable(bgDrawables[i]);
            bean.setColorName(colorsName[i]);
            bean.setColorValue(colorsValue[i]);
            if(i == 0){
                bean.setCheck(true);
            }
            datas.add(bean);
        }
        BgColorListAdapter adapter = new BgColorListAdapter(getContext());
        commonXR.setAdapter(adapter);
        adapter.setData(datas);
    }

    class BgColorBean{
        private int bgDrawable;
        private String colorName;
        private String colorValue;
        private boolean isCheck = false;

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getColorValue() {
            return colorValue;
        }

        public void setColorValue(String colorValue) {
            this.colorValue = colorValue;
        }

        public int getBgDrawable() {
            return bgDrawable;
        }

        public void setBgDrawable(int bgDrawable) {
            this.bgDrawable = bgDrawable;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }
    }

    class BgColorListAdapter extends BaseRecyclerAdapter<BgColorBean> {

        private Context context;

        public BgColorListAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public void bindView(RecyclerViewHolder holder, int position) {
            BgColorBean bean = getData().get(position);
            ImageView colorIv = holder.getView(R.id.colorIv);
            ImageView checkIv = holder.getView(R.id.checkIv);

            TextView colorNameTv = holder.getView(R.id.colorNameTv);
            if(bean.isCheck()){
                checkIv.setImageResource(R.mipmap.check_bgcolor);
            }else{
                checkIv.setImageBitmap(null);
            }
            colorIv.setBackgroundResource(bean.getBgDrawable());
            colorNameTv.setText(bean.getColorName());
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        };

        @Override
        public int getLayoutId(int viewType) {
            return R.layout.item_bgcolor;
        }

    }
}
