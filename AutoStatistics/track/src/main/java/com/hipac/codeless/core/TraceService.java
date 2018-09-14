package com.hipac.codeless.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.hipac.codeless.config.Constants;
import com.hipac.codeless.config.StrategyConfig;
import com.hipac.codeless.store.EventDatabase;
import com.hipac.codeless.util.MsgLogger;
import com.hipac.codeless.util.SharedPreferenceUtil;
import com.hipac.codeless.util.StringUtil;
import com.hipac.codeless.util.ThreadPoolManager;
import com.hipac.codeless.worker.ReaderTask;

import java.util.Map;

/**
 * Created by youri on 2018/3/5.
 * 初始化配置类，事件统计方法代理者
 */

public class TraceService {
    public static Context mContext;
    public static StrategyConfig baseConfig;
    public static EventDatabase mDataBase;
    private static volatile  boolean hasDbReady = false;
    private static String mCurrentPage = "";
    private static String[] ignorePages = {""};


    public static synchronized void init(@NonNull final Application application, @NonNull StrategyConfig config, boolean debugOn){
         if (application == null){
             MsgLogger.e("context can't be  null, and TraceService init failed");
             return;
         }
         if (config == null || config.getReporter() == null){
             MsgLogger.e("StrategyConfig can't be  null,IReporter must be setted!!!,and TraceService init failed");
             return;
         }
        mContext = application.getApplicationContext();
        baseConfig = config;
        if (!hasDbReady) {
            Analyser.instance().initDatabase(new DatabaseInitCallback() {
                @Override
                public void initFinished(boolean success) {
                    hasDbReady = success;
                    MsgLogger.e("database init result is :"+hasDbReady);
                }
            });
        }
        MsgLogger.setDebug(debugOn);
        MsgLogger.e("HipacStatService_init:"+ DataCore.instance());
        application.registerActivityLifecycleCallbacks(new TraceLifeCallback());
    }




    public static  void onPageStart(String page,String ref,String utrp,String utrpUrl,String extendsFields){
        if (mContext == null || !hasDbReady) {
            return;
        }

        if (!StringUtil.empty(page)){
            Analyser.instance().onPageStart(page,ref, Constants.BUSINESS_TYPE_NATIVE_PAGE,utrp,utrpUrl,"",extendsFields);
        }
    }

    public static  void onPageEnd(String page,String ref,String utrp,String utrpUrl,String extendsFields){
        if (mContext == null || !hasDbReady) {
            return;
        }
        if (!StringUtil.empty(page)){
            Analyser.instance().onPageStart(page,ref, Constants.BUSINESS_TYPE_NATIVE_PAGE,utrp,utrpUrl,"",extendsFields);
        }
    }

    public static  void onResume(String page,String ref,String pageType,String utrp,String utrpUrl,String url,String extendsFields){
        if (mContext == null || !hasDbReady) {
            return;
        }
        if (!StringUtil.empty(page)){
            Analyser.instance().onResume(page,ref,pageType,utrp,utrpUrl,url,extendsFields);
        }
    }

    public static  void onPause(String page,Map<String,String> params){
        if (mContext == null || !hasDbReady) {
            return;
        }
        if (!StringUtil.empty(page)){
            Analyser.instance().onPause(page,params);
        }
    }

    public static  void onEvent(String page,String viewPath){

    }

    public static  void onEvent(String page, String ref,String utrp,String utrpUrl,String url,String viewPath, Object extendsFields){
        if (mContext == null || !hasDbReady) {
            return;
        }
        try {
            String data = (String) extendsFields;
            Analyser.instance().onEvent(page,ref,utrp,utrpUrl,url,viewPath,data);
            share(viewPath);

        }catch (Exception e){

        }
    }


    public static void setCurrentPage(String page){
        mCurrentPage = page;
        SharedPreferenceUtil.setRefPage(mContext,page);
    }

    public static String getCurrentPage(){
        return mCurrentPage == null ? "" : mCurrentPage;
    }


    public static void share(String data){
        if (baseConfig != null && baseConfig.getShareInterface() != null && baseConfig.share()){
            baseConfig.getShareInterface().eventPath(mContext,data);
        }
    }


    public interface DatabaseInitCallback{
        void initFinished(boolean success);
    }


    private static class TraceLifeCallback implements Application.ActivityLifecycleCallbacks{
        int   activityStartCount=0;
        private String tag = "";
        private String hopTraceTag = "";

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            activityStartCount++;
            if (activityStartCount == 1){
                //应用处于后台
                if (hasDbReady) {
                    tag = "trace-task-"+System.currentTimeMillis();
                    hopTraceTag = "hop-trace-task-"+System.currentTimeMillis();
                    MsgLogger.e("handle when app foreground tag is "+tag);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ThreadPoolManager.instance().scheduleExecute(new ReaderTask(),15 ,tag);
                        }
                    }, 3000);
                }
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityStartCount--;
            if (activityStartCount == 0){
                //应用处于后台
                if (hasDbReady) {
                    MsgLogger.e("handle when app background");
                    DataCore.instance().handleAppBackground();
                    ThreadPoolManager.instance().cancel(tag);
                    ThreadPoolManager.instance().cancel(hopTraceTag);
                }
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

}
