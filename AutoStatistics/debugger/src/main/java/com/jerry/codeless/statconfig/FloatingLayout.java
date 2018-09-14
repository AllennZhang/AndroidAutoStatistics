package com.jerry.codeless.statconfig;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by youri on 2018/3/14.
 */

public class FloatingLayout extends android.support.v7.widget.AppCompatTextView {

    private int lastX;
    private int lastY;
    private int startX;
    private int startY;
    private int statebarHeight;
    WindowManager mWindowManager;
    WindowManager.LayoutParams layoutParams;
    private Context mContext;

    public FloatingLayout(Context context) {
        this(context,null);
    }

    public FloatingLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FloatingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        statebarHeight = getStatusBarHeight();
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.O){
            //解决Android 7.1.1起不能再用Toast的问题（先解决crash）
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else if (Build.VERSION.SDK_INT >Build.VERSION_CODES.N){
            //解决Android 7.1.1起不能再用Toast的问题（先解决crash）
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }else {
            /*以下代码块使得android6.0之后的用户不必再去手动开启悬浮窗权限*/
            String packname = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
            if (permission) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        }
        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
        layoutParams.x = 0;
        layoutParams.y = 0;

        //设置图片格式，效果为背景透明
        layoutParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        layoutParams.gravity = Gravity.START|Gravity.TOP;
        //设置悬浮窗口长宽数据
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        setText("hfklaflakfjla;afffffffffffff");
        setBackgroundColor(Color.parseColor("#e8e8e8"));

//        setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
//                ClipData clipData = ClipData.newPlainText("text",getText().toString().trim());
//                clipboardManager.setPrimaryClip(clipData);
//                return true;
//            }
//        });
        setOnTouchListener(new OnTouchListener() {

            private long endTime;
            private long startTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lastX = (int)event.getRawX();
                lastY = (int) (event.getRawY()-statebarHeight);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        Log.e("FloatingService","DOWN_startX:"+startX+"  startY:"+startY);
                       break;
                    case MotionEvent.ACTION_MOVE:
                        layoutParams.x = lastX -startX;
                        layoutParams.y = lastY - startY;
                        Log.e("FloatingService","MOVE_layoutParams.x:"+layoutParams.x+"  layoutParams.y:"+layoutParams.y);
                        Log.e("FloatingService","MOVE_lastX:"+lastX+"  lastY:"+lastY);
                        mWindowManager.updateViewLayout(FloatingLayout.this,layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        endTime = System.currentTimeMillis();
//                mWindowManager.updateViewLayout(this,layoutParams);
                        if (/*Math.abs(lastX - startX) <= 10 &&*/ Math.abs(lastY - startY) <= 10 && endTime - startTime > 2000){
                            //认为是点击事件
                            ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("text",getText().toString().trim());
                            clipboardManager.setPrimaryClip(clipData);
                        }else {
                            //认为是滑动事件，return true 消费事件
                        }
                        break;

                }
                return false;
            }
        });
        mWindowManager.addView(this,layoutParams);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        lastX = (int)event.getRawX();
//        lastY = (int) (event.getRawY()-statebarHeight);
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                startX = (int) event.getX();
//                startY = (int) event.getY();
//                Log.e("FloatingService","DOWN_startX:"+startX+"  startY:"+startY);
//                return  true;
//            case MotionEvent.ACTION_MOVE:
//                layoutParams.x = lastX -startX;
//                layoutParams.y = lastY - startY;
//                Log.e("FloatingService","MOVE_layoutParams.x:"+layoutParams.x+"  layoutParams.y:"+layoutParams.y);
//                Log.e("FloatingService","MOVE_lastX:"+lastX+"  lastY:"+lastY);
//                mWindowManager.updateViewLayout(this,layoutParams);
//                break;
//            case MotionEvent.ACTION_UP:
////                mWindowManager.updateViewLayout(this,layoutParams);
//                if (/*Math.abs(lastX - startX) <= 10 &&*/ Math.abs(lastY - startY) <= 10){
//                    //认为是点击事件
//                    return false;
//                }else {
//                    //认为是滑动事件，return true 消费事件
//                    return true;
//                }
//
//        }
//        return false;
//    }

    private int getStatusBarHeight() {
        Rect frame = new Rect();
        getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }


}
