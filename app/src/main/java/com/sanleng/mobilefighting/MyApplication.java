package com.sanleng.mobilefighting;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.company.NetSDK.NET_DEVICEINFO_Ex;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadQueue;
import com.yolanda.nohttp.rest.RequestQueue;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends MultiDexApplication {

    private static Application _instance;
    public static RequestQueue requestQueue = null;// 请求队列
    public static DownloadQueue downloadQueue;// 下载队列
    public static ImageLoader imageLoader;// 下载队列

    private long mloginHandle;
    private NET_DEVICEINFO_Ex mDeviceInfo;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this); // 初始化 JPush

        _instance = this;
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(this);
        // 初始化NoHttp
        NoHttp.init(this);

        // 初始化请求队列
        requestQueue = NoHttp.newRequestQueue();
        downloadQueue = NoHttp.newDownloadQueue();

        setLoginHandle(0);
        setDeviceInfo(null);

        DisplayImageOptions o = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).writeDebugLogs()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).defaultDisplayImageOptions(o).build();
        ImageLoader.getInstance().init(config);

    }

    public long getLoginHandle() {
        return mloginHandle;
    }

    public void setLoginHandle(long loginHandle) {
        this.mloginHandle = loginHandle;
    }

    public NET_DEVICEINFO_Ex getDeviceInfo() {
        return mDeviceInfo;
    }

    public void setDeviceInfo(NET_DEVICEINFO_Ex mDeviceInfo) {
        this.mDeviceInfo = mDeviceInfo;
    }

    public static Application getInstance() {
        return _instance;
    }
}
