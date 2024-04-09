package com.wzj.mvvm_test.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wzj.mvvm_test.db.bean.News;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Completable 不发出任何数据的异步操作的可观察序列
 */
@Dao
public interface NewsDao {
    @Query("SELECT * FROM news")
    Flowable<List<News>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<News> news);

    @Query("DELETE FROM news")
    Completable deleteAll();
}
