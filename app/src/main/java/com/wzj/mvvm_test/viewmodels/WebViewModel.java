package com.wzj.mvvm_test.viewmodels;

import androidx.lifecycle.LiveData;

import com.wzj.mvvm_test.model.NewsDetailResponse;
import com.wzj.mvvm_test.repository.WebRepository;

public class WebViewModel extends BaseViewModel {
    public LiveData<NewsDetailResponse> newsDetail;

    public void getNewDetail(String uniquekey) {
        WebRepository webRepository = new WebRepository();
        newsDetail = webRepository.getNewsDetail(uniquekey);
        failed = webRepository.failed;
    }
}
