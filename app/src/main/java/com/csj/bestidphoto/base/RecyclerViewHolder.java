package com.csj.bestidphoto.base;

import android.util.SparseArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.csj.bestidphoto.view.ExpandableView.ExpandableViewHoldersUtil;

/**
 * Created by Administrator on 2017/1/16.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder implements ExpandableViewHoldersUtil.Expandable {
    private SparseArray<View> arrayView;
    private View expandTitle;
    private View expandView;
    private AniListener aniListener;

    public void setAniListener(AniListener aniListener) {
        this.aniListener = aniListener;
    }

    public AniListener getAniListener() {
        return aniListener;
    }

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        arrayView = new SparseArray<>();
    }

    public void setExpandView(View expandView) {
        this.expandView = expandView;
    }

    public void setExpandTitle(View expandTitle) {
        this.expandTitle = expandTitle;
    }

    public View getExpandTitle() {
        return expandTitle;
    }

    /**
     * 通过填写的itemId来获取具体的View的对象
     *
     * @param itemId R.id.***
     * @param <T>    必须是View的子类
     * @return
     */
    public <T extends View> T getView(int itemId) {
        //arrayVie类似于Map容器，get(key)取出的是value值
        View mView = arrayView.get(itemId);
        if (mView == null) {
            //实例化具体的View类型
            mView = itemView.findViewById(itemId);
            arrayView.put(itemId, mView);
        }
        return (T) mView;
    }

    @Override
    public View getExpandView() {
        return expandView;
    }

    public interface AniListener {
        void open();

        void close();
    }
}
