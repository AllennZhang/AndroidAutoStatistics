package com.hipac.codeless.update;

import android.arch.persistence.room.ColumnInfo;

/**
 * Created by youri on 2018/3/7.
 *
 * page : String类型，页面控制器名称
 * viewPath:String 类型，点击View的路径
 * eventType: String类型，"0" 页面曝光  "1" 点击事件
 * time：事件发生时间 时间戳字符串  1520516664491
 * pageType: "1" 标记原生页面，"2"标记H5页面
 */

public class TraceData {





  @ColumnInfo(name = "page")
  public String page;

  @ColumnInfo(name = "ref")
  public String ref;

  @ColumnInfo(name = "viewPath")
  public String viewPath;

  @ColumnInfo(name = "eventType")
  public String eventType;


  @ColumnInfo(name = "pageType")
  public String pageType;



  @ColumnInfo(name = "extendFields")
  public String extendFields;





  public TraceData(String page, String ref,String eventType, String pageType,String viewPath,  String extendFields){
     this.page = page;
     this.ref = ref;
     this.eventType = eventType;
     this.pageType = pageType;
     this.viewPath = viewPath;
     this.extendFields = extendFields;
  }


    @Override
    public String toString() {

      return
              "{\npage: "+page
              +"\nref:"+ref
              +"\npageType:"+pageType
              +"\nviewPath:"+viewPath
              +"\nextendFileds:"+extendFields+"}";

    }
}
