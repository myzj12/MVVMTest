package com.wzj.mvvm_test.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.wzj.mvvm_test.BaseApplication;
import com.wzj.mvvm_test.R;
import com.wzj.mvvm_test.network.utils.KLog;

import kotlin.math.UMathKt;


/**
 * 自定义View
 */
public class CustomImageView extends ShapeableImageView {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .placeholder(R.drawable.wallpaper_bg)//图片加载出来前,显示的图片
            .fallback(R.drawable.wallpaper_bg)//url为空的时候,显示的图片
            .error(R.mipmap.ic_loading_failed);//图片加载失败后,显示的图片

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
    @BindingAdapter(value = {"networkUrl"},requireAll = false)
    public static void setNetworkUrl(ImageView imageView,String url){
        Glide.with(BaseApplication.getContext()).load(url).apply(OPTIONS).into(imageView);
    }
}
