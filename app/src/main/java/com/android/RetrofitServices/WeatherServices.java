package com.android.RetrofitServices;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.List;
/**
 * Created by Ngoc Vu on 12/31/2017.
 */

public interface WeatherServices {
    @GET("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"Ho Chi Minh, vn\")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
    Call<List<Query>> getAnswers();
}
