package com.jtao.coolweather.model;

/**
 * Created by Tap on 2015/9/19.
 */
public class City {
    private String cityName;
    private String pyName;
    private String pyProvinceName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPyProvinceName() {
        return pyProvinceName;
    }

    public void setPyProvinceName(String pyProvinceName) {
        this.pyProvinceName = pyProvinceName;
    }

    public String getPyName() {
        return pyName;
    }

    public void setPyName(String pyName) {
        this.pyName = pyName;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityName='" + cityName + '\'' +
                ", pyName='" + pyName + '\'' +
                ", pyProvinceName='" + pyProvinceName + '\'' +
                '}';
    }
}
