package com.hipac.codeless.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by youri on 2018/3/5.
 */

public class SharedPreferenceUtil {

    private static final String DEF_SP = "share_attr";
    public static final String REF_PAGE = "refPage";



    public static SharedPreferences getSharedPreferences(Context context,String name){
        if (context == null) {
            return  null;
        }
        if (StringUtil.empty(name)){
            return PreferenceManager.getDefaultSharedPreferences(context);
        }
       return context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }

    public static void setRefPage(Context context,String page){
        getSharedPreferences(context,DEF_SP).edit().putString(REF_PAGE,page).commit();
    }

    public static String getRefPage(Context context){
       return getSharedPreferences(context,DEF_SP).getString(REF_PAGE,"");
    }

}
