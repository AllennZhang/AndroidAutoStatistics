package com.hipac.codeless.update;


import com.hipac.codeless.core.TraceService;
import com.hipac.codeless.define.IDataCache;
import com.hipac.codeless.util.MsgLogger;

import java.util.List;

/**
 * Created by youri on 2018/3/8.
 */

public class NewDBEngine implements IDataCache<TraceEvent> {
    @Override
    public void writeEvent(TraceEvent event) {
        try {
            if (TraceService.mDataBase != null) {
                TraceService.mDataBase.traceEventDao().insert(event);
            }
        }catch (Exception e){
            MsgLogger.e("writeEvent-error: "+e.toString());
        }
    }

    @Override
    public void writeEvents(List<TraceEvent> events) {
        try {
            if (TraceService.mDataBase != null) {
                TraceService.mDataBase.traceEventDao().insert(events);
            }
        }catch (Exception e){
            MsgLogger.e("writeEvents-error: "+e.toString());
        }
    }

    @Override
    public List<TraceEvent> readEvents() {
        List<TraceEvent> query = null;
        try {
            if (TraceService.mDataBase != null){
                query = TraceService.mDataBase.traceEventDao().query();
            }
        }catch (Exception e){
            query = null;
            MsgLogger.e("readEvents-error: "+e.toString());
        }
      return query;
    }

    @Override
    public long readCount() {
        long count = 0;
        try {
            if (TraceService.mDataBase != null){
                count = TraceService.mDataBase.traceEventDao().queryCount();
            }
        }catch (Exception e){
            count = 0;
            MsgLogger.e("readCount-error: "+e.toString());
        }
        return count;
    }

    @Override
    public void clearSentEvent(TraceEvent event) {
        try {
            if (TraceService.mDataBase != null){
                TraceService.mDataBase.traceEventDao().delete(event);
            }
        }catch (Exception e){
            MsgLogger.e("clearSentEvent-error: "+e.toString());
        }
    }


    @Override
    public void clearSentEvents(List<TraceEvent> list) {
        try {
            if (TraceService.mDataBase != null){
                TraceService.mDataBase.traceEventDao().delete(list);
            }
        }catch (Exception e){
            MsgLogger.e("clearSentEvents-error: "+e.toString());
        }
    }


}
