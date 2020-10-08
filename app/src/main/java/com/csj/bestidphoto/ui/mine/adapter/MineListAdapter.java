package com.csj.bestidphoto.ui.mine.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.ui.mine.bean.MinePhotoBean;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MineListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private static final String TAG = MineListAdapter.class.getSimpleName();

    public MineListAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(MinePhotoBean.MINE_ITEM_TYPE_CONTENT, R.layout.item_minephoto);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MultiItemEntity multiItemEntity) {

        switch (multiItemEntity.getItemType()) {
            case MinePhotoBean.MINE_ITEM_TYPE_CONTENT:
                RoundedImageView headPhotoIv = holder.getView(R.id.headPhotoIv);
                TextView sizeTv = holder.getView(R.id.sizeTv);
                TextView sizeInfoTv = holder.getView(R.id.sizeInfoTv);
                break;
        }
    }
}