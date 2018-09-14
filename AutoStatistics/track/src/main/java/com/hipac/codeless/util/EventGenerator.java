package com.hipac.codeless.util;

import com.hipac.codeless.store.table.CombineEvent;
import com.hipac.codeless.store.table.StatEvent;

/**
 * Created by youri on 2018/3/8.
 */

public class EventGenerator {

    @Deprecated
    public static StatEvent generateClickEvent(String viewPath,String eventType,String time){
        StatEvent event = new StatEvent(eventType,"",viewPath);
        return event;
    }

    @Deprecated
    public static StatEvent generatePageEvent(String page,String eventType,String time){
        StatEvent event = new StatEvent(eventType,page,"");
        return event;

    }


    public static CombineEvent generateCombinePageEvent(String type,String page,String utp, String time){
        CombineEvent event = new CombineEvent(time,type,new StatEvent(utp,page,""));
        return event;

    }

    public static CombineEvent generateCombineClickEvent(String type,String utp,String viewPath,String time){
        CombineEvent event = new CombineEvent(time,type,new StatEvent(utp,"",viewPath));
        return event;
    }

}
