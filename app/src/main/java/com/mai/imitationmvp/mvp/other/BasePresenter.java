package com.mai.imitationmvp.mvp.other;

import com.mai.imitationmvp.retrofit.ApiStores;
import com.mai.imitationmvp.retrofit.AppClient;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Billy_Cui on 2017/1/3.
 */

public class BasePresenter<V> {
    public V mvpView;
    protected ApiStores mApiStores;
    private CompositeSubscription mCompositeSubscription;
    
    public void attachView(V mvpView){
        this.mvpView = mvpView;
        mApiStores = AppClient.retrofit().create(ApiStores.class);
    }

    /**
     * 清空布局,销毁Rxjava
     */
    public void detachView() {
        this.mvpView = null ;
        onUnsubscribe();
    }

    //RXjava取消注册,以避免内存泄露
    private void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubScription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
        );
    }
}
