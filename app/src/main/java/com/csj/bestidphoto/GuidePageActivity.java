package com.csj.bestidphoto;

import android.animation.ArgbEvaluator;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.csj.bestidphoto.base.BaseActivity;
import com.csj.bestidphoto.view.guide.CountdownView;
import com.csj.bestidphoto.view.guide.IndicatorScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        noTitleBar();
        super.onCreate(savedInstanceState);
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
            imageView.setImageResource(mImgIDs.getResourceId(i, 0));
            ViewList.add(imageView);
        }
        initEvent();
        vpGuide.setAdapter(new GuidePagesAdapter());
    }

    private void initEvent() {
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //设置页面背景色
//                int color = (int) mMArgbEvaluator.evaluate(positionOffset, colorBg[position % colorBg.length],colorBg[(position + 1) % colorBg.length]);
//                mRootView.setBackgroundColor(color);
            }

            @Override
            public void onPageSelected(int position) {
                if (position >= ViewList.size() - 1) {
                    btnStart.setVisibility(View.VISIBLE);
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

    private void noTitleBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//继承Activity中使用
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

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
