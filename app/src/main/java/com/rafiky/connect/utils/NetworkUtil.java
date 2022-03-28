package com.rafiky.connect.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class NetworkUtil {

    private static NetworkInfo activeNetwork;

    private static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context) {
        activeNetwork = getNetworkInfo(context);
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static void openSettings(Context context){
        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

}
