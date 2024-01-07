package com.tech.cybercars.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

public class NetworkUtil {
    final static int NO_INTERNET = 0;
    final static int WIFI_CONNECTED = 1;
    final static int MOBILE_DATA_CONNECTED = 2;
    final static String TAG = "tracking";

    /**
     *  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     * @param activity
     * @return int NO_INTERNET | WIFI_CONNECTED | MOBILE_DATA_CONNECTED
     */
    public static int NetworkStatus(Activity activity){
        boolean wifi_connected;
        boolean mobile_data_connected;

        ConnectivityManager connect_manager = activity.getSystemService(ConnectivityManager.class);
        Network currentNetwork = connect_manager.getActiveNetwork();

        NetworkCapabilities caps = connect_manager.getNetworkCapabilities(currentNetwork);
        LinkProperties linkProperties = connect_manager.getLinkProperties(currentNetwork);
        return 1;
//        if(network_info != null && network_info.isConnected()){
//            wifi_connected = network_info.getType() == ConnectivityManager.TYPE_WIFI;
//            mobile_data_connected = network_info.getType() == ConnectivityManager.TYPE_MOBILE;
//
//            if(wifi_connected){
//                return NetworkUtil.WIFI_CONNECTED;
//            } else if(mobile_data_connected){
//                return NetworkUtil.MOBILE_DATA_CONNECTED;
//            }
//        }
//        return NetworkUtil.NO_INTERNET;
    }

    public void NetworkTracking(Activity activity){
        ConnectivityManager connect_manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        connect_manager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                Log.e(TAG, "The default network is now: " + network);
            }
            @Override
            public void onLost(@NonNull Network network) {
                Log.e(TAG, "The application no longer has a default network. The last default network was " + network);
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                Log.e(TAG, "The default network changed capabilities: " + networkCapabilities);
            }

            @Override
            public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
                Log.e(TAG, "The default network changed link properties: " + linkProperties);
            }
        });
    }

}
