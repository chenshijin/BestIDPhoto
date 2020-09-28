package com.csj.bestidphoto.utils;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AntiShakeUtil {
    private List<OneClickUtil> utils = new ArrayList<>();

    public boolean check(Object o) {
        String flag = null;
        if(o == null)
            flag = Thread.currentThread().getStackTrace()[2].getMethodName();
        else
            flag = o.toString();
        for (OneClickUtil util : utils) {
            if (util.getMethodName().equals(flag)) {
                return util.check();
            }
        }
        OneClickUtil clickUtil = new OneClickUtil(flag);
        utils.add(clickUtil);
        return clickUtil.check();
    }

    public boolean check() {
        return check(null);
    }

    public class OneClickUtil {
        private String methodName;
        public static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime = 0;

        public OneClickUtil(String methodName) {
            this.methodName = methodName;
        }

        public String getMethodName() {
            return methodName;
        }

        public boolean check() {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                return false;
            } else {
                return true;
            }
        }
    }
}
