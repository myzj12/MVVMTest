package com.wzj.mvvm_test.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;
import android.view.View;

import com.wzj.mvvm_test.R;
import com.wzj.mvvm_test.databinding.ActivitySplashBinding;
import com.wzj.mvvm_test.utils.Constant;
import com.wzj.mvvm_test.utils.EasyAnimation;
import com.wzj.mvvm_test.utils.MVUtils;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        //设置为深色模式
        setStatusBar(true);
        EasyAnimation.moveViewWidth(binding.tvTranslate, () -> {
            binding.tvMvvm.setVisibility(View.VISIBLE);
            jumpActivity(MVUtils.getBoolean(Constant.IS_LOGIN) ? MainActivity.class : LoginActivity.class);
        });
    }
}