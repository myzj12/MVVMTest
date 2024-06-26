package com.wzj.mvvm_test.network;

import com.wzj.mvvm_test.network.errorhandler.ExceptionHandle;
import com.wzj.mvvm_test.network.errorhandler.HttpErrorHandler;
import com.wzj.mvvm_test.network.interceptor.RequestInterceptor;
import com.wzj.mvvm_test.network.interceptor.ResponseInterceptor;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络API
 * OkHttp做底层的网络请求访问
 * Retrofit做上层网络请求接口的封装,同时将需要的数据解析成实体,同时Retrofit还有对RxJava的支持,这样就可以在请求的时候做线程切换,切换到子线程,
 * 在数据返回的时候切换到主线程。避免了在主线程中进行耗时操作的问题。
 */
public class NetworkApi {

    /**
     * 获取App运行状态及版本信息,用于日子打印
     */
    private static INetworkRequiredInfo iNetworkRequiredInfo;

    /**
     * Api访问地址
     */
    private static String BASE_URL = null;
    private static OkHttpClient okHttpClient;
    private static final HashMap<String, Retrofit> retrofitHashMap = new HashMap<>();

    /**
     * 初始化
     */
    public static void init(INetworkRequiredInfo networkRequiredInfo) {
        iNetworkRequiredInfo = networkRequiredInfo;
    }

    /**
     * 创建serviceClass的实例
     */
    public static <T> T createServer(Class<T> serviceClass, int type) {
        //设置Url类型
        setUrlType(type);
        return getRetrofit(serviceClass).create(serviceClass);
    }

    /**
     * 配置OkHttp
     */
    private static OkHttpClient getOkHttpClient() {
        //不为空说明已经配置过了,直接返回即可
        if (okHttpClient == null) {
            //okhttp构建器
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //设置缓存大小
            int cacheSize = 100 * 1024 * 1024;
            //设置网络请求超时时长,这里设置为6s
            builder.connectTimeout(6, TimeUnit.SECONDS);
            //添加请求拦截器,如果接口有请求头的话,可以放在这个拦截器里面
            builder.addInterceptor(new RequestInterceptor(iNetworkRequiredInfo));
            //添加返回拦截器,可用于查看接口请求耗时,对于网络优化有帮助
            builder.addInterceptor(new ResponseInterceptor());
            //当程序在debug过程中则打印数据日志,方便调试用
            if (iNetworkRequiredInfo != null && iNetworkRequiredInfo.isDebug()) {
                //iNetworkRequiredInfo不为空且处于debug状态下则初始化日志拦截器
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                //设置要打印日志的内容等级,BODY为主要内容,还有BASIC,HEADERS,NONE
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                //将拦截器添加到okHttp构建器中
                builder.addInterceptor(httpLoggingInterceptor);
            }
            //okhttp配置完成
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    /**
     * 配置Retrofit
     * serviceClass 服务类
     */
    private static Retrofit getRetrofit(Class serviceClass) {
        if (retrofitHashMap.get(BASE_URL + serviceClass.getName()) != null) {
            //刚才上面定义的Map中键是String,值是Retrofit,当键不为空时,必然有值,有值则直接返回
            return retrofitHashMap.get(BASE_URL + serviceClass.getName());
        }
        //初始化Retrofit Retrofit是对okhttp的封装,通常是对网络请求做处理,也可以处理返回数据
        //Retrofit构建器
        Retrofit.Builder builder = new Retrofit.Builder();
        //设置访问地址
        builder.baseUrl(BASE_URL);
        //设置OkHttp客户端,传入上面写好的方法即可获得配置后的OkHttp客户端
        builder.client(getOkHttpClient());
        //设置数据解析器 会自动把请求返回的结果(json字符串)通过Gson转化工厂自动转化成与其结构相符的实体Bean
        builder.addConverterFactory(GsonConverterFactory.create());
        //设置请求回调,使用RxJava对网络返回进行处理
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        //retrofit配置完成
        Retrofit retrofit = builder.build();
        //放入map中
        retrofitHashMap.put(BASE_URL + serviceClass.getName(), retrofit);
        //最后返回即可
        return retrofit;

    }


    /**
     * 配置RxJava 完成线程的切换
     *
     * @param observer 这个observer要注意不要使用Lifecycle中的Observer
     * @param <T>      泛型
     * @return
     */
    public static <T> ObservableTransformer<T, T> applySchedulers(final Observer<T> observer) {
        return upstream -> {
            Observable<T> observable = upstream
                    .subscribeOn(Schedulers.io())//设置被观察者线程为普通子线程
                    .observeOn(AndroidSchedulers.mainThread())//设置观察者线程为Android主线程
                    .map(NetworkApi.getAppErrorHandler())//判断有没有500的错误,有则进入getAppErroHandler()
                    .onErrorResumeNext(new HttpErrorHandler<>());//判断有没有400的错误
            //订阅观察者
            observable.subscribe(observer);
            return observable;
        };
    }


    /**
     * 错误码处理
     */
    protected static <T> Function<T, T> getAppErrorHandler() {
        return response -> {
            //当response返回出现500之类的错误时
            if (response instanceof BaseResponse && ((BaseResponse) response).responseCode >= 500) {
                //通过这个异常处理,得到用户可以知道的原因
                ExceptionHandle.ServerException exception = new ExceptionHandle.ServerException();
                exception.code = ((BaseResponse) response).responseCode;
                exception.message = ((BaseResponse) response).responseError != null ? ((BaseResponse) response).responseError : "";
                //抛出异常
                throw exception;
            }
            return response;
        };
    }

    private static void setUrlType(int type) {
        switch (type) {
            case 0:
                //必应
                BASE_URL = "https://cn.bing.com";
                break;
            case 1:
                //热门壁纸
                BASE_URL = "http://service.picasso.adesk.com";
                break;
            case 2:
                //聚合API,请求新闻列表
                BASE_URL ="http://v.juhe.cn";
                break;
            case 3:
                //聚合API,请求热门视频
                BASE_URL="http://apis.juhe.cn";
            default:
                break;
        }
    }
}
