package com.wzj.mvvm_test.network.interceptor;

import com.wzj.mvvm_test.network.utils.KLog;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 返回拦截器(响应拦截器)
 */
public class ResponseInterceptor implements Interceptor {
    private static final String TAG = "ResponseInterceptor";

    /**
     * 拦截
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        long requestTime = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
        KLog.i(TAG, "requestSpendTime=" + (System.currentTimeMillis() - requestTime) + "ms");
        return response;
    }
}
