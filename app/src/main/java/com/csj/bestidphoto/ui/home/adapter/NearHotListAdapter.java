package com.csj.bestidphoto.ui.home.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.ui.home.bean.NearHotBean;
import com.csj.bestidphoto.utils.PictureUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NearHotListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private static final String TAG = NearHotListAdapter.class.getSimpleName();

    private View.OnClickListener rvChildClickListener;

    public void setRvChildClickListener(View.OnClickListener rvChildClickListener) {
        this.rvChildClickListener = rvChildClickListener;
    }

    public NearHotListAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(NearHotBean.HOME_ITEM_TYPE_TITLE, R.layout.item_home_title);
        addItemType(NearHotBean.HOME_ITEM_TYPE_CONTENT, R.layout.item_nearhot);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MultiItemEntity multiItemEntity) {

        switch (multiItemEntity.getItemType()) {
            case NearHotBean.HOME_ITEM_TYPE_CONTENT:
                NearHotBean bean = (NearHotBean)multiItemEntity;
                TextView sizeTv = holder.getView(R.id.sizeTv);
                TextView sizeInfoTv = holder.getView(R.id.sizeInfoTv);
                TextView docamaraTv = holder.getView(R.id.docamaraTv);

                sizeTv.setText(bean.getPhotoModelName());
                if(bean.getModel() == 2){
                    sizeInfoTv.setText(String.format("%1$d × %2$d px | %3$d × %4$d mm",bean.getPxW(),bean.getPxH(),bean.getMmW(),bean.getMmH()));
                }else{
                    sizeInfoTv.setText(String.format("%1$d × %2$d px",bean.getPxW(),bean.getPxH()));
                }
                docamaraTv.setOnClickListener(rvChildClickListener);
                docamaraTv.setTag(bean);
                break;
        }
    }


}
