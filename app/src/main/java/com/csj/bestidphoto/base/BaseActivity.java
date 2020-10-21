
package com.csj.bestidphoto.base;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.csj.bestidphoto.R;
import com.csj.bestidphoto.ad.AdUtil;
import com.csj.bestidphoto.ad.NativeInteractionAd;
import com.csj.bestidphoto.comm.SysConfig;
import com.csj.bestidphoto.ui.AllPhotoModelListActivity;
import com.csj.bestidphoto.ui.PhotoCropActivity;
import com.csj.bestidphoto.ui.PhotoEditorActivity;
import com.csj.bestidphoto.ui.PhotoStandardModelDetailActivity;
import com.csj.bestidphoto.ui.dialog.LoadingDialogFm;
import com.csj.bestidphoto.utils.StatusCompat;
import com.csj.bestidphoto.utils.Timer_Task;
import com.csj.bestidphoto.utils.ToastUtil;
import com.google.gson.Gson;
import com.maoti.lib.utils.LogUtil;
import com.maoti.lib.utils.Utils;
import com.sowell.mvpbase.presenter.BasePresenter;
import com.sowell.mvpbase.view.MvpBaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<P extends BasePresenter> extends MvpBaseActivity<P> implements IBaseView {
    public final String TAG = BaseActivity.this.getClass().getSimpleName();

    protected Unbinder mBinder;
    private View layoutBack;
    private MyHandler mHandler;
    private Toast toast;
    protected Gson gson = new Gson();
    @SuppressLint("StaticFieldLeak")
    private static LoadingDialogFm loading;
    private Timer_Task loadingTimer;


//    public SkeletonScreen skeletonScreen;// 骨架屏
    public final int MSG_DOWN_FAIL = 1;
    public String SIM_MSG = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        try {
            int layoutResID = initView(savedInstanceState);
            //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID);
//                layoutBack();
                //绑定到butterknife
                mBinder = ButterKnife.bind(this);
                try {
                    getSupportActionBar().hide();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            if (e instanceof InflateException) {
                throw e;
            }
        }

        mHandler = new MyHandler(this);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        //判断是否开启了底部导航栏.如果开启,网上挪一下位置
//        checkDeviceHasNavigationBar();
        initData(savedInstanceState);

    }

    public void setContentView(int layout) {
        super.setContentView(R.layout.activity_base);
        View main = LayoutInflater.from(getContext()).inflate(layout, null);
        ViewGroup parent = findViewById(R.id.baseRl);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        if(Utils.checkDeviceHasNavigationBar(this)){
//            rlp.bottomMargin = Utils.getNavigationBarHeight(this);
//        }else{
//            rlp.bottomMargin = 0;//Utils.dipToPx(this,50F)
//        }

        parent.addView(main, rlp);
    }

    private NativeInteractionAd mNativeInteractionAd;//插屏广告
    private boolean isFirstIn = true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(isFirstIn){
            isFirstIn = false;
            if(AdUtil.canShowAd()){
                showNativeInteractionAd(this);
            }
        }
    }

    private void showNativeInteractionAd(Activity act){
        if(act instanceof PhotoCropActivity || act instanceof PhotoStandardModelDetailActivity || act instanceof AllPhotoModelListActivity || act instanceof PhotoEditorActivity){
            if(SysConfig.getInstance().getAdConfig() != null){
                mNativeInteractionAd = new NativeInteractionAd(this);
                int w_px = Utils.getWindowWidth(getContext());
                int w_dp = 2 * Utils.pxToDip(getContext(),w_px) / 3;
                mNativeInteractionAd.loadExpressAd(SysConfig.getInstance().getAdConfig().getFloatscreenid(),w_dp,w_dp * 3 / 2);
//            mNativeInteractionAd.loadExpressAd(SysConfig.getInstance().getAdConfig().getFloatscreenid(),600,900);
            }
        }
    }

    //    /**
//     * 显示网络状态
//     *
//     * @param isShow
//     */
//    private void showNetWorkStatu(boolean isShow) {
////        if (!(this instanceof CommentAssistantActivity)) {
////            return;
////        }
//        getHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                ViewGroup parent = findViewById(R.id.baseRl);
//                NetWorkStatuTextView tv = findViewById(R.id.net_work_statu);//
//                if (tv == null) {
//                    tv = new NetWorkStatuTextView(BaseActivity.this);
//                    tv.setId(R.id.net_work_statu);
//                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    View titleV = findViewById(R.id.titleBar);
//
//                    if (titleV != null) {
//                        ViewGroup.LayoutParams vlp = titleV.getLayoutParams();
//                        rlp.topMargin = titleV.getHeight() + Utils.getStatusBarHeight(BaseActivity.this);
//                        if (vlp instanceof RelativeLayout.LayoutParams) {
//                            ((RelativeLayout.LayoutParams) vlp).bottomMargin = isShow ? ViewUtil.getViewHeight(tv) : 0;
//                        } else if (vlp instanceof LinearLayout.LayoutParams) {
//                            ((LinearLayout.LayoutParams) vlp).bottomMargin = isShow ? ViewUtil.getViewHeight(tv) : 0;
//                        } else if (vlp instanceof FrameLayout.LayoutParams) {
//                            ((FrameLayout.LayoutParams) vlp).bottomMargin = isShow ? ViewUtil.getViewHeight(tv) : 0;
//                        }
//                        titleV.setLayoutParams(vlp);
//                    } else {
//                        rlp.topMargin = Utils.dipToPx(getContext(), 60F);
//                    }
//                    parent.addView(tv, rlp);
//                } else {
//                    View titleV = findViewById(R.id.titleBar);
//                    if (titleV != null) {
//                        ViewGroup.LayoutParams vlp = titleV.getLayoutParams();
//                        if (vlp instanceof RelativeLayout.LayoutParams) {
//                            ((RelativeLayout.LayoutParams) vlp).bottomMargin = isShow ? ViewUtil.getViewHeight(tv) : 0;
//                        } else if (vlp instanceof LinearLayout.LayoutParams) {
//                            ((LinearLayout.LayoutParams) vlp).bottomMargin = isShow ? ViewUtil.getViewHeight(tv) : 0;
//                        } else if (vlp instanceof FrameLayout.LayoutParams) {
//                            ((FrameLayout.LayoutParams) vlp).bottomMargin = isShow ? ViewUtil.getViewHeight(tv) : 0;
//                        }
//                        titleV.setLayoutParams(vlp);
//                    }
//
//                }
//
//                dealOtherNetWorkStatu(isShow);
//
//            }
//        });
//
//    }

    public void dealOtherNetWorkStatu(boolean isShow) {

    }

    public void setStatusBarColor() {
        StatusCompat.setStatusBarColor(this, getColor(R.color.colorPrimary));//title_bar_bg
    }

    public abstract int initView(@Nullable Bundle savedInstanceState);

    public boolean isDisabledView() {
        return this.isDestroyed() || this.isFinishing();
    }

//    /**
//     * 设置界面缓存
//     *
//     * @param cacheKye
//     * @param body
//     */
//    public void setCache(String cacheKye, Object body) {
//        try {
//            if (body != null && cacheKye != null && cacheKye.trim().length() > 0) {
//                CacheManager cacheManager = CacheManager.getInstance();
//                cacheManager.putCache(cacheKye, gson.toJson(body));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void setCache(Object body) {
//        try {
//            if (body != null) {
//                CacheManager cacheManager = CacheManager.getInstance();
//                cacheManager.putCache(this.TAG, gson.toJson(body));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 读取缓存
//     *
//     * @param cacheKye
//     * @param typeOfT
//     * @param <T>
//     * @return
//     */
//    public <T> T getCache(String cacheKye, Type typeOfT) {
//        try {
//            if (cacheKye != null && cacheKye.trim().length() > 0) {
//                CacheManager cacheManager = CacheManager.getInstance();
//                return gson.fromJson(cacheManager.getCache(cacheKye), typeOfT);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public <T> T getCache(Type typeOfT) {
//        try {
//            CacheManager cacheManager = CacheManager.getInstance();
//            return gson.fromJson(cacheManager.getCache(this.TAG), typeOfT);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    public abstract void initData(@Nullable Bundle savedInstanceState);

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(String event) {
        //处理全局通用消息
    }

//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void Event(NetWorkStatu event) {//网络状态监听
//        if (event.isNetWorkConn()) {
//            showNetWorkStatu(false);
//        } else {
//            showNetWorkStatu(true);
//        }
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                hideSoftKeyBoard(ev);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void hideSoftKeyBoard(MotionEvent ev) {
        View v = getCurrentFocus();
        if (isShouldHideInput(v, ev)) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            hideSoftInput(v.getWindowToken(), im);
        }
    }

