package com.wzj.mvvm_test.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.wzj.mvvm_test.R;
import com.wzj.mvvm_test.model.User;
import com.wzj.mvvm_test.utils.MVUtils;
import com.wzj.mvvm_test.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private com.wzj.mvvm_test.databinding.ActivityLoginBinding dataBinding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //数据绑定视图
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        loginViewModel = new LoginViewModel();
        User user = new User("admin", "12345");
        loginViewModel.getUser().setValue(user);

        Log.d("TAG", "onCreate: 存");
        MVUtils.put("age", 24);

        int age = MVUtils.getInt("age", 0);
        Log.d("TAG", "onCreate:取" + age);


        //获取观察对象
        MutableLiveData<User> user1 = loginViewModel.getUser();
        user1.observe(this, user2 -> {
            dataBinding.setViewModel(loginViewModel);
        });

        dataBinding.btnLogin.setOnClickListener(v -> {
            if (loginViewModel.user.getValue().getAccount().isEmpty()) {
                Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (loginViewModel.user.getValue().getPwd().isEmpty()) {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        });

    }
}