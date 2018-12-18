package com.sanleng.mobilefighting.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.interfacer.MediaControllerInterface;
import com.sanleng.mobilefighting.util.MyController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 视频播放界面
 */
public class RercyclerItemctivity extends Activity {
    private static final String TAG = "TestActivity";

    private SurfaceView mSurfaceView;
    private PLMediaPlayer mMediaPlayer;
    private AVOptions mAVOptions;

    private int mSurfaceWidth = 0;
    private int mSurfaceHeight = 0;

    private String mVideoPath = null;
    private long mLastUpdateStatTime = 0;
    private String is_over = "0";
    private int sv_height;// 记录非全屏状态时，surfaceView的高度，以便退出全屏时，设置回来
    boolean firstRendering = true;// 视频第一次渲染，在OnInfoListener 中改变值，保证恢复到先前播放的进度
    private MyController controller;
    private FrameLayout fl_surfaceview_parent;
    private boolean seekbarDrag = false;

    // ==========文章
    private WebView webView;
    private TextView t_name;
    private TextView t_category;
    private TextView t_frequency;

    private String url;
    private String name;
    private String category;
    private String frequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.module_activity_video_detail);
        super.onCreate(savedInstanceState);
        addArticle();
        boolean isLiveStreaming = getIntent().getIntExtra("liveStreaming", 0) == 1;
        Intent intent = getIntent();
        mVideoPath = intent.getStringExtra("record_url");
        // 播放地址

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        fl_surfaceview_parent = (FrameLayout) findViewById(R.id.fl_surfaceview_parent);
        mSurfaceView.getHolder().addCallback(mCallback);
        mSurfaceWidth = getResources().getDisplayMetrics().widthPixels;
        mSurfaceHeight = getResources().getDisplayMetrics().heightPixels;

        mAVOptions = new AVOptions();
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        int codec = getIntent().getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, codec);
        mAVOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming ? 1 : 0);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        controller = new MyController(this);

        ViewTreeObserver vto2 = mSurfaceView.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mSurfaceView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sv_height = mSurfaceView.getHeight();
                mSurfaceView.setLayoutParams(
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, sv_height));
            }
        });

        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.show();
            }
        });
    }

    private boolean isPausing = false;// 是否是暂停状态
    private boolean isFullScreen = false;// 是否是全屏状态
    private MediaControllerInterface.MediaControl mPlayerControl = new MediaControllerInterface.MediaControl() {
        @Override
        public void VideoStart() {
            mMediaPlayer.start();
        }

        @Override
        public void VideoResume() {
            mMediaPlayer.start();
        }

        @Override
        public void VideoPause() {
            mMediaPlayer.pause();
        }

        @Override
        public void VideoStop() {
            mMediaPlayer.stop();
        }

        @Override
        public long getDuration() {
            return mMediaPlayer.getDuration();
        }

        @Override
        public long getCurrentPosition() {
            return mMediaPlayer.getCurrentPosition();
        }

        @Override
        public void seekTo(long var1) {
            mMediaPlayer.seekTo(var1);
        }

        @Override
        public boolean isPlaying() {
            return mMediaPlayer.isPlaying();
        }

        @Override
        public boolean isPausing() {
            return isPausing;
        }

        @Override
        public boolean isFullScreen() {
            return isFullScreen;
        }

        @Override
        public void toCollect() {
        }

        @Override
        public void actionForFullScreen() {
            if (isFullScreen) {
                // 退出全屏
                Exit_full_screen();
            } else {
                To_full_screen();
            }
        }
    };

    // 进入全屏的操作
    public void To_full_screen() {
        isFullScreen = true;
        fl_surfaceview_parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置activity横屏
        getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);// 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    // 退出全屏的操作
    public void Exit_full_screen() {
        isFullScreen = false;
        fl_surfaceview_parent
                .setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, sv_height));
        mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, sv_height));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置activity竖屏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);// 显示状态栏
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
        controller.release();

        // 释放资源
        webView.destroy();
        webView = null;

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void releaseWithoutStop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(null);
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    // 播放准备
    private void prepare() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            return;
        }

        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new PLMediaPlayer(this, mAVOptions);
            }
            mMediaPlayer.setDebugLoggingEnabled(true);
            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mMediaPlayer.setLooping(true);// 循环播放
            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setDataSource(mVideoPath);
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            mMediaPlayer.prepareAsync();
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // surfaceview回调函数
    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            prepare();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            release();
            releaseWithoutStop();
        }
    };

    // 视频大小改变的监听
    private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLMediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(PLMediaPlayer mp, int width, int height) {
            // resize the display window to fit the screen
            if (width != 0 && height != 0) {
                float ratioW = (float) width / (float) mSurfaceWidth;
                float ratioH = (float) height / (float) mSurfaceHeight;
                float ratio = Math.max(ratioW, ratioH);
                width = (int) Math.ceil((float) width / ratio);
                height = (int) Math.ceil((float) height / ratio);
                FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(width, height);
                layout.gravity = Gravity.CENTER;
                mSurfaceView.setLayoutParams(layout);
            }
        }
    };

    // mediaplayer准备好监听
    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(PLMediaPlayer mp, int preparedTime) {
            controller.setControl(mPlayerControl);
            controller.setAnchorView(fl_surfaceview_parent);
            controller.setSeekBarEnabled(false);
            mMediaPlayer.start();
        }
    };

    /**
     * 转换播放时间
     *
     * @param milliseconds 传入毫秒值
     * @return 返回 hh:mm:ss或mm:ss格式的数据
     */
    @SuppressLint("SimpleDateFormat")
    public String getShowTime(long milliseconds) {
        // 获取日历函数
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat dateFormat = null;
        // 判断是否大于60分钟，如果大于就显示小时。设置日期格式
        if (milliseconds / 60000 > 60) {
            dateFormat = new SimpleDateFormat("hh:mm:ss");
        } else {
            dateFormat = new SimpleDateFormat("mm:ss");
        }
        return dateFormat.format(calendar.getTime());
    }

    // 视频播放状态监听
    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:// 第一帧视频已成功渲染
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//                    showToastTips("first video render time: " + extra + "ms");
                    if (firstRendering && "0".equals(is_over)) {// 第一次渲染，并且是未看过
                        firstRendering = false;
                    }
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_GOP_TIME:
                    break;
                case PLMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    break;
                case PLMediaPlayer.MEDIA_INFO_SWITCHING_SW_DECODE:
                    break;
                case PLMediaPlayer.MEDIA_INFO_METADATA:
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_BITRATE:
                case PLMediaPlayer.MEDIA_INFO_VIDEO_FPS:
