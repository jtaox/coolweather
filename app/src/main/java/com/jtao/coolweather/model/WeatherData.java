package com.jtao.coolweather.model;

/**
 * Created by jtaob on 2015/9/25.
 */
/**
 * 具体天气数据类
 */
public class WeatherData{
    public WeatherData(String date, String dayPictureUrl, String nightPictureUrl, String weather, String wind, String temperature) {
        this.date = date;
        this.dayPictureUrl = dayPictureUrl;
        this.nightPictureUrl = nightPictureUrl;
        this.weather = weather;
        this.wind = wind;
        this.temperature = temperature;
    }

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
