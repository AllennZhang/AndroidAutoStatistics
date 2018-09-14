package com.hipac.codeless.store;

import com.hipac.codeless.core.TraceService;
import com.hipac.codeless.define.IDataCache;
import com.hipac.codeless.store.table.CombineEvent;

import java.util.List;

/**
 * Created by youri on 2018/3/8.
 */

public class DBManager implements IDataCache<CombineEvent> {

    @Override
    public void writeEvent(CombineEvent event) {
        try {
            if (TraceService.mDataBase != null ) {
                TraceService.mDataBase.eventDao().insert(event);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void writeEvents(List<CombineEvent> list) {
        try {
            if (TraceService.mDataBase != null ) {
                TraceService.mDataBase.eventDao().insert(list);
            }
        }catch (Exception e){

        }
    }

    @Override
    public List<CombineEvent> readEvents() {
        List<CombineEvent> query = null ;
        try {
            if (TraceService.mDataBase != null ) {
                query =   TraceService.mDataBase.eventDao().query();
            }
        }catch (Exception e){
               query = null;
        }
       return query;

    }

    @Override
    public long readCount() {
        long count = 0;
        try {
            if (TraceService.mDataBase != null){
                count = TraceService.mDataBase.eventDao().queryCount();
            }
        }catch (Exception e){
            count = 0;
        }
        return count;
    }

    @Override
    public void clearSentEvent(CombineEvent event) {
        try {
            if (TraceService.mDataBase != null){
                TraceService.mDataBase.eventDao().delete(event);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void clearSentEvents(List<CombineEvent> list) {
        try {
            if (TraceService.mDataBase != null){
                TraceService.mDataBase.eventDao().delete(list);
            }
        }catch (Exception e){

        }
    }


}
