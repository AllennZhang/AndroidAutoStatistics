package com.jerry.codeless.statconfig;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by youri on 2018/3/14.
 */

public class FloatingService extends Service {


    public static final int MSG_TOAST = 9000;
    public static final String ACTION_SHUTDOWN = "shutDown_Service";
    public static boolean shutDownFloating ;
    WindowManager mWindowManager;
    WindowManager.LayoutParams layoutParams;
    //用于检测状态栏高度.
    int statusBarHeight = 36;
    int lastX = 0;
    int lastY = 0;
    int startX = 0;
    int startY = 0;
    private FloatingReLayout floatingLayout;
    private Handler mHandler ;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_TOAST:
                        Toast.makeText(FloatingService.this,"复制成功",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
        floatingLayout = new FloatingReLayout(this);
        mWindowManager = floatingLayout.getWindowManager();
        floatingLayout.setCallback(new FloatingReLayout.Callback() {
            @Override
            public void clip() {
              if (mHandler != null){
                 Message msg = Message.obtain();
                 msg.what = MSG_TOAST;
                 mHandler.sendMessage(msg);
              }
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null ){
            String data = intent.getStringExtra("inner_data");
            String action = intent.getAction();
            if (ACTION_SHUTDOWN.equals(action)){
                close();
                stopSelf();
            }else {
                if (floatingLayout != null) {
                Log.e("onStartCommand-",data+"");
                floatingLayout.setContent(data);
//                    floatingLayout.setContent("安卓程序运行在手机端，用户更加注重程序的流畅度，因此程序的流畅性是安卓程序性能的一个重要指标。程序员在编写程序时需要注意一些常用的技巧以提高程序的流畅性。以下是个人总结的一些开发经验，如有疏漏还望指出一 View的优化1） 降低View树的高度，即减少View的层级嵌套，使用RelativeLayout替代LinearLayout。2） 使用include或者merge标签，将布局包含进来。3） 使用ViewStub，一些布局文件在正常情况下不会显示出来，可以使用ViewStub使其在使用时在加载。4） 不要在onDraw()等类似绘制函数中执行大量操作或者相对耗时的任务，影响View的流畅度。5） 不要在onDraw()类似的频繁被调用刷新的函数中创建局部对象，这会耗用大量内存。6） 避免过度绘制。出现过度绘制主要是由于View的backgroud重复绘制。首先Window本身就有自己的background，如果要自定义自己的background，得记得把window的background设为null。7） ListView的优化。ListView使用ViewHolder实现View的复用，并且不要在getView中执行耗时操作。二 内存泄露严格检查程序是否存在内存泄露的情况。三 优化系统的响应速度安卓程序运行在手机端，用户更加注重程序的流畅度，因此程序的流畅性是安卓程序性能的一个重要指标。程序员在编写程序时需要注意一些常用的技巧以提高程序的流畅性。以下是个人总结的一些开发经验，如有疏漏还望指出一 View的优化1） 降低View树的高度，即减少View的层级嵌套，使用RelativeLayout替代LinearLayout。2） 使用include或者merge标签，将布局包含进来。3） 使用ViewStub，一些布局文件在正常情况下不会显示出来，可以使用ViewStub使其在使用时在加载。4） 不要在onDraw()等类似绘制函数中执行大量操作或者相对耗时的任务，影响View的流畅度。5） 不要在onDraw()类似的频繁被调用刷新的函数中创建局部对象，这会耗用大量内存。6） 避免过度绘制。出现过度绘制主要是由于View的backgroud重复绘制。首先Window本身就有自己的background，如果要自定义自己的background，得记得把window的background设为null。7） ListView的优化。ListView使用ViewHolder实现View的复用，并且不要在getView中执行耗时操作。二 内存泄露严格检查程序是否存在内存泄露的情况。三 优化系统的响应速度安卓程序运行在手机端，用户更加注重程序的流畅度，因此程序的流畅性是安卓程序性能的一个重要指标。程序员在编写程序时需要注意一些常用的技巧以提高程序的流畅性。以下是个人总结的一些开发经验，如有疏漏还望指出一 View的优化1） 降低View树的高度，即减少View的层级嵌套，使用RelativeLayout替代LinearLayout。2） 使用include或者merge标签，将布局包含进来。3） 使用ViewStub，一些布局文件在正常情况下不会显示出来，可以使用ViewStub使其在使用时在加载。4） 不要在onDraw()等类似绘制函数中执行大量操作或者相对耗时的任务，影响View的流畅度。5） 不要在onDraw()类似的频繁被调用刷新的函数中创建局部对象，这会耗用大量内存。6） 避免过度绘制。出现过度绘制主要是由于View的backgroud重复绘制。首先Window本身就有自己的background，如果要自定义自己的background，得记得把window的background设为null。7） ListView的优化。ListView使用ViewHolder实现View的复用，并且不要在getView中执行耗时操作。二 内存泄露严格检查程序是否存在内存泄露的情况。三 优化系统的响应速度kjafjalkjfakljfkaljflajfklajfkaljflajflajffffffffffffffffffafjalfjaljfalfjafl");
                }
            }
        }

        return START_STICKY;
    }


    private void close (){
        if (mWindowManager != null && floatingLayout != null){
            mWindowManager.removeView(floatingLayout);
            Log.e("FloatingService","remove floatingLayout");
        }
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
        Log.e("FloatingService","closed Service");
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        if (mWindowManager != null && floatingLayout != null && floatingLayout.getParent() != null){
            mWindowManager.removeView(floatingLayout);
        }
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
        Log.e("FloatingService","onDestory");
        super.onDestroy();
    }
}
