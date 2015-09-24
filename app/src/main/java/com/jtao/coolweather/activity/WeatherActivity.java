package com.jtao.coolweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jtao.coolweather.R;
import com.jtao.coolweather.util.HttpUtil;

/**
 * Created by jtaob on 2015/9/24.
 */
public class WeatherActivity extends Activity {

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
     * 用于显示气温2
     */
    private TextView temp2Text;
    /**
     * 用于显示当前日期
     */
    private TextView currentDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weather);

        //初始化各控件
        init();

        String cityName = getIntent().getStringExtra("city_name");

        if(TextUtils.isEmpty(cityName)){  //

        } else { //查询天气
            publicText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);

        }

    }


    private void queryWeatherInfo(String cityName){
        String address = getAddress(cityName);
    }

    private void queryFromServer(String address){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private String getAddress(String cityName){
        //http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=yourkey
    //http://api.map.baidu.com/telematics/v3/weather?location=" + cityName +"&mcode=AA:13:49:7E:3E:EA:59:70:E4:4C:CC:91:F2:FD:50:1E:68:C8:20:79;com.jtao.coolweather&output=json&ak=vX2ygLXn9Rhq8GOKHCcjbUs9";
        return "http://api.map.baidu.com/telematics/v3/weather?location=\" + cityName +\"&mcode=AA:13:49:7E:3E:EA:59:70:E4:4C:CC:91:F2:FD:50:1E:68:C8:20:79;com.jtao.coolweather&output=json&ak=vX2ygLXn9Rhq8GOKHCcjbUs9";
    }

    private void init() {
        weatherInfoLayout = (LinearLayout) findViewById(R.id.ll_info);
        cityNameText = (TextView) findViewById(R.id.tv_city_name);
        publicText = (TextView) findViewById(R.id.tv_public_text);
        weatherDespText = (TextView) findViewById(R.id.tv_weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
    }
}
