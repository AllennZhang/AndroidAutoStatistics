package com.hipac.codeless.worker;


import com.hipac.codeless.core.DataCore;
import com.hipac.codeless.core.LogSender;
import com.hipac.codeless.update.TraceEvent;
import com.hipac.codeless.util.MsgLogger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReaderTask implements Runnable {

    public ReaderTask() {

    }

    @Override
    public void run() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        MsgLogger.e("reader-task is running: current thread is: "+Thread.currentThread().getName()+ " time is: "+format.format(date));
        long count = DataCore.instance().getDataManager().readCount();
        List<TraceEvent> events = DataCore.instance().getDataManager().readEvents();
        MsgLogger.e("reader-task stat total is "+ count +"  send size is "+(events == null ? 0 :events.size()));
        if (events != null && events.size() >10){
            LogSender.instance().sendEvents(events);
        }

    }
}
