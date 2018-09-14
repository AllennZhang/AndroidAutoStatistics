package com.hipac.codeless.define;

import android.content.Context;

import com.hipac.codeless.update.TraceEvent;

/**
 * Created by youri on 2018/3/16.
 * 数据共享接口
 */

public interface ShareInterface {

        void eventPath(Context context, String viewPath);
        void eventChain(Context context, TraceEvent traceEvent);

}
