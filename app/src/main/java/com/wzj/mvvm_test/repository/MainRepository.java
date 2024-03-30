package com.wzj.mvvm_test.repository;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.wzj.mvvm_test.api.ApiService;
import com.wzj.mvvm_test.model.BiYingResponse;
import com.wzj.mvvm_test.network.BaseObserver;
import com.wzj.mvvm_test.network.NetworkApi;
import com.wzj.mvvm_test.network.utils.KLog;

/**
 * Main存储库,用于对数据进行处理
 * Repository只做获取数据的相关操作 例如:从网络上获取数据 从本地磁盘获取数据 等等
 * 这是为了保证唯一性,如果一个接口在多个地方使用,每一个都写到对应的ViewModel中,会有很多重复代码,这样写的好处就是减少了代码。
 */
public class MainRepository {

    @SuppressLint("CheckResult")
    public MutableLiveData<BiYingResponse> getBiYing() {
        //存放数据
        final MutableLiveData<BiYingResponse> biyingImage = new MutableLiveData<>();
        //创建ApiService 初始化Retrofit
        ApiService apiService = NetworkApi.createServer(ApiService.class);
        //compose允许你将一系列操作符应用到Observable流上,返回一个新Observable,你可以自定义一个Transformer,将一组
        //操作符封装成一个可复用的部分 然后通过链式调用来编写代码
        apiService.biying().compose(NetworkApi.applySchedulers(new BaseObserver<BiYingResponse>() {
            @Override
            public void onSuccess(BiYingResponse biYingResponse) {
                KLog.d(new Gson().toJson(biYingResponse));
                biyingImage.postValue(biYingResponse);
            }

            @Override
            public void onFailure(Throwable e) {
                KLog.e("BiYing Error: " + e.toString());
            }
        }));
        return biyingImage;
    }
}
