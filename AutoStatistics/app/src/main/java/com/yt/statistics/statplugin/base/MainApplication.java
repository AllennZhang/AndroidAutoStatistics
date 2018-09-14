package com.yt.statistics.statplugin.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.hipac.codeless.config.StrategyConfig;
import com.hipac.codeless.core.TraceService;
import com.jerry.codeless.statconfig.ConfigDebugger;
import com.yt.statistics.statplugin.StatPointReporter;
import com.yt.statistics.statplugin.StatReporter;
import com.yt.statistics.statplugin.StatShareInterface;

/**
 * Created by youri on 2018/3/7.
 */

public class MainApplication extends Application {
  public static Context appContext;
  public static boolean  shutDownFloating;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        StrategyConfig config = new StrategyConfig();
        config.setReporter(new StatReporter());
        config.setShareInterface(new StatShareInterface());
        TraceService.init(this,config,true);
        //开启debug悬浮窗

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
            ConfigDebugger.instance().init(this, new StatPointReporter());
        }
        String packageName = getPackageName();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, 0);
            Log.e("Application","versionName :"+packageInfo.versionName+"---versionCode:"+packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {

        }
    }
}
