package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.myview.FullVideoView;
import com.sanleng.mobilefighting.util.AudioController;
import com.sanleng.mobilefighting.util.LightnessController;

/**
 * 视频播放
 *
 * @author Qiaoshi
 */
public class VideoSurfaceActivity extends Activity
		implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, CompoundButton.OnCheckedChangeListener,
		Handler.Callback, SeekBar.OnSeekBarChangeListener {

	private FullVideoView mFullVideoView;
	private TextView curent_position, duration;
	private CheckBox pause_play, full_screen_controller;
	// VideoView的容器
	private FrameLayout video_container;
	private String video_url;
	private Handler mHandler = new Handler(this);
	private SeekBar progress_ctroller;
	private LinearLayout bottem_controller;
	private int mHeight;
	private SeekBar bright_display;

	// ==========文章
	private RelativeLayout r_back;
	private RelativeLayout ryout;
	private WebView webView;
	private TextView t_name;
	private TextView t_category;
	private TextView t_frequency;

	private String url;
	private String name;
	private String category;
	private String frequency;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_videosurface);
		initView();
	}

	public SeekBar getBright_display() {
		return bright_display;
	}

	private void initView() {
		Intent intent = getIntent();
		video_url = intent.getStringExtra("record_url");
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
		ryout = (RelativeLayout) findViewById(R.id.ryout);
		r_back = (RelativeLayout) findViewById(R.id.r_back);
		r_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 设置亮度的seekbar
		bright_display = (SeekBar) findViewById(R.id.brightness_display);
		mFullVideoView = (FullVideoView) findViewById(R.id.videoView);
		// 时间控件
		curent_position = (TextView) findViewById(R.id.current_position);
		duration = (TextView) findViewById(R.id.duration);
		// 2 设置暂停
		pause_play = (CheckBox) findViewById(R.id.pause_play);
		pause_play.setOnCheckedChangeListener(this);
		// 3 是否全屏
		full_screen_controller = (CheckBox) findViewById(R.id.full_screen_controller);
		full_screen_controller.setOnCheckedChangeListener(this);
		// 5 设置SeekBard 并设置他的点击事件
		progress_ctroller = (SeekBar) findViewById(R.id.progress_controller);
		progress_ctroller.setOnSeekBarChangeListener(this);

		mFullVideoView.setVideoPath(video_url);
		// 1, 设置mFullVideoView的点击事件 是指预加载和加载错误
		mFullVideoView.setOnPreparedListener(this);
		mFullVideoView.setOnErrorListener(this);
		bottem_controller = (LinearLayout) findViewById(R.id.bottem_controller);
		// 4 设置最外层frameLayout的点击事件
		video_container = (FrameLayout) findViewById(R.id.video_container);
		video_container.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				switch (motionEvent.getAction()) {
					case MotionEvent.ACTION_DOWN: // checkBox和LinearLayout显示
						pause_play.setVisibility(View.VISIBLE);
						bottem_controller.setVisibility(View.VISIBLE);
						mHandler.sendEmptyMessageDelayed(PAUSE_AUTO_DISAPPEAR, 3000);
						break;
				}
				return false;// 不修改 点击事件还会继续传递
			}
		});
		// handler发送消息 3秒之后播放和暂停的按钮消失
		mHandler.sendEmptyMessageDelayed(PAUSE_AUTO_DISAPPEAR, 3000);
	}

	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
		// 当刚进入时让按钮默认是选中状态,视屏默认是播放状态
		pause_play.setChecked(true);// 让视屏进行播放
	}

	@Override
	public boolean onError(MediaPlayer mediaPlayer, int i, int i1) { // 当视屏播放错误时,弹出一个对话框
//		FalseHintsDialog falseHintsDialog=new FalseHintsDialog(VideoSurfaceActivity.this);
//		falseHintsDialog.show();
		return true;
	}

	// checkBox状态栏发生改变的监听 两个checkbox的状态栏
	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean user) {
		switch (compoundButton.getId()) {
			case R.id.pause_play:
				if (user) {// 用户选中 视频播放 显示成暂停的图标
					// 首先视频播放
					mFullVideoView.start();
					// 修改播放的时间 handler帮助每隔一秒更新播放进度
					mHandler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 1000);

				} else {
					mFullVideoView.pause();
					// 移除 不让他继续去发送handler
					mHandler.removeMessages(UPDATE_PROGRESS);
				}
				break;
			case R.id.full_screen_controller:
				if (user) {// 用户点击了 全屏展示
					// 设置全屏
					ryout.setVisibility(View.GONE);
					getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
					// 设置手机屏幕横向
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					// 需要修改VieoView 的高度 如果不修改 当它横屏时不会去充满整个屏幕
					// 首先获得原来的高度
					mHeight = video_container.getHeight();
					// 重新制定高度
					ViewGroup.LayoutParams params = video_container.getLayoutParams();
					params.height = ViewGroup.LayoutParams.MATCH_PARENT;
					video_container.setLayoutParams(params);

				} else {// 从全屏状态回去
					ryout.setVisibility(View.VISIBLE);
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
					getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);// 显示状态栏
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					// 还原之前的高度
					ViewGroup.LayoutParams portrait_params = video_container.getLayoutParams();
					portrait_params.height = mHeight;
					video_container.setLayoutParams(portrait_params);

				}
				break;
		}
	}

	// 更新进度的标志
	public static final int UPDATE_PROGRESS = 1;
	public static final int PAUSE_AUTO_DISAPPEAR = 2;// 播放和暂停按钮设置
	public static final int BRIGHTNESS_DISPLAY_DISAPPEAR = 3;

	@Override
	public boolean handleMessage(Message message) {
		switch (message.what) {
			case UPDATE_PROGRESS:// 更新进度
				// 视频总时间 设置它的总时间是多少
				int duration = mFullVideoView.getDuration();
				if (progress_ctroller.getMax() == 100) {
					progress_ctroller.setMax(duration);
				}
				CharSequence format = DateFormat.format("mm:ss", duration);
				this.duration.setText(format);

				// 设置当前播放的进度 并将当前的播放进度设置文本中

				int currentPosition = mFullVideoView.getCurrentPosition();
				CharSequence current_position = DateFormat.format("mm:ss", currentPosition);
				this.curent_position.setText(current_position);

				// 设置seekbar的当前进度
				progress_ctroller.setProgress(currentPosition);
				mHandler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 1000);
				break;
			case PAUSE_AUTO_DISAPPEAR:
				bottem_controller.setVisibility(View.GONE);
				pause_play.setVisibility(View.GONE);
				break;
			case BRIGHTNESS_DISPLAY_DISAPPEAR:
				bright_display.setVisibility(View.GONE);
				break;
		}
		return true;
	}

	// seekbar点击事件实现的方法 当拉动的时候使得视屏也跟着动
	@Override
	public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
		if (b) {
			mFullVideoView.seekTo(i);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	private boolean isLandscape;

	// 横竖屏回调的方法 当没法在返回事件中写的时候 应该去重写这个方法,当横竖屏切换时必然会回调该方法
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		switch (newConfig.orientation) {
			case Configuration.ORIENTATION_LANDSCAPE:// 横屏
				isLandscape = true;
				break;
			case Configuration.ORIENTATION_PORTRAIT:// 竖屏
				isLandscape = false;
				break;
		}
		super.onConfigurationChanged(newConfig);
	}

	//	private long last_time = 0;
