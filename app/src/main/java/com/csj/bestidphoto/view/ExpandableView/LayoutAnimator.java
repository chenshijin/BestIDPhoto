package com.csj.bestidphoto.view.ExpandableView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

public class LayoutAnimator {

    public static class LayoutHeightUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private final View view;

        public LayoutHeightUpdateListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            final ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = (int) animation.getAnimatedValue();
            view.setLayoutParams(lp);
        }

    }

    public static Animator ofHeight(View view, int start, int end) {
        final ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new LayoutHeightUpdateListener(view));
        return animator;
    }
}
