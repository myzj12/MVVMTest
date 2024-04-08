package com.wzj.mvvm_test.api;

import com.wzj.mvvm_test.model.BiYingResponse;
import com.wzj.mvvm_test.model.WallPaperResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 所有的Api网络接口
 */
public interface ApiService {

    /**
     * 必应每日一图
     */
    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1")
    Observable<BiYingResponse> biying();

    /**
     * 热门壁纸
     */
    @GET("/v1/vertical/vertical?limit=30&skip=180&adult=false&first=0&order=hot")
    Observable<WallPaperResponse> wallPaper();
}