//    /**
//     * 定时的骨架屏展示
//     *
//     * @param rootView
//     * @param layoutView
//     * @param time
//     */
//    public void showScreenView(View rootView, int layoutView, int time) {
//        skeletonScreen = Skeleton.bind(rootView)
//                .load(layoutView)
//                .duration(time)
//                .color(R.color.shimmer_color)
//                .angle(0)
//                .show();
//    }

//    /**show
//     * 无定时的骨架屏展示，需单独关闭
//     *
//     * @param rootView
//     * @param layoutView
//     */
//    public void showScreenView(View rootView, int layoutView) {
//        skeletonScreen = Skeleton.bind(rootView)
//                .load(layoutView)
//                .color(R.color.shimmer_color)
//                .angle(0)
//                .show();
//    }

//    /**
//     * 隐藏
//     */
//    public void hideScreenView() {
//        if (null != skeletonScreen) {
//            skeletonScreen.hide();
//        }
//    }

//    /**
//     * 设置推送TAG
//     *
//     * @param bean
//     */
//    public void setPushTAG(LoginBean bean) {
//        PushApplication.getInstance().registerPushTag(bean.getToken(), this);
//    }


//    /**
//     * 设置退出状态
//     *
//     * @param boolea
//     */
//    public void setSingin(Boolean boolea) {
//        SPfUtil.getInstance().setBoolean(SPFKey.IsSingIN, boolea);
//    }

