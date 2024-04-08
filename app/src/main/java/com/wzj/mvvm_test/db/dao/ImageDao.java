package com.wzj.mvvm_test.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wzj.mvvm_test.db.bean.Image;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import retrofit2.http.DELETE;

/**
 * 数据访问对象 通过它来进行增删改查
 *
 * @Dao 定义数据访问对象
 */
@Dao
public interface ImageDao {
    @Query("SELECT * FROM image")
    List<Image> getAll();

    @Query("SELECT * FROM image WHERE uid LIKE :uid LIMIT 1")
    Flowable<Image> queryById(int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(Image... images);

    @Delete
    void delete(Image image);
}
