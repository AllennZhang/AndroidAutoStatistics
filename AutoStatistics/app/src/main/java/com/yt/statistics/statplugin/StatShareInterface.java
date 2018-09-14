package com.yt.statistics.statplugin;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hipac.codeless.define.ShareInterface;

import com.hipac.codeless.update.TraceEvent;
import com.jerry.codeless.statconfig.FloatingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by youri on 2018/3/16.
 */

public class StatShareInterface implements ShareInterface {
    private static List<TraceEvent> mCache = new ArrayList<>();
    private static Gson gson = new Gson();
    @Override
    public void eventPath(Context context,String viewPath) {

    }

    @Override
    public void eventChain(Context context, TraceEvent traceEvent) {
        if (mCache.size() > 5){
            mCache.clear();
        }
        mCache.add(traceEvent);
        String json = gson.toJson(mCache);
        Log.e("ShareInterface",json+"");
        Intent intent = new Intent(context, FloatingService.class);
        intent.putExtra("inner_data",prettyJson(json));
        if (!FloatingService.shutDownFloating) {
            context.startService(intent);
        }
    }





    /**
     * It is used for json pretty print
     */
    private static final int JSON_INDENT = 2;
    public static String prettyJson(String message){
        if(TextUtils.isEmpty(message)){
            return message;
        }
        try {
            message = message.trim();
            if (message.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(message);
                return jsonObject.toString(JSON_INDENT);
            }
            if (message.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(message);
                return jsonArray.toString(JSON_INDENT);
            }
            return message;
        } catch (JSONException e) {
            return message;
        }
    }


}
