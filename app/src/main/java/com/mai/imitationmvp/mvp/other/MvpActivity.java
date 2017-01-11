package com.mai.imitationmvp.mvp.other;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mai.imitationmvp.ui.BaseActivity;

/**
 * Created by Billy_Cui
 * on 2017/1/3.
 * github: https://github.com/SunshineCui
 */

public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {
    protected P mvpPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }
    
    public void showLoading(){showProgressDialog();}
    
    public void hideLoading(){dismissProgressDialog();}
}
