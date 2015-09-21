package com.jtao.coolweather.util;

import com.jtao.coolweather.activity.HttpCallbackListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener){

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

                    while((line = reader.readLine()) != null){
                        response.append(line);
                    }

                    if(listener !=null){
                        listener.onFinish(response.toString());
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    if(listener != null){
                        listener.onError(e);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if(listener != null){
                        listener.onError(e);
                    }
                }
            }
        }).start();

    }
}
