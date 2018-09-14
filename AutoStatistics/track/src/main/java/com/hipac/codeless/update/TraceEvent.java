package com.hipac.codeless.update;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.hipac.codeless.define.ITraceEvent;

/**
 * Created by youri on 2018/3/26.
 */

@Entity(tableName = "traceEvent")
public class TraceEvent implements ITraceEvent {

    @PrimaryKey/*(autoGenerate = true)*/
    @NonNull
    @ColumnInfo(name = "requestTime")
    public String requestTime ;


    @ColumnInfo(name = "type")
    public String type;





    @Embedded
    public TraceData data;


    public TraceEvent(String requestTime, String type, TraceData data){
        this.requestTime = requestTime;
        this.type = type;
        this.data= data;
    }



    @Override
    public String toString() {
        return "\n{requestTime: "+requestTime
                +"\ntype:"+type
                +"\ndata: "+data.toString()+"\n}";

    }

}
