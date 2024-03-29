package com.wzj.mvvm_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.wzj.mvvm_test.databinding.ActivityMainBinding;
import com.wzj.mvvm_test.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {


    private MainViewModel mainViewModel;
    private com.wzj.mvvm_test.databinding.ActivityMainBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //创建ViewModel对象
        mainViewModel = new MainViewModel();
        //创建实体类
        User admin = new User("admin", "123456");
        //给ViewModel设置初始值
        mainViewModel.getUser().setValue(admin);

        //获取观察对象
        MutableLiveData<User> user1 = mainViewModel.getUser();

        //将ViewModel设置到xml中 也就是 给 xml中的data标签赋值
       user1.observe(this,user -> {
           dataBinding.setViewModel(mainViewModel);
       });

        dataBinding.btnLogin.setOnClickListener(v->{
          if(mainViewModel.user.getValue().getAccount().isEmpty()){
              Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
              return;
          }
          if(mainViewModel.user.getValue().getPwd().isEmpty()){
              Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
              return;
          }
          Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
      });
    }
}