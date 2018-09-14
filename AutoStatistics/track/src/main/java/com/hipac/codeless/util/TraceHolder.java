package com.hipac.codeless.util;

import com.hipac.codeless.config.Constants;
import com.hipac.codeless.core.TraceService;

import java.util.HashMap;

/**
 * Created by wangxinbo on 2017/10/27.
 */

public class TraceHolder {
    private static final String GOODS_DETAIL="application/detail.html";

    private static String pageExtendFildes;

    public static final String ENTER_PAGE = "0";
    public static final String ELEMENT_CLICK="1";

    public static final String CHANEL = "6.0.0.0.0";

    public static final String DEFAULT_PRE = "";

    //上一个页面最后一个viewPath或者上一个page
    private static String referencePage = "";
    //
    public static String currentPage = "";
    public static String utrpUrl = "";
    public static String h5Url = "";

    public static HashMap<Class<?>,String> pageExtendsMap = new HashMap<>();


    public static void saveStatisticsPoint(String pointCode){

    }

    public static void changePagePoint(String newagePoint){

    }

    public static void h5EventStatistics(String type,String utrp,String url,String extendsFields){
     if (ENTER_PAGE.equals(type)){
         h5PageResume(utrp,url,extendsFields);
     }else if (ELEMENT_CLICK.equals(type)){
       h5ClickSavePoint(utrp,url,extendsFields);
     }
    }

    private static void h5PageResume(String utrp,String url,String extendsFields){
        TraceService.onResume("H5Page",getReferencePage(),Constants.BUSINESS_TYPE_H5PAGE,utrp,getUtrpUrl(),url,extendsFields);
        changeRefPage("H5Page");
        changeUtrpUrl(utrp);
    }

    private static void  h5ClickSavePoint(String utrp,String url,String extendsFields){
        TraceService.onEvent("H5Page",getReferencePage(),utrp,getUtrpUrl(),url,"",extendsFields);
        changeRefPage("H5Page");
        changeUtrpUrl(utrp);
    }



    public static void setPageExtendFields(Class<?> clazz, String extendFileds){
        pageExtendFildes = extendFileds;
        pageExtendsMap.put(clazz,extendFileds);
    }

    public static String getPageExtendFields(Class<?> clazz){
        return  pageExtendsMap.get(clazz) == null ? "" : pageExtendsMap.get(clazz);
    }

    public static void changeRefPage(String page){
        referencePage = page;
    }


    public static String getReferencePage(){
        return referencePage == null ? "" : referencePage ;
    }

    public static void changeH5Url(String url){
        h5Url = url;
    }

    public static String getH5Url(){
        return h5Url == null ? "" : h5Url;
    }

    public static void changeUtrpUrl(String utrp){
        utrpUrl = utrp;
    }

    public static String getUtrpUrl(){
        return utrpUrl == null ? "" : utrpUrl;
    }
}
