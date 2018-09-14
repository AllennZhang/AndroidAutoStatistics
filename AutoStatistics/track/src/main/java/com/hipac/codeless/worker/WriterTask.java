package com.hipac.codeless.worker;


import com.hipac.codeless.core.DataCore;

import java.util.List;

public class WriterTask<T> implements Runnable{

    private List<T> mDatas ;
    public WriterTask(List<T> datas) {
        mDatas = datas;
    }

    @Override
    public void run() {
//        MsgLogger.e("write-task is running: current thread is: "+Thread.currentThread().getName());
        if (mDatas != null && mDatas.size() >0) {
            DataCore.instance().getDataManager().writeEvents(mDatas);
        }
    }
}
