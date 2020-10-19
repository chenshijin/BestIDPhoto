package com.csj.bestidphoto.ui.dialog;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.csj.bestidphoto.MApp;
import com.csj.bestidphoto.R;
import com.csj.bestidphoto.comm.SPKey;
import com.csj.bestidphoto.utils.PrefManager;
import com.csj.bestidphoto.utils.ToastUtil;

import java.util.Calendar;

/**
 * Created by kenny on 2020/5/23.
 * Loading 提示
 */
public class PrivacyDialog extends BaseDialogFragment {

    private static final String TAG = "PrivacyDialog";
    long lastClickTime = Calendar.getInstance().getTimeInMillis();
    public static final int MIN_CLICK_DELAY_TIME = 300;

    private CheckBox checkTv;
    private TextView agreeTv;
    private ImageView closeIv;

    @Override
    public int getContentViewResId() {
        return R.layout.dialog_privacy_policy;
    }

    @Override
    public int getCloseBtnId() {
        return -1;
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
    public View initData(View v) {
        setCancelable(false);
        checkTv = v.findViewById(R.id.checkTv);
        agreeTv = v.findViewById(R.id.agreeTv);
        closeIv = v.findViewById(R.id.closeIv);

        agreeTv.setOnClickListener(mainOnClickListener);
        closeIv.setOnClickListener(mainOnClickListener);
        return v;
    }

    View.OnClickListener mainOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.agreeTv:
                    if(checkTv.isChecked()){
                        PrefManager.setPrefBoolean(SPKey._PRIVACY_POLICY_AGREE,true);
                        dismiss();
                    }else{
                        ToastUtil.showShort("请先勾选同意该隐私政策！");
                    }
                    break;
                case R.id.closeIv:
                    dismiss();
                    MApp.exitApp();
                    break;
            }
        }
    };

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

