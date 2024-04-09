package com.wzj.mvvm_test.repository;

import androidx.lifecycle.MutableLiveData;

import com.wzj.mvvm_test.api.ApiService;
import com.wzj.mvvm_test.model.NewsDetailResponse;
import com.wzj.mvvm_test.network.BaseObserver;
import com.wzj.mvvm_test.network.NetworkApi;

public class WebRepository {
    final MutableLiveData<NewsDetailResponse> newsDetail = new MutableLiveData<>();
    public final MutableLiveData<String> failed = new MutableLiveData<>();

    /**
     * 获取新闻详情数据
     */
    public MutableLiveData<NewsDetailResponse> getNewsDetail(String uniquekey){
        NetworkApi.createServer(ApiService.class,2)
                .newsDetail(uniquekey).compose(NetworkApi.applySchedulers(new BaseObserver<NewsDetailResponse>() {
                    @Override
                    public void onSuccess(NewsDetailResponse newsDetailResponse) {
                        if(newsDetailResponse.getError_code() == 0){
                            newsDetail.setValue(newsDetailResponse);
                        }else {
                            failed.postValue(newsDetailResponse.getReason());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        failed.postValue("NewsDetail Error: "+e.toString());
                    }
                }));
        return newsDetail;
    }
}
