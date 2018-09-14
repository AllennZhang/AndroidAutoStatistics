package com.yt.statistics.statplugin;

import android.util.Log;

import com.jerry.codeless.statconfig.PointReporter;

/**
 * Created by youri on 2018/3/20.
 */

public class StatPointReporter implements PointReporter {

    @Override
    public void sendPoint(String redpil, String viewPath) {
        Log.e("StatPointReporter","redpil :"+redpil+"   viewPath:"+viewPath);
    }
}
