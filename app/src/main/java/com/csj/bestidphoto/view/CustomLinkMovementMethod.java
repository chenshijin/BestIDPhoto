package com.csj.bestidphoto.view;

import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

/**
 * Created by Administrator on 2019/3/28.
 */

public class CustomLinkMovementMethod extends LinkMovementMethod {

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        boolean b = super.onTouchEvent(widget,buffer,event);
        //解决点击事件冲突问题
        if(!b && event.getAction() == MotionEvent.ACTION_UP){
//            ViewParent parent = widget.getParent();//处理widget的父控件点击事件
//            if (parent instanceof ViewGroup) {
//                return ((ViewGroup) parent).performClick();
//            }
            return doClick(widget,b);
        }
        return b;
    }

    private boolean doClick(View v, boolean def){
        ViewParent parent = v.getParent();//处理widget的父控件点击事件
        if (parent != null && parent instanceof ViewGroup) {
            boolean hasClick = v.performClick();
            if(hasClick){
                return hasClick;
            }else{
                return doClick((View)parent,def);
            }
        }

        return def;
    }

    public static CustomLinkMovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new CustomLinkMovementMethod();

        return sInstance;
    }


    private static CustomLinkMovementMethod sInstance;

}
