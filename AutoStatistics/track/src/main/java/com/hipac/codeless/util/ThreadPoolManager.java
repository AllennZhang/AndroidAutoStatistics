package com.hipac.codeless.util;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by youri on 2018/3/6.
 */

public class ThreadPoolManager {
    private static ThreadPoolManager mInstance = new ThreadPoolManager();

    private int corePoolSize;//核心线程池的数量
    private int maxPoolSize;//最大线程池数量
    private long keepAliveTime = 60;//存活时间
    private TimeUnit unit = TimeUnit.SECONDS;
    private ThreadPoolExecutor executorService;
    private static Map<String ,Future<?>> poolTasks = new HashMap<>();

    public static ThreadPoolManager instance(){
        return mInstance;
    }

    private ThreadPoolManager(){
        corePoolSize = 2;
        maxPoolSize = Runtime.getRuntime().availableProcessors()*2+1;
        executorService = new ScheduledThreadPoolExecutor(corePoolSize,
                new ThreadFactory() {
                    AtomicInteger mCount = new AtomicInteger(1);
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r,"trace-thread"+mCount.getAndIncrement());
                    }
                },
                new ThreadPoolExecutor.DiscardOldestPolicy()) {
        };
        executorService.setKeepAliveTime(keepAliveTime,unit);
        executorService.setMaximumPoolSize(maxPoolSize);
    }


    public Future<?> submit(Runnable runnable,String tag){
        if(runnable==null)return null;

        Future<?> future = executorService.submit(runnable);
        if (!StringUtil.empty(tag)){
            poolTasks.put(tag,future);
        }
        return future;
    }

    public Future<?> scheduleExecute(Runnable runnable,String tag){
        return scheduleExecute(runnable,10,tag);
    }

    public Future<?> scheduleExecute(Runnable runnable,long delay,String tag){
        if (runnable == null) return null;
        if (executorService instanceof ScheduledExecutorService){
            ScheduledExecutorService scheduledExecutor = (ScheduledExecutorService) executorService;
            Future<?> future = scheduledExecutor.scheduleWithFixedDelay(runnable,0,delay <= 0 ? 10 : delay,unit);
            if (!StringUtil.empty(tag)){
                poolTasks.put(tag,future);
            }
            return future;
        }else {
            Future<?> future = executorService.submit(runnable);
            if (!StringUtil.empty(tag)){
                poolTasks.put(tag,future);
            }
            return future;
        }
    }


    public void cancel(String tag){
        Future<?> future = poolTasks.get(tag);
        if(future==null)return;
        try {
            future.cancel(true);
            executorService.purge();
            poolTasks.remove(tag);
        }catch (Exception e){
            MsgLogger.e("cancel-error:"+e.toString());
        }
    }

    public void cancelAll(){
        for (Map.Entry<String,Future<?>> entry : poolTasks.entrySet()){
            Future<?> future = entry.getValue();
            if (future != null){
                try {
                    future.cancel(true);
                }catch (Exception e){
                    MsgLogger.e("cancelAll-error:"+e.toString());
                }
            }
        }
        try {
            poolTasks.clear();
            executorService.purge();
        }catch (Exception e){
            MsgLogger.e("cancelAll-error:"+e.toString());
        }
    }


    public void shutDown(){
        try {
            poolTasks.clear();
            executorService.purge();
            executorService.shutdown();
        }catch (Exception e){
            MsgLogger.e("shutDown-error:"+e.toString());
        }
    }

}
