package com.wzj.mvvm_test.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wzj.mvvm_test.db.bean.Image;
import com.wzj.mvvm_test.db.bean.WallPaper;
import com.wzj.mvvm_test.db.dao.ImageDao;
import com.wzj.mvvm_test.db.dao.WallPaperDao;

/**
 * 数据库类,它是数据库的持有者
 * entities:当前数据库中的表
 * version : 当前数据库版本号
 * exportSchema : 不允许导出
 */
@Database(entities = {Image.class, WallPaper.class}, version = 2, exportSchema = false)
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
                    mInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "mvvm_demo")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return mInstance;
    }

    /**
     * 版本升级,添加wallpaper表
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `wallpaper` " +
                    "(uid INTEGER NOT NULL, " +
                    "img TEXT, " +
                    "PRIMARY KEY(`uid`))"
            );
        }
    };

    public abstract ImageDao imageDao();
    public abstract WallPaperDao wallPaperDao();
}
