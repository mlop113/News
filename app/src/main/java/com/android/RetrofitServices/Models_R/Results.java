
package com.android.RetrofitServices.Models_R;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Results implements Serializable
{

    @SerializedName("channel")
    @Expose
    private Channel channel;
    private final static long serialVersionUID = 4581205571566910904L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Results() {
    }

    /**
     * 
     * @param channel
     */
    public Results(Channel channel) {
        super();
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
