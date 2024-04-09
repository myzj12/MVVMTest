package com.wzj.mvvm_test.model;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.wzj.mvvm_test.BR;

public class User extends BaseObservable {
    private String account;
    private String pwd;

    public User(String account, String pwd) {
        this.account = account;
        this.pwd = pwd;
    }

    @Bindable
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
        notifyPropertyChanged(BR.account);//通知改变 所有参数改变
    }

    @Bindable
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        notifyPropertyChanged(BR.pwd);
    }
}