//    /**
//     * 判断是否登录状态，未登录直接进入登录页面
//     *
//     * @param cls
//     */
//    public void OpenLoginActivity(Class<?> cls) {
//        if (isSingIN()) {
//            openPage(cls);
//        } else {
//            openLoginActivity();
//        }
//    }

//    /**
//     * 判断是否登录状态，未登录直接进入登录页面
//     *
//     * @param to
//     */
//    public void OpenLoginActivity(Intent to) {
//        if (isSingIN()) {
//            openPage(to);
//        } else {
//            openLoginActivity();
//        }
//    }

//    /**
//     * 判断是否登录状态，未登录直接进入登录页面
//     */
//    public boolean isLoginToLoginUI() {
//        if (isSingIN()) {
//            return true;
//        } else {
//            openLoginActivity();
//            return false;
//        }
//    }

//    public void openLoginActivity() {
//        InitSMIShow();
//    }

//    /**
//     * 一键登录
//     */
//    private void InitSMIShow() {
//        SMIDialog.getInstance(getContext(), BaseActivity.this).initSMI(new SMIDialog.ListerBack() {
//            @Override
//            public void backSMIState(Boolean boolen) {
//                if (boolen) {// 判断是否可以用sim卡登录
//                    showProgress();
//                    SMIDialog.getInstance(getContext(), BaseActivity.this).showView();
//                } else {
//                    OpenActivity();
//                }
//            }
//
//            @Override
//            public void hideProgres() {
//                hideProgress();
//            }
//
//            @Override
//            public void opneLoginActivity() {
//                if (!isSingIN()) {
//                    OpenActivity();
//                }
//            }
//
//            @Override
//            public void showToast(String msg) {
//                hideProgres();
//                SIM_MSG = msg;
//                handler.sendEmptyMessage(MSG_DOWN_FAIL);
//            }
//        });
//
//
//    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DOWN_FAIL:
                    showToast(SIM_MSG);
                    break;
            }
        }

        ;
    };


//    public void OpenActivity() {
//        openPage(LoginCodeActivity.class);
//    }


//    /**
//     * 判断是否登录
//     *
//     * @return
//     */
//    public Boolean isSingIN() {
//        return SPfUtil.getInstance().getBoolean(SPFKey.IsSingIN);
//    }

//    /**
//     * 获取token
//     *
//     * @return
//     */
//    public String getToken() {
//        return SPfUtil.getInstance().getString(SPFKey.TOKEN);
//    }

