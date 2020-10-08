package com.csj.bestidphoto.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csj.bestidphoto.R;
import com.maoti.lib.utils.Utils;
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLoadMoreView;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullRefreshView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XRecycleView extends LinearLayout {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_layout)
    QMUIPullLayout pullLayout;
    @BindView(R.id.pull_refresh_view)
//    BallRefreshHeader pullRefreshView;
    QMUIPullRefreshView pullRefreshView;
    @BindView(R.id.pull_loadmore_view)
    QMUIPullLoadMoreView pullLoadmoreView;
    private Context context;
    private boolean openSwipeAction = false;
    private PullListener pullListener;
    private SwipeListener swipeListener;
    private QMUIPullLayout.PullAction refreshAction;
    private QMUIPullLayout.PullAction loadMoreAction;
    private int dp50;


    public XRecycleView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public XRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public XRecycleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    public XRecycleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyle) {
        View inflate = inflate(context, R.layout.v_xrecycleview, this);
        ButterKnife.bind(inflate);
        dp50 = Utils.dipToPx(context, 30F);
        if (attrs != null) {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.XRecycleView, defStyle, 0);

            int pullEdge = a.getInt(R.styleable.XRecycleView_pullEdge, -1);
            if (pullEdge != -1 && pullEdge != 0) {
                pullLayout.setEnabledEdges(pullEdge);
            } else if (pullEdge == 0) {
                pullLayout.setEnabledEdges(QMUIPullLayout.PUL_EDGE_ALL);
            } else {
                pullLayout.setEnabledEdges(QMUIPullLayout.PULL_EDGE_LEFT);//暂时用这个表示没有上下拉动作
            }

            boolean swipeEable = a.getBoolean(R.styleable.XRecycleView_swipeEable, false);
            setOpenSwipeAction(swipeEable);//开启侧滑菜单

            a.recycle();
        }

//        pullRefreshView.setSize(CircularProgressDrawable.DEFAULT);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pullLayout.setActionListener(new QMUIPullLayout.ActionListener() {
            @Override
            public void onActionTriggered(@NonNull QMUIPullLayout.PullAction pullAction) {
                pullLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_TOP) {//刷新
                            if (pullListener != null) {
                                refreshAction = pullAction;
                                pullListener.onRefresh(pullAction);
                            }
                        } else if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_BOTTOM) {//加载更多
                            if (pullListener != null) {
                                loadMoreAction = pullAction;
                                pullListener.onLoadMore(pullAction);
                            }
                        }

                    }
                }, 100);
            }
        });

    }

    public void setHasNoMore() {
        pullLoadmoreView.setHasNoMore();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.addItemDecoration(itemDecoration);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setRecyclerViewBackgroundColor(int color) {
        pullLayout.setBackgroundColor(color);
        recyclerView.setBackgroundColor(color);
    }

//    public void autoRefresh() {
//        try {
//            pullRefreshView.onActionTriggered();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    int[] location = new int[2];
//                    pullLayout.getLocationInWindow(location);
//                    int x = location[0]; // view距离window 左边的距离（即x轴方向）
//                    int y = location[1];
//
//                    SystemClock.sleep(500);
//                    pullLayout.onNestedScroll(recyclerView,0,0,x,y);
//                    SystemClock.sleep(50);
//                    pullLayout.onNestedScroll(recyclerView,0,0,x,y-dp50);
//                    SystemClock.sleep(50);
//                    pullLayout.onNestedScroll(recyclerView,0,0,x,y-(dp50*2));
//                    SystemClock.sleep(50);
//                    pullLayout.onNestedScroll(recyclerView,0,0,x,y-(dp50*3));
//                    SystemClock.sleep(50);
//
//                }
//            }).start();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 设置拉动方式
     *
     * @param PUL_EDGE
     */
    public void setEnabledEdges(int PUL_EDGE) {
        pullLayout.setEnabledEdges(PUL_EDGE);
    }

    /**
     * 关掉各种动作
     *
     * @param pullAction
     */
    public void finishActionRun(QMUIPullLayout.PullAction pullAction) {
        pullLayout.finishActionRun(pullAction);
    }

    /**
     * 关掉所有动作
     */
    public void finishAllActionsRun() {
        if (refreshAction != null) {
            finishActionRun(refreshAction);
        }
        if (loadMoreAction != null) {
            finishActionRun(loadMoreAction);
        }
    }

    public void doRefresh(){
        pullLayout.doRefresh();
    }

    /**
     * 设置侧滑菜单点击回调
     *
     * @param swipeListener
     */
    public void setSwipeListener(SwipeListener swipeListener) {
        this.swipeListener = swipeListener;
    }

    /**
     * 设置拉动回调
     *
     * @param pullListener
     */
    public void setPullListener(PullListener pullListener) {
        this.pullListener = pullListener;
    }

    /**
     * 开启侧滑菜单
     *
     * @param openSwipeAction
     */
    public void setOpenSwipeAction(boolean openSwipeAction) {
        this.openSwipeAction = openSwipeAction;
        if (openSwipeAction) {
            swipeAction.attachToRecyclerView(recyclerView);
        } else {
            swipeAction.attachToRecyclerView(null);
        }
    }

    QMUIRVItemSwipeAction swipeAction = new QMUIRVItemSwipeAction(true, new QMUIRVItemSwipeAction.Callback() {
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            mAdapter.remove(viewHolder.getAdapterPosition());//可在此做滑动删除
            if (swipeListener != null) {
                swipeListener.onSwiped(viewHolder, direction);
            }
        }

        @Override
        public int getSwipeDirection(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return QMUIRVItemSwipeAction.SWIPE_LEFT;//滑动方向
        }

        @Override
        public void onClickAction(QMUIRVItemSwipeAction swipeAction, RecyclerView.ViewHolder selected, QMUISwipeAction action) {
            super.onClickAction(swipeAction, selected, action);
            if (swipeListener != null) {
                swipeListener.onClickAction(swipeAction, selected, action);
            }
//            Toast.makeText(getContext(),"你点击了第 " + selected.getAdapterPosition() + " 个 item 的" + action.getText(),Toast.LENGTH_SHORT).show();
//            if(mAdapter.mAction1 == action){
//                mAdapter.remove(selected.getAdapterPosition());
//            }else{
//                swipeAction.clear();
//            }
        }
    });

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public interface PullListener {
        void onRefresh(QMUIPullLayout.PullAction pullAction);

        void onLoadMore(QMUIPullLayout.PullAction pullAction);
    }

    public interface SwipeListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);

        void onClickAction(QMUIRVItemSwipeAction swipeAction, RecyclerView.ViewHolder selected, QMUISwipeAction action);
    }

