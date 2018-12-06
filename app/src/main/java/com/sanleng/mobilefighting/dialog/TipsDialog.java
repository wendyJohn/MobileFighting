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

public class TipsDialog extends Dialog implements android.view.View.OnClickListener {

	Context context;
	private TextView cancel;
	private TextView binding;
	private Handler handler;

	public TipsDialog(Context context, Handler handler) {
		super(context);
		this.context = context;
		this.handler = handler;
	}

	public TipsDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.setContentView(R.layout.tipsdialog);
		this.setCancelable(false);// 设置点击屏幕Dialog不消失
		cancel = (TextView) findViewById(R.id.cancel);
		binding = (TextView) findViewById(R.id.binding);
		cancel.setOnClickListener(this);
		binding.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		取消
			case R.id.cancel:
				dismiss();
				break;
//			绑定
			case R.id.binding:
				Message msg = new Message();
				msg.what = 88886666;
				handler.sendMessage(msg);
				dismiss();
				break;
			default:
				break;
		}
	}

}
