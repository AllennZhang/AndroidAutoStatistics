package com.hipac.codeless.util;

/**
 * Created by youri on 2018/3/5.
 */

public class StringUtil {

    public static boolean empty(String str){
        boolean empty = false;
        if (str == null || str.equals("")){
            empty = true;
        }
        return empty;
    }
}