//    class Adapter extends RecyclerView.Adapter<QMUISwipeViewHolder> {
//
//        private List<String> mData = new ArrayList<>();
//        private List<QMUISwipeAction> actions = new ArrayList<>();
//
////        final QMUISwipeAction mAction1;
////        final QMUISwipeAction mAction2;
////        final QMUISwipeAction mAction3;
////        final QMUISwipeAction mAction4;
//
//        public Adapter(Context context) {
////            QMUISwipeAction.ActionBuilder builder = new QMUISwipeAction.ActionBuilder()
////                    .textSize(QMUIDisplayHelper.sp2px(context, 14))
////                    .textColor(Color.WHITE)
////                    .paddingStartEnd(QMUIDisplayHelper.dp2px(getContext(), 14));
////
////            mAction1 = builder
////                    .text("删除")
////                    .backgroundColor(Color.RED)
////                    .icon(ContextCompat.getDrawable(context, R.drawable.icon_quick_action_delete_line))
////                    .orientation(QMUISwipeAction.ActionBuilder.VERTICAL)
////                    .reverseDrawOrder(false)
////                    .build();
////            mAction2 = builder
////                    .text("查词典")
////                    .backgroundColor(Color.BLUE)
////                    .icon(ContextCompat.getDrawable(context, R.drawable.icon_quick_action_dict))
////                    .orientation(QMUISwipeAction.ActionBuilder.VERTICAL)
////                    .reverseDrawOrder(true)
////                    .build();
////            mAction3 = builder
////                    .text("分享")
////                    .backgroundColor(Color.BLACK)
////                    .icon(ContextCompat.getDrawable(context, R.drawable.icon_quick_action_share))
////                    .orientation(QMUISwipeAction.ActionBuilder.HORIZONTAL)
////                    .reverseDrawOrder(false)
////                    .build();
////            mAction4 = builder
////                    .text("复制")
////                    .backgroundColor(Color.GRAY)
////                    .icon(ContextCompat.getDrawable(context, R.drawable.icon_quick_action_copy))
////                    .orientation(QMUISwipeAction.ActionBuilder.HORIZONTAL)
////                    .reverseDrawOrder(true)
////                    .build();
//        }
//
//        public void setActions(List<QMUISwipeAction> actions) {
//            this.actions = actions;
//        }
//
//        public void setData(@Nullable List<String> list) {
//            mData.clear();
//            if (list != null) {
//                mData.addAll(list);
//            }
//            notifyDataSetChanged();
//        }
//
//        public void remove(int pos) {
//            mData.remove(pos);
//            notifyItemRemoved(pos);
//        }
//
//        public void add(int pos, String item) {
//            mData.add(pos, item);
//            notifyItemInserted(pos);
//        }
//
//        public void prepend(@NonNull List<String> items) {
//            mData.addAll(0, items);
//            notifyDataSetChanged();
//        }
//
//        public void append(@NonNull List<String> items) {
//            mData.addAll(items);
//            notifyDataSetChanged();
//        }
//
//        @NonNull
//        @Override
//        public QMUISwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lay_xrecycleview_action, parent, false);
//            final QMUISwipeViewHolder vh = new QMUISwipeViewHolder(view);
//            for (QMUISwipeAction action : actions) {
//                vh.addSwipeAction(action);
//            }
////            vh.addSwipeAction(mAction1);
////            vh.addSwipeAction(mAction2);
////            vh.addSwipeAction(mAction3);
////            vh.addSwipeAction(mAction4);
//            view.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Toast.makeText(getContext(),"click position=" + vh.getAdapterPosition(),Toast.LENGTH_SHORT).show();
//                }
//            });
//            return vh;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull QMUISwipeViewHolder holder, int position) {
//            TextView textView = holder.itemView.findViewById(R.id.text);
//            textView.setText(mData.get(position));
//        }
//
//        @Override
//        public int getItemCount() {
//            return mData.size();
//        }
//    }
}
