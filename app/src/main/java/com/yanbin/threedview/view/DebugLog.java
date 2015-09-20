package com.yanbin.threedview.view;

import android.util.Log;

public class DebugLog {

    private static boolean debugOn = true;

    public static void print(Object logObject, String log){
        if(debugOn)
            Log.d(logObject.getClass().getSimpleName(), log);
    }

    public static boolean isDebugOn(){
        return debugOn;
    }
}
