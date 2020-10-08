package com.csj.bestidphoto.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * @类名: FocusedCheckBox
 * @描述: 可滚动CheckBox
 * @作者: Administrator
 * @日期: 2015-12-18 下午3:36:42
 */
public class FocusedCheckBox extends CheckBox {

    public FocusedCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FocusedCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusedCheckBox(Context context) {
        super(context);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
