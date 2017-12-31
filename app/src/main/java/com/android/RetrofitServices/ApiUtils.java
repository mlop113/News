package com.android.RetrofitServices;

/**
 * Created by Ngoc Vu on 12/31/2017.
 */

public class ApiUtils {
    public static final String BASE_URL = "https://query.yahooapis.com/v1/public/yql?q=";

    public static WeatherServices getWService() {
        return RetrofitClient.getClient(BASE_URL).create(WeatherServices.class);
    }
}
