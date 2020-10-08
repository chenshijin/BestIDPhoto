package com.maoti.lib.db;

public interface SPFKey {
    String TOKEN="TOKEN";
    String IsSingIN = "ISSINGIN";
    String WhiteAndheaven = "WhiteAndheaven";
    String USER_ID="user_id";
    String USER_TYPE = "user_type";
    String EMOJJ = "keys_emojj";
    String LOCAL_KEY_WORD="LocalKeyword";
    String SHARE_IS_FIRST = "isFirst";  //判断程序是否是第一次运行
    String SHOW_AGREEMENT = "SHOW_AGREEMENT";  //是否显示用户协议
    String LoginBean = "LoginBean";

    String push_news = "push_news";
    String push_comments = "push_comments";
    String push_all = "push_all";
    String open_numbe = "open_numbe"; //当前足球推送设置开启数量
    String http_url="http_url";//保存http请求地址
    String ws_url="ws_url";//保存websokey地址
    String FAN_CIR_HASNEWS = "fan_cir_hasnews";//球迷圈是否有新说说
    String FAN_CIR_NEWEST_USER = "fan_cir_newest_user";//球迷圈最新说说的用户
    String FAN_CIR_NEW_DRAFIT = "fan_cir_new_drafit";//球迷圈的草稿(只保存最近一条)
    String FAN_CIR_NEW_DRAFIT_IMGS = "fan_cir_new_drafit_imgs";//球迷圈的草稿图片
    String POST_DRAFIT = "post_drafit";//帖子草稿
    String UNREAD_MSG_NUM = "unread_msg_num";//未读消息数

    String HW_TOKEN="HW_TOKEN";//华为token保存
    String CID="CID";//个推推送需要用到
    String FOLLOW_UNREAD = "follow_unread";//关注是否有未读消息
    String HOME_FOLLOW_UNREAD = "home_follow_unread";//home关注是否有未读消息
    String IS_FIRST_IN_CIR = "is_first_in_cir";//是否是打开猫体圈
    String FABULOUS = "fabulous";;//用来标记开发者模式

    //---------------------比赛提示设置-------------------------------
    String MATCH_REMIND = "MatchRemind";//仅提示关注赛事
    String GOAL_VOICE= "GoalVoice";// 进球声音提示
    String GOAL_POP= "GoalPop";// 进球弹窗提示
    String GOAL_SHOCK= "GoalShock";// 进球震动提示
    String RED_YELLOW_VOICE= "RedYellowVoice";// 红黄牌声音提示
    String RED_YELLOW_POP= "RedYellowPop";// 红黄牌弹窗提示
    String RED_YELLOW_SHOCK= "RedYellowShock";// 红黄牌震动提示
    String GOAL_RANKING= "GoalRanking";// 球队排名
    String HANDICAP_INDEX= "HandicapIndex";//盘口指数
    String CORNERS= "Corners";//角球数
    String RED_AND_YELLOW_CARDS= "RedAndYellowCards";// 红黄牌提示

    //---------------------足球比赛设置-------------------------------
    String OPEN_PUSH="open_push";//开启推送
    String STARTING_LINEUP="starting_lineup";//首发阵容
    String KICK_OFF="kick_off";//开始
    String END="end";//结束
    String GOAL="goal";//进球
    String RED_CARD="red_card";//红牌
    String YELLOW_CARD="yellow_card";//黄牌
    String CORNER_KICK="corner_kick";//角球

    //------------------------启动时缓存的数据 -------------------------

     String WX_LOGIN = "wx_login";//是否显示微信登录&分享
     String QQ_LOGIN= "qq_login";//是否显示QQ登录&分享
     String WB_LOGIN= "wb_login";//是否显示微博登录&分享
     String ICO_NAME= "iconName";//app启动图片
     String LAUCH_IMG_URL = "lauchImgUrl";//启动页的图片地址

     String RESIZE_BY_WIDTH = "resize_byWidth";
     String RESIZE_BY_FILLWH= "resize_byFillWH";
     String RESIZE_BY_SCALE= "resize_byScale";
     String CIRCLE_AVATAR= "circle_avatar";
     String BLUR= "blur";
     String INTERLACE= "interlace";
     String INFO= "info";

}
