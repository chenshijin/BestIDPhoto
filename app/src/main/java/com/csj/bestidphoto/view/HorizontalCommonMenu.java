package com.csj.bestidphoto.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.csj.bestidphoto.MApp;
import com.csj.bestidphoto.R;
import com.maoti.lib.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/4/24.
 */

public class HorizontalCommonMenu extends LinearLayout {

    @BindView(R.id.itemParent)
    RelativeLayout itemParent;
    @BindView(R.id.menuLogoIv)
    ImageView menuLogoIv;
    @BindView(R.id.menuTv)
    TextView menuTv;
    @BindView(R.id.msgTv)
    TextView msgTv;
    @BindView(R.id.jtIv)
    ImageView jtIv;
    @BindView(R.id.jtLL)
    LinearLayout jtLL;
    @BindView(R.id.head)
    ImageView head;
    private Context context;
    private String menuText;
    private String rightText;

    private Boolean isrightImg;


//    public HorizontalCommonMenu(Context context) {
//        super(context);
//        initView(context, null, 0);
//    }

    public HorizontalCommonMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public HorizontalCommonMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HorizontalCommonMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr);
    }

    public TextView getMenuTv() {
        return menuTv;
    }

    public TextView getMsgTv() {
        return msgTv;
    }

    private void initView(Context context, AttributeSet attrs, int defStyle) {
        this.context = context;
        View inflate = inflate(context, R.layout.horizontalmenu, this);
        ButterKnife.bind(inflate);

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalCommonMenu, defStyle, 0);
        isrightImg = a.getBoolean(R.styleable.HorizontalCommonMenu_isrightImg,true);//是否显示右侧箭头
        menuText = a.getString(R.styleable.HorizontalCommonMenu_menuText);
        rightText = a.getString(R.styleable.HorizontalCommonMenu_rightText);
        int rightheadImg =  a.getResourceId(R.styleable.HorizontalCommonMenu_rightheadImg, -1);//添加右侧头像，默认不显示
        int leftImg = a.getResourceId(R.styleable.HorizontalCommonMenu_leftImg, -1);
        int rightImg = a.getResourceId(R.styleable.HorizontalCommonMenu_rightImg, -1);
        int rightTextSize = a.getInteger(R.styleable.HorizontalCommonMenu_rightTextSize, -1);
        int menuTextTextSize = a.getInteger(R.styleable.HorizontalCommonMenu_menuTextTextSize, -1);
        if (rightTextSize != -1) {
            msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dipToPx(MApp.getInstance(), rightTextSize));
        }

        if (menuTextTextSize != -1) {
            menuTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dipToPx(MApp.getInstance(), menuTextTextSize));
        }
        if (leftImg != -1) {
            menuLogoIv.setVisibility(View.VISIBLE);
            menuLogoIv.setImageResource(leftImg);
        }else{
            menuLogoIv.setVisibility(View.GONE);
        }
        if(!isrightImg){
            jtIv.setVisibility(View.INVISIBLE);
        }
        if (rightImg != -1) {
            jtIv.setImageResource(rightImg);
        }
        if(rightheadImg != -1){
            head.setImageResource(rightheadImg);
            head.setVisibility(View.VISIBLE);
        }

        int left_paddingleft = a.getInteger(R.styleable.HorizontalCommonMenu_left_paddingleft, -1);
        int right_marginRight = a.getInteger(R.styleable.HorizontalCommonMenu_right_marginRight, -1);
        if (left_paddingleft != -1) {
            menuTv.setPadding(Utils.dipToPx(MApp.getInstance(), left_paddingleft), 0, 0, 0);
        }

        if (right_marginRight != -1) {
            LayoutParams rlp = (LayoutParams) jtIv.getLayoutParams();
            rlp.rightMargin = right_marginRight;
        }


//        ColorStateList menuTextColor = a.getColorStateList(R.styleable.HorizontalCommonMenu_menuTextColor);
        int menuTextColor = a.getColor(R.styleable.HorizontalCommonMenu_menuTextColor, -1);
        if (menuTextColor != -1) {
            menuTv.setTextColor(menuTextColor);
        }
        int rightTextColor = a.getColor(R.styleable.HorizontalCommonMenu_rightTextColor, -1);
        if (rightTextColor != -1) {
            msgTv.setTextColor(rightTextColor);
        }

        menuTv.setText(menuText);
        msgTv.setText(rightText);

        a.recycle();
    }

    public ImageView getheadID(){
        return head;
    }

    public void setRightImge(int rightImg) {
        if(rightImg==-1){
            jtIv.setVisibility(GONE);
        }else{
            jtIv.setVisibility(VISIBLE);
            jtIv.setImageResource(rightImg);
        }
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
        menuTv.setText(menuText);
    }

    public void setMenuText(Spanned menuText) {
        menuTv.setText(menuText);
    }

    public void setRightText(String rightText, int color) {

        if(rightText!=null && rightText.length()>15){
            rightText = rightText.substring(0,14)+"..";
        }
        this.rightText = rightText;
        msgTv.setText(rightText);
        if (color != -1) {
            msgTv.setTextColor(color);
        }
    }

}
