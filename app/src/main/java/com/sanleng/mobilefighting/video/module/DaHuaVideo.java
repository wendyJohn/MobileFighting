package com.sanleng.mobilefighting.video.module;

import android.content.Context;
import android.os.Environment;
import android.view.SurfaceView;

import com.company.NetSDK.NET_DEVICEINFO_Ex;
import com.sanleng.mobilefighting.video.common.NetSDKLib;


public class DaHuaVideo {

    private Context mContext;
    private IPLoginModule mLoginModule;
    private LivePreviewModule mLiveModule;


    private String mAddress;
    private String mPort;
    private String mUsername;
    private String mPassword;

    public  DaHuaVideo(Context context){
        this.mContext =context;
        /**
         *  Initializing the NetSDKLib is important and necessary to ensure that
         *  all the APIs of INetSDK.jar are effective.
         *  娉ㄦ剰: 蹇呴』璋冪敤 init 鎺ュ彛鍒濆鍖� INetSDK.jar 浠呴渶瑕佷竴娆″垵濮嬪寲*/
        NetSDKLib.getInstance().init();

        final String file = new String(Environment.getExternalStorageDirectory().getPath() + "/sdk_log.log");
        NetSDKLib.getInstance().openLog(file);

        mLoginModule = new IPLoginModule();
        mLiveModule = new LivePreviewModule(context);
    }

    public long getLoginHandle(){
        return mLoginModule.getLoginHandle();
    }

    public NET_DEVICEINFO_Ex getDeviceInfo(){
        return mLoginModule.getDeviceInfo();
    }
    public int errorCode(){
        return mLoginModule.errorCode();
    }

    public boolean login() {
        mAddress = "10.101.208.102";
        mPort = "51033";
        mUsername = "admin";
        mPassword = "admin";
        return mLoginModule.login(mAddress, mPort, mUsername, mPassword);
    }

    public void startPlay(final SurfaceView view) {
        mLiveModule.startPlay(0,0,view);
    }

    public void initSurfaceView(final SurfaceView view) {
        mLiveModule.initSurfaceView(view);
    }

    public void stopPlay() {
        mLiveModule.stopRealPlay();
        mLiveModule = null;
    }
    public boolean logout() {
        Boolean result = false;
        if(null != mLoginModule) {
            result=mLoginModule.logout();
            mLoginModule = null;
        }
        return result;
    }



}
