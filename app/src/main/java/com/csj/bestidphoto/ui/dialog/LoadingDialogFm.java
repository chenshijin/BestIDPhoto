package com.csj.bestidphoto.ui.dialog;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.csj.bestidphoto.R;

import java.util.Calendar;

/**
 * Created by kenny on 2020/5/23.
 * Loading 提示
 */
public class LoadingDialogFm extends BaseDialogFragment {

    private static final String TAG = "SkinSettingDialogFm";
    private TextView msgtext;
    private ImageView animIv;

    private AnimationDrawable animationDrawable;
    public String Msg = "";
    long lastClickTime = Calendar.getInstance().getTimeInMillis();
    public static final int MIN_CLICK_DELAY_TIME = 300;
    private int tagSec = 1;//多次显示标识

    public int getTagSec() {
        return tagSec;
    }

    public void setTagSec(int tagSec) {
        this.tagSec = tagSec;
    }

    public LoadingDialogFm(String msg){
        this.Msg = msg;
    }

    @Override
    public int getContentViewResId() {
      int view = 0;
//        if (Build.VERSION.SDK_INT >= 23) {
            view = R.layout.dialog_loading_layout;
//        } else {
//            view = R.layout.dialog_loading_layout2;
//        }
        return view;
    }

    @Override
    public int getCloseBtnId() {
        return R.id.msgtext;
    }

    @Override
    public String getLocation() {
        return CENTER;
    }

    public boolean canClose(){
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime=currentTime;
            return true;
        }
         return false;
    }




    @Override
    public View initData(View view) {
        msgtext = view.findViewById(R.id.msgtext);
        animIv = view.findViewById(R.id.animIv);

        animIv.setImageResource(R.drawable.progress_drawable_white2);
        animationDrawable = (AnimationDrawable) animIv.getDrawable();
        // 2. 获取动画对象
        animationDrawable.start();
        // 3. 启动动画

        if(Msg == null || Msg.length()==0){
            msgtext.setVisibility(View.GONE);
        }else{
            msgtext.setVisibility(View.VISIBLE);
            msgtext.setText(Msg);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void dismiss() {
        super.dismiss();
//        LogUtil.i(TAG,"进度框释放内存");
//        tryRecycleAnimationDrawable(animationDrawable);//由于偶尔出现菊花显示不了，暂时屏蔽，暂不确定是否会有OOM ---2020/8/25 by csj
    }

    /**
     * 回收每一帧的图片，释放内存资源
     * 取出AnimationDrawable中的每一帧逐个回收，并且设置Callback为null
     */
    private static void tryRecycleAnimationDrawable(AnimationDrawable animationDrawable) {
        if (animationDrawable != null) {
            animationDrawable.stop();
            for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
                Drawable frame = animationDrawable.getFrame(i);
                if (frame instanceof BitmapDrawable) {
                    ((BitmapDrawable) frame).getBitmap().recycle();
                }
                frame.setCallback(null);
            }
            animationDrawable.setCallback(null);
        }
    }


}

