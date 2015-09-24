package com.jtao.coolweather.model;

/**
 * Created by jtaob on 2015/9/24.
 */

/**
 *    error: 0,
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
 */
public class Weather {
    //Attire, Car, Tour, Ill, Exercise, Light;

    public String index;

    private String currentCity;
    private String date;
    private String pm25;
    /**
     * 穿衣指数
     */
    public Index attire;
    /**
     * 洗车指数
     */
    public Index Car;
    /**
     * 旅游指数
     */
    public Index Tour;
    /**
     * 生病指数
     */
    public Index Ill;
    /**
     * 运动指数
     */
    public Index Exercise;
    /**
     * 紫外线指数
     */
    public Index Light;
    /**
     * 今天
     */
    public WeatherData today;
    /**
     * 明天
     */
    public WeatherData tomorrow;
    /**
     * 后天
     */
    private WeatherData after;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public void setAttire(Index attire) {
        this.attire = attire;
    }

    public void setCar(Index car) {
        Car = car;
    }

    public void setTour(Index tour) {
        Tour = tour;
    }

    public void setIll(Index ill) {
        Ill = ill;
    }

    public void setExercise(Index exercise) {
        Exercise = exercise;
    }

    public void setLight(Index light) {
        Light = light;
    }

    /**
     * 具体天气数据类
     */
    class WeatherData{
        /**
         * 时间，如果是当天，有实时温度
         */
        public String date;
        /**
         * 白天天气图片URL
         */
        public String dayPictureUrl;
        /**
         * 晚上天气图片URL
         */
        public String nightPictureUrl;
        /**
         * 天气
         */
        public String weather;
        /**
         * 风
         */
        public String wind;
        /**
         * 温度
         */
        public String temperature;
    }

    /**
     * 生活指数类
     */

}
