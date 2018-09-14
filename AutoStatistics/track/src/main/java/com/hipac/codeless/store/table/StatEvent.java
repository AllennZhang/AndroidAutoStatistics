package com.hipac.codeless.store.table;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

/**
 * Created by youri on 2018/3/7.
 *
 * page : String类型，页面控制器名称
 * viewPath:String 类型，点击View的路径
 * eventType: String类型，"0" 页面曝光  "1" 点击事件
 * time：事件发生时间 时间戳字符串  1520516664491
 */

@Entity(tableName = "event")
public class StatEvent {


  @ColumnInfo(name = "page")
  public String page;

  @ColumnInfo(name = "viewPath")
  public String viewPath;

  @ColumnInfo(name = "eventType")
  public String utp;




  public StatEvent(String utp,String page,String viewPath){
     this.utp = utp;
     this.page = page;
     this.viewPath = viewPath;
  }


    @Override
    public String toString() {
      return "eventType:"+utp
              +"\npage: "+page
              +"\nviewPath:"+viewPath;

    }
}
