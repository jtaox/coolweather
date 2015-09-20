package com.jtao.coolweather.model;

/**
 * Created by Tap on 2015/9/19.
 */
public class Province {

    private String quName;
    private String pyName;
    private String CityName;

    public String getQuName() {
        return quName;
    }

    public void setQuName(String quName) {
        this.quName = quName;
    }

    public String getPyName() {
        return pyName;
    }

    public void setPyName(String pyName) {
        this.pyName = pyName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    @Override
    public String toString() {
        return "----->Province{" +
                "quName='" + quName + '\'' +
                ", pyName='" + pyName + '\'' +
                ", CityName='" + CityName + '\'' +
                '}';
    }
}
