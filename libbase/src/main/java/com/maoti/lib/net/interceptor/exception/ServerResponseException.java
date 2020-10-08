package com.maoti.lib.net.interceptor.exception;

/**
 * 服务器返回的异常
 */
public class ServerResponseException extends RuntimeException {
    public int mErrorCode;
    public Object mBody;

    public ServerResponseException(int errorCode, String errorMessage, Object body) {
        //super("服务器响应失败，错误码："+errorCode+"，错误原因"+cause, new Throwable("Server error"));
        super(errorMessage);
        this.mErrorCode = errorCode;
        this.mBody = body;
    }
}
