package com.jtao.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jtao.coolweather.service.AutoUpdateService;

/**
 * Created by jtaob on 2015/9/27.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateService.class);  //当接收到广播时，再次执行服务
        context.startService(intent);
        Log.d("Sea", "开启服务");
    }
}
