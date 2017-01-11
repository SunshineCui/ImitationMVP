package com.mai.imitationmvp.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mai.imitationmvp.R;
import com.mai.imitationmvp.retrofit.ApiStores;
import com.mai.imitationmvp.retrofit.AppClient;
import com.wuxiaolong.androidutils.library.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Billy_Cui on 2016/12/29.
 */
public class BaseActivity extends AppCompatActivity {
    public Activity mActivity;
    public ApiStores apiStores = AppClient.retrofit().create(ApiStores.class);
    private CompositeSubscription mCompositeSubscription;
    private List<Call> mCalls;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        mActivity = this;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
        mActivity = this;
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
        mActivity = this;
    }

    @Override
    protected void onDestroy() {
        callCancel();
        onUnsubscribe();
        super.onDestroy();
    }

    public void addCalls(Call call){
        if (mCalls == null){
            mCalls = new ArrayList<>();
        }
        mCalls.add(call);
    }
    
    private void callCancel(){
        if (mCalls != null && mCalls.size() >0) {
            for (Call call : mCalls){
                if (!call.isCanceled())call.cancel();
            }
        }  
        mCalls.clear();
    }
    
    public void addSubscription (Observable observable, Subscriber subscriber){
        if (mCompositeSubscription == null){
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber));
    }

    public void addSubscription (Subscriber subscriber){
        if (mCompositeSubscription == null){
            mCompositeSubscription = new CompositeSubscription();
            mCompositeSubscription.add(subscriber);
        }
    }
    
    
    
    private void onUnsubscribe() {
        LogUtil.d("onUnsubscribe");
        //取消注册,以避免内存泄露
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions())
            mCompositeSubscription.unsubscribe();
    }

    public Toolbar initToolBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null ){
            setSupportActionBar(toolbar);
            TextView toolbaTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbaTitle.setText(title);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        
        return toolbar;
    }

    public Toolbar initToolBarAsHome(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null ){
            setSupportActionBar(toolbar);
            TextView toolbaTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbaTitle.setText(title);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        
        return toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();//返回
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toastShow(int resID) {
        Toast.makeText(mActivity, resID, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resID) {
        Toast.makeText(mActivity, resID, Toast.LENGTH_SHORT).show();
    }
    
    public ProgressDialog mProgressDialog;
    
    public ProgressDialog showProgressDialog() {
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage("加载中");
        mProgressDialog.show();
        return mProgressDialog;
    }

    public ProgressDialog showProgressDialog(CharSequence message) {
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        return mProgressDialog;
    }
    
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
