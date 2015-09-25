package com.jtao.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.cardemulation.CardEmulation;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jtao.coolweather.model.Index;
import com.jtao.coolweather.model.Weather;
import com.jtao.coolweather.model.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by jtaob on 2015/9/24.
 */
public class Utility {
    /**
     * 解析服务器返回的json数据，并将解析出来的数据存储到本地
     *
     * @param response
     */
    public static Weather handleWeaeherResponse(String response) {
        Weather weather = new Weather();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getInt("error") != 0){
                //结果异常
                return null;
            }
            weather.setDate(jsonObject.getString("date")); //日期

            Log.e("ChooseAresActivity", response);

            JSONArray resultArray = jsonObject.getJSONArray("results");  //results 获取对象
            //JSONObject resultArray = jsonObject.getJSONObject("results");
            for(int i = 0; i < resultArray.length(); i++){
                JSONObject resultObj = (JSONObject) resultArray.get(i); //数组下只有一个对象

                weather.setCurrentCity(resultObj.getString("currentCity"));  //设置currentCity，当前城市
                weather.setPm25(resultObj.getString("pm25"));  //设置pm25
                //通过index获取一个数组，该数组中有5个对象，分别为穿衣，洗车，旅游，感冒，运动，紫外线强度
                JSONArray indexs = resultObj.getJSONArray("index");
                for (int k = 0; k < indexs.length(); k++) { //遍历index
                    JSONObject obj = (JSONObject) indexs.get(k);
                    //weather.setAttire(new Index(obj.getString("tipt"), obj.getString("des"), obj.getString("zs")));
                    weather.indexMap.put(obj.getString("tipt"), new Index(obj.getString("tipt"), obj.getString("des"), obj.getString("zs")));  //放入指数数据，key为tipt值
                }
                //通过weather_data获取一个数据，该数组中有4个对象，分别为今天，明天，后天，大后天的天气情况，这里只用今天，明天，后天，三天的信息
                JSONArray weather_data = resultObj.getJSONArray("weather_data");
                for (int j = 0; j < 3; j++) { //遍历weather_data
                    JSONObject obj = (JSONObject) weather_data.get(j);
                    weather.weatherDatas.add(new WeatherData(obj.getString("date"), obj.getString("dayPictureUrl"), obj.getString("nightPictureUrl"), obj.getString("weather"), obj.getString("wind"), obj.getString("temperature")));
                }

            }

            return weather;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将天气信息保存在SharedPreferences
     * @param context
     */
    public static void saveWeatherInfo(Context context, Weather weather){

        //Attire, Car, Tour, Ill, Exercise, Light;

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        //存入基本信息
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", weather.getCurrentCity());
        editor.putString("data_date", weather.getDate());
        editor.putString("pm25", weather.getPm25());
        editor.putLong("my_date", System.currentTimeMillis());
        editor.commit();

        //指数相关数据
        saveIndex(weather.indexMap.get("穿衣指数"), editor);
        saveIndex(weather.indexMap.get("洗车指数"), editor);
        saveIndex(weather.indexMap.get("旅游指数"), editor);
        saveIndex(weather.indexMap.get("感冒指数"), editor);
        saveIndex(weather.indexMap.get("运动指数"), editor);
        saveIndex(weather.indexMap.get("紫外线强度指数"), editor);

        //天气信息相关
        saveWeatherData(weather.weatherDatas.get(0), editor, "today");
        saveWeatherData(weather.weatherDatas.get(1), editor, "tomorrow");
        saveWeatherData(weather.weatherDatas.get(2), editor, "after");


    }

    private static void saveWeatherData(WeatherData wd, SharedPreferences.Editor editor, String key){
        editor.putString(key + "_date", wd.date);
        editor.putString(key + "_dayPictureUrl", wd.dayPictureUrl);
        editor.putString(key + "_nightPictureUrl", wd.nightPictureUrl);
        editor.putString(key + "_weather", wd.weather);
        editor.putString(key + "_wind", wd.wind);
        editor.putString(key + "_temperature", wd.temperature);
    }

    private static void saveIndex(Index index, SharedPreferences.Editor editor){
        String title = index.title;
        editor.putString(title + "_title", index.title);
        editor.putString(title + "_zs", index.zs);
        editor.putString(title + "_desc", index.desc);
        editor.commit();
    }
}
