package com.hipac.codeless.util;

import android.util.Log;

/**
 * Created by youri on 2018/3/5.
 */

public class MsgLogger {

    private static boolean DEBUG = false;

    public static String TAG = "MsgLogger";


    public static void setDebug(boolean debugOn){
        DEBUG = debugOn;
    }

    public static void i(String message){
        if (DEBUG) {
            Log.i(TAG, message);
        }
    }

    public static void v(String message){
        if (DEBUG) {
            Log.v(TAG, message);
        }
    }

    public static void d(String message){
        if (DEBUG) {
            Log.d(TAG, message);
        }
    }

    public static void w(String message){
        if (DEBUG) {
            Log.w(TAG, message);
        }
    }

    public static void e(String message){
        if (DEBUG) {
            Log.e(TAG, message);
        }
    }

}
