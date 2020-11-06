package com.maoti.lib.net.interceptor;

import android.util.Log;
import android.widget.Toast;

import com.maoti.lib.event.PushMsgEvent;
import com.maoti.lib.event.ClearUnReadEvent;
import com.maoti.lib.utils.LogUtil;
import com.maoti.libbase.BuildConfig;
import com.google.gson.JsonParseException;
import com.maoti.lib.BaseApplication;
import com.maoti.lib.net.NetConstant;
import com.maoti.lib.net.ResponseResult;
import com.maoti.lib.net.interceptor.exception.ApiException;
import com.maoti.lib.net.interceptor.exception.NoDataExceptionException;
import com.maoti.lib.net.interceptor.exception.ServerResponseException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


/**
 * Created by csy on 2017/4/18.
 */

public abstract class DefaultObserver<T> implements Observer<ResponseResult<T>> {
    private Disposable disposable;


    public DefaultObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }



    @Override
    public void onNext(ResponseResult<T> response) {
        onSuccess(response);
    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof HttpException) {     //   HTTP错误
            LogUtil.i(NetConstant.logTag,"网络错误1" + e.getMessage());
            onException(NetConstant.BAD_NETWORK, "网络错误");
        } else if (e instanceof IOException) {   //   连接错误
            LogUtil.i(NetConstant.logTag,"链接错误2" + e.getMessage());
            onException(NetConstant.CONNECT_ERROR, "网络错误");
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            LogUtil.i(NetConstant.logTag,"解析错误3" + e.toString());
            e.printStackTrace();
            onException(NetConstant.PARSE_ERROR, "解析错误");
        } else if (e instanceof ServerResponseException) {
            ServerResponseException responseException = (ServerResponseException) e;
            int code = responseException.mErrorCode;
            onException(code, e.getMessage());
        } else if(e instanceof ApiException){
            //服务器错误码
            ApiException apiException = (ApiException) e;
            int code = apiException.getErrorCode();

            if(BuildConfig.IS_DEBUG&&code != 1){
                Toast.makeText(BaseApplication.getContext(),"[错误码:"+code+" 错误消息:"+e.getMessage()+"]",Toast.LENGTH_LONG).show();
            }

            if(code == NetConstant.NO_EXPIRED || code == NetConstant.FORCED_TO_LOGOFF_ERROR){//账号过期或账号未登录
                EventBus.getDefault().postSticky(new PushMsgEvent(PushMsgEvent.JPUSH_TYPE_UNREAD_MSG_NUM, "-1"));
                EventBus.getDefault().post(new ClearUnReadEvent(4));
            }

//            if(code == Constant.NO_EXPIRED ||
//               code == Constant.FORCED_TO_LOGOFF_ERROR ||
//               code == Constant.USER_LOGIN_OUT_OK_ERROR ||
//               code == Constant.FORCED_TO_LOGOFF_ERROR ||
//                code == Constant.USER_STATUS_ERROR){// 失效账户的错误码
//                Logger.i("当前请求回来code"+ code);
//                SPfUtil.getInstance().setBoolean(SPFKey.IsSingIN,false);
//                EventBus.getDefault().post(new EventEntity(EventBusKey.logout,"",null));
//            }
            LogUtil.i(NetConstant.logTag,"解析错误" + e.toString());
            onException(code, e.getMessage());
        } else if (e instanceof NoDataExceptionException) {
            onSuccess(null);
        } else {
            LogUtil.i(NetConstant.logTag,Objects.requireNonNull(e.getMessage()));
            onException(NetConstant.Exception, "未知异常");
        }

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onComplete() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 请求成功
     *
     * @param result 服务器返回的数据
     */
    abstract public void onSuccess(ResponseResult<T> result);

    abstract public void onException(int code, String eMsg);

}
