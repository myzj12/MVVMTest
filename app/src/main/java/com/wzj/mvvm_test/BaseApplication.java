package com.wzj.mvvm_test;

import android.app.Application;
import android.content.Context;

import com.wzj.mvvm_test.network.NetworkApi;

public class BaseApplication extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        NetworkApi.init(new NetworkRequiredInfo(this));
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
