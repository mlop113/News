package com.android.Effect;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ngoc Vu on 1/2/2018.
 */

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;
    public Session(Context ctx)
    {
        this.ctx=ctx;
        prefs=ctx.getSharedPreferences("myapp",Context.MODE_PRIVATE);
         editor=prefs.edit();
    }
    public void setLoginin(boolean logggedin)
    {
        editor.putBoolean("loggedInmode",logggedin);
        editor.commit();
    }
    public boolean loggedin()
    {
        return prefs.getBoolean("loggedInmode",false);
    }
}
