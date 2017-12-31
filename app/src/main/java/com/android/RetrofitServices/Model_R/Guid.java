
package com.android.RetrofitServices.Model_R;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Guid implements Serializable
{

    @SerializedName("isPermaLink")
    @Expose
    private String isPermaLink;
    private final static long serialVersionUID = -1683193387136269116L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Guid() {
    }

    /**
     * 
     * @param isPermaLink
     */
    public Guid(String isPermaLink) {
        super();
        this.isPermaLink = isPermaLink;
    }

    public String getIsPermaLink() {
        return isPermaLink;
    }

    public void setIsPermaLink(String isPermaLink) {
        this.isPermaLink = isPermaLink;
    }

}
