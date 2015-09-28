package com.jtao.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jtao.coolweather.R;
import com.jtao.coolweather.db.CoolWeatherDB;
import com.jtao.coolweather.model.City;
import com.jtao.coolweather.model.Province;
import com.jtao.coolweather.util.HttpUtil;
import com.jtao.coolweather.util.LocationUtil;
import com.jtao.coolweather.util.XmlParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tap on 2015/9/19.
 */
public class ChooseAreaActivity extends Activity {

    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_CITY = 2;

    //UI相关
    private TextView tv_prompt;
    private TextView tv_title;
    private ListView lv_list;

    private ProgressDialog progressDialog;

    private boolean isFromWeatherActivity;

    /**
     * 省列表
     */
    private List<Province> provinceList;
    /**
     * 市列表
     */
    private List<City> cityList;
    /**
     * 选中的省
     */
    private Province selectedProvince;
    /**
     * 选中的市
     */
    private City selectedCity;
    /**
     * 选中的级别
     */
    private int currentLevel;

    /**
     * ListView数据集合
     */
    private List<String> dataList = new ArrayList<>();
    private ArrayAdapter<String> adapter;


    private CoolWeatherDB db;

    private MyRunnable myRunnable;

    private LocationUtil lu;

    private Location location;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    tv_prompt.setText("定位结果:" + (String) msg.obj);
                    break;
                case 2:
                    Toast.makeText(ChooseAreaActivity.this, "加载失败...", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_choosearea);

        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("city_selected", false)) { //如果已经选择了城市
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        init();
        queryProvinces(); //启动加载省级数据

        //开启定位TextView
        tv_prompt.setVisibility(View.VISIBLE);
        tv_prompt.setText("正在定位");

        lu = new LocationUtil();
        //获取location对象
        location = lu.requestLocation(this);

        if (location != null) {  //获取位置成功
            //Log.d("ChooseAreaActivity", "----->直接获取位置信息");

            String response = HttpUtil.cityNameFromCoord(location2coordinate(location));
            //HttpUtil.sendHttpRequest(address, listener);
            //设置TextView显示位置

            HttpUtil.sendHttpRequest(response, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    //获取服务器返回的字符串
                    String cityName = HttpUtil.parseCityName(response);
                    Message message = Message.obtain();
                    message.obj = cityName;
                    message.what = 1;
                    handler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {
                    Log.d("ChooseAreaActivity", "连接失败");
                }
            });

            tv_prompt.setText("");
        } else {  //未定位成功，请求开启位置更新功能
            lu.requestLocationUpdates(new ObtainLocationListener() {
                @Override
                public void locationUpdates(Location location) {
                    if (location != null) {
                        Log.d("ChooseAreaActivity", "----->通过监听位置更新，获取信息");
                        String address = getAddress(location2coordinate(location));
                        //HttpUtil.sendHttpRequest(address, listener);
                        lu.removeLocationUpdates();   //关闭更新
                        Log.d("ChooseAreaActivity", "关闭监听位置更新");
                        tv_prompt.setText(address);  //显示定位结果
                    }
                }
            });
        }
        myRunnable = new MyRunnable();
        handler.postDelayed(myRunnable, 10000);


        ////////
        //new ObtainProvinceSet().execute("http://flash.weather.com.cn/wmaps/xml/china.xml");
        //new ObtainCitySet().execute("http://flash.weather.com.cn/wmaps/xml/shanxi.xml", "shanxi");
        ///////

    }

    private class MyRunnable implements Runnable {
        public void run() {
            //10秒后进行判断
            if (location == null && lu.isLocationListener) {
            tv_prompt.setText("好像定位失败了哦");
                lu.removeLocationUpdates();
            }
        }
    }

    /**
     * 初始化
     */
    private void init() {
        tv_prompt = (TextView) findViewById(R.id.tv_prompt);
        tv_title = (TextView) findViewById(R.id.tv_title);
        lv_list = (ListView) findViewById(R.id.lv_list);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);

        lv_list.setAdapter(adapter);

        db = CoolWeatherDB.getInstance(this);

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCity();
                } else if (currentLevel == LEVEL_CITY) {
                    String cityName = cityList.get(position).getCityName();
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("city_name", cityName);
                    startActivity(intent);
                    finish();
                }

            }
        });

        tv_prompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"经纬度解析失败".equals(tv_prompt.getText())) {
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    String city_name = tv_prompt.getText().toString();

                    intent.putExtra("city_name", city_name.split(":")[1]);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     * 查询指定省下所有城市名，优先从数据库中查找，如果没有数据，则从接口获取
     */
    private void queryCity() {
        cityList = db.loadCities(selectedProvince.getPyName());
        if (cityList.size() > 0) {  //数据库中有数据
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            dataChanged(selectedProvince.getQuName(), LEVEL_CITY);
        } else {
            //从服务器获取
            queryFromServer(selectedProvince.getPyName(), "city");
        }
    }

    /**
     * 查询全国所有省，优先从数据库中查，如果没有数据，则从接口获取
     */
    private void queryProvinces() {
        provinceList = db.loadProvinces();
        if (provinceList.size() > 0) { //数据库中有数据
            dataList.clear();
            Log.d("ChooseAreaActivity", "provinces size -->" + provinceList.size());
            for (Province province : provinceList) {
                dataList.add(province.getQuName());
            }
            dataChanged("中国", LEVEL_PROVINCE);
            //currentLevel = LEVEL_PROVINCE;
        } else {
            //通过服务器获取
            queryFromServer(null, "province");
            /*new ObtainProvinceSet().execute("http://flash.weather.com.cn/wmaps/xml/china.xml");
            provinceList = db.loadProvinces();
            if (provinceList.size() > 0) {
                for (Province province : provinceList) {
                    dataList.add(province.getQuName());
                }
                dataChanged("中国", LEVEL_PROVINCE);
            }*/
        }
    }

    /**
     * 通知adapter数据改变、设置title文本..
     *
     * @param title
     * @param level
     */
    private void dataChanged(String title, int level) {
        /*if (level != LEVEL_PROVINCE) {
            dataList.clear();
        }*/
        adapter.notifyDataSetChanged(); //通知数据集合改变
        lv_list.setSelection(0); //将光标移置第一位
        tv_title.setText(title);
        currentLevel = level;
    }


    /**
     * 通过location获取经纬度信息
     *
     * @param location
     */
    private HashMap<String, Double> location2coordinate(Location location) {
        HashMap<String, Double> coordinate = new HashMap<>();

        //纬度
        coordinate.put("latitude", location.getLatitude());

        //经度
        coordinate.put("longitude", location.getLongitude());

        return coordinate;
    }

    private String getAddress(HashMap<String, Double> coord) {
        double latitude = coord.get("latitude");
        double longitude = coord.get("longitude");

        String address = "http://api.map.baidu.com/telematics/v3/weather?location=" + longitude + "," + latitude + "&mcode=AA:13:49:7E:3E:EA:59:70:E4:4C:CC:91:F2:FD:50:1E:68:C8:20:79;com.jtao.coolweather&output=json&ak=vX2ygLXn9Rhq8GOKHCcjbUs9";
        Log.d("ChooseAreaActivity", "address--->" + address);
        return address;

    }

    private void queryFromServer(final String provinceName, final String type) {

        final String address;
        if (!TextUtils.isEmpty(provinceName)) {
            address = "http://flash.weather.com.cn/wmaps/xml/" + provinceName.trim() + ".xml";
        } else {
            address = "http://flash.weather.com.cn/wmaps/xml/china.xml";
        }
        showProgressDialog();  //显示对话框
        dataList.clear();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("province".equals(type)) {  //省份
                    provinceList = XmlParser.parserProvinceXMLWithPull(response);
                    for (Province province : provinceList) {
                        dataList.add(province.getQuName());
                        db.saveProvince(province);
                    }
                    //adapter.notifyDataSetChanged(); //防止异常
                    handler.sendEmptyMessage(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            dataChanged("中国", LEVEL_PROVINCE);
                        }
                    });

                } else if ("city".equals(type)) { //城市
                    cityList = XmlParser.parserCityXMLWithPull(response, provinceName);
                    for (City city : cityList) {
                        dataList.add(city.getCityName());
                        db.saveCity(city);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            dataChanged(selectedProvince.getQuName(), LEVEL_CITY);
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                closeProgressDialog();
                handler.sendEmptyMessage(2);
                currentLevel = LEVEL_PROVINCE;
            }
        });

    }

    /**
     * 打开进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private long currentTiem = 0;

    /**
     * 捕获返回键，根据等级判断应该返回省列表，还是退出
     */
    @Override
    public void onBackPressed() {
        if (isFromWeatherActivity) {
            //如果是从WeatherActivity跳转过来的，按下返回键应该重新回去
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        } else if (currentLevel == LEVEL_PROVINCE) {
            //finish();
            if ((System.currentTimeMillis() - currentTiem) < 2000) {
                finish();
            }
            currentTiem = System.currentTimeMillis();
            Toast.makeText(ChooseAreaActivity.this, "再按一次推出程序", Toast.LENGTH_SHORT).show();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        }
    }

    class MyHttpCallbackListener implements HttpCallbackListener {

        @Override
        public void onFinish(String response) {
            Log.d("ChooseAreaActivity", response);
        }

        @Override
        public void onError(Exception e) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (location == null && lu.isLocationListener) {
            lu.removeLocationUpdates();
        }
        handler.removeCallbacks(myRunnable);
    }
}
