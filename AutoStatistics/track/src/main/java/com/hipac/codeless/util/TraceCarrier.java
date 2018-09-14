package com.hipac.codeless.util;

import android.support.annotation.Nullable;
import android.view.View;

public class TraceCarrier {
    private static final int TRACE_DATA_ID = 0x7b00a1;
    private final static int LABEL_ID = 0x7b00b1;
    public static void setExtendFields(View view, @Nullable String label, @Nullable String extendFields){
       if (view != null ){
           view.setTag(LABEL_ID,label);
           view.setTag(TRACE_DATA_ID,extendFields);
       }
    }

}
