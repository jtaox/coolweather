package com.jtao.coolweather.util;

import android.os.AsyncTask;
import android.util.Log;

import com.jtao.coolweather.activity.MyApplication;
import com.jtao.coolweather.db.CoolWeatherDB;
import com.jtao.coolweather.model.Province;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by Tap on 2015/9/20.
 */
public class ObtainCitySet extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        //地址
        final String address = params[0];


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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        CoolWeatherDB db = CoolWeatherDB.getInstance(MyApplication.getContext());
        List<Province> provinces = XmlParser.parserProvinceXMLWithPull(s);
        for(Province province : provinces){
            db.saveProvince(province);
        }
        db.close();
    }
}
