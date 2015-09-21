package com.jtao.coolweather.util;

import android.os.AsyncTask;
import android.util.Log;

import com.jtao.coolweather.activity.MyApplication;
import com.jtao.coolweather.db.CoolWeatherDB;
import com.jtao.coolweather.model.City;

import java.util.List;

/**
 * Created by Tap on 2015/9/21.
 */
public class ObtainCitySet extends AsyncTask<String, Void, String> {

    private String pyProvinceName;

    @Override
    protected String doInBackground(String... params) {
        pyProvinceName = params[1];
        return HttpUtil.getXMLStringWithConnection(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        CoolWeatherDB db = CoolWeatherDB.getInstance(MyApplication.getContext());
        Log.d("ChooseAreaActivity", "----------------::>" + pyProvinceName);
        List<City> cityList = XmlParser.parserCityXMLWithPull(s, pyProvinceName);
        for (City city : cityList){
            Log.d("ChooseAreaActivity", city.toString());
            db.saveCity(city);
        }
        //db.close();
    }
}
