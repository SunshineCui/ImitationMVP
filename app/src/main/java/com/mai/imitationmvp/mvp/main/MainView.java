package com.mai.imitationmvp.mvp.main;

/**
 * Created by Billy_Cui
 * on 2017/1/3.
 * github: https://github.com/SunshineCui
 */

public interface MainView extends BaseView {
    void getDataSuccess(MainModel model);

    void getDataFail(String msg);
}
