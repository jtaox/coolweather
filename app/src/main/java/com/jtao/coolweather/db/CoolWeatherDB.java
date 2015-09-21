package com.jtao.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jtao.coolweather.model.City;
import com.jtao.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tap on 2015/9/19.
 */

public class CoolWeatherDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "cool_weather";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase db;

    /**
     * 构造方法私有化
     * @param context
     */
    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB实例
     * @param context
     * @return
     */
    public synchronized static CoolWeatherDB getInstance(Context context){
        if(coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 保存Province实例
     * @param province
     */

    public void saveProvince(Province province){
        if(province != null){
            ContentValues values = new ContentValues();
            values.put("quName", province.getQuName());
            values.put("pyName", province.getPyName());
            values.put("cityName", province.getCityName());
            Log.d("ChooseAreaActivity", "province db 是空 ？==" + (db == null ? "为空" : "有数据"));
            db.insert("Province", null, values);
        }
    }


    /**
     * 获取所有省份
     * @return 包含所有省份的List集合
     */
    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setQuName(cursor.getString(cursor.getColumnIndex("quName")));
                province.setPyName(cursor.getString(cursor.getColumnIndex("pyName")));
                province.setCityName(cursor.getString(cursor.getColumnIndex("cityName")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        if(cursor != null){
            cursor.close();
        }
        return list;
    }

    /**
     * 将City实例存储到数据库
     * @param city
     */
    public void saveCity(City city){
        if(city != null){
            ContentValues values = new ContentValues();
            Log.d("ChooseAreaActivity", "contentvalues :" + city.toString());
            values.put("city_name", city.getCityName());
            values.put("pyProvinceName", city.getPyProvinceName());
            values.put("pyName", city.getPyName());
            Log.d("ChooseAreaActivity", "City db 是空 ？==" + (db == null ? "为空" : "有数据"));
            db.insert("City", null, values);
        }
    }

    /**
     *获取指定province下所有city信息
     * @param provinceId
     * @return
     */
    public List<City> loadCities(int provinceId){
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City", null, "province_dd = ?", new String[]{String.valueOf(provinceId)}, null, null, null); //查询该Province下所有城市信息
        if(cursor.moveToFirst()){
            do {
                City city = new City();
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setPyProvinceName(cursor.getString(cursor.getColumnIndex("pyProvinceName")));
                city.setPyName(cursor.getString(cursor.getColumnIndex("pyName")));
                list.add(city);
            } while (cursor.moveToNext());
        }
        if(cursor != null){
            cursor.close();
        }
        return list;
    }


    public void close(){
        db.close();
        db = null;
    }

  /*  public void saveCounty(County county){
        ContentValues values = new ContentValues();
        values.put("county_name", county.getCountyName());
        values.put("county_code", county.getCountyCode());
        values.put("city_id", county.getCityid());
        db.insert("County", null, values);
    }

    public List<County> loadCounties(int cityId){
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
        if(cursor.moveToFirst()){
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityid(String.valueOf(cityId));
            } while(cursor.moveToNext());
        }
        if(cursor != null){
            cursor.close();
        }
        return list;

    }*/
}
