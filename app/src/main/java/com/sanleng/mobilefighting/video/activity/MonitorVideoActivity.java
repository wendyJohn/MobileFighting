package com.sanleng.mobilefighting.video.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.sanleng.mobilefighting.MyApplication;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.video.module.DaHuaVideo;
import com.sanleng.mobilefighting.video.module.LivePreviewModule;

public class MonitorVideoActivity extends Activity implements SurfaceHolder.Callback, OnClickListener {

	/* private IPLoginModule mLoginModule; */
	private MyApplication app;
	Resources res;
	SurfaceView mRealView;
	/*
	 * LivePreviewModule mLiveModule; private String mAddress; private String mPort;
	 * private String mUsername; private String mPassword;
	 */
	private DaHuaVideo video;
	private Button preview_focus_add;
	private LivePreviewModule mLiveModule;
	private Spinner mSelectChannel;
	private RelativeLayout r_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitorvideo);

		/*
		 * mAddress = "10.101.208.102"; mPort = "51033"; mUsername = "admin"; mPassword
		 * = "admin";
		 */

		/**
		 * Initializing the NetSDKLib is important and necessary to ensure that all the
		 * APIs of INetSDK.jar are effective. 注意: 必须调用 init 接口初始化 INetSDK.jar 仅需要一次初始化
		 */
		/*
		 * NetSDKLib.getInstance().init();
		 *
		 * final String file = new
		 * String(Environment.getExternalStorageDirectory().getPath() + "/sdk_log.log");
		 * NetSDKLib.getInstance().openLog(file);
		 */

		app = (MyApplication) getApplication();
		res = getResources();
		/*
		 * mLoginModule = new IPLoginModule();
		 *
		 * mLiveModule = new LivePreviewModule(MainActivity.this);
		 */

		video = new DaHuaVideo(this);

		setupView();
	}

	private void setupView() {

		mRealView = (SurfaceView) findViewById(R.id.real_view);
		mRealView.getHolder().addCallback(this);

		LoginTask loginTask = new LoginTask();
		loginTask.execute();

		preview_focus_add = (Button) findViewById(R.id.preview_focus_add);
		preview_focus_add.setOnClickListener(this);
		mSelectChannel = (Spinner) findViewById(R.id.select_channel);
		r_back = (RelativeLayout) findViewById(R.id.r_back);
		r_back.setOnClickListener(this);
	}

	/// LoginTask
	private class LoginTask extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			return video.login();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				app.setLoginHandle(video.getLoginHandle());
				app.setDeviceInfo(video.getDeviceInfo());

				video.startPlay(mRealView);

			} else {
//                ToolKits.showMessage(MonitorVideoActivity.this, getErrorCode(getResources(), video.errorCode()));
			}
		}
	}

	@Override
	protected void onDestroy() {
		video.stopPlay();
		mRealView = null;
		video.logout();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// while onResume we should logout the device.

		// mLoginModule.logout();
		super.onResume();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		video.initSurfaceView(mRealView);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.preview_focus_add:
//			mLiveModule.ptzControlEx(mSelectChannel.getSelectedItemPosition(),
//					SDK_PTZ_ControlType.SDK_PTZ_FOCUS_ADD_CONTROL, (byte) 8);
				break;
			case R.id.r_back:
				finish();
				break;
			default:
				break;
		}
	}

}
