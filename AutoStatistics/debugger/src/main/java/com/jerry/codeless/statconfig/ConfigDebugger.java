package com.jerry.codeless.statconfig;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by youri on 2018/3/20.
 */

public class ConfigDebugger {

    public PointReporter mReporter;

    private ConfigDebugger(){

    }

    private static class Holder{
        public static ConfigDebugger MINSTANCE = new ConfigDebugger();
    }

    public static ConfigDebugger instance(){
        Log.e("ConfigDebugger","ConfigDebugger-instance1: "+Holder.MINSTANCE);
        Log.e("ConfigDebugger","ConfigDebugger-instance2: "+Holder.MINSTANCE);
        return Holder.MINSTANCE;
    }

    public synchronized void init(@NonNull Context context, PointReporter reporter){
        mReporter = reporter;
        if (context != null){
            context.startService(new Intent(context,FloatingService.class));
        }
    }

}
