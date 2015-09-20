package com.jtao.coolweather.activity;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.jtao.coolweather.R;
import com.jtao.coolweather.util.HttpUtil;
import com.jtao.coolweather.util.LocationUtil;
import com.jtao.coolweather.util.ObtainCitySet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by Tap on 2015/9/19.
 */
public class ChooseAreaActivity extends Activity {

    private MyHttpCallbackListener listener;

    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choosearea);

        listener = new MyHttpCallbackListener();

        final LocationUtil lu = new LocationUtil();

        Location location = lu.requestLocation(this);

        ////////
        new ObtainCitySet().execute("http://flash.weather.com.cn/wmaps/xml/china.xml");
        ///////

        try {
            str = URLEncoder.encode("晋城", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(location != null){
            Log.d("ChooseAreaActivity", "----->直接获取位置信息");

            String address = getAddress(location2coordinate(location));

            HttpUtil.sendHttpRequest(address, listener);
        } else {
            lu.requestLocationUpdates(new ObtainLocationListener() {
                @Override
                public void locationUpdates(Location location) {
                    if(location != null){
            Log.d("ChooseAreaActivity", "----->通过监听位置更新，获取信息");
                        String address = getAddress(location2coordinate(location));
                       HttpUtil.sendHttpRequest(address, listener);
                        lu.removeLocationUpdates();
                        Log.d("ChooseAreaActivity", "关闭监听位置更新");
                    }
                }
            });
        }

    }

    class MyHttpCallbackListener implements HttpCallbackListener{

        @Override
        public void onFinish(String response) {
            Log.d("ChooseAreaActivity", response);
        }

        @Override
        public void onError(Exception e) {

        }
    }

    /**
     * 通过location获取经纬度信息
     * @param location
     */
    private HashMap<String, Double> location2coordinate(Location location){
        HashMap<String, Double> coordinate = new HashMap<>();

        //纬度
        coordinate.put("latitude", location.getLatitude());

        //经度
        coordinate.put("longitude", location.getLongitude());

        return coordinate;
    }

    private String getAddress(HashMap<String, Double> coord){
        double latitude = coord.get("latitude");
        double longitude = coord.get("longitude");

        String address = "http://api.map.baidu.com/telematics/v3/weather?location=" + longitude + "," + latitude +"&mcode=AA:13:49:7E:3E:EA:59:70:E4:4C:CC:91:F2:FD:50:1E:68:C8:20:79;com.jtao.coolweather&output=json&ak=vX2ygLXn9Rhq8GOKHCcjbUs9";
    Log.d("ChooseAreaActivity", "address--->" + address);
        return address;

    }
}
