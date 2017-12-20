package com.android.Models;

import java.io.Serializable;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class Tag implements Serializable
{

    private String name;

    public Tag(String name) {
        super();
        this.name = name;
    }

    public Tag() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
