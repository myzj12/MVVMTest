package com.wzj.mvvm_test.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.translation.ViewTranslationCallback;

public class EasyAnimation {

    /**
     * 开始眨眼动画
     */
    public static void startBlink(View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setStartOffset(20);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        view.startAnimation(alphaAnimation);
    }

    /**
     * 开始眨眼动画
     *
     * @param view           需要设置动画的View
     * @param alphaAnimation 透明度动画
     */
    public static void startBlink(View view, AlphaAnimation alphaAnimation) {
        view.startAnimation(alphaAnimation);
    }

    /**
     * 停止眨眼动画
     */
    public static void stopBlink(View view) {
        if (view != null) {
            view.clearAnimation();
        }
    }

    public static void moveViewWidth(View view, TranslateCallback callback) {
        view.post(() -> {
            //通过post拿到的tvTranslate.getWidth()不为为0
            //平移动画,
            //view.getWidth() 表示动画的起点和终点再X轴方向上的偏移量,从0像素开始,移动到view款到像素的位置
            TranslateAnimation translateAnimation = new TranslateAnimation(0, view.getWidth(), 0, 0);
            translateAnimation.setDuration(1000);
            //动画结束后,view保持再动画结束时的位置
            translateAnimation.setFillAfter(true);
            view.startAnimation(translateAnimation);

            //动画监听
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //检查Android版本
                    callback.animationEnd();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        });
    }

    /**
     * 移动指定View的宽度
     */
    public static void moveViewWidth(View view, TranslateCallback callback, TranslateAnimation translateAnimation) {
        view.post(() -> {
            //通过post拿到tvTranslate.getWidth()不会为0
            view.startAnimation(translateAnimation);

            //动画监听
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //检查Android版本
                    callback.animationEnd();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        });
    }

    public interface TranslateCallback {
        //动画结束
        void animationEnd();
    }
}
