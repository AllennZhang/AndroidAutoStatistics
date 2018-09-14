package com.yt.statistics.statplugin;

import com.hipac.codeless.define.IReporter;
import com.hipac.codeless.define.TrackSenderCallback;

import com.hipac.codeless.update.TraceEvent;

import java.util.List;


/**
 * Created by youri on 2018/3/7.
 */

public class StatReporter implements IReporter {
//   private static Gson gson = new Gson();

    @Override
    public void reportStatTraceLogs(List<TraceEvent> list, TrackSenderCallback trackSenderCallback) {
        if (list == null) {
            trackSenderCallback.handleResult(false);
        }
//        Log.e("App","=====send-logs: "+gson.toJson(list));
        trackSenderCallback.handleResult(true);
    }
}
