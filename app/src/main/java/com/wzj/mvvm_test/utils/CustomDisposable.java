package com.wzj.mvvm_test.utils;


import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CustomDisposable {
    private static final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static <T> void addDisposable(Flowable<T> flowable, Consumer<T> consumer) {
        compositeDisposable.add(flowable
                .subscribeOn(Schedulers.io())//指定被观察者发送事件的线程
                .observeOn(AndroidSchedulers.mainThread())//指定观察者接收事件的线程
                .subscribe(consumer));//订阅
    }

    public static <T> void addDisposable(Completable completable, Action action) {
        compositeDisposable.add(completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action));
    }
}
