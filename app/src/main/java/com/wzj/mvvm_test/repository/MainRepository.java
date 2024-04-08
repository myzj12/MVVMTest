package com.wzj.mvvm_test.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.wzj.mvvm_test.BaseApplication;
import com.wzj.mvvm_test.api.ApiService;
import com.wzj.mvvm_test.db.bean.Image;
import com.wzj.mvvm_test.model.BiYingResponse;
import com.wzj.mvvm_test.network.BaseObserver;
import com.wzj.mvvm_test.network.NetworkApi;
import com.wzj.mvvm_test.utils.CustomDisposable;
import com.wzj.mvvm_test.utils.DateUtil;
import com.wzj.mvvm_test.network.utils.KLog;
import com.wzj.mvvm_test.utils.Constant;
import com.wzj.mvvm_test.utils.MVUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Main存储库,用于对数据进行处理
 * Repository只做获取数据的相关操作 例如:从网络上获取数据 从本地磁盘获取数据 等等
 * 这是为了保证唯一性,如果一个接口在多个地方使用,每一个都写到对应的ViewModel中,会有很多重复代码,这样写的好处就是减少了代码。
 */
public class MainRepository {

    private static final String TAG = MainRepository.class.getSimpleName();
    final MutableLiveData<BiYingResponse> biyingImage = new MutableLiveData<>();

    /**
     * 保存数据
     */
    private void saveImageData(BiYingResponse biYingResponse) {
        //记录今日已请求
        MVUtils.put(Constant.IS_TODAY_REQUEST, true);
        //记录此次请求的最晚有效时间戳
        MVUtils.put(Constant.REQUEST_TIMESTAMP, DateUtil.getMillisNextEarlyMorning());
        BiYingResponse.ImagesBean bean = biYingResponse.getImages().get(0);
        //保存到数据库
        Completable insert = BaseApplication.getDb().imageDao().insertAll(new Image(1, bean.getUrl(), bean.getUrlbase(), bean.getCopyright(), bean.getCopyrightlink(), bean.getTitle()));
        //RxJava处理Room数据存储
        CustomDisposable.addDisposable(insert, () -> Log.d(TAG, "saveImageData: 插入数据成功"));
    }

    /**
     * 从网络上请求数据
     */
    private void requestNetworkApi() {
        Log.d(TAG, "requestNetworkApi: 从网络上获取");
        ApiService apiService = NetworkApi.createServer(ApiService.class);
        apiService.biying().compose(NetworkApi.applySchedulers(new BaseObserver<BiYingResponse>() {
            @Override
            public void onSuccess(BiYingResponse biYingResponse) {
                //存储到本地数据库中,并记录今日已请求了的数据
                saveImageData(biYingResponse);
                biyingImage.setValue(biYingResponse);
            }


            @Override
            public void onFailure(Throwable e) {
                KLog.e("BiYing Error: " + e.toString());
            }
        }));
    }

    /**
     * 从本地数据库获取
     */
    private void getLocalDB() {
        Log.d(TAG, "getLocalDB: 从本地数据库获取");
        BiYingResponse biYingResponse = new BiYingResponse();
        //从数据库获取
        Flowable<Image> imageFlowable = BaseApplication.getDb().imageDao().queryById(1);
        //RxJava处理Room数据获取
        CustomDisposable.addDisposable(imageFlowable,image -> {
            BiYingResponse.ImagesBean imagesBean = new BiYingResponse.ImagesBean();
            imagesBean.setUrl(image.getUrl());
            imagesBean.setUrlbase(image.getUrlbase());
            imagesBean.setCopyright(image.getCopyright());
            imagesBean.setCopyrightlink(image.getCopyrightlink());
            imagesBean.setTitle(image.getTitle());
            List<BiYingResponse.ImagesBean> list = new ArrayList<>();
            list.add(imagesBean);
            biYingResponse.setImages(list);
            biyingImage.postValue(biYingResponse);
        });
    }

    //获取数据
    @SuppressLint("CheckResult")
    public MutableLiveData<BiYingResponse> getBiYing() {
        //今日此接口是否已经请求
        if (MVUtils.getBoolean(Constant.IS_TODAY_REQUEST)) {
            //判断当前时间 例如通过:DateUtil.getTimestamp()得到 当前时间为:2024-04-08 23:48:04
            //是否小于等于 MVUtils.getLong(Constant.REQUEST_TIMESTAMP)) 例如 2024-04-08 12:00:00
            if (DateUtil.getTimestamp() <= MVUtils.getLong(Constant.REQUEST_TIMESTAMP)) {
                Log.d(TAG, "当前时间为:" + DateUtil.getDateTime() + "    最晚有效时间戳为:" + MVUtils.getLong(Constant.REQUEST_TIMESTAMP));
                //当前时间未超过次日0点,从本地获取
                getLocalDB();
            } else {
                //大于则数据需要更新,从网络获取
                requestNetworkApi();
            }
        } else {
            //没有请求过接口 或 当前时间,从网络获取
            requestNetworkApi();
        }
        return biyingImage;
    }
}
