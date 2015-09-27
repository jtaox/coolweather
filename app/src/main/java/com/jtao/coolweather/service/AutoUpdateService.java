package com.jtao.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.jtao.coolweather.activity.HttpCallbackListener;
import com.jtao.coolweather.model.Weather;
import com.jtao.coolweather.util.HttpUtil;
import com.jtao.coolweather.util.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by jtaob on 2015/9/27.
 */
public class AutoUpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHout= 1000 * 60 * 60 * 8; //一小时
        long triggerAtTime = SystemClock.elapsedRealtime() + anHout;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String cityName = "";
        try {
            cityName = URLEncoder.encode(preferences.getString("city_name", ""), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String address = "http://api.map.baidu.com/telematics/v3/weather?location=" + cityName + "&mcode=AA:13:49:7E:3E:EA:59:70:E4:4C:CC:91:F2:FD:50:1E:68:C8:20:79;" +
         "com.jtao.coolweather&output=json&ak=vX2ygLXn9Rhq8GOKHCcjbUs9";

        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Weather weather = Utility.handleWeatherResponst(response);
                Utility.saveWeatherInfo(AutoUpdateService.this, weather);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
