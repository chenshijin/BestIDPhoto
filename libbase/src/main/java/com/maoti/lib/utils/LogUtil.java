package com.maoti.lib.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.maoti.lib.net.NetConstant;

/**
 * 提供统一的log打印工具类
 * @author kenny
 */
public class LogUtil {
	
	/** 五种Log日志类型 */
	
	/** 调试日志类型 */
	public static final int DEBUG = 111;
	/** 错误日志类型 */
	public static final int ERROR = 112;
	/** 信息日志类型 */
	public static final int INFO = 113;
	/** 详细信息日志类型 */
	public static final int VERBOSE = 114;
	/** 警告日志类型 */
	public static final int WARN = 115;

	public static void i(Class cl, String message){
		if(TextUtils.isEmpty(message)){
			return;
		}
		if(NetConstant.LOGON){
			Log.i(cl.getSimpleName(), message);
		}
	}

	public static void i(String Tag, String format, Object... args) {

		if(TextUtils.isEmpty(format)){
			return;
		}

		if (NetConstant.LOGON) {
			Log.i(Tag, String.format(format, args));
		}
	}

	public static void i(String Tag, String Message) {
		if(TextUtils.isEmpty(Message)){
			return;
		}
		if (NetConstant.LOGON) {
			Log.i(Tag, Message);
		}
	}

	public static void i(Context context, String message){
		if(TextUtils.isEmpty(message)){
			return;
		}
		if(NetConstant.LOGON){
			Log.i(context.getClass().getSimpleName(), message);
		}
	}

	public static void i(Object obj, String message){
		if(TextUtils.isEmpty(message)){
			return;
		}
		if(NetConstant.LOGON){
			Log.i(obj.getClass().getSimpleName(), message);
		}
	}

	public static void i(String Tag, String Message, int Style) {
		if(TextUtils.isEmpty(Message)){
			return;
		}
		if (NetConstant.LOGON) {
			switch (Style) {
			case DEBUG: {
				Log.d(Tag, Message);
			}
				break;
			case ERROR: {
				Log.e(Tag, Message);
			}
				break;
			case INFO: {
				Log.i(Tag, Message);
			}
				break;
			case VERBOSE: {
				Log.v(Tag, Message);
			}
				break;
			case WARN: {
				Log.w(Tag, Message);
			}
				break;
			default:
				Log.i(Tag, Message);
				break;
			}
		}
	}
}
