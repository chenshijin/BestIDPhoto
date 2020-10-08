package com.maoti.lib.net.interceptor.exception;

/**
 * Creation Time: 2018/8/20 10:44.
 * Author: King.
 * Description: 服务端的异常处理类，根据与服务端约定的code判断
 */
public class ApiException extends RuntimeException{

    private int errorCode;

    public ApiException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
