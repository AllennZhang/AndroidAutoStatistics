package com.hipac.codeless.store.table;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.hipac.codeless.define.ITraceEvent;

/**
 * Created by youri on 2018/3/26.
 */

@Entity(tableName = "combineEvent")
public class CombineEvent implements ITraceEvent {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String requestTime ;


    @ColumnInfo(name = "type")
    public String type;

    //@Embedded 将StatEvent作为单独的一列插入数据库，默认是不支持直接对象间引用
    @Embedded
    public StatEvent data;


    public CombineEvent(String requestTime,String type,StatEvent data){
        this.requestTime = requestTime;
        this.type = type;
        this.data= data;
    }

    @Override
    public String toString() {
        return "id: "+requestTime
                +"\ntype:"+type
                +"\nevent: "+data.toString();

    }
}
