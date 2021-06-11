package com.swb.customerview;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * @auther 72774
 * @date 2021/6/5  14:15
 * @description
 */
public class App extends Application {

    private static Handler sHandler;
    private static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sHandler = new Handler(Looper.myLooper());
        sContext = getApplicationContext();

    }

    public static Context getContext(){
        return sContext;
    }

    public static Handler getHandler(){
        return sHandler;
    }
}
