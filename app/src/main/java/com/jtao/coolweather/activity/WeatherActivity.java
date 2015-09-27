package com.jtao.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jtao.coolweather.R;
import com.jtao.coolweather.model.Weather;
import com.jtao.coolweather.util.HttpUtil;
import com.jtao.coolweather.util.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jtaob on 2015/9/24.
 */
public class WeatherActivity extends Activity implements View.OnClickListener {

    private LinearLayout weatherInfoLayout;

    /**
     * 用于显示城市名
     */
    private TextView cityNameText;
    /**
     * 用于显示发布时间
     */
    private TextView publicText;
    /**
     * 用于显示天气描述信息
     */
    private TextView weatherDespText;
    /**
     * 用于显示气温1
     */
    private TextView temp1Text;
    /**
     * 用于显示当前日期
     */
    private TextView currentDateText;
    /**
     * 切换城市按钮
     */
    private Button switchCity;
    /**
     * 刷新天气按钮
     */
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weather);

        //初始化各控件
        init();

        String cityName = getIntent().getStringExtra("city_name");

        if (TextUtils.isEmpty(cityName)) {  //
            //showWeather();
            //修改为打开自动更新
            //queryWeatherInfo(Utility.obtainSPSCityName(WeatherActivity.this));
            String cn = Utility.obtainSPSCityName(WeatherActivity.this);
            if(!TextUtils.isEmpty(cn)){
                queryWeatherInfo(cn);
            }

        } else { //查询天气
            publicText.setText("同步中...");
            Log.d("ChooseAreaActivity", "----->" + cityName + "<----");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            Log.d("ChooseAreaActivity", "地址：" + getAddress(cityName));
            queryWeatherInfo(cityName);
        }

    }


    /**
     * 传入城市名，查询天气
     * @param cityName
     */
    private void queryWeatherInfo(String cityName) {
        String address = getAddress(cityName);
        queryFromServer(address);
    }

    private void queryFromServer(String address) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("ChooseAreaActivity", "----->" + response + "<----");
                final Weather weather = Utility.handleWeatherResponst(response);
                if (weather == null) {
                    //服务器返回结果异常
                    Log.d("ChooseAreaActivity", "服务器返回数据异常");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            publicText.setText("同步失败，数据异常");
                        }
                    });
                } else {
                    Log.d("ChooseAreaActivity", "weather ----->" + weather.toString() + "<----");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utility.saveWeatherInfo(WeatherActivity.this, weather);
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    /**
     * 从SharedPreferences中读取天气信息，并显示到界面
     */
    private void showWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name", ""));
        temp1Text.setText(prefs.getString("today_temperature", ""));
        weatherDespText.setText(prefs.getString("today_weather", ""));
       //publicText.setText("今天" + prefs.getString("today_date", ""));
        SimpleDateFormat sdf = new SimpleDateFormat("M月d日k点m分", Locale.CHINA);
        publicText.setText("更新于" + sdf.format(new Date(prefs.getLong("my_date", 0))));
        currentDateText.setText(prefs.getString("today_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }

    private String getAddress(String cityName) {
        //http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=yourkey
        //http://api.map.baidu.com/telematics/v3/weather?location=" + cityName +"&mcode=AA:13:49:7E:3E:EA:59:70:E4:4C:CC:91:F2:FD:50:1E:68:C8:20:79;com.jtao.coolweather&output=json&ak=vX2ygLXn9Rhq8GOKHCcjbUs9";
        String encodedName = encode(cityName);
        if(encodedName == null){
            return null;
        }
        return "http://api.map.baidu.com/telematics/v3/weather?location=" + encodedName + "&mcode=AA:13:49:7E:3E:EA:59:70:E4:4C:CC:91:F2:FD:50:1E:68:C8:20:79;com.jtao.coolweather&output=json&ak=vX2ygLXn9Rhq8GOKHCcjbUs9";
    }

    private String encode(String name){
        try {
            return URLEncoder.encode(name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void init() {
        weatherInfoLayout = (LinearLayout) findViewById(R.id.ll_info);
        currentDateText = (TextView) findViewById(R.id.tv_current_date);
        cityNameText = (TextView) findViewById(R.id.tv_city_name);
        publicText = (TextView) findViewById(R.id.tv_public_text);
        weatherDespText = (TextView) findViewById(R.id.tv_weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);

        refreshWeather = (Button) findViewById(R.id.refresh_weather);
        switchCity = (Button) findViewById(R.id.switch_city);
        refreshWeather.setOnClickListener(this);
        switchCity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_city: //切换城市
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather: //刷新天气
                publicText.setText("同步中...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String cityName = prefs.getString("city_name", ""); //获取配置文件中存储的城市名，根据城市名查询天气
                if(!TextUtils.isEmpty(cityName)){
                    queryWeatherInfo(cityName);
                    Toast.makeText(this, "正在向服务端请求数据...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
