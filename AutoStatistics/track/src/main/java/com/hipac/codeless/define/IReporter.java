package com.hipac.codeless.define;

import android.support.annotation.WorkerThread;

import com.hipac.codeless.update.TraceEvent;

import java.util.List;

/**
 * Created by youri on 2018/3/5.
 * 提供给第三方上报的数据，其他基础参数由第三方提供
 */

public interface IReporter {

    /**
     * 无痕埋点上报
     * @param dataList
     * @param callback
     */
    @WorkerThread
    void reportStatTraceLogs(List<TraceEvent> dataList, TrackSenderCallback callback);


}
