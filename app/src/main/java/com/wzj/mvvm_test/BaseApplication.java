package com.wzj.mvvm_test;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.tencent.mmkv.MMKV;
import com.wzj.mvvm_test.db.AppDatabase;
import com.wzj.mvvm_test.network.NetworkApi;
import com.wzj.mvvm_test.utils.MVUtils;

public class BaseApplication extends Application {
    public static Context context;
    //数据库
    public static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        NetworkApi.init(new NetworkRequiredInfo(this));
        context = getApplicationContext();
        //MMVK初始化
        MMKV.initialize(this);
        //工具类初始化
        MVUtils.getInstance();
        //创建本地数据库
        db = AppDatabase.getInstance(this);
    }

    public static AppDatabase getDb() {
        return db;
    }

    public static Context getContext() {
        return context;
    }
}
