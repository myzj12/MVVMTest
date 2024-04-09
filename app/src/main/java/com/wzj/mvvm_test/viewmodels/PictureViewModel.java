package com.wzj.mvvm_test.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wzj.mvvm_test.db.bean.WallPaper;
import com.wzj.mvvm_test.repository.PictureRepository;

import java.util.List;

public class PictureViewModel extends BaseViewModel {

    public LiveData<List<WallPaper>> wallPaper;
    private final PictureRepository pictureRepository;

    PictureViewModel(PictureRepository pictureRepository){
        this.pictureRepository = pictureRepository;
    }

    public void getWallPaper() {
        wallPaper = pictureRepository.getWallPaper();
        failed = pictureRepository.failed;
    }
}
