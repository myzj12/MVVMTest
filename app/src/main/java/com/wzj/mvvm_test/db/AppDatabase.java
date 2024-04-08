package com.wzj.mvvm_test.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wzj.mvvm_test.db.bean.Image;
import com.wzj.mvvm_test.db.dao.ImageDao;

/**
 * 数据库类,它是数据库的持有者
 * entities:当前数据库中的表
 * version : 当前数据库版本号
 * exportSchema : 不允许导出
 */
@Database(entities = {Image.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "mvvm_demo";
    private static volatile AppDatabase mInstance;

    /**
     * 单例模式
     */
    public static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AppDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "mvvm_demo").build();
                }
            }
        }
        return mInstance;
    }

    public abstract ImageDao imageDao();
}
