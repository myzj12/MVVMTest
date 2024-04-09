package com.wzj.mvvm_test.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.wzj.mvvm_test.model.NewsResponse;
import com.wzj.mvvm_test.repository.NewsRepository;

public class NewsViewModel extends BaseViewModel {
    public LiveData<NewsResponse> news;

    public void getNews() {
        NewsRepository newsRepository = new NewsRepository();
        failed = newsRepository.failed;

        news = newsRepository.getNews();
    }
}