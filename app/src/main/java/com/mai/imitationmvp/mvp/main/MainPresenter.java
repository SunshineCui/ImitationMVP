package com.mai.imitationmvp.mvp.main;

import com.mai.imitationmvp.mvp.other.BasePresenter;
import com.mai.imitationmvp.retrofit.ApiCallback;

/**
 * Created by Billy_Cui
 * on 2017/1/3.
 * github: https://github.com/SunshineCui
 */

public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(MainView view) {
        attachView(view);
    }

    public void loadDataByRetrofitRxjava(String cityId) {
        mvpView.showLoading();
        addSubScription(mApiStores.loadDataByRetrofitRxjava(cityId),
                new ApiCallback<MainModel>() {
                    @Override
                    public void onSuccess(MainModel model) {
                        mvpView.getDataSuccess(model);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.getDataFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }
}
