package com.wzj.mvvm_test.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.wzj.mvvm_test.BaseApplication;
import com.wzj.mvvm_test.api.ApiService;
import com.wzj.mvvm_test.db.bean.News;
import com.wzj.mvvm_test.model.NewsResponse;
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

public class NewsRepository {
    private static final String TAG = NewsRepository.class.getSimpleName();
    final MutableLiveData<NewsResponse> news = new MutableLiveData<>();
    public final MutableLiveData<String> failed = new MutableLiveData<>();


    /**
     * 获取新闻数据
     */
    public MutableLiveData<NewsResponse> getNews() {
        //今日此接口是否已经请求
        if(MVUtils.getBoolean(Constant.IS_TODAY_REQUEST_NEWS)){
            //接口是未超时
            if(DateUtil.getTimestamp() <= MVUtils.getLong(Constant.REQUEST_TIMESTAMP_NEWS)){
                getNewForLocalDB();
            }else {
                getNewsForNetwork();
            }
        }else{
            getNewsForNetwork();
        }
        return news;
    }

    /**
     * 从本地数据库获取新闻
     */
    private void getNewForLocalDB() {
        NewsResponse newsResponse = new NewsResponse();
        NewsResponse.ResultBean resultBean = new NewsResponse.ResultBean();

        List<NewsResponse.ResultBean.DataBean> dataBeanList = new ArrayList<>();
        Flowable<List<News>> listFlowable = BaseApplication.getDb().newsDao().getAll();
        CustomDisposable.addDisposable(listFlowable, newss -> {
            for (News news1 : newss) {
                NewsResponse.ResultBean.DataBean dataBean = new NewsResponse.ResultBean.DataBean();
                dataBean.setUniquekey(news1.getUniquekey());
                dataBean.setTitle(news1.getTitle());
                dataBean.setDate(news1.getDate());
                dataBean.setAuthor_name(news1.getAuthor_name());
                dataBean.setCategory(news1.getCategory());
                dataBean.setThumbnail_pic_s(news1.getThumbnail_pic_s());
                dataBean.setIs_content(news1.getIs_content());
                dataBeanList.add(dataBean);
            }
            resultBean.setData(dataBeanList);
            newsResponse.setResult(resultBean);
            news.postValue(newsResponse);

        });
    }

    /**
     * 从网络获取新闻数据
     */
    private void getNewsForNetwork() {
        Log.d(TAG, "getNewsForNetwork: 从网络获取 新闻数据");
        NetworkApi.createServer(ApiService.class, 2).news().compose(NetworkApi.applySchedulers(new BaseObserver<NewsResponse>() {
            @Override
            public void onSuccess(NewsResponse newsResponse) {
                if (newsResponse.getError_code() == 0) {
                    //保存本地数据
                    saveNews(newsResponse);
                    news.setValue(newsResponse);
                } else {
                    failed.postValue(newsResponse.getReason());
                }
            }

            @Override
            public void onFailure(Throwable e) {
                failed.postValue("News Error" + e.toString());
            }
        }));
    }


    /**
     * 保存热门新闻
     * 1.记录今天是否请求 保存当前时间,如果是同一天就不用重复请求了
     */
    private void saveNews(NewsResponse newsResponse) {

        MVUtils.put(Constant.IS_TODAY_REQUEST_NEWS, true);
        MVUtils.put(Constant.REQUEST_TIMESTAMP_NEWS, DateUtil.getMillisNextEarlyMorning());

        Completable deleteAll = BaseApplication.getDb().newsDao().deleteAll();
        //使用RxJava处理线程切换
        CustomDisposable.addDisposable(deleteAll, () -> {
            Log.d(TAG, "saveNews: 删除数据成功");
            List<News> newsList = new ArrayList<>();
            for (NewsResponse.ResultBean.DataBean dataBean : newsResponse.getResult().getData()) {
                newsList.add(new News(dataBean.getUniquekey(),
                        dataBean.getTitle(), dataBean.getDate(),
                        dataBean.getCategory(),
                        dataBean.getAuthor_name(),
                        dataBean.getUrl(),
                        dataBean.getThumbnail_pic_s(),
                        dataBean.getIs_content()));
            }
            //保存到数据库中
            Completable insertAll = BaseApplication.getDb().newsDao().insertAll(newsList);
            Log.d(TAG, "saveNews: 插入数据: " + newsList.size() + "条");
            //RxJava处理Room数据库
            //使用RxJava处理线程切换
            CustomDisposable.addDisposable(insertAll, () -> Log.d(TAG, "saveNews: 新闻数据保存成功"));
        });
    }
}
