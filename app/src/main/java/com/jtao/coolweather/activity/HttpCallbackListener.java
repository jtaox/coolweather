package com.jtao.coolweather.activity;

/**
 * Created by Tap on 2015/9/19.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
