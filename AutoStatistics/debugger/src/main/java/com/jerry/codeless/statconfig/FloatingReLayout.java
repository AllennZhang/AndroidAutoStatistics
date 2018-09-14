package com.jerry.codeless.statconfig;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by youri on 2018/3/14.
 */

public class FloatingReLayout extends RelativeLayout {

    private int lastX;
    private int lastY;
    private int startX;
    private int startY;
    private int statebarHeight;
    WindowManager mWindowManager;
    WindowManager.LayoutParams layoutParams;
    private Context mContext;
    private TextView tvContent;
    private boolean isClick = false;
    private Callback mCallback;
    private ConfigDebugger mDebugger;

    private boolean isClose = false;

    private double mMaxHeight = 720;

    public FloatingReLayout(Context context) {
        this(context,null);
    }

    public FloatingReLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FloatingReLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        statebarHeight = getStatusBarHeight();
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else if (Build.VERSION.SDK_INT >24){
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

        View childView = LayoutInflater.from(context).inflate(R.layout.floating_relative_layout,this);
        tvContent = childView.findViewById(R.id.tv_content);
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        final TextView btnClose =childView.findViewById(R.id.btn_close);
        TextView btnMapping = childView.findViewById(R.id.btn_mapping);
        TextView btnCopy = childView.findViewById(R.id.btn_copy);
        TextView btnShutDown = childView.findViewById(R.id.btn_shutdown);
        btnCopy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", tvContent.getText().toString().trim());
                clipboardManager.setPrimaryClip(clipData);
                if (mCallback != null){
                    mCallback.clip();
                }
            }
        });

        btnMapping.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                  showDialog(tvContent.getText().toString().trim());
            }
        });

        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClose){
                  btnClose.setText("收起");
                  tvContent.setVisibility(VISIBLE);
                }else {
                    tvContent.setVisibility(GONE);
                    btnClose.setText("展开");
                }
                isClose = !isClose;
            }
        });

        btnShutDown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,FloatingService.class);
                intent.setAction(FloatingService.ACTION_SHUTDOWN);
                mContext.startService(intent);
                FloatingService.shutDownFloating = true;
            }
        });
//        LayoutParams lp = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
////        addView(childView,lp);
        init();
        mWindowManager.addView(this,layoutParams);
        mDebugger = ConfigDebugger.instance();
    }

    private void init() {
        mMaxHeight = Math.min(mMaxHeight, 0.7 * (float) getScreenHeight(getContext()));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            heightSize = heightSize <= mMaxHeight ? heightSize
                    : (int) mMaxHeight;
        }

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = heightSize <= mMaxHeight ? heightSize
                    : (int) mMaxHeight;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = heightSize <= mMaxHeight ? heightSize
                    : (int) mMaxHeight;
        }
        int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,
                heightMode);
        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         long endTime = 0;
         long startTime = 0;
         lastY = (int) (event.getRawY()-statebarHeight);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        Log.e("FloatingService","DOWN_startX:"+startX+"  startY:"+startY);
                       break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(lastX - startX) >= 5 && Math.abs(lastY - startY) >= 5 ){
                            //认为是滑动事件，
                            layoutParams.x = lastX -startX;
                            layoutParams.y = lastY - startY;
                            Log.e("FloatingService","MOVE_layoutParams.x:"+layoutParams.x+"  layoutParams.y:"+layoutParams.y);
                            Log.e("FloatingService","MOVE_lastX:"+lastX+"  lastY:"+lastY);
                            mWindowManager.updateViewLayout(FloatingReLayout.this,layoutParams);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        endTime = System.currentTimeMillis();
                        mWindowManager.updateViewLayout(this,layoutParams);
                        if (endTime - startTime > 100){
                            //认为是点击事件
                            isClick = false;
                        }else {
                            isClick = true;
                        }
                        break;
                }

                if (isClick){
                // todo
                }
                return true;

    }

    private int getStatusBarHeight() {
        Rect frame = new Rect();
        getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     */
    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public void setContent(String content){
        if (tvContent != null){
            tvContent.setText(content);
        }
    }

   private void showDialog(final String data){
       final EditText editText = new EditText(this.getContext());
       editText.setHint("请输入对应的key");
       editText.setFilters( new InputFilter[]{ new  InputFilter.LengthFilter( 20 )});
       AlertDialog alertDialog = new AlertDialog.Builder(this.getContext())
                                     .setTitle("ViewPath—映射")
                                     .setView(editText)
                                     .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             String text = editText.getText().toString().trim();
                                             if (!TextUtils.isEmpty(text)){
                                               Log.e("FloatingService","text:"+text);
                                             }
                                             if (mDebugger != null && mDebugger.mReporter != null){
                                                 mDebugger.mReporter.sendPoint(text,data);
                                             }
                                         }
                                     })
                                     .create();
       if (Build.VERSION.SDK_INT >24){
           //解决Android 7.1.1起不能再用Toast的问题（先解决crash）
           alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
       }else {
            /*以下代码块使得android6.0之后的用户不必再去手动开启悬浮窗权限*/
           String packname = this.getContext().getPackageName();
           PackageManager pm = this.getContext().getPackageManager();
           boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
           if (permission) {
               alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
           } else {
               alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
           }
       }
       alertDialog.show();
   }

    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    public interface Callback{
       void clip();
   }

   public void setCallback(Callback callback){
       this.mCallback = callback;
   }
}
