package com.csj.bestidphoto.view.ExpandableView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.csj.bestidphoto.base.RecyclerViewHolder;

public class ExpandableViewHoldersUtil {

    public static void openH(final RecyclerView.ViewHolder holder, final View expandView, final boolean animate) {
        if (animate) {
            expandView.setVisibility(View.VISIBLE);
            final Animator animator = ViewHolderAnimator.ofItemViewHeight(holder);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(expandView, View.ALPHA, 1);
                    alphaAnimator.addListener(new ViewHolderAnimator.ViewHolderAnimatorListener(holder));
                    alphaAnimator.start();
                    if(holder instanceof RecyclerViewHolder){
                        if(((RecyclerViewHolder)holder).getAniListener() != null){
                            ((RecyclerViewHolder)holder).getAniListener().open();
                        }
                    }
                }
            });
            animator.start();
        }else {
            expandView.setVisibility(View.VISIBLE);
            expandView.setAlpha(1);
            if(holder instanceof RecyclerViewHolder){
                if(((RecyclerViewHolder)holder).getAniListener() != null){
                    ((RecyclerViewHolder)holder).getAniListener().open();
                }
            }
        }
    }

    public static void closeH(final RecyclerView.ViewHolder holder, final View expandView, final boolean animate) {
        if (animate) {
            expandView.setVisibility(View.GONE);
            final Animator animator = ViewHolderAnimator.ofItemViewHeight(holder);
            expandView.setVisibility(View.VISIBLE);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    expandView.setVisibility(View.GONE);
                    expandView.setAlpha(0);
                    if(holder instanceof RecyclerViewHolder){
                        if(((RecyclerViewHolder)holder).getAniListener() != null){
                            ((RecyclerViewHolder)holder).getAniListener().close();
                        }
                    }
                }
                @Override
                public void onAnimationCancel(Animator animation) {
                    expandView.setVisibility(View.GONE);
                    expandView.setAlpha(0);
                    if(holder instanceof RecyclerViewHolder){
                        if(((RecyclerViewHolder)holder).getAniListener() != null){
                            ((RecyclerViewHolder)holder).getAniListener().close();
                        }
                    }
                }
            });
            animator.start();
        }else {
            expandView.setVisibility(View.GONE);
            expandView.setAlpha(0);
            if(holder instanceof RecyclerViewHolder){
                if(((RecyclerViewHolder)holder).getAniListener() != null){
                    ((RecyclerViewHolder)holder).getAniListener().close();
                }
            }
        }
    }

    public static interface Expandable {
        public View getExpandView();
    }

    public static class KeepOneH<VH extends RecyclerView.ViewHolder & Expandable> {
        private int opened = -1;

        public void bind(VH holder, int pos) {
            if (pos == opened)
                ExpandableViewHoldersUtil.openH(holder, holder.getExpandView(), false);
            else
                ExpandableViewHoldersUtil.closeH(holder, holder.getExpandView(), false);
        }

        @SuppressWarnings("unchecked")
        public void toggle(VH holder) {//false - 收起，true - 展开
            if (opened == holder.getPosition()) {
                opened = -1;
                ExpandableViewHoldersUtil.closeH(holder, holder.getExpandView(), true);
            }else {
                int previous = opened;
                opened = holder.getPosition();
                ExpandableViewHoldersUtil.openH(holder, holder.getExpandView(), true);

                final VH oldHolder = (VH) ((RecyclerView) holder.itemView.getParent()).findViewHolderForPosition(previous);
                if (oldHolder != null){
                    ExpandableViewHoldersUtil.closeH(oldHolder, oldHolder.getExpandView(), true);
                }
            }
        }
    }

}
