package com.wzj.mvvm_test.api;

import com.wzj.mvvm_test.model.BiYingResponse;
import com.wzj.mvvm_test.model.NewsDetailResponse;
import com.wzj.mvvm_test.model.NewsResponse;
import com.wzj.mvvm_test.model.VideoResponse;
import com.wzj.mvvm_test.model.WallPaperResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

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

    /**
     * 聚合聚合头条
     */
    @GET("/toutiao/index?type=&page=&page_size=&is_filter=&key=0aca3f5c05f51304d89a6eafc99903db")
    Observable<NewsResponse> news();

    /**
     * 聚合热门视频
     */
    @GET("/fapig/douyin/billboard?type=hot_video&size=20&key=950323d62f70a74678ff8644f5a92ed5")
    Observable<VideoResponse> video();

    /**
     * 聚合新闻数据详情
     */
    @GET("/toutiao/content?key=99d3951ed32af2930afd9b38293a08a2")
    Observable<NewsDetailResponse> newsDetail(@Query("uniquekey") String uniquekey);

}
