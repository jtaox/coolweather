package com.jtao.coolweather.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jtao.coolweather.activity.ObtainLocationListener;

import java.util.List;

/**
 * Created by Tap on 2015/9/19.
 */
public class LocationUtil {

    /**
     * 位置改变事件
     */
    private LocationListener locationListener;

    private LocationManager locationManager;

    public Location requestLocation(Context context) {

        String provider = null;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //获取可用的位置提供器
        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            //没有可用的位置提供器，无法定位
            Toast.makeText(context, "无法定位", 0).show();
            return null;
        }


        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        return location;

    }

    /**
     * 请求位置监听
     */
    public void requestLocationUpdates(final ObtainLocationListener listener){
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                listener.locationUpdates(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 1, locationListener);
    }

    /**
     * 关闭位置监听
     */
    public void removeLocationUpdates(){
        locationManager.removeUpdates(locationListener);
    }

}