//    // 无网络错误码判断
//    public Boolean getNoNetworkkey(int code) {
//        switch (code) {
//            case NetConstant.Exception: //系统错误
//            case NetConstant.PARSE_ERROR: //解析数据失败
//            case NetConstant.BAD_NETWORK: //网络问题
//            case NetConstant.CONNECT_ERROR: //连接错误
//            case NetConstant.CONNECT_TIMEOUT://连接超时
//            case NetConstant.UNKNOWN_ERROR: //未知错误
//                return true;
//        }
//        return false;
//    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    public static void hideSoftInput(IBinder token, InputMethodManager im) {
        if (token != null) {
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            }
            /*
             * else if (v.getId()==R.id.message_send_btn) { // 点击发送的事件，忽略它。
             * return false; }
             */
            else {
                return true;
            }
        } // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void showProgress() {
        showProgress("正在加载中", 13);
    }

    @Override
    public void showProgress(String message) {
        showProgress(true, message);
    }

    @Override
    public void showProgress(int strRes) {
        showProgress(getString(strRes));
    }


    /**
     * 加载对话框
     */
    @Override
    public synchronized void showProgress(final boolean flag, final String message) {
        if (loading != null) {
            loading.dismiss();
            loading = null;
        }

//        if (AntiShakeUtil2.check()) {
//            return;
//        }

        loading = new LoadingDialogFm(message);
        try {
            if (ActivityManager.isUserAMonkey()) {
                loading.setCancelable(true);
            } else {
                loading.setCancelable(true);//点击外部可取消  2020/8/25 by csj
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loading.show(getSupportFragmentManager(), "");
    }

    /**
     * 关闭加载对话框
     */
    @Override
    public void hideProgress() {
        try {
            if (loadingTimer != null) {
                loadingTimer.stopTimer();
            }
            if (loading != null) {
                if (!loading.canClose()) {
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (loading != null) {
                                    loading.dismiss();
                                    loading = null;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 300);
                } else {
                    loading.dismiss();
                    loading = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 定时关闭，即最多等待时间就关闭进度条
     *
     * @param message
     * @param sec     秒数
     */
    public synchronized void showProgress(String message, int sec) {
        try {
            showProgress(message);
            if (loadingTimer == null) {
                loadingTimer = new Timer_Task(getHandler(), 400, sec * 1000L, null, 1);
            }
            loadingTimer.startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
    }


    @Override
    public void setProgressCancelListener(DialogInterface.OnCancelListener onCancelListener) {
//        if (progressDialog != null) {
//            progressDialog.setOnCancelListener(onCancelListener);
//        }
    }

    @Override
    public void showToast(String msg) {
        if (!isFinishing()) {
            ToastUtil.showShort(msg);
        }
    }

    @Override
    public void showToast(int res) {
        showToast(getString(res));
    }

    @Override
    public void openPage(Class clazz) {
        openPage(new Intent(this, clazz));
    }

    public void finishAndOpenPage(Class clazz) {
        finish();
        openPage(clazz);
    }

    @Override
    public void openPage(Intent intent) {
        startActivity(intent);
//        overridePendingTransition(R.anim.activity_right_enter, R.anim.activity_right_exit);
    }

    @Override
    public void openPageForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
//        overridePendingTransition(R.anim.activity_right_enter, R.anim.activity_right_exit);
    }

    @Override
    public void close() {
        finish();
//        overridePendingTransition(R.anim.activity_right_enter, R.anim.activity_right_exit);
    }

    @Override
    public void clearUser() {
////        UserManager.getInstance().clearUserInfo();
//        openPage(LoginActivity.class);
//        ActivityManager.getInstance().finishAllActivityExceptOne(LoginActivity.class);
    }


//    /**
//     * 返回上层
//     */
//    public void layoutBack() {
//        layoutBack = findViewById(R.id.layoutBack);
//        if (layoutBack != null) {
//            layoutBack.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finishThisPage();
//                }
//            });
//        }
//    }

    protected void finishThisPage() {
        this.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if(mNativeInteractionAd != null){
                mNativeInteractionAd.onDestroy();
            }
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }

            if (mHandler != null) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mBinder != null) {
            mBinder.unbind();
        }

        if (toast != null) {
            toast.cancel();
            toast = null;
        }

        hideProgress();
    }

    public MyHandler getHandler() {
        if (mHandler == null) {
            mHandler = new MyHandler(this);
        }
        return mHandler;
    }

    public void sendMessage(int what, Object obj) {
        if (mHandler != null) {
            Message msg = mHandler.obtainMessage();
            msg.what = what;
            msg.obj = obj;
            mHandler.sendMessage(msg);
        }
    }

    public void sendHandlerMessage(int what) {
//        Message message = mHandler.obtainMessage(3, 1, 2, "java");
        Message msg = Message.obtain();
        msg.what = what;
        if (mHandler != null) {
            mHandler.sendMessage(msg);
        } else {
            LogUtil.i(TAG, "baseHandler为空");
        }
    }

    public void sendHandlerMessageDelayed(int what, Object obj, long delayMillis) {
        getHandler();
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        if (mHandler != null) {
            mHandler.sendMessageDelayed(msg, delayMillis);
        } else {
            LogUtil.i(TAG, "baseHandler为空");
        }
    }

    public void sendEmptyHandlerMessageDelayed(int what, long delayMillis) {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(what, delayMillis);
        } else {
            LogUtil.i(TAG, "baseHandler为空");
        }
    }

    protected void processMessage(Message message) {
    }

    public static class MyHandler extends Handler {
        private WeakReference<BaseActivity> mWeakReference;

        public MyHandler(BaseActivity act) {
            mWeakReference = new WeakReference<BaseActivity>(act);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            BaseActivity act = mWeakReference.get();
            if (act != null) {
                act.processMessage(msg);
            }
            switch (msg.what) {
                case 400:
                    try {
                        if (loading != null) {
                            if (loading.getTagSec() == 2) {
                                loading.dismiss();
                                loading = null;
                                if (act != null) {
                                    act.showToast("网络不可用,请稍后再试!");
                                }
                            } else {
                                if (act != null) {
                                    act.showProgress("网络信号不太好,请耐心等待!", 30);
                                }
                                loading.setTagSec(2);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    }


    @Override
    public void finish() {
        super.finish();
    }

//    @NonNull
//    @Override
//    public AppCompatDelegate getDelegate() {
//        return SkinAppCompatDelegateImpl.get(this, this);
//    }

}
