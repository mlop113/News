package com.android.RetrofitServices.Models_R;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;
/**
 * Created by Ngoc Vu on 1/1/2018.
 */

public interface WeaService {
    @GET("yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22hochiminh%2C%20vn%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
    Call<ResponeServices> getAnswers();
}
