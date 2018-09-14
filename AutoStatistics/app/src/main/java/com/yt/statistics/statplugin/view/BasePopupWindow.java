package com.yt.statistics.statplugin.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.PopupWindowCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.PopupWindow;

import com.yt.statistics.statplugin.DisplayUtil;
import com.yt.statistics.statplugin.R;


/**
 * Created by shujian on 17/6/17.
 * 带背景变暗效果
 */

public abstract class BasePopupWindow extends PopupWindow {

    View mBgMaskView;
    ViewGroup mBgParentWindow;

    public BasePopupWindow(Context context) {
        super(context);
    }

    public void showAtLocationWithBgAnimate(ViewGroup bgParentWindow, View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        this.mBgParentWindow = bgParentWindow;
        mBgMaskView = new View(mBgParentWindow.getContext());
        mBgMaskView.setBackgroundColor(Color.BLACK);
        mBgMaskView.setAlpha(0f);
        mBgParentWindow.addView(mBgMaskView);
        mBgMaskView.animate().alphaBy(0f)
                .alpha(0.5f).setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(parent.getResources().getInteger(R.integer.animate_duration))
                .start();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void dismiss() {
        super.dismiss();
        if (mBgMaskView!=null) {
            mBgMaskView.animate().alphaBy(0.5f)
                    .alpha(0f).setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(mBgMaskView.getResources().getInteger(R.integer.animate_duration))
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            mBgParentWindow.removeView(mBgMaskView);
                            mBgParentWindow = null;
                            mBgMaskView = null;
                        }
                    })
                    .start();
        }
    }

    public void show(View parent, int gravity, int x, int y) {
        if (null != parent) {
            super.showAtLocation(parent,gravity,x,y);

        }
    }

    //设置popup显示位置
    public void show(View anchorView) {
        if (null != anchorView) {
            int[] a=new int[2];
            anchorView.getLocationInWindow(a);
            int height = DisplayUtil.getDisplayHeight() - a[1]-anchorView.getHeight();
            this.setHeight(height);
            PopupWindowCompat.showAsDropDown(this,anchorView,0,0, Gravity.NO_GRAVITY);
        }
    }

    public void setAniStyle(int style){
        this.setAnimationStyle(style);
    }


}
