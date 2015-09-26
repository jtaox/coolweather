package com.jtao.coolweather.util;

import android.util.Log;

import com.jtao.coolweather.activity.HttpCallbackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Tap on 2015/9/19.
 */
public class HttpUtil {


    public static String getXMLStringWithConnection(String address) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(address);
            //打开连接
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(8000);
            connection.setConnectTimeout(8000);

            InputStream is = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuffer response = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }


            return response.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onError(e);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
            }
        }).start();
    }

    /**
     * 通过传入带有经纬度的hashmap集合，转为一个可以解析为城市名的api
     * @param coord
     * @return
     */
    public static String cityNameFromCoord(HashMap<String, Double> coord) {
        /**
         * coordinate.put("latitude", location.getLatitude());

         //经度
         coordinate.put("longitude", location.getLongitude());
         */

        double latitude = coord.get("latitude");
        double longitude = coord.get("longitude");

        return "http://api.map.baidu.com/telematics/v3/reverseGeocoding?location=" + longitude + "," + latitude + "&mcode=AA:13:49:7E:3E:EA:59:70:E4:4C:CC:91:F2:FD:50:1E:68:C8:20:79;com.jtao.coolweather&output=json&coord_type=gcj02&ak=vX2ygLXn9Rhq8GOKHCcjbUs9";
    }

    public static String parseCityName(String jsonData){
        String cityName = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if("Success".equals(jsonObject.getString("status"))){ //数据获取正常
                cityName = jsonObject.getString("city");

                if(cityName.endsWith("市")){
                    cityName = cityName.substring(0, cityName.length() - 1);
                }

            } else {
                cityName = "经纬度解析失败";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return cityName;
        }
        Log.d("ChooseAreaActivity", "定位获取的城市是：" + cityName);
        return cityName;
    }

}
