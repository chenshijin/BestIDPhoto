package com.maoti.lib;

import android.content.Context;

public class BaseApplication {

    private static Context context;

    public static void initBaseApplication(Context _context){
        context=_context;
    }

    public static Context getContext(){
        return context;
    }

}
