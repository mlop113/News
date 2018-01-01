
package com.android.RetrofitServices.Models_R;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Atmosphere implements Serializable
{

    @SerializedName("humidity")
    @Expose
    private String humidity;
    @SerializedName("pressure")
    @Expose
    private String pressure;
    @SerializedName("rising")
    @Expose
    private String rising;
    @SerializedName("visibility")
    @Expose
    private String visibility;
    private final static long serialVersionUID = 7170042843144588886L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Atmosphere() {
    }

    /**
     * 
     * @param rising
     * @param humidity
     * @param pressure
     * @param visibility
     */
    public Atmosphere(String humidity, String pressure, String rising, String visibility) {
        super();
        this.humidity = humidity;
        this.pressure = pressure;
        this.rising = rising;
        this.visibility = visibility;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getRising() {
        return rising;
    }

    public void setRising(String rising) {
        this.rising = rising;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

}
