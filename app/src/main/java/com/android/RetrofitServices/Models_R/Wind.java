
package com.android.RetrofitServices.Models_R;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind implements Serializable
{

    @SerializedName("chill")
    @Expose
    private String chill;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("speed")
    @Expose
    private String speed;
    private final static long serialVersionUID = 8846580599563462823L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Wind() {
    }

    /**
     * 
     * @param speed
     * @param direction
     * @param chill
     */
    public Wind(String chill, String direction, String speed) {
        super();
        this.chill = chill;
        this.direction = direction;
        this.speed = speed;
    }

    public String getChill() {
        return chill;
    }

    public void setChill(String chill) {
        this.chill = chill;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

}