//                    updateStatInfo();
                    break;
                case PLMediaPlayer.MEDIA_INFO_CONNECTED:
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    // 视频缓存监听
    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(PLMediaPlayer mp, int percent) {
            long current = System.currentTimeMillis();
            if (current - mLastUpdateStatTime > 3000) {
                mLastUpdateStatTime = current;
            }
        }
    };

    private boolean onComplet = false;// 是否已经播放完一个视频
    // 视频播放完成监听
    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer mp) {
            // 设置播放标记为false
            controller.isCompleted();
        }
    };

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:// 网络异常
                    /**
                     * SDK will do reconnecting automatically
                     */
                    // 如果网络异常播放失败，尝试重新连接
                    reload();
                    return false;
                case PLMediaPlayer.ERROR_CODE_OPEN_FAILED:// 播放器打开失败
                    // 重新打开播放器
                    release();// 先关闭先前的播放器
                    prepare();
                    break;
                case PLMediaPlayer.ERROR_CODE_SEEK_FAILED:// seek拖动失败

                    break;
                case PLMediaPlayer.ERROR_CODE_PLAYER_DESTROYED:// 播放器已被销毁，需要再次 setVideoURL 或 prepareAsync
                    // 重新打开播放器
                    release();// 先关闭先前的播放器
                    prepare();
                    break;
                default:// 未知错误
                    break;
            }
            return true;
        }
    };

    // 如果网络异常播放失败，尝试重新连接
    public void reload() {
        release();// 先关闭先前的播放器
        // 判断网络连接
        prepare();
    }

    //	加载文章内容
    private void addArticle() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        name = intent.getStringExtra("name");
        category = intent.getStringExtra("category");
        frequency = intent.getStringExtra("frequency");

        t_name = (TextView) findViewById(R.id.t_name);
        t_category = (TextView) findViewById(R.id.t_category);
        t_frequency = (TextView) findViewById(R.id.t_frequency);

        t_name.setText(name);
        t_category.setText(category);
        t_frequency.setText(frequency);

        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(url);// 加载url
    }
}
