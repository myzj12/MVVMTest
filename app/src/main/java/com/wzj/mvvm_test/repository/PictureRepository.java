package com.wzj.mvvm_test.repository;

import androidx.lifecycle.MutableLiveData;

import com.wzj.mvvm_test.BaseApplication;
import com.wzj.mvvm_test.db.bean.WallPaper;
import com.wzj.mvvm_test.utils.CustomDisposable;

import java.util.List;

import io.reactivex.Flowable;

public class PictureRepository {
    private final MutableLiveData<List<WallPaper>> wallPaper = new MutableLiveData<>();
    public final MutableLiveData<String> failed = new MutableLiveData<>();

    public MutableLiveData<List<WallPaper>> getWallPaper(){
        Flowable<List<WallPaper>> listFlowable = BaseApplication.getDb().wallPaperDao().getAll();
        CustomDisposable.addDisposable(listFlowable,wallPaper::postValue);
        return wallPaper;
    }
}
