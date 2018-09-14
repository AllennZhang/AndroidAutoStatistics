package com.hipac.codeless.define;

import java.util.List;

/**
 * Created by youri on 2018/3/7.
 * 数据访问接口，后期可以拓展
 */

public interface IDataCache<T extends ITraceEvent> {

    void writeEvent(T event);

    void writeEvents(List<T> list);

    List<T> readEvents();

    long readCount();


    void clearSentEvent(T events);

    void clearSentEvents(List<T> list);


}
