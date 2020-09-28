
package com.csj.bestidphoto.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.sowell.mvpbase.presenter.BasePresenter;
import com.sowell.mvpbase.view.MvpBaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 基础Fragment
 * @author kenny
 * @date 2020/5/14.
 */
public abstract class BaseFragment<P extends BasePresenter> extends MvpBaseFragment<P> implements IBaseView{

    public final String TAG = BaseFragment.this.getClass().getSimpleName();
    private MyHandler mHandler;

    public ViewGroup mView;

    public boolean mShowTitleBar = true;

    protected Context mContext;

    protected Unbinder mBinder;
    protected Gson gson=new Gson();

    public BaseFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        int resId = getContentViewResId();
        //获取fragment需要显示的view
        View contentView = getContentView();
        if (contentView == null) {
            //获取资源Id为-1不初始化
            if (resId != -1) {
                //初始化容器,获取容器实体View
                contentView = LayoutInflater.from(getActivity()).inflate(getContentViewResId(), null);
            }
        }
        if (contentView != null) {

            initToolBar(contentView);
            //是否绑定依赖注入
            mBinder = ButterKnife.bind(this, contentView);
            //回调initViews
            initView(contentView, savedInstanceState);
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }

        return contentView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(String event){

    }

    public abstract int getContentViewResId();


    public abstract View initView(View contentView, @Nullable Bundle savedInstanceState);


    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

//    /**
//     * 设置缓存
//     */
//    public void setCache(Object body){
//        Log.d(TAG, "setCache: "+this.TAG);
//        getBaseActivity().setCache(TAG,body);
//    }
//    public void setCache(String cacheKye,Object body){
//        getBaseActivity().setCache(cacheKye,body);
//    }

//    /**
//     * 读取缓存
//     */
//    public <T> T getCache(Type typeOfT){
//        return (T) getBaseActivity().getCache(TAG,typeOfT);
//    }
//    public <T> T getCache(String cacheKye, Type typeOfT){
//        return (T) getBaseActivity().getCache(cacheKye,typeOfT);
//    }

//    /**
//     * 定时的骨架屏展示
//     * @param rootView
//     * @param layoutView
//     * @param time
//     */
//    public void showScreenView(View rootView,int layoutView,int time){
//        getBaseActivity().showScreenView(rootView,layoutView,time);
//    }
//
//    /**
//     * 无定时的骨架屏展示，需单独关闭
//     * @param rootView
//     * @param layoutView
//     */
//    public void showScreenView(View rootView,int layoutView){
//        getBaseActivity().showScreenView(rootView,layoutView);
//    }
//
//    /**
//     * 隐藏 骨架屏
//     */
//    public void hideScreenView(){
//        getBaseActivity().hideScreenView();
//    }
//
//    /**
//     * 判断是否登录状态，未登录直接进入登录页面
//     * @param cls
//     */
//    public void OpenLoginActivity(Class<?> cls){
//      getBaseActivity().OpenLoginActivity(cls);
//    }
//
//    /**
//     * 判断是否登录状态，未登录直接进入登录页面
//     * @param to
//     */
//    public void OpenLoginActivity(Intent to){
//        getBaseActivity().OpenLoginActivity(to);
//    }
//
//    // 无网络错误码判断
//    public Boolean  getNoNetworkkey(int code){
//        return  getBaseActivity().getNoNetworkkey(code);
//    }
//
//    /**
//     * 判断是否登录
//     * @return
//     */
//    public Boolean isSingIN(){
//        return  getBaseActivity().isSingIN();
//    }
//
//
//    public void  setSingin(Boolean boolea){
//        getBaseActivity().setSingin(boolea);
//    }
//    /**
//     * 获取token
//     * @return
//     */
//    public  String  getToken(){
//        return getBaseActivity().getToken();
//    }
//
//
//    // 无网络错误码判断
//    public Boolean getBackstagekey(int code){
//      return   getBaseActivity().getNoNetworkkey(code);
//    }

//    public void openDialog(boolean cancelable, boolean canceledOnTouchOutside,String msg) {
//        getBaseActivity().openDialog(cancelable,canceledOnTouchOutside,msg);
//    }
//
//    public void closeDialog() {
//        getBaseActivity().closeDialog();
//    }
    /**
     * init Tool Bar
     *
     * @param view
     */
    private void initToolBar(View view) {

    }

    public MyHandler getHandler() {
        if (mHandler == null) {
            mHandler = new MyHandler(this);
        }
        return mHandler;
    }

