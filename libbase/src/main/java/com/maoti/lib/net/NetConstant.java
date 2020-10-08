package com.maoti.lib.net;

import com.maoti.libbase.BuildConfig;

public interface NetConstant {
    boolean LOGON = BuildConfig.IS_DEBUG;
    /**
     * 网络请求超时时间毫秒
     */
    int DEFAULT_TIMEOUT = 1000*30;

    /**
     * 打印网络数据的tag
     */
    String logTag="net";

    String Secret = "Dds#SDafu23!4R5SDFS8*2";//网站秘钥

    int SUCCESS = 0; // 访问成功
    int NO_DATA_MSG = 1; //  失败


    int COMMENT_ERROR = 110;//"服务器繁忙，稍后再试！"; //
    int ARGS_MISS_ERROR =111; //  "参数缺失！";//111
    int ARGS_ILLEGAL_ERROR = 112; // "参数非法！";//112
    int UPLOAD_FAIL_ERROR =113; //  "文件上传失败！";//113
    int FETCH_OSS_STS_ERROR =114; //  "获取sts授权失败！";//114
    int MISSING_WEB_TOKEN_ERROR = 115; // "缺失参数webToken！";//115
    int VERIFY_SIGN_FAIL_ERROR =116; //  "验证签名失败！";//116
    int NETWORK_REQUEST_TIMEOUT_ERROR = 117; // "网络请求超时！";//117
    int NO_EXPIRED = 118;//账号过期
    int USER_LOGOUT = 124;//用户已注销
    int FORCED_TO_LOGOFF_ERROR = 119; //此账号已在其它设备登录！
    int USER_LOGIN_OUT_OK_ERROR  =20020;//=>'此账号已经注销、请重新注册！',//20020
    int CHECHKED_THIRD_ID  =132;//=>'此用户账号未注册！',//20020
    int  USER_PHONE = 20024; //该用户未绑定手机，会跳入绑定手机界面
    int ARTICLE_UNLIKE_ERROR = 10002; // "取消点赞失败！";//
    int ARTICLE_COLLECT_ERROR = 10012; // "收藏文章失败";//10012
    int ARTICLE_UNOLLECT_ERROR = 10013; // "取消文章收藏失败";//10013
    int ARTICLE_LIKED_ERROR = 10001; // "点赞失败！";//10001
    int ARTICLE_TIPOFF_ERROR = 10003; // "文章举报失败！";//10003
    int ARTICLE_COMMENT_TIPOFF_ERROR =10004; //  "文章评论举报失败！";//10004
    int ARTICLE_COMMENT_PUBLISH_ERROR = 10005; // "发布文章评论失败！";//10005
    int CIRCLE_SUBSCRIBE_ERROR =10006; //  "关注圈子失败！";//10006
    int CIRCLE_UNSUBSCRIBE_ERROR =10007; //  "取关关注圈子失败！";//10007
    int ARTICLE_COMMENT_LIKED_ERROR = 10008; // "点赞失败！";//10008
    int ARTICLE_COMMENT_UNLIKE_ERROR = 10009; // "取消点赞失败！";//10009
    int ARTICLE_AUTHOR_SUBSCRIBE_ERROR =10010; //  "关注作者失败！";//10010
    int ARTICLE_AUTHOR_UNSUBSCRIBE_ERROR = 10011; // "取消关注作者失败！";//10011
    int PHONE_NOT_REGISTERED_ERROR  =20001; //  "电话号码未注册";//20001
    int PHONE_ALREADY_EXISTED_ERROR  =20002; //  "电话号码已经被注册";//20002
    int PHONE_NOT_EMPTY_ERROR  = 20006; // "电话号码不能为空";//20006
    int PHONE_EQUAL_ERROR  = 20007; // "两个电话号码相同";//20007
    int PHONE_NOT_EQUAL_ERROR  =20008; //  "电话输入有误";//20008
    int PASSWORD_NOT_EMPTY_ERROR  =20009; //  "密码不能为空";//20009
    int PASSWORD_NOT_EQUAL_ERROR  = 20010; // "密码不正确";//20010
    int PASSWORD_WRONG_TOO_MUCH_ERROR  = 20012; // "密码错误次数过多";//20012
    int PHONE_CAPTCHA_ERROR  = 20003; // "验证码有误";//20003
    int USER_STATUS_ERROR  =20011; //  "账号异常";//20011
    int OPERATION_TOO_MUCH_ERROR  = 20013; // "操作过于频繁";//20013
    int USER_UPDATE_COUNT_ERROR  = 20004; // "修改次数已达上线";//20004
    int WAIT_FOR_REVIEW_ERROR  = 20005; // "等待审核中";//20005
    int USER_FOLLOW_FRIENDS_ERROR  = 20014; // "关注好友失败";//20014
    int FOLLOW_ALL = 1200001;//拉黑失败code

    int Exception = 99999;//系统错误
    int PARSE_ERROR = 99998;//解析数据失败
    int BAD_NETWORK = 99997;//网络问题
    int CONNECT_ERROR = 99996;//连接错误
    int CONNECT_TIMEOUT = 99995;//连接超时
    int UNKNOWN_ERROR = 99994;//未知错误
}
