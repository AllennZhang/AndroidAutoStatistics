//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yt.statistics.statplugin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.yt.statistics.statplugin.base.MainApplication;

public class DisplayUtil {
    public static DisplayMetrics metrics;
    private static int statusBarHeight;
    private static int naviBarHeight;
    private static int smartbarShownHeight;

    public DisplayUtil() {
    }

    public static void setSmartbarShownHeight(int height) {
        smartbarShownHeight = height;
    }

    public static DisplayMetrics getMetrics() {
        return metrics;
    }

    public static int px2dip(float pxValue) {
        return (int)(pxValue / metrics.density + 0.5F);
    }

    public static void hackExitFullScreenTransition(Window window) {
        makeWindowNoLimit(window);
    }

    public static void toggleFullscreen(Window window, boolean fullscreen) {
        LayoutParams attrs = window.getAttributes();
        if(fullscreen) {
            attrs.flags |= 1024;
        } else {
            attrs.flags &= -1025;
        }

        window.setAttributes(attrs);
    }







    public static void makeWindowNoLimit(Window window) {
        LayoutParams attributes = window.getAttributes();
        attributes.flags |= 768;
        window.setAttributes(attributes);
    }

    private boolean isSoftShowing(Activity activity) {
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom - this.getSoftButtonsBarHeight(activity) != 0;
    }

    @TargetApi(17)
    private int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        return realHeight > usableHeight?realHeight - usableHeight:0;
    }

    public static int dip2px(float dipValue) {
        return metrics != null?(int)(dipValue * metrics.density + 0.5F):0;
    }

    public static int px2sp(float pxValue) {
        return (int)(pxValue / metrics.scaledDensity + 0.5F);
    }

    public static int sp2px(float spValue) {
        return (int)(spValue * metrics.scaledDensity + 0.5F);
    }

    public static int getDisplayHeight() {
        return metrics.heightPixels - smartbarShownHeight;
    }

    public static int getDisplayWidth() {
        return metrics.widthPixels;
    }

    static {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) MainApplication.appContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        metrics = dm;
        if(metrics.widthPixels > metrics.heightPixels) {
            int oldWidth = metrics.widthPixels;
            metrics.widthPixels = metrics.heightPixels;
            metrics.heightPixels = oldWidth;
        }

        statusBarHeight = 0;
        naviBarHeight = 0;
        smartbarShownHeight = 0;
    }
}
