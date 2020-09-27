package com.csj.bestidphoto.utils;

import android.text.format.DateFormat;

import com.maoti.lib.utils.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author
 * @description: 和时间相关的工具类
 * @date 2017/6/19  20:08
 */

public class TimeUtils {

    public static final long ONE_MINUTE_MILLIONS = 60 * 1000;
    public static final long ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS;
    public static final long ONE_DAY_MILLIONS = 24 * ONE_HOUR_MILLIONS;

    /**
     * 获取短时间格式
     * 年月日时分秒
     * @return
     */
    public static String getLongTime(long time) {
        Date date = new Date(time * 1000);
        Date curDate = new Date();

        String str = "";
        long durTime = curDate.getTime() - date.getTime();

        int dayStatus = calculateDayStatus(date, new Date());

        if (durTime <= 60 * ONE_MINUTE_MILLIONS) {
            str = "刚刚";
        } else if (dayStatus == 0) {
            str = durTime / ONE_HOUR_MILLIONS + "小时前";
        }else if (isSameYear(date, curDate) && dayStatus < -1) {
            str = DateFormat.format("MM-dd", date).toString();
        } else {
            str = DateFormat.format("yyyy-MM-dd", date).toString();
        }
        return str;
    }
    public static String getMMdd(long time) {
        Date date = new Date(time * 1000);
        String str = "";
        str = DateFormat.format("MM月dd日", date).toString();
        return str;
    }

    /**
     * 获取短时间格式
     * 年月日时分秒
     * @return
     */
    public static String getChatTime(long time) {
        Date date = new Date(time * 1000);
        Date curDate = new Date();

        String str = "";
        int dayStatus = calculateDayStatus(date, new Date());

        if (dayStatus == 0) {
            str = DateFormat.format("HH:mm", date).toString();
        } else if (dayStatus == -1) {
            str = "昨天" + DateFormat.format("HH:mm", date);
        } else if (isSameYear(date, curDate) && dayStatus < -1) {
            str = DateFormat.format("MM-dd HH:mm", date).toString();
        } else {
            str = DateFormat.format("yyyy-MM-dd HH:mm", date).toString();
        }
        return str;
    }

    /**
     * 获取短时间格式
     * 年月日时分秒
     * @return
     */
    public static String getShortTime(long time) {
        Date date = new Date(time * 1000);
        Date curDate = new Date();

        String str = "";
        long durTime = curDate.getTime() - date.getTime();

        int dayStatus = calculateDayStatus(date, new Date());

        if (durTime <= 60 * ONE_MINUTE_MILLIONS) {
            str = "刚刚";
        } else if (dayStatus == 0) {
            str = durTime / ONE_HOUR_MILLIONS + "小时前";
        } else if (dayStatus == -1) {
            str = DateFormat.format("MM-dd HH:mm", date).toString();
        } else  if (isSameYear(date, curDate) && dayStatus < -1) {
            str = DateFormat.format("MM-dd HH:mm", date).toString();
        } else {
            str = DateFormat.format("yyyy-MM-dd HH:mm", date).toString();
        }
        return str;
    }

    public static String getYM(String cc_time) {// 将时间戳转换为日期(单位：秒)
        if (cc_time == null) {
            return "";
        }
        String re_Strtime = null;

        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");// 定义格式，不显示毫秒
        Timestamp tts = new Timestamp(Long.parseLong(cc_time) * 1000L);
        re_Strtime = df.format(tts);

        // SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // long lcc_time=Long.parseLong(cc_time);//
        // re_Strtime=sdf.format(new Date(lcc_time * 1000L));

        return re_Strtime;
    }

    public static String getYM(long cc_time) {// 将时间戳转换为日期(时间戳单位是：毫秒)
        String re_Strtime = null;

        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");// 定义格式，
        Timestamp tts = new Timestamp(cc_time);//* 1000L
        re_Strtime = df.format(tts);

        // SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // long lcc_time=Long.parseLong(cc_time);//
        // re_Strtime=sdf.format(new Date(lcc_time * 1000L));

        return re_Strtime;
    }

    public static String getYM2(long cc_time) {// 将时间戳转换为日期(时间戳单位是：秒)
        String re_Strtime = null;

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM");// 定义格式，
        Timestamp tts = new Timestamp(cc_time * 1000L);//
        re_Strtime = df.format(tts);

        // SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // long lcc_time=Long.parseLong(cc_time);//
        // re_Strtime=sdf.format(new Date(lcc_time * 1000L));

        return re_Strtime;
    }

    public static String getyyyy_MM_dd_HH_mm(long cc_time) {// 将时间戳转换为日期(时间戳单位是：秒)
        String re_Strtime = null;

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd  HH:mm");// 定义格式，不显示毫秒
        Timestamp tts = new Timestamp(cc_time * 1000L);//* 1000L
        re_Strtime = df.format(tts);

        return re_Strtime;
    }

    public static String getMM_dd_HH_mm(long cc_time) {// 将时间戳转换为日期(时间戳单位是：秒)
        String re_Strtime = null;

        SimpleDateFormat df = new SimpleDateFormat("MM/dd  HH:mm");// 定义格式，不显示毫秒
        Timestamp tts = new Timestamp(cc_time * 1000L);//* 1000L
        re_Strtime = df.format(tts);

        return re_Strtime;
    }

    public static String getyyyy_MM_dd(long cc_time) {// 将时间戳转换为日期(时间戳单位是：秒)
        String re_Strtime = null;

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");// 定义格式，不显示毫秒
        Timestamp tts = new Timestamp(cc_time * 1000L);//* 1000L
        re_Strtime = df.format(tts);

        return re_Strtime;
    }



