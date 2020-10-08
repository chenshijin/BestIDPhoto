package com.maoti.lib.event;

import java.io.Serializable;

public class ClearUnReadEvent implements Serializable {
    private int type;//4-消除球迷圈消息红点

    public ClearUnReadEvent(int type){
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
