package com.csj.bestidphoto.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

/**
 * Created by kenny on 2020/5/23.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    private static final String TAG = "BaseDialogFragment";

    public final String TOP="TOP";
    public final String BOTTM="BOTTM";
    public final String CENTER="CENTER";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        int viewId = getContentViewResId();
        if(viewId > 0) {
            View view = inflater.inflate(viewId, container, false);
            return initData(view);
        }else{
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getCloseBtnId() > 0){
            View view = getDialog().findViewById(getCloseBtnId());
            view.setOnClickListener(v -> Objects.requireNonNull(getDialog()).dismiss());
        }

        Window dialogWindow = getDialog().getWindow();
        if (dialogWindow != null) {
            dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
            dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            if(TOP.equals(getLocation())){
                lp.gravity = Gravity.TOP;
            }else if(CENTER.equals(getLocation())){
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
            }else {
                lp.gravity = Gravity.BOTTOM;
            }
            lp.windowAnimations = android.R.style.Animation_InputMethod;
            dialogWindow.setAttributes(lp);
        }
    }

    public abstract View initData(View view);

    public abstract int getContentViewResId();

    public abstract int getCloseBtnId();

    public abstract String getLocation();
}
