
package com.android.RetrofitServices.Models_R;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Astronomy implements Serializable
{

    @SerializedName("sunrise")
    @Expose
    private String sunrise;
    @SerializedName("sunset")
    @Expose
    private String sunset;
    private final static long serialVersionUID = 6961631676899541582L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Astronomy() {
    }

    /**
     * 
     * @param sunset
     * @param sunrise
     */
    public Astronomy(String sunrise, String sunset) {
        super();
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

}
