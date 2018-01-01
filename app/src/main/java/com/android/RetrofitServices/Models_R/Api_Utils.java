package com.android.RetrofitServices.Models_R;

/**
 * Created by Ngoc Vu on 1/1/2018.
 */

public class Api_Utils {
    public static final String BASE_URL = "https://query.yahooapis.com/v1/public/";

    public static WeaService getWeaservice() {
        return RetrofitClient.getClient(BASE_URL).create(WeaService.class);
    }
}