    /**
     * 是否显示TitleBar
     *
     * @return
     */
    private boolean isShowTitleBar() {
        return mShowTitleBar;
    }


    /**
     * @see MyHandler#processMessage(Message)
     */
    protected void processMessage(Message message) {

    }

    public View getContentView() {
        return null;
    }

    public static class MyHandler extends android.os.Handler{

        private WeakReference<BaseFragment> mFragment;

        public MyHandler(BaseFragment fragment) {
            mFragment = new WeakReference<BaseFragment>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            BaseFragment fragment = mFragment.get();
            if (fragment != null) {
                fragment.processMessage(msg);
            }
        }
    }

    public void sendMessage(int what, Object obj){
        getHandler();
        if(mHandler != null){
            Message msg = mHandler.obtainMessage();
            msg.what = what;
            msg.obj = obj;
            mHandler.sendMessage(msg);
        }
    }

    public void sendMessageDelay(int what, Object obj, int delay){
        getHandler();
        if(mHandler != null){
            Message msg = mHandler.obtainMessage();
            msg.what = what;
            msg.obj = obj;
            mHandler.sendMessageDelayed(msg, delay);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBinder != null) {
            mBinder.unbind();
        }

        try {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证Activit 和 fragment 是否活动中
     *
     * @return true 活动中   false 已退出
     */
    public boolean validateFragmentIsActivitys() {
        if (getActivity() == null || getActivity().isFinishing() || !isAdded()) {
            return false;
        }
        return true;
    }

    public Context getApplicationContext() {
        if (getActivity() != null) {
            return getActivity().getApplicationContext();
        }
        return getContext();
    }


    public boolean onBackPressed() {
        return false;
    }

    public AlertDialog mAlertDialog;

    public AlertDialog showConfirmDialog(String message,
                                         DialogInterface.OnClickListener confirmListener,
                                         boolean cancelBtn) {
        dismissConfirmDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("系统提示");
        builder.setMessage(message);
        builder.setPositiveButton("确认", confirmListener);
        if (cancelBtn) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        mAlertDialog = builder.create();
        mAlertDialog.show();

        return mAlertDialog;
    }

    public void dismissConfirmDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

//    /**
//     * 判断是否登录状态，未登录直接进入登录页面
//     */
//    public boolean isLoginToLoginUI(){
//        if(isSingIN()){
//            return true;
//        }else {
//            getBaseActivity().openLoginActivity();
//            return false;
//        }
//    }

    /**
     * 获取当前Fragment状态
     *
     * @return true为正常 false为未加载或正在删除
     */
    private boolean getStatus() {
        return (isAdded() && !isRemoving());
    }

    /**
     * 获取Activity
     *
     * @return
     */
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void showProgress(boolean flag, String message) {
        if (getStatus()) {
            getBaseActivity().showProgress(flag, message);
        }
    }

    @Override
    public void showProgress(String message) {
        showProgress(true, message);
    }

    public void showProgress(String msg,int sec){
        getBaseActivity().showProgress(msg, sec);
    }

    @Override
    public void showProgress(int strRes) {
        showProgress(getString(strRes));
    }

    @Override
    public void hideProgress() {
       try{
           getBaseActivity().hideProgress();
       } catch (Exception e){
           e.printStackTrace();
       }
    }

    @Override
    public void setProgressCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        getBaseActivity().setProgressCancelListener(onCancelListener);
    }

    @Override
    public void showToast(String msg) {
        if (getStatus()) {
            getBaseActivity().showToast(msg);
        }
    }

    public void startActivity(Class cls){
        startActivity(new Intent(getContext(), cls));
    }

    @Override
    public void showToast(int res) {
        showToast(getString(res));
    }

    @Override
    public void openPage(Class clazz) {
        getBaseActivity().openPage(clazz);
    }

    @Override
    public void openPage(Intent intent) {
        getBaseActivity().openPage(intent);
    }

    @Override
    public void openPageForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
////        getBaseActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        getBaseActivity().overridePendingTransition(R.anim.activity_right_enter, R.anim.activity_right_exit);
    }

    @Override
    public void close() {
        getBaseActivity().close();
    }

//    @Override
//    public <T> void startAsync(Observable<T> observable, Observer<T> observer) {
//        observable
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(this.<T>bind())
//                .subscribe(observer);
//    }

    @Override
    public void clearUser() {
        getBaseActivity().clearUser();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissConfirmDialog();
    }
    // 刷新频道排序的一个方法,提供子类需要的重写
    public void refreshChannelSort(String json,String current){
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
