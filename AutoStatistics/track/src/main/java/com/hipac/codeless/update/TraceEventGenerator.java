package com.hipac.codeless.update;

/**
 * Created by youri on 2018/3/8.
 */

public class TraceEventGenerator {


    public static TraceEvent generatePageEvent(String type, String page,String ref ,String utp,String pageType,String utrp, String utrpUrl,String url,String extendFields ,String time){
        TraceEvent event = new TraceEvent(time,type,new TraceData(page,ref,utp,pageType,"",extendFields));
        return event;

    }

    public static TraceEvent generateClickEvent(String type, String page,String ref,String utp,String pageType, String utrp,String utrpUrl,String url,String viewPath, String extendFields,String time){
        TraceEvent event = new TraceEvent(time,type,new TraceData(page,ref,utp,pageType,viewPath,extendFields));
        return event;
    }
}
