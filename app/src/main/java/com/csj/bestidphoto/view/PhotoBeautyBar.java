package com.csj.bestidphoto.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.csj.bestidphoto.MApp;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseRecyclerAdapter;
import com.csj.bestidphoto.base.RecyclerViewHolder;
import com.lamfire.utils.StringUtils;
import com.lzy.imagepicker.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoBeautyBar extends FrameLayout {
    @BindView(R.id.commonXR)
    XRecycleView commonXR;

    private final String[] beautyName = new String[]{"原图", "磨皮", "美白", "瘦脸", "眼睛"};
    private final String[] beautyType = new String[]{"", "Smooth_White-1", "Smooth_White-2", "ShapeType2", "ShapeType8"};//Smooth_White-1和Smooth_White-2都取Smooth_White
    private final int[] checkDrawables = new int[]{R.mipmap.bres_check, R.mipmap.bsmooth_check, R.mipmap.bwhite_check, R.mipmap.bthinface_check, R.mipmap.beye_check};
    private final int[] unCheckDrawables = new int[]{R.mipmap.bres, R.mipmap.bsmooth, R.mipmap.bwhite, R.mipmap.bthinface, R.mipmap.beye};
    @BindView(R.id.valueSb)
    SeekBar valueSb;
    @BindView(R.id.shapeSb)
    SeekBar shapeSb;
    @BindView(R.id.shapeRl)
    RelativeLayout shapeRl;
    private int dp16 = Utils.dp2px(MApp.getInstance(), 16F);
    private BeautyListAdapter adapter;

    private BeautyBean selectBean;
    private OnBeautyCheckListener mOnBeautyCheckListener;
    private float smoothValue = 0.5F;
    private float whiteValue = 0.5F;
    private float shapeValue = 0.5F;
    private float shortFaceValue = 0.5F;
    private float eyeValue = 0.5F;

    public void setmOnBeautyCheckListener(OnBeautyCheckListener mOnBeautyCheckListener) {
        this.mOnBeautyCheckListener = mOnBeautyCheckListener;
    }

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
        commonXR.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        initData();
        valueSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mOnBeautyCheckListener != null){
                    String value = "";
                    if(getSelectBean().getBeautyType().startsWith("Smooth_White")){
                        if(getSelectBean().getBeautyType().equals("Smooth_White-1")){
                            smoothValue = ((float) seekBar.getProgress()) / ((float) seekBar.getMax());
                        }else if(getSelectBean().getBeautyType().equals("Smooth_White-2")){
                            whiteValue = ((float) seekBar.getProgress()) / ((float) seekBar.getMax());
                        }

                        value = String.format("%1$.1f-%2$.1f-%3$.1f",smoothValue,whiteValue,shapeValue);
                    }else if(getSelectBean().getBeautyType().startsWith("ShapeType2")){
                        shortFaceValue = ((float) seekBar.getProgress()) / ((float) seekBar.getMax());
                        value = String.format("%.1f",shortFaceValue);
                    }else if(getSelectBean().getBeautyType().startsWith("ShapeType8")){
                        eyeValue = ((float) seekBar.getProgress()) / ((float) seekBar.getMax());
                        value = String.format("%.1f",eyeValue);
                    }
                    mOnBeautyCheckListener.doPhoto(getSelectBean().getBeautyType(),value);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
        shapeSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mOnBeautyCheckListener != null){
                    String value = "";
                    if(getSelectBean().getBeautyType().startsWith("Smooth_White")){
                        shapeValue = ((float) seekBar.getProgress()) / ((float) seekBar.getMax());
                        value = String.format("%1$.1f-%2$.1f-%3$.1f",smoothValue,whiteValue,shapeValue);
                    }
                    mOnBeautyCheckListener.doPhoto(getSelectBean().getBeautyType(),value);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
    }

    private void initData() {
        List<BeautyBean> datas = new ArrayList<>();
        BeautyBean bean;
        for (int i = 0; i < beautyName.length; i++) {
            bean = new BeautyBean();
            bean.setBeautyName(beautyName[i]);
            bean.setCheckLogo(checkDrawables[i]);
            bean.setUnCheckLogo(unCheckDrawables[i]);
            bean.setBeautyType(beautyType[i]);
            if (i == 0) {
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
                if(getSelectBean() != null && getSelectBean().beautyType.equals(((BeautyBean) data).beautyType)){

                }else{
                    setSelectBean((BeautyBean) data);
                    refreshMenus();
                    dealSeekBar(getSelectBean().getBeautyType());
                    if(mOnBeautyCheckListener != null){
                        String value = "";
                        if(getSelectBean().getBeautyType().startsWith("Smooth_White")){
                            if(getSelectBean().getBeautyType().equals("Smooth_White-1")){
                                valueSb.setProgress((int)(smoothValue * 100F));
                            }else if(getSelectBean().getBeautyType().equals("Smooth_White-2")){
                                valueSb.setProgress((int)(whiteValue * 100F));
                            }

                            value = String.format("%1$.1f-%2$.1f-%3$.1f",smoothValue,whiteValue,shapeValue);
                        }else if(getSelectBean().getBeautyType().startsWith("ShapeType2")){
                            valueSb.setProgress((int)(shortFaceValue * 100F));
                            value = String.format("%.1f",shortFaceValue);
                        }else if(getSelectBean().getBeautyType().startsWith("ShapeType8")){
                            valueSb.setProgress((int)(eyeValue * 100F));
                            value = String.format("%.1f",eyeValue);
                        }
                        mOnBeautyCheckListener.doPhoto(getSelectBean().getBeautyType(),value);
                    }
                }
            }
        });
    }

    private void dealSeekBar(String beautyType){
        if(StringUtils.isEmpty(beautyType)){
            valueSb.setVisibility(View.GONE);
            shapeRl.setVisibility(View.GONE);
        }else if(beautyType.startsWith("Smooth_White")){
            valueSb.setVisibility(View.VISIBLE);
            shapeRl.setVisibility(View.VISIBLE);
        }else{
            valueSb.setVisibility(View.VISIBLE);
            shapeRl.setVisibility(View.GONE);
        }
    }

    private void refreshMenus() {
        if (getSelectBean() != null) {
            for (BeautyBean bean : adapter.getData()) {
                if (bean.beautyType.equals(getSelectBean().beautyType)) {
                    bean.setCheck(true);
                } else {
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
            String[] type = beautyType.split("-");
            return type[0];
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
            checkIv.setBackgroundResource(bean.isCheck() ? R.drawable.beauty_check_bg : R.drawable.beauty_uncheck_bg);
            logoIv.setImageResource(bean.isCheck() ? bean.getCheckLogo() : bean.getUnCheckLogo());
            if(bean.isCheck()){
                setSelectBean(bean);
            }
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

    public interface OnBeautyCheckListener {
        void doPhoto(String type,String value);
    }
}
