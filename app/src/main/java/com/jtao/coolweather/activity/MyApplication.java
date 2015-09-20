package com.jtao.coolweather.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tap on 2015/9/20.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
