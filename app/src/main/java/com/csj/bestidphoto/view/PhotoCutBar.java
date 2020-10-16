package com.csj.bestidphoto.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.csj.bestidphoto.R;
import com.csj.bestidphoto.base.BaseRecyclerAdapter;
import com.csj.bestidphoto.base.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoCutBar extends FrameLayout {
    @BindView(R.id.commonXR)
    XRecycleView commonXR;

    private final String[] cutName = new String[]{"一寸","二寸","小一寸","小二寸","大一寸","英语四六级考试","学籍照片"};
    private final int[] cutWList = new int[]{295,413,260,413,390,144,307};
    private final int[] cutHList = new int[]{413,579,378,513,567,192,378};
    private BeautyListAdapter adapter;
    private int cutW = 335;
    private int cutH = 453;

    private OnCutCheckListener mOnCutCheckListener;
    private CutMenuBean selectBean;

    public int getCutW() {
        return cutW;
    }

    public void setCutW(int cutW) {
        this.cutW = cutW;
    }

    public int getCutH() {
        return cutH;
    }

    public void setCutH(int cutH) {
        this.cutH = cutH;
    }

    public void setmOnCutCheckListener(OnCutCheckListener mOnCutCheckListener) {
        this.mOnCutCheckListener = mOnCutCheckListener;
    }

    public CutMenuBean getSelectBean() {
        return selectBean;
    }

    public void setSelectBean(CutMenuBean selectBean) {
        this.selectBean = selectBean;
    }

    public PhotoCutBar(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public PhotoCutBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PhotoCutBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PhotoCutBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View inflate = inflate(context, R.layout.v_photo_cut_bar, this);
        ButterKnife.bind(inflate);
        commonXR.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        initData();
    }

    private void initData(){
        List<CutMenuBean> datas = new ArrayList<>();
        CutMenuBean bean;
        for(int i = 0; i < cutName.length; i++){
            bean = new CutMenuBean();
            bean.setCutName(cutName[i]);
            bean.setCutW(cutWList[i]);
            bean.setCutH(cutHList[i]);
            datas.add(bean);
        }
        adapter = new BeautyListAdapter(getContext());
        commonXR.setAdapter(adapter);
        adapter.setData(datas);
    }

    private void refreshMenus(){
        if(getSelectBean() != null){
            for(CutMenuBean bean : adapter.getData()){
                if(bean.cutName.equals(getSelectBean().cutName)){
                    bean.setCheck(true);
                }else{
                    bean.setCheck(false);
                }
            }

            adapter.notifyDataSetChanged();
        }
    }

    class CutMenuBean {
        private String cutName;
        private int cutW;
        private int cutH;
        private boolean isCheck = false;

        public int getCutH() {
            return cutH;
        }

        public void setCutH(int cutH) {
            this.cutH = cutH;
        }

        public int getCutW() {
            return cutW;
        }

        public void setCutW(int cutW) {
            this.cutW = cutW;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getCutName() {
            return cutName;
        }

        public void setCutName(String cutName) {
            this.cutName = cutName;
        }
    }

    class BeautyListAdapter extends BaseRecyclerAdapter<CutMenuBean> {

        private Context context;

        public BeautyListAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public void bindView(RecyclerViewHolder holder, int position) {
            CutMenuBean bean = getData().get(position);
            FocusedCheckBox cutNameCb = holder.getView(R.id.cutNameCb);
            TextView cutNameTv = holder.getView(R.id.cutNameTv);

            cutNameCb.setChecked(bean.isCheck());
            cutNameCb.setText(bean.getCutName());
            cutNameTv.setText(bean.getCutW() + "*" + bean.getCutH() + "px");
            cutNameCb.setOnClickListener(listener);
            cutNameCb.setTag(bean);
        }

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                CutMenuBean bean = (CutMenuBean)v.getTag();
                setSelectBean(bean);
                refreshMenus();
                setCutW(bean.getCutW());
                setCutH(bean.getCutH());
                if(mOnCutCheckListener != null){
                    mOnCutCheckListener.onCutCheck(bean.getCutName(),bean.getCutW(),bean.getCutH());
                }
            }
        };

        @Override
        public int getLayoutId(int viewType) {
            return R.layout.item_cut_menu;
        }

    }

    public interface OnCutCheckListener{
        void onCutCheck(String photoModelName,int w,int h);
    }
}
