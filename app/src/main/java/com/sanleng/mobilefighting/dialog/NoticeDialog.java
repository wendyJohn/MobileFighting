package com.sanleng.mobilefighting.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;

public class NoticeDialog extends Dialog implements android.view.View.OnClickListener {

	Context context;
	private TextView notice;
	private Handler mHandler;

	public NoticeDialog(Context context, Handler mHandler) {
		super(context);
		this.context = context;
		this.mHandler = mHandler;
	}

	public NoticeDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.setContentView(R.layout.noticedialog);
		this.setCancelable(false);// 设置点击屏幕Dialog不消失
		notice = (TextView) findViewById(R.id.notice);
		notice.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.notice:
				Message mymsg = new Message();
				mymsg.what = 76565;
				mHandler.sendMessage(mymsg);
				dismiss();
				break;
			default:
				break;
		}
	}

}
