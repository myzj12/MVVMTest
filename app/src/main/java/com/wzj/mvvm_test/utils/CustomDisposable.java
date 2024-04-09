package com.wzj.mvvm_test.utils;


import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CustomDisposable {
    //统一管理
    private static final CompositeDisposable compositeDisposable = new CompositeDisposable();

    //获取到的数据在主线程处理
    public static <T> void addDisposable(Flowable<T> flowable, Consumer<T> consumer) {
        compositeDisposable.add(flowable
                .subscribeOn(Schedulers.io())//指定被观察者发送事件的线程
                .observeOn(AndroidSchedulers.mainThread())//指定观察者接收事件的线程
                .subscribe(consumer));//订阅
    }


    /**
     * 这段代码的作用是将一个Completable异步操作添加到CompositeDisposable中，并指定了在I/O调度器上执行异步操作，
     * 在Android主线程上接收操作结果并执行action。这样的设计在Android应用中非常常见，因为它遵循了“不要在主线程上执行
     * 耗时操作”的最佳实践，并且通过RxJava的链式调用，使得代码更加简洁和易于理解。同时，通过使用CompositeDisposable，
     * 确保了资源的合理管理和释放。
     */
    public static <T> void addDisposable(Completable completable, Action action) {
        compositeDisposable.add(completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action));
    }
}