    /**
     * 判断是否是同一年
     *
     * @param targetTime
     * @param compareTime
     * @return
     */
    public static boolean isSameYear(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.YEAR);

        return tarYear == comYear;
    }

    /**
     * 判断是否处于今天还是昨天，0表示今天，-1表示昨天，小于-1则是昨天以前
     *
     * @param targetTime
     * @param compareTime
     * @return
     */
    public static int calculateDayStatus(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarDayOfYear = tarCalendar.get(Calendar.DAY_OF_YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comDayOfYear = compareCalendar.get(Calendar.DAY_OF_YEAR);

        return tarDayOfYear - comDayOfYear;
    }

    /**
     * 将秒数转换成00:00的字符串，如 118秒 -> 01:58
     *
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
                        + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }


    /**
     * 时间戳格式转换
     */
    static String daynames[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static String getnewchattime(String time) {

        long timesamp = Long.parseLong(DateUtils.dateToStamp(time));
        String result = "";
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(timesamp);
        String timeformat = "mm月dd日 hh:mm";
        String YEARtimeformat = "yyyy年M月d日 hh:mm";
        String am_pm = "";
        int hour = otherCalendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 6) {
            am_pm = "凌晨";
        } else if (hour >= 6 && hour < 12) {
            am_pm = "早上";
        } else if (hour == 12) {
            am_pm = "中午";
        } else if (hour > 12 && hour < 18) {
            am_pm = "下午";
        } else if (hour >= 18) {
            am_pm = "晚上";
        }

        timeformat = "MM-dd- " + am_pm + "HH:mm";
        YEARtimeformat = "yyyy-MM-dd- " + am_pm + "HH:mm";
//        timeformat = "MM-dd HH:mm";
//        YEARtimeformat = "yyyy-MM-dd  HH:mm";
        boolean YEARtemp = todayCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);
        if (YEARtemp) {
            int todayMONTH = todayCalendar.get(Calendar.MONTH);
            int otherMONTH = otherCalendar.get(Calendar.MONTH);
            if (todayMONTH == otherMONTH) {//表示是同一个月
                int temp = todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = am_pm + gethourandmin(timesamp);
                        break;
                    case 1:
                        result = "昨天" + gethourandmin(timesamp);
//                        result = "昨天";
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayofMONTH = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayofMONTH = todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if (dayofMONTH == todayofMONTH) {//表示是同一周
                            int dayofweek = otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if (dayofweek != 1) {//判断当前是不是星期日   如想显示为：周日 12:09 可去掉此判断
                                result = daynames[otherCalendar.get(Calendar.DAY_OF_WEEK) - 1] + gethourandmin(timesamp);
                            } else {
                                result = gettime(timesamp, timeformat);
                            }
                        } else {
                            result = gettime(timesamp, timeformat);
                        }
                        break;
                    default:
                        result = gettime(timesamp, timeformat);
                        break;
                }
            } else {
                result = gettime(timesamp, timeformat);
            }
        } else {
            result = getYEARtime(timesamp, YEARtimeformat);
        }
        return result;
    }

    /**
     * 当天的显示时间格式
     *
     * @param time
     * @return
     */
    public static String gethourandmin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 当天的显示时间格式
     *
     * @param time
     * @return
     */
    public static String getDayhourandmin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd hh:mm");
        return format.format(new Date(time));
    }

    /**
     * 当天的显示时间格式
     *
     * @param time
     * @return
     */
    public static String gethouran(long time) {
        SimpleDateFormat format = new SimpleDateFormat("hh");
        return format.format(new Date(time));
    }


    /**
     * 不同一周的显示时间格式
     *
     * @param time
     * @param timeformat
     * @return
     */
    public static String gettime(long time, String timeformat) {
        SimpleDateFormat format = new SimpleDateFormat(timeformat);
        return format.format(new Date(time));
    }

    /**
     * 不同年的显示时间格式
     *
     * @param time
     * @param YEARtimeformat
     * @return
     */
    public static String getYEARtime(long time, String YEARtimeformat) {
        SimpleDateFormat format = new SimpleDateFormat(YEARtimeformat);
        return format.format(new Date(time));
    }

    /**
     * 获取精确到秒的时间戳
     * @return
     */
    public static int getSecondTimestampTwo(){
        Date date= new Date();
        String timestamp = String.valueOf(date.getTime()/1000);
        return Integer.valueOf(timestamp);
    }

    /**
     *
     * @param dates 根据日期(yyyy-MM-dd)获取星期几
     * @return
     */
    public static String getWeek(String dates){
        String value=null;
        java.text.DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date=null;
        try {
            date=formatter.parse(dates);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        GregorianCalendar calendar=new GregorianCalendar();
        calendar.setTime(date);
        System.out.println(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
        int v=calendar.get(Calendar.DAY_OF_WEEK);
        switch (v){
            case 1:
                value="周日";
                break;
            case 2:
                value="周一";
                break;
            case 3:
                value="周二";
                break;
            case 4:
                value="周三";
                break;
            case 5:
                value="周四";
                break;
            case 6:
                value="周五";
                break;
            case 7:
                value="周六";
                break;
        }

        return value;
    }

    /**
     * 根据时间戳获取星期
     * @param time
     * @return
     */
    public static String getWeek(long time){
        return getWeek(getyyyy_MM_dd(time));
    }

}
