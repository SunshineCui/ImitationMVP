package com.mai.imitationmvp.retrofit;

import com.mai.imitationmvp.mvp.main.MainModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Billy_Cui on 2016/12/29.
 */

public interface ApiStores {
    
    //baseUrl
    String API_SERVER_URL = "http://www.weather.com.cn/";
    
    //加载天气
    @GET("adat/sk/{cityID}.html")
    Call<MainModel> loadDataByRetrofit(@Path("cityID") String cityID);
 
    //加载天气
    @GET("adat/sk/{cityID}.html")
    Observable<MainModel> loadDataByRetrofitRxjava(@Path("cityID") String cityID);
    
}
