package com.csj.bestidphoto.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    public static final String TAG = BaseRecyclerAdapter.class.getClass().getSimpleName();
    private List<T> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public BaseRecyclerAdapter(Context context) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(getLayoutId(viewType), parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null && mDatas != null && mDatas.size() > position){
                    mOnItemClickListener.onItemClick(v,position,getItemData(position));
                }
            }
        });

        bindView(holder, position);
    }

    abstract public void bindView(RecyclerViewHolder holder, final int position);

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    @LayoutRes
    abstract public int getLayoutId(int viewType);

    /**
     * 获取上下文对象
     *
     * @return
     */
    public Context getContext() {
        return mContext;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 获取指定item数据
     *
     * @param position
     * @return
     */
    public T getItemData(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    /**
     * 获取数据集合
     *
     * @return
     */
    public List<T> getData() {
        return mDatas;
    }

    /**
     * 设置数据集合
     *
     * @param datas
     */
    public void setData(List<T> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * 移除指定item的数据
     *
     * @param position
     */
    public void removeData(int position) {
        this.mDatas.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 移除
     * @param o
     */
    public void removeData(T o) {
        this.mDatas.remove(o);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View item, int position, Object data);
    }
}
