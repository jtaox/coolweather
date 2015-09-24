package com.jtao.coolweather.util;

import com.jtao.coolweather.model.Index;
import com.jtao.coolweather.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jtaob on 2015/9/24.
 */
public class Utility {
    /**
     * 解析服务器返回的json数据，并将解析出来的数据存储到本地
     *
     * @param response
     */
    public static void handleWeaeherResponse(String response) {

        /**
         * {
         error: 0,
         status: "success",
         date: "2013-07-17",
         results:
         [
         {
         currentCity: "北京市",
         weather_data:
         [
         {
         date: "周三(今天, 实时：24℃)",
         dayPictureUrl: "http://api.map.baidu.com/images/weather/day/duoyun.png",
         nightPictureUrl: "http://api.map.baidu.com/images/weather/night/duoyun.png",
         weather: "多云",
         wind: "微风",
         temperature: "23℃~ 15℃"
         },
         {
         date: "明天（周四）",
         dayPictureUrl: "http://api.map.baidu.com/images/weather/day/leizhenyu.png",
         nightPictureUrl: "http://api.map.baidu.com/images/weather/night/zhongyu.png",
         weather: "雷阵雨转中雨",
         wind: "微风",
         temperature: "29～22℃"
         },
         {
         date: "后天（周五）",
         dayPictureUrl: "http://api.map.baidu.com/images/weather/day/yin.png",
         nightPictureUrl: "http://api.map.baidu.com/images/weather/night/duoyun.png",
         weather: "阴转多云",
         wind: "微风",
         temperature: "31～23℃"
         },
         {
         date: "大后天（周六）",
         dayPictureUrl: "http://api.map.baidu.com/images/weather/day/duoyun.png",
         nightPictureUrl: "http://api.map.baidu.com/images/weather/night/duoyun.png",
         weather: "多云",
         wind: "微风",
         temperature: "31～24℃"
         }
         ]
         },
         {
         currentCity: "合肥市",
         weather_data:
         [
         {
         date: "今天（周三）",
         dayPictureUrl: "http://api.map.baidu.com/images/weather/day/duoyun.png",
         nightPictureUrl: "http://api.map.baidu.com/images/weather/night/duoyun.png",
         weather: "多云",
         wind: "东风3-4级",
         temperature: "27℃"
         },
         {
         date: "明天（周四）",
         dayPictureUrl: "http://api.map.baidu.com/images/weather/day/duoyun.png",
         nightPictureUrl: "http://api.map.baidu.com/images/weather/night/duoyun.png",
         weather: "多云",
         wind: "东北风3-4级",
         temperature: "35～27℃"
         },
         {
         date: "后天（周五）",
         dayPictureUrl: "http://api.map.baidu.com/images/weather/day/duoyun.png",
         nightPictureUrl: "http://api.map.baidu.com/images/weather/night/duoyun.png",
         weather: "多云",
         wind: "南风",
         temperature: "35～27℃"
         },
         {
         date: "大后天（周六）",
         dayPictureUrl: "http://api.map.baidu.com/images/weather/day/duoyun.png",
         nightPictureUrl: "http://api.map.baidu.com/images/weather/night/duoyun.png",
         weather: "多云",
         wind: "东风",
         temperature: "34～27℃"
         }
         ]
         }
         ]
         }
         *
         */
        Weather weather = new Weather();
        try {
            JSONObject jsonObject = new JSONObject(response);
            weather.setDate(jsonObject.getString("date")); //日期
            JSONObject results = jsonObject.getJSONObject("results");  //results 获取对象
            weather.setCurrentCity(results.getString("currentCity"));  //设置currentCity，当前城市
            weather.setPm25(results.getString("pm25"));  //设置pm25
            //通过index获取一个数组，该数组中有5个对象，分别为穿衣，洗车，旅游，感冒，运动，紫外线强度
            JSONArray indexs = results.getJSONArray("index");
            for (int i = 0; i < indexs.length(); i++) { //遍历index
             JSONObject obj = (JSONObject) indexs.get(i);
              weather.setAttire(new Index(obj.getString("tipt"), obj.getString("des"), obj.getString("zs")));
            }
            //通过weather_data获取一个数据，该数组中有4个对象，分别为今天，明天，后天，大后天的天气情况，这里只用今天，明天，后天，三天的信息
            JSONArray weather_data = results.getJSONArray("weather_data");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
