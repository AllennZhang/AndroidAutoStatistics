package com.hipac.codeless.config;

import com.hipac.codeless.define.IReporter;
import com.hipac.codeless.define.ShareInterface;

/**
 * Created by youri on 2018/3/5.
 * 策略配置
 */

public class StrategyConfig {
    //log存储容量默认50条
    private int logCapacity = 20;
    //上报的网络框架由外部提供，统一实现reportLogs方法
    private IReporter mReporter;
    //数据共享接口
    private ShareInterface mShareInterface;
    //是否分享数据
    private boolean canShare = false;



    public StrategyConfig(){

    }

    public StrategyConfig(int logCapacity, IReporter mReporter) {
        this.logCapacity = logCapacity;
        this.mReporter = mReporter;
    }

    public IReporter getReporter(){
        return mReporter;
    }
    public void setReporter(IReporter reporter){
        this.mReporter = reporter;
    }

    public void setLogCapacity(int capacity){
        this.logCapacity = capacity;
    }

    public int  getCapacity(){
        return logCapacity;
    }

    public boolean share(){
        return canShare;
    }

    public void setCanShare(boolean canShare){
        this.canShare = canShare;
    }

    public ShareInterface getShareInterface(){
        return mShareInterface;
    }

    public void setShareInterface(ShareInterface shareInterface){
        this.mShareInterface = shareInterface;
    }

}
