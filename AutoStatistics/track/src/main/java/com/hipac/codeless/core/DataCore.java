package com.hipac.codeless.core;

import com.hipac.codeless.define.IDataCache;
import com.hipac.codeless.update.NewDBEngine;
import com.hipac.codeless.update.TraceEvent;
import com.hipac.codeless.util.MsgLogger;
import com.hipac.codeless.util.ThreadPoolManager;
import com.hipac.codeless.worker.WriterTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youri on 2018/3/5.
 * 数据交互中心
 */

public class DataCore {
    //默认缓存容量 20条数据
    private  static final int CACHE_CAPACITY = 2;
    private  static int SEND_CAPACITY = 20;
    public static final int DB_CACHE_TOP_LIMIT = 500;
    private IDataCache mDataManager ;


    private  static DataCore mInstance = new DataCore();
    private  volatile List<TraceEvent> mChachedList ;


    public  IDataCache getDataManager(){
        return mDataManager;
    }


    private DataCore(){
            mChachedList = new ArrayList<>();
            mDataManager = new NewDBEngine();
            if (TraceService.baseConfig != null && TraceService.baseConfig.getCapacity() >0){
                SEND_CAPACITY = TraceService.baseConfig.getCapacity();
            }

    }




    public static  DataCore instance(){
        return mInstance;
    }

    public  void saveData(TraceEvent statEvent){
        synchronized (TraceService.mDataBase) {
            if (mChachedList.size() >= CACHE_CAPACITY){
                //到达缓存容量，本地存储事件
                List<TraceEvent> tmp = new ArrayList<>(mChachedList);
                ThreadPoolManager.instance().submit(new WriterTask<TraceEvent>(tmp), "");
                MsgLogger.e("mChachedList: full size is  " + mChachedList.size());
                //内存缓存当前事件
                clearCahce();
                mChachedList.add(statEvent);
            }else {
                mChachedList.add(statEvent);
                MsgLogger.e("mChachedList: "+mChachedList.size());
            }
        }
    }

    private void clearCahce(){
        if (mChachedList == null) {
            mChachedList = new ArrayList<>(CACHE_CAPACITY);
        }else {
            mChachedList.clear();
        }
    }


    /**
     * Do not do this in mainThread
     * Otherwise throw java.lang.IllegalStateException:
     * Cannot access database on the main thread
     * since it may potentially lock the UI for a long period of time.
     * @return
     */
    public  void handleAppBackground(){
        if (mChachedList != null && mChachedList.size() >0){
            synchronized (TraceService.mDataBase) {
                final List<TraceEvent> tmp = new ArrayList<>(mChachedList);
                clearCahce();
                ThreadPoolManager.instance().submit(new WriterTask<TraceEvent>(tmp), "");
                MsgLogger.e("write success data size is " + tmp.size());
            }
        }
    }


}
