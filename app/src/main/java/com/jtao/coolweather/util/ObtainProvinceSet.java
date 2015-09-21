package com.jtao.coolweather.util;

import android.os.AsyncTask;

import com.jtao.coolweather.activity.MyApplication;
import com.jtao.coolweather.db.CoolWeatherDB;
import com.jtao.coolweather.model.Province;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Tap on 2015/9/20.
 */
public class ObtainProvinceSet extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        //地址
        final String address = params[0];


        String response = HttpUtil.getXMLStringWithConnection(address);

        return response == null ? null : response;


    }



    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(s == null){
            return;
        }

        CoolWeatherDB db = CoolWeatherDB.getInstance(MyApplication.getContext());
        List<Province> provinces = XmlParser.parserProvinceXMLWithPull(s);
        for(Province province : provinces){
            db.saveProvince(province);
        }
        //db.close();
    }
}
