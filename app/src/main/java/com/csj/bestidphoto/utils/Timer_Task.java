package com.csj.bestidphoto.utils;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时器
 * @author Administrator
 *
 */
public class Timer_Task {

    private Timer timer;
    private TimerTask task;
    private Handler handler;
    private int what;
    private long delay;
    private Object obj;
    private int model = 1;//1-单次定时   2-多次定时

    public Timer_Task(Handler handler, int what, long delay, Object obj, int model){
        this.handler=handler;
        this.what=what;
        this.delay=delay;
        this.obj=obj;
        this.model = model;
    }

    public void startTimer() {
        stopTimer();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = what;
                message.obj=getObj();
                handler.sendMessage(message);
            }
        };

        if(model == 1){
            timer.schedule(task, delay);//
        }else{
            timer.schedule(task, delay,1000);//
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

}
