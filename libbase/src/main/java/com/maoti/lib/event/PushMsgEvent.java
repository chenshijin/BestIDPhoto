package com.maoti.lib.event;

import java.io.Serializable;

public class PushMsgEvent implements Serializable {
    public final static int JPUSH_TYPE_ARTICLE=0;//文章
    public final static int JPUSH_TYPE_TOPIC=1;//话题
    public final static int JPUSH_TYPE_CIRCLE=2;//圈子
    public final static int JPUSH_TYPE_GONGGAO = 3;//公告
    public final static int JPUSH_TYPE_FAN_NEWS = 4;//球迷圈有新说说
    public final static int JPUSH_TYPE_UNREAD_MSG_NUM = 5;//消息红点总数
    public final static int JPUSH_TYPE_CHAT = 6;//评论聊天
    public final static int JPUSH_TYPE_UNREAD_FOLLOW = 7;//关注的红点
    public final static int JPUSH_TYPE_HOME_UNREAD_FOLLOW = 8;//首页关注的红点
    public final static int JPUSH_TYPE_ATTENTION = 9;//关注赛事详情

    private int type;// 4-球迷圈有新说说,5是红点
    private String message;

    public PushMsgEvent(int type, String message){
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "JpushMsgEvent{" +
                "type=" + type +
                ", message='" + message + '\'' +
                '}';
    }
}
