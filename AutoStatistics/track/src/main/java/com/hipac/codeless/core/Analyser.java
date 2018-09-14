package com.hipac.codeless.core;

import com.hipac.codeless.config.Constants;
import com.hipac.codeless.store.EventDatabase;
import com.hipac.codeless.update.TraceEvent;
import com.hipac.codeless.update.TraceEventGenerator;
import com.hipac.codeless.util.ThreadPoolManager;

import java.util.Map;

/**
 * Created by youri on 2018/3/5.
 * 事件分发处理对象
 */

public class Analyser {

   private static  final Analyser mInstance = new Analyser();




    private Analyser(){
    }


    public static Analyser instance(){
        return mInstance;
    }

   public void onPageStart(final String page, String ref ,String pageType, final String utrp, final String utrpUrl, String url, final String extendsFields){
       TraceEvent traceEvent = TraceEventGenerator.generatePageEvent(Constants.BUSINESS_TYPE_PAGE_EXPOSURE,page,ref,Constants.EVENT_TYPE_PAGE,pageType,utrp,utrpUrl,url,extendsFields,System.currentTimeMillis() + "");
//       MsgLogger.e("Page_Exposure:"+traceEvent.toString());
       DataCore.instance().saveData(traceEvent);
   }

    public void onPageEnd(String page,Map<String,String> params){

    }

    public void onResume(final String page, final String ref,String pageType, final String utrp, final String utrpUrl, String url, String extendFields){
        TraceEvent traceEvent = TraceEventGenerator.generatePageEvent(Constants.BUSINESS_TYPE_PAGE_EXPOSURE,page,ref,Constants.EVENT_TYPE_PAGE,pageType,utrp,utrpUrl,url,extendFields,System.currentTimeMillis() + "");
//        MsgLogger.e("Page_Exposure:"+traceEvent.toString());
        DataCore.instance().saveData(traceEvent);
    }

    public void onPause(String page,Map<String,String> params){

    }

    public void onEvent(String page,String ref,String utrp,String utrpUrl,String url, String viewPath,String extendsFields){
        TraceEvent traceEvent = TraceEventGenerator.generateClickEvent(Constants.BUSINESS_TYPE_ELEMENT_CLICK,page,ref,Constants.EVENT_TYPE_CLICK,"",utrp,utrpUrl,url,viewPath,extendsFields,System.currentTimeMillis() + "");
//        MsgLogger.e("Click_Event:"+traceEvent.toString());
        DataCore.instance().saveData(traceEvent);
    }





    public synchronized void initDatabase(final TraceService.DatabaseInitCallback callback){
        ThreadPoolManager.instance().submit(new Runnable() {
            @Override
            public void run() {
                EventDatabase database = EventDatabase.instance(TraceService.mContext);
                if (callback != null){
                    callback.initFinished(database == null ? false :true);
                }
                TraceService.mDataBase = database;
            }
        },"");
    }


}
