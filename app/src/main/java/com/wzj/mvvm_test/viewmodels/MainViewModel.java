package com.wzj.mvvm_test.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wzj.mvvm_test.model.BiYingResponse;
import com.wzj.mvvm_test.model.WallPaperResponse;
import com.wzj.mvvm_test.repository.MainRepository;

import java.io.Closeable;

public class MainViewModel extends ViewModel {

    public MutableLiveData<BiYingResponse> biying;
    public MutableLiveData<WallPaperResponse> wallPaper;

    public void getWallPaper() {
        wallPaper = new MainRepository().getWallPaper();
    }

    public void getBiying() {
        biying = new MainRepository().getBiYing();
    }
}
