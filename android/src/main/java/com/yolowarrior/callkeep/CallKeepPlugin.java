package com.yolowarrior.callkeep;

import android.util.Log;

public class CallKeepPlugin {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}