//
	// 点击回退键 如果是全屏,点击时首先回到竖屏
	@Override
	public void onBackPressed() {
//		// 判断是否是横屏
//		if (isLandscape) {
//			// 设置屏幕为竖屏
//			full_screen_controller.setChecked(false);
//		} else { // 超过两秒
//			if (System.currentTimeMillis() - last_time > 2000) {
//				// 设置屏幕为竖屏
//				last_time = System.currentTimeMillis();
//			} else {
//				super.onBackPressed();
//			}
//		}
	}

	private float laxt_x, laxt_y;

	// 手指在屏幕上滑动时 快进 快退 声音 屏幕亮度
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isLandscape) {// 手机横屏
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 手指按下
					// 手指按下 记录
					laxt_x = event.getX();
					laxt_y = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动
					// 如果在x轴方向移动的变化大于y轴 快进 快退
					float x = event.getX();
					float y = event.getY();
					if (Math.abs(x - laxt_x) > Math.abs(y - laxt_y)) {// 快进,退
						if (Math.abs(x - laxt_x) > 10) {
							int widthPixels = this.getResources().getDisplayMetrics().widthPixels;
							float xDelta = x - laxt_x;
							if (x - laxt_x > 10) {// 进
								forwa(xDelta, widthPixels);
							} else if (x - laxt_x < -10) {// 退

								backward(xDelta, widthPixels);// 该值是负数
							}
						}
					} else {// 视屏亮度
						// 手机屏幕左半部分 修改亮度
						if (Math.abs(y - laxt_y) > 10) {// 用户有意识进行滑动
							float yDilta = y - laxt_y;
							// y轴方向的总高度
							int heightPixels = this.getResources().getDisplayMetrics().heightPixels;

							if (x < this.getResources().getDisplayMetrics().widthPixels / 2) { //
								if (y - laxt_y > 10) {
									LightnessController.turnDown(this, yDilta, heightPixels);
								} else if (y - laxt_y < -10) {
									LightnessController.turnUp(this, yDilta, heightPixels);
								}
							} else { // 手机屏幕右半部分 修改声音
								if (y - laxt_y > 10) {// 向下移动 声音降低
									AudioController.turnDown(this, yDilta, heightPixels);
								} else if (y - laxt_y < -10) {// 向上移动,声音反放打
									AudioController.turnup(this, yDilta, heightPixels);
								}
							}
						}
					}
					break;
				case MotionEvent.ACTION_UP: // 修改手指滑动的位置
					laxt_x = event.getX();
					laxt_y = event.getY();

					mHandler.sendEmptyMessageDelayed(BRIGHTNESS_DISPLAY_DISAPPEAR, 1000);
					break;
			}
		} else {// 竖屏展示

		}
		return super.onTouchEvent(event);
	}

	private void backward(float xDelta, int widthPixels) {
		int duration = mFullVideoView.getDuration();
		int currentPosition = mFullVideoView.getCurrentPosition();
		float durationDelta = xDelta / widthPixels * duration;// 负数

		int video_move_position = (int) Math.max(0, currentPosition + durationDelta);
		mFullVideoView.seekTo(video_move_position);

		progress_ctroller.setProgress(video_move_position);
		// 当前播放的时间
		curent_position.setText(DateFormat.format("mm:ss", video_move_position));
	} // 视屏快进

	private void forwa(float xDelta, int widthPixels) {
		int duration = mFullVideoView.getDuration();
		int currentPosition = mFullVideoView.getCurrentPosition();
		float durationDelta = xDelta / widthPixels * duration;
		// 参数二是相加得到的 有可能大于视屏的总长度
		int video_move_position = (int) Math.min(duration, currentPosition + durationDelta);
		mFullVideoView.seekTo(video_move_position);
		// 移动seekbar
		progress_ctroller.setProgress(video_move_position);
		// 当前播放的时间
		curent_position.setText(DateFormat.format("mm:ss", video_move_position));

	} // 程序退出 将handler中所发送的消息移除

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeMessages(UPDATE_PROGRESS);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		AudioManager am = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
		switch (keyCode) {
			case KeyEvent.KEYCODE_VOLUME_UP:
				am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
				am.adjustStreamVolume(AudioManager.STREAM_DTMF, AudioManager.ADJUST_RAISE, 0);
				break;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
				am.adjustStreamVolume(AudioManager.STREAM_DTMF, AudioManager.ADJUST_RAISE, 0);
				break;
			case KeyEvent.KEYCODE_BACK:
				finish();
				break;
			default:
				break;
		}



		return super.onKeyDown(keyCode, event);
	}

}
