
package com.android.RetrofitServices.Models_R;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponeServices implements Serializable
{

    @SerializedName("query")
    @Expose
    private Query query;
    private final static long serialVersionUID = -6207675977218335823L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ResponeServices() {
    }

    /**
     * 
     * @param query
     */
    public ResponeServices(Query query) {
        super();
        this.query = query;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

}
