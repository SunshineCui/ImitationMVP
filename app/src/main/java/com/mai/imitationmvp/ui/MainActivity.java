package com.mai.imitationmvp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mai.imitationmvp.R;
import com.mai.imitationmvp.mvp.main.MainModel;
import com.mai.imitationmvp.mvp.main.MainPresenter;
import com.mai.imitationmvp.mvp.main.MainView;
import com.mai.imitationmvp.mvp.other.MvpActivity;
import com.mai.imitationmvp.retrofit.ApiCallback;
import com.mai.imitationmvp.retrofit.RetrofitCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class MainActivity extends MvpActivity<MainPresenter> implements MainView {

    @Bind(R.id.text)
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBarAsHome("MVP+Retrofit+Rxjava");
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void getDataSuccess(MainModel model) {
        //接口成功回调
        dataSuccess(model);
    }

    @Override
    public void getDataFail(String msg) {
        toastShow(msg);
    }

    private void dataSuccess(MainModel model) {
        MainModel.WeatherinfoBean weatherInfo = model.getWeatherinfo();
        String showData = "城市:" + weatherInfo.getCity()
                + "风向:" + weatherInfo.getWD()
                + "风级:" + weatherInfo.getWS()
                + "发布时间:" + weatherInfo.getTime();
        text.setText(showData);
    }

    @OnClick({R.id.button0, R.id.button1, R.id.button2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button0:
                loadDataByRetrofit();
                break;

            case R.id.button1:
                loadDataByRetrofitRxjava();
                break;

            case R.id.button2:
                mvpPresenter.loadDataByRetrofitRxjava("101210101");
                break;
            default:
                break;
        }
    }

    private void loadDataByRetrofitRxjava() {
        showProgressDialog();
        addSubscription(apiStores.loadDataByRetrofitRxjava("101220602"),
                new ApiCallback<MainModel>() {
                    @Override
                    public void onSuccess(MainModel model) {
                        dataSuccess(model);
                    }

                    @Override
                    public void onFailure(String msg) {
                        toastShow(msg);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                }
        );
    }

    private void loadDataByRetrofit() {
        showProgressDialog();
        Call<MainModel> call = apiStores.loadDataByRetrofit("101190201");
        call.enqueue(new RetrofitCallback<MainModel>() {
            @Override
            public void onSuccess(MainModel model) {
                dataSuccess(model);
            }

            @Override
            public void onFailure(int code, String msg) {
                toastShow(msg);
            }

            @Override
            public void onThrowable(Throwable throwable) {
                toastShow(throwable.getMessage());
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
        addCalls(call);
    }
}
