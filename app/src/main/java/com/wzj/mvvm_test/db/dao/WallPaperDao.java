package com.wzj.mvvm_test.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wzj.mvvm_test.db.bean.WallPaper;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface WallPaperDao {
    @Query("SELECT * FROM wallpaper")
    Flowable<List<WallPaper>> getAll();

    //onConflict = OnConflictStrategy.REPLACE 如果遇到主键或唯一约束冲突(即尝试插入的数据已经存在于数据库中),则应该
    //替换掉数据库中现有的记录，而不是抛出一个异常。
    //Completable表示异步任务完成且不发出任何数据的可观察序列
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<WallPaper> wallPapers);

    @Query("DELETE FROM wallpaper")
    Completable deleteAll();
}
