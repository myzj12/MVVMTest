package com.wzj.mvvm_test.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.wzj.mvvm_test.R;
import com.wzj.mvvm_test.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private com.wzj.mvvm_test.databinding.ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        initView();
    }

    private void initView() {
        //获取navController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.news_fragment:
                    binding.tvTitle.setText("头条新闻");
                    navController.navigate(R.id.news_fragment);
                    break;
                case R.id.video_fragment:
                    binding.tvTitle.setText("热门视频");
                    navController.navigate(R.id.video_fragment);
                    break;
            }
            return true;
        });
    }
}