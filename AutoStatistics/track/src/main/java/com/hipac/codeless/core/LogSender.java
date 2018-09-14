package com.hipac.codeless.core;

import com.google.gson.Gson;
import com.hipac.codeless.define.IReporter;
import com.hipac.codeless.define.TrackSenderCallback;

import com.hipac.codeless.update.TraceEvent;
import com.hipac.codeless.util.MsgLogger;
import com.hipac.codeless.util.ThreadPoolManager;

import java.util.List;

/**
 * Created by youri on 2018/3/5.
 * 数据上报
 */

public class LogSender {
private static   volatile IReporter mReporter;
    private LogSender(){
        if (TraceService.baseConfig != null) {
            mReporter = TraceService.baseConfig.getReporter();
        }
    }

    private static class SenderHolder{
        public static final LogSender MINSTANCE = new LogSender();
    }

    public static LogSender instance(){
        return SenderHolder.MINSTANCE;
    }


    public void sendEvents(final List<TraceEvent> list){
        if (mReporter == null || list == null || list.size() == 0){
            return;
        }

        Gson gson = new Gson();
        String json = gson.toJson(list);
        MsgLogger.e("==========================LogSender-report statTrace logs starting==============================");
        MsgLogger.e("==========================LogRecords:=========size: " + list.size()+"===============content: "+json);
        mReporter.reportStatTraceLogs(list, new TrackSenderCallback() {
            @Override
            public void handleResult(boolean success) {
                if (success) {
                    ThreadPoolManager.instance().submit(new Runnable() {
                        @Override
                        public void run() {
                            DataCore.instance().getDataManager().clearSentEvents(list);
                        }
                    },"");
                    MsgLogger.e("==========================LogSender-report statTrace logs success==============================");
                }else {
                    ThreadPoolManager.instance().submit(new Runnable() {
                        @Override
                        public void run() {
                            long count = DataCore.instance().getDataManager().readCount();
                            MsgLogger.e("queryCount: "+count);
                            if (count >= DataCore.DB_CACHE_TOP_LIMIT){
                                DataCore.instance().getDataManager().clearSentEvents(list);
                            }
                        }
                    },"");
                }
            }
        });
    }


}
