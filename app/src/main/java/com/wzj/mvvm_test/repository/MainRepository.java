package com.wzj.mvvm_test.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.wzj.mvvm_test.BaseApplication;
import com.wzj.mvvm_test.api.ApiService;
import com.wzj.mvvm_test.db.bean.Image;
import com.wzj.mvvm_test.db.bean.WallPaper;
import com.wzj.mvvm_test.model.BiYingResponse;
import com.wzj.mvvm_test.model.WallPaperResponse;
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

    //必应图片
    final MutableLiveData<BiYingResponse> biyingImage = new MutableLiveData<>();

    //热门壁纸
    final MutableLiveData<WallPaperResponse> wallPaper = new MutableLiveData<>();
    public final MutableLiveData<String> failed = new MutableLiveData<>();



    /**
     * 获取壁纸数据
     */
    public MutableLiveData<WallPaperResponse> getWallPaper() {
        //今日此接口是否已经请求
        if(MVUtils.getBoolean(Constant.IS_TODAY_REQUEST_WALLPAPER)){
            //是否超时
            if(DateUtil.getTimestamp() <= MVUtils.getLong(Constant.REQUEST_TIMESTAMP_WALLPAPER)){
                //从本地获取数据
                getLocalDBForWallPaper();
            }else {
                requestNetworkApiForWallPaper();
            }
        }else {
            requestNetworkApiForWallPaper();
        }

        return wallPaper;
    }

    /**
     * 从网络获取壁纸数据
     */
    private void requestNetworkApiForWallPaper(){
        Log.d(TAG, "requestNetworkApiForWallPaper: 从网络获取 热门壁纸");
        NetworkApi.createServer(ApiService.class,1).wallPaper().compose(NetworkApi.applySchedulers(new BaseObserver<WallPaperResponse>() {
            @Override
            public void onSuccess(WallPaperResponse wallPaperResponse) {
                if(wallPaperResponse.getCode()==0){
                    //保存本地数据
                    saveWallPaper(wallPaperResponse);
                    wallPaper.setValue(wallPaperResponse);
                }else {
                    failed.postValue(wallPaperResponse.getMsg());
                }
            }

            @Override
            public void onFailure(Throwable e) {
                failed.postValue("WallPaper Error: "+e.toString());
            }
        }));
    }

    /**
     * 从本地数据库获取热门壁纸
     */
    private void getLocalDBForWallPaper(){
        Log.d(TAG, "getLocalDBForWallPaper: 从本地数据库获取 热门壁纸");
        WallPaperResponse wallPaperResponse = new WallPaperResponse();
        WallPaperResponse.ResBean resBean = new WallPaperResponse.ResBean();
        List<WallPaperResponse.ResBean.VerticalBean> verticalBeanList = new ArrayList<>();
        Flowable<List<WallPaper>> listFlowable = BaseApplication.getDb().wallPaperDao().getAll();
        CustomDisposable.addDisposable(listFlowable,wallPapers -> {
            for (WallPaper paper : wallPapers) {
                WallPaperResponse.ResBean.VerticalBean verticalBean = new WallPaperResponse.ResBean.VerticalBean();
                verticalBean.setImg(paper.getImg());
                verticalBeanList.add(verticalBean);
            }
            resBean.setVertical(verticalBeanList);
            wallPaperResponse.setRes(resBean);
            wallPaper.postValue(wallPaperResponse);
        });
    }

    /**
     * 保存热门壁纸数据
     */
    private void saveWallPaper(WallPaperResponse wallPaperResponse) {
        //记录今天已经请求了数据 并且 记录数据时间戳
        MVUtils.put(Constant.IS_TODAY_REQUEST_WALLPAPER, true);
        MVUtils.put(Constant.REQUEST_TIMESTAMP_WALLPAPER, DateUtil.getMillisNextEarlyMorning());

        //先删除原来的数据
        Completable deleteAll = BaseApplication.getDb().wallPaperDao().deleteAll();

        CustomDisposable.addDisposable(deleteAll, () -> {
            Log.d(TAG, "saveWallPaper: 删除数据成功");
            List<WallPaper> wallPaperList = new ArrayList<>();
            for (WallPaperResponse.ResBean.VerticalBean verticalBean : wallPaperResponse.getRes().getVertical()) {
                wallPaperList.add(new WallPaper(verticalBean.getImg()));
            }
            //保存到数据库
            Completable insertAll = BaseApplication.getDb().wallPaperDao().insertAll(wallPaperList);
            Log.d(TAG, "saveWallPaper: 插入数据: "+wallPaperList.size()+"条");
            //RxJava处理Room数据存储
            CustomDisposable.addDisposable(insertAll,()-> Log.d(TAG, "saveWallPaper: 热门壁纸数据保存成功"));
        });
    }

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

    /**
     * 从本地数据库获取
     */
    private void getLocalDB() {
        Log.d(TAG, "getLocalDB: 从本地数据库获取");
        BiYingResponse biYingResponse = new BiYingResponse();
        //从数据库获取
        Flowable<Image> imageFlowable = BaseApplication.getDb().imageDao().queryById(1);
        //RxJava处理Room数据获取
        CustomDisposable.addDisposable(imageFlowable, image -> {
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

    /**
     * 从网络上请求数据
     */
    private void requestNetworkApi() {
        Log.d(TAG, "requestNetworkApi: 从网络上获取");
        ApiService apiService = NetworkApi.createServer(ApiService.class, 0);
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
    //获取数据
}
