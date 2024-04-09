package com.wzj.mvvm_test.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.wzj.mvvm_test.BaseApplication;
import com.wzj.mvvm_test.api.ApiService;
import com.wzj.mvvm_test.db.bean.Video;
import com.wzj.mvvm_test.model.VideoResponse;
import com.wzj.mvvm_test.network.BaseObserver;
import com.wzj.mvvm_test.network.NetworkApi;
import com.wzj.mvvm_test.utils.Constant;
import com.wzj.mvvm_test.utils.CustomDisposable;
import com.wzj.mvvm_test.utils.DateUtil;
import com.wzj.mvvm_test.utils.MVUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class VideoRepository {
    public static final String TAG = VideoRepository.class.getSimpleName();
    final MutableLiveData<VideoResponse> video = new MutableLiveData<>();
    public final MutableLiveData<String> failed = new MutableLiveData<>();

    /**
     * 获取视频数据
     */
    public MutableLiveData<VideoResponse> getVideo() {
        //今日此接口是否已经请求
        if (MVUtils.getBoolean(Constant.IS_TODAY_REQUEST_VIDEO)) {
            if (DateUtil.getTimestamp() <= MVUtils.getLong(Constant.REQUEST_TIMESTAMP_VIDEO)) {
                getVideoForLocalDB();
            }else {
                getVideoForNetWork();
            }
        }else {
            getVideoForNetWork();
        }
        return video;
    }

    /**
     * 从本地数据库获取数据
     */
    private void getVideoForLocalDB() {
        Log.d(TAG, "getVideoForLocalDB: 从本地数据库获取 视频数据");
        VideoResponse videoResponse = new VideoResponse();

        List<VideoResponse.ResultBean> dataBeanList = new ArrayList<>();
        Flowable<List<Video>> listFlowable = BaseApplication.getDb().videoDao().getAll();
        //开启线程，从数据库中获取数据
        CustomDisposable.addDisposable(listFlowable, videos -> {
            for (Video video1 : videos) {
                VideoResponse.ResultBean resultBean = new VideoResponse.ResultBean();
                resultBean.setTitle(video1.getTitle());
                resultBean.setShare_url(video1.getShare_url());
                resultBean.setAuthor(video1.getAuthor());
                resultBean.setHot_words(video1.getHot_words());
                resultBean.setItem_cover(video1.getItem_cover());
                dataBeanList.add(resultBean);
            }
            videoResponse.setResult(dataBeanList);
            video.postValue(videoResponse);
        });
    }

    /**
     * 从网络获取热门视频数据
     */
    private void getVideoForNetWork() {
        Log.d(TAG, "getVideoForNetWork: 从网络 获取热门视频数据");
        NetworkApi.createServer(ApiService.class, 3).video().compose(NetworkApi.applySchedulers(new BaseObserver<VideoResponse>() {
            @Override
            public void onSuccess(VideoResponse videoResponse) {
                if (videoResponse.getError_code() == 0) {
                    //保存本地数据
                    saveVideo(videoResponse);
                    video.postValue(videoResponse);
                } else {
                    failed.postValue(videoResponse.getReason());
                }
            }

            @Override
            public void onFailure(Throwable e) {
                failed.postValue("Video Error:" + e.toString());
            }
        }));
    }


    /**
     * 保存热门壁纸数据
     */
    private void saveVideo(VideoResponse videoResponse) {
        MVUtils.put(Constant.IS_TODAY_REQUEST_VIDEO, true);
        MVUtils.put(Constant.REQUEST_TIMESTAMP_VIDEO, DateUtil.getMillisNextEarlyMorning());

        Completable deleteAll = BaseApplication.getDb().videoDao().deleteAll();
        CustomDisposable.addDisposable(deleteAll, () -> {
            Log.d(TAG, "saveVideo: 删除成功");
            List<Video> videos = new ArrayList<>();
            for (VideoResponse.ResultBean resultBean : videoResponse.getResult()) {
                videos.add(new Video(resultBean.getTitle(),
                        resultBean.getShare_url(),
                        resultBean.getAuthor(),
                        resultBean.getItem_cover(),
                        resultBean.getHot_words()));
            }
            //保存到数据库
            Completable insertAll = BaseApplication.getDb().videoDao().insertAll(videos);
            Log.d(TAG, "saveVideo: 插入数据:" + videos.size() + "条");
            CustomDisposable.addDisposable(insertAll, () -> Log.d(TAG, "saveVideo: 视频数据保存成功"));
        });
    }
}
