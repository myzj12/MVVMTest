package com.wzj.mvvm_test.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wzj.mvvm_test.model.BiYingResponse;
import com.wzj.mvvm_test.model.WallPaperResponse;
import com.wzj.mvvm_test.repository.MainRepository;

import java.io.Closeable;

public class MainViewModel extends BaseViewModel {

    public MutableLiveData<BiYingResponse> biying;
    public MutableLiveData<WallPaperResponse> wallPaper;
//    private final MainRepository mainRepository;

//    MainViewModel(MainRepository mainRepository) {
//        this.mainRepository = mainRepository;
//    }

    public void getWallPaper() {
        wallPaper = new MainRepository().getWallPaper();
//        failed = mainRepository.failed;
    }

    public void getBiying() {
        biying = new MainRepository().getBiYing();
//        failed = mainRepository.failed;
    }
}
