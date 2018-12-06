package com.sanleng.mobilefighting.video.common;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.company.NetSDK.CB_fDisConnect;
import com.company.NetSDK.CB_fHaveReConnect;
import com.company.NetSDK.INetSDK;
import com.company.NetSDK.LOG_SET_PRINT_INFO;
import com.company.NetSDK.NET_PARAM;

import java.io.File;

/**
 * Created by 29779 on 2017/4/10.
 * This Class is Created for init INetSDK.jar
 */
public final class NetSDKLib {
    private final static String TAG = "NetSDKLib";
    private static NetSDKLib instance = new NetSDKLib();
    private boolean mbInit = false;

    private DeviceDisConnect mDisconnect;
    private DeviceReConnect mReconnect;

    /// Timeout of NetSDK API
    /// INetSDK 鎺ュ彛瓒呮椂鏃堕棿
    public static final int TIMEOUT_5S = 5000;      // 5 second
    public static final int TIMEOUT_10S = 10000;    // 10 second
    public static final int TIMEOUT_30S = 30000;    // 30 second

    private NetSDKLib() {
        mDisconnect = new DeviceDisConnect();
        mReconnect = new DeviceReConnect();
    }

    public static NetSDKLib getInstance() {
        return instance;
    }

    /// Init NetSDK library's resources.
    /// 鍒濆鍖� NETSDK
    public synchronized void init() {
        INetSDK.LoadLibrarys();
        if (mbInit) {
            return;
        }
        mbInit = true;

        /// Init NetSDK, and set disconnect callback.
        /// 鍒濆鍖栨帴鍙ｅ湪鎵�鏈夌殑SDK鍑芥暟涔嬪墠璋冪敤 骞惰缃柇绾垮洖璋� :褰揳pp鍜岃澶囩缃戠粶鏂紑鏃讹紝浼氳Е鍙戝洖璋�
        /// 璇ユ帴鍙ｄ粎闇�璋冪敤涓�娆�
        boolean zRet = INetSDK.Init(mDisconnect);
        if (!zRet) {
            Log.e(TAG, "init NetSDK error!");
            return;
        }

/**
 * Time 201811021705
 * /// Set Reconnect callback.
 * /// 璁剧疆鏂嚎閲嶈繛鍥炶皟 : 褰揳pp閲嶆柊杩炴帴涓婅澶囨椂锛屼細瑙﹀彂璇ュ洖璋�;
 * /// 姝ゅ榛樿涓嶄娇鐢�*/
        INetSDK.SetAutoReconnect(mReconnect);

        /// Close the SDK Log
        // closeSDKLog();

        /// Set global parameters of NetSDK.
        NET_PARAM stNetParam = new NET_PARAM();
        stNetParam.nWaittime = TIMEOUT_10S; // Time out of common Interface.
        stNetParam.nSearchRecordTime = TIMEOUT_30S; // Time out of Playback interface.
        INetSDK.SetNetworkParam(stNetParam);
    }

    /// Cleanup NetSDK library's resources.
    /// 娓呯悊 INetSDK.jar 璧勬簮
    public synchronized void cleanup() {
        /// only be invoked for once
        if (mbInit) {
            INetSDK.Cleanup();
            mbInit = false;
        }
    }

    public boolean isFileExist(String fileName) {
        if (fileName == null) {
            return false;
        }

        File file = new File(fileName);
        return file.exists();
    }

    /// Open SDK log
    /// 鎵撳紑 SDK 鏃ュ織
    public boolean openLog(String logFile) {
        if (!isFileExist(logFile)) {
            return false;
        }

        LOG_SET_PRINT_INFO logInfo = new LOG_SET_PRINT_INFO();
        logInfo.bSetPrintStrategy = true;
        logInfo.nPrintStrategy = 0; // 0 - Saved as file. 1 - show log in the console.
        logInfo.bSetFilePath = true;
        System.arraycopy(logFile.getBytes(), 0, logInfo.szLogFilePath, 0, logFile.length());

        return INetSDK.LogOpen(logInfo);
    }

    /// Close SDK log
    /// 鍏抽棴鏃ュ織
    public boolean closeLog() {
        return INetSDK.LogClose();
    }

    /// while app disconnect with device, the interface will be invoked.
    /// 鏂嚎鍥炶皟
    public class DeviceDisConnect implements CB_fDisConnect {
        @Override
        public void invoke(long loginHandle, final String deviceIp, int devicePort) {
/*            mHandler.post(new Runnable() {
                @Override
                public void run() {
                   //杩斿洖閲嶆柊璋冪敤涓婚〉闈�
                }
            });*/
        }
    }

    /// After app reconnect the device, the interface will be invoked.
    /// 閲嶈繛鍥炶皟
    public class DeviceReConnect implements CB_fHaveReConnect {
        @Override
        public void invoke(long loginHandle, String deviceIp, int devicePort) {
        }
    }

    private Handler mHandler = new Handler(Looper.myLooper());

}
