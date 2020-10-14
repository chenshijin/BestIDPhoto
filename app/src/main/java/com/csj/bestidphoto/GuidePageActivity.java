package com.csj.bestidphoto;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.csj.bestidphoto.base.BaseActivity;
import com.csj.bestidphoto.comm.SPKey;
import com.csj.bestidphoto.utils.PrefManager;
import com.csj.bestidphoto.utils.StatusCompat;
import com.csj.bestidphoto.view.guide.CountdownView;
import com.csj.bestidphoto.view.guide.IndicatorScrollView;
import com.maoti.lib.utils.LogUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;

public class GuidePageActivity extends BaseActivity {


    @BindView(R.id.vpGuide)
    ViewPager vpGuide;
    @BindView(R.id.btnStart)
    Button btnStart;
    @BindView(R.id.countdownView)
    CountdownView countdownView;
    @BindView(R.id.indicatorScrollView)
    IndicatorScrollView indicatorScrollView;
    @BindView(R.id.rootView)
    RelativeLayout rootView;
    //    private int colorBg[];
    private TypedArray mImgIDs;
    private List<View> ViewList;
    private ArgbEvaluator mMArgbEvaluator;
    private int mViewPagerIndex;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        noTitleBar();
//        super.onCreate(savedInstanceState);
//    }

    public void setStatusBarColor() {
        StatusCompat.setStatusBarColor(this,  Color.TRANSPARENT);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_guidepage;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        indicatorScrollView.setmScrollCount(getResources().obtainTypedArray(R.array.splash_icon).length());
        vpGuide.addOnPageChangeListener(indicatorScrollView);
        mMArgbEvaluator = new ArgbEvaluator();
//        colorBg = getResources().getIntArray(R.array.splash_bg);
        mImgIDs = getResources().obtainTypedArray(R.array.splash_icon);
        ViewList = new ArrayList<>();
//        Log.e(TAG," "+mImgIDs);
        for (int i = 0; i < mImgIDs.length(); i++) {
//            Log.e(TAG," "+mImgIDs.getResourceId(i,0));
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(mImgIDs.getResourceId(i, 0));
            ViewList.add(imageView);
        }
        initEvent();
        vpGuide.setAdapter(new GuidePagesAdapter());

        PrefManager.setPrefBoolean(SPKey._SHOW_GUIDE,false);
    }

    private void initEvent() {
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //设置页面背景色
//                int color = (int) mMArgbEvaluator.evaluate(positionOffset, colorBg[position % colorBg.length],colorBg[(position + 1) % colorBg.length]);
//                mRootView.setBackgroundColor(color);
                if((mViewPagerIndex == (ViewList.size() - 1)) && (mViewPagerIndex == position)){
                    LogUtil.i(TAG,"正在向左滑动");
                    openPage(MainActivity.class);
                    finish();
                }else{
                    LogUtil.i(TAG,"正在向右滑动");
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position >= ViewList.size() - 1) {
                    btnStart.setVisibility(View.GONE);
                    //取消定时
//                    CountdownView.startCountDown();
//                    CountdownView.setVisibility(View.VISIBLE);
//                    CountdownView.setAddCountDownListener(new CountdownView.OnCountDownFinishListener() {
//                        @Override
//                        public void countDownFinished() {
//                            openPage(MainActivity.class);
//                            finish();
//                        }
//                    });
                } else {
                    btnStart.setVisibility(View.GONE);
                    countdownView.cancelCountDown();
                    countdownView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == SCROLL_STATE_DRAGGING){//state有三种状态下文会将，当手指刚触碰屏幕时state的值为1，我们就在这个时候给mViewPagerIndex 赋值。
                    mViewPagerIndex = vpGuide.getCurrentItem();
                }
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(MainActivity.class);
                finish();
            }
        });
    }

//    private void noTitleBar() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//继承Activity中使用
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//    }

    private class GuidePagesAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return ViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = ViewList.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
