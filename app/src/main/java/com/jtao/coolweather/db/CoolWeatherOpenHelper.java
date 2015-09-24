package com.jtao.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tap on 2015/9/19.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    /**
     * province建表语句
     */
    public static final String CREATE_PROVINCE = "create table Province(id integer primary key autoincrement, quName text, pyName text, cityName text)";

    public static final String CREATE_CITY = "create table City(id integer primary key autoincrement, city_name text, pyProvinceName text, pyName text)";

    //暂时取消县级
    //public static final String CREATE_COUNTY = "create table County(id integer primary key autoincrement, county_name text, count_code text, city_id integer)";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        //db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
