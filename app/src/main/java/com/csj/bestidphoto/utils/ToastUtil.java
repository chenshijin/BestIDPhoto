package com.csj.bestidphoto.utils;

import android.widget.Toast;

import com.csj.bestidphoto.MApp;

import java.util.Timer;
import java.util.TimerTask;


public class ToastUtil {

    private static Toast sToast = null;

    /**
     * 全局Toast
     *
     * @param msg
     * @param duration
     */
    public static Toast show(String msg, int duration) {
        try {
            if (sToast == null) {
                sToast = Toast.makeText(MApp.getInstance(), msg, duration);
            } else {
                sToast.cancel();
                sToast = null;
                sToast = Toast.makeText(MApp.getInstance(), msg, duration);
            }
            sToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sToast;

    }

    /**
     * 全局Toast
     *
     * @param msg
     * @param duration
     */
    public static Toast show(int msg, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(MApp.getInstance(), msg, duration);
        } else {
            sToast.cancel();
            sToast = null;
            sToast = Toast.makeText(MApp.getInstance(), msg, duration);
        }
        sToast.show();
        return sToast;
    }

    public static Toast showLong(String msg) {
        return show(msg, Toast.LENGTH_LONG);
    }


    public static Toast showShort(String msg) {
        return show(msg, Toast.LENGTH_SHORT);
    }

    public static Toast showLong(int msg) {
        return show(msg, Toast.LENGTH_LONG);
    }


    /**
     * @param toast    Toast对象 需要设置为Toast.LENGTH_LONG
     * @param showtime 第一个参数为我们创建的Toast对象，第二个参数为我们想要设置显示的毫秒数！
     */
    public static void showMyToast(final Toast toast, final int showtime) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3500);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, showtime);
    }


}
