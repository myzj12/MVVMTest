package com.wzj.mvvm_test.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.wzj.mvvm_test.BaseApplication;
import com.wzj.mvvm_test.network.utils.KLog;

import kotlin.math.UMathKt;


/**
 * 自定义View
 */
public class CustomImageView extends AppCompatImageView {
    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param imageView 图片视图
     * @param url       网络url
     */
    @BindingAdapter(value = {"biyingUrl"}, requireAll = false)
    public static void setBiyingUrl(ImageView imageView, String url) {
        String assembleUrl = "http://cn.bing.com" + url;
        KLog.d(assembleUrl);
        Glide.with(BaseApplication.getContext()).load(assembleUrl).into(imageView);
    }

    /**
     * 普通网络地址图片
     * @param imageView 图片视图
     * @param url  图片地址
     */
    public static void setNetworkUrl(ImageView imageView,String url){
        Glide.with(BaseApplication.getContext()).load(url).into(imageView);
    }
}
