package com.jtao.coolweather.model;

/**
 * Created by jtaob on 2015/9/24.
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Weather {



    //Attire, Car, Tour, Ill, Exercise, Light;


    private String currentCity;
    private String date;
    private String pm25;

    public HashMap<String, Index> indexMap = new HashMap<>();
    public ArrayList<WeatherData> weatherDatas = new ArrayList<>();



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


    @Override
    public String toString() {
        return "Weather{" +
                ", currentCity='" + currentCity + '\'' +
                ", date='" + date + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", indexMap=" + indexMap +
                ", weatherDatas=" + weatherDatas +
                '}';
    }
}
