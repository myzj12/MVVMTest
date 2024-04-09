package com.wzj.mvvm_test;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.tencent.mmkv.MMKV;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.wzj.mvvm_test.db.AppDatabase;
import com.wzj.mvvm_test.network.NetworkApi;
import com.wzj.mvvm_test.utils.MVUtils;

import java.util.HashMap;

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
        initX5WebView();
    }

    private void initX5WebView(){
        HashMap map = new HashMap(2);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER,true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE,true);
        QbSdk.initTbsSettings(map);
        //搜集本地tbs内核信息并上报服务器,服务器返回结果决定使用哪个内核
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5内核初始化完成的回调,为true表示x5内核加载成功,否则表示x5内核加载失败,会自动切换到系统内核
                Log.d("TAG", "onViewInitFinished:"+b);
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),cb);
    }

    public static AppDatabase getDb() {
        return db;
    }

    public static Context getContext() {
        return context;
    }
}
