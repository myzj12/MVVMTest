package com.wzj.mvvm_test.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.wzj.mvvm_test.db.bean.WallPaper;
import com.wzj.mvvm_test.repository.PictureRepository;

import java.util.List;

public class PictureViewModel extends ViewModel {
    public LiveData<List<WallPaper>> wallPaper;

    public void getWallPaper(){
        wallPaper = new PictureRepository().getWallPaper();
    }
}
