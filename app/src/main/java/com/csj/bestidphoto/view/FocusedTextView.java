package com.csj.bestidphoto.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @类名: FocusedTextView
 * @描述: 可滚动textview
 * @作者: Administrator
 * @日期: 2015-12-18 下午3:36:42
 */
public class FocusedTextView extends TextView {

    public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusedTextView(Context context) {
        super(context);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
